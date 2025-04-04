package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.AudioMetadataDto;
import com.eql.cda.track.flow.service.AudioMetadataService;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AudioMetadataServiceImpl implements AudioMetadataService {

    // Annonce qu'on va définir une méthode publique appelée 'extractMetadata'
    // Elle reçoit un fichier uploadé (MultipartFile) en entrée
    // Elle peut potentiellement causer une erreur générale (throws Exception)
    // Elle promet de retourner un objet 'AudioMetadataDto' (qui contiendra BPM et durée)
    @Override
    public AudioMetadataDto extractMetadata(MultipartFile multipartFile) throws Exception {
        // Prépare une "boîte" (variable) vide appelée 'tempFile' pour contenir un fichier temporaire plus tard.
        // On met 'null' (rien) dedans pour l'instant.
        File tempFile = null;
        // Commence une section "à risque". Le code ici pourrait échouer (ex: fichier invalide).
        try {

            // Appelle une autre méthode (convertMultiPartToFile) pour transformer
            // le fichier reçu de l'upload (en mémoire) en un vrai fichier temporaire sur le disque.
            // Met ce fichier temporaire dans notre boîte 'tempFile'.
            tempFile = convertMultiPartToFile(multipartFile);

            // Utilise une bibliothèque spéciale (jaudiotagger) pour lire les infos du fichier audio temporaire.
            // Le résultat (infos sur le fichier) est mis dans la variable 'audioFile'.
            AudioFile audioFile = AudioFileIO.read(tempFile);

            // Essaye de récupérer les "étiquettes" (tags) du fichier (artiste, album, BPM...).
            // Met le résultat dans la variable 'tag'.
            // Important: Le commentaire dit que 'tag' pourrait être vide ('null') si le fichier n'a pas d'étiquettes.
            Tag tag = audioFile.getTag(); // Peut être null

            // Essaye de récupérer les informations "techniques" du fichier (durée, format...).
            // Met le résultat dans la variable 'header'.
            AudioHeader header = audioFile.getAudioHeader();

            // Prépare une boîte pour le BPM (Beats Per Minute). On met 'null' car on ne l'a pas encore trouvé.
            // On utilise 'Integer' (objet) pour pouvoir stocker 'null' si on ne trouve pas de BPM.
            Integer bpm = null;

            // Vérifie si on a bien trouvé des étiquettes ('tag' n'est pas 'null').
            if (tag != null) {
                // Commence une petite section "à risque" juste pour le BPM. Lire/convertir le BPM peut échouer.
                try {
                    // Demande à l'étiquette ('tag') la valeur du champ "BPM". Le résultat est du texte ('String').
                    String bpmStr = tag.getFirst(FieldKey.BPM);
                    // Vérifie si on a reçu du texte pour le BPM et qu'il n'est pas vide.
                    if (bpmStr != null && !bpmStr.isBlank()) {
                        // Le BPM est souvent un texte (ex: "120.0").
                        // 1. Convertit le texte en nombre décimal (Double).
                        // 2. Arrondit ce nombre à l'entier le plus proche (Math.round).
                        // 3. Convertit le résultat en entier (int).
                        // 4. Stocke cet entier dans notre variable 'bpm'.
                        bpm = (int) Math.round(Double.parseDouble(bpmStr));
                    }
                    // Si la conversion du texte en nombre échoue (NumberFormatException)
                    // ou si l'opération n'est pas supportée (UnsupportedOperationException)...
                } catch (NumberFormatException | UnsupportedOperationException e) {
                    // Affiche un message d'erreur dans la console (partie "erreurs").
                    System.err.println("Could not parse BPM: " + e.getMessage());
                    // On continue sans BPM (la variable 'bpm' reste 'null').
                }
            } // Fin de la vérification des étiquettes (tag != null)

            // Prépare une boîte pour la durée en secondes. On met 'null' pour l'instant.
            Integer durationSeconds = null;
            // Vérifie si on a bien trouvé les infos techniques ('header' n'est pas 'null').
            if (header != null) {
                // Demande à l'en-tête ('header') la durée de la piste. La bibliothèque la donne en secondes.
                // Stocke cette durée dans la variable 'durationSeconds'.
                durationSeconds = header.getTrackLength();
            }

            // Crée un nouvel objet 'AudioMetadataDto' pour stocker les résultats.
            // On lui donne le BPM et la durée qu'on a trouvés (peuvent être 'null').
            AudioMetadataDto audioMetadataDto = new AudioMetadataDto(bpm, durationSeconds);
            // Renvoie cet objet 'AudioMetadataDto' comme résultat de la méthode 'extractMetadata'.
            return audioMetadataDto;

            // Commence un bloc "nettoyage". Ce code s'exécutera TOUJOURS,
            // que le 'try' ait réussi ou échoué, pour s'assurer qu'on nettoie bien.
        } finally {
            // Vérifie si on a créé un fichier temporaire ('tempFile' n'est pas 'null')
            // ET s'il existe toujours sur le disque.
            if (tempFile != null && tempFile.exists()) {
                // Essaye de supprimer le fichier temporaire. 'delete()' renvoie faux si ça échoue.
                // Donc, 'if (!tempFile.delete())' veut dire "si la suppression a échoué".
                if (!tempFile.delete()) {
                    // Affiche un message d'erreur si on n'a pas pu supprimer le fichier temporaire.
                    System.err.println("Could not delete temporary file: " + tempFile.getAbsolutePath());
                }
            }
        } // Fin du bloc 'finally' (nettoyage)
    } // Fin de la méthode 'extractMetadata'



    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        // Demande à Java de créer un fichier temporaire vide sur le disque.
        // Le nom commencera par "audio_", suivi de caractères aléatoires, puis "_" et le nom original du fichier.
        // L'objet 'File' représentant ce nouveau fichier est stocké dans 'convFile'.
        File convFile = File.createTempFile("audio_", "_" + file.getOriginalFilename());

        // Commence un bloc 'try-with-resources'. C'est spécial : ça va automatiquement
        // fermer le 'FileOutputStream' à la fin, même s'il y a une erreur.
        // 'FileOutputStream' est un outil pour écrire des données DANS un fichier.
        // On ouvre 'convFile' (notre fichier temporaire) pour écriture et on appelle l'outil 'fos'.
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            // Récupère le contenu binaire (les octets) du fichier uploadé ('file').
            // Écrit tous ces octets dans le fichier temporaire via l'outil 'fos'.
            fos.write(file.getBytes());
        } // Fin du 'try-with-resources'. L'outil 'fos' est automatiquement fermé ici,
        // ce qui garantit que toutes les données sont bien écrites sur le disque.

        // Renvoie l'objet 'File' ('convFile') qui représente le fichier temporaire maintenant rempli.
        return convFile;
    } // Fin de la méthode 'convertMultiPartToFile'
}
