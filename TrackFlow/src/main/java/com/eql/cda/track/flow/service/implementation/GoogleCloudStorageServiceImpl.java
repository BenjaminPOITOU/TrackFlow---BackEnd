package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.service.StorageService;
import com.google.cloud.WriteChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class GoogleCloudStorageServiceImpl implements StorageService {


    private static final Logger logger = LogManager.getLogger();
    // Déclare une variable 'storage' qui contiendra l'outil principal pour parler à l'API Google Cloud Storage.
    private final Storage storage;

    // Déclare une variable 'bucketName' qui contiendra le nom de ton "seau" (bucket) GCS où les fichiers seront stockés.
    private final String bucketName;

    @Autowired
    public GoogleCloudStorageServiceImpl(Storage storage, @Value("${gcs.bucket.name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(MultipartFile file, String destinationFileName) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("Cannot upload empty file.");
        }

        BlobId blobId = BlobId.of(bucketName, destinationFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try (WriteChannel writer = storage.writer(blobInfo);
             InputStream input = file.getInputStream()) {

            byte[] buffer = new byte[1024];
            int limit;
            while ((limit = input.read(buffer)) >= 0) {
                writer.write(java.nio.ByteBuffer.wrap(buffer, 0, limit));
            }

        } catch (StorageException e) {
            System.err.println("GCS Upload Error: " + e.getMessage());
            throw new IOException("Failed to upload file to GCS: " + e.getMessage(), e);
        }

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, destinationFileName);
    }


    /** Section de commentaire expliquant ce que fait la méthode deleteFile. */
    // @Override : Implémente la méthode 'deleteFile' de l'interface 'StorageService'.
    // Méthode pour supprimer un fichier de GCS.
    // Paramètre :
    //   - String objectPath : Le chemin/nom du fichier à supprimer DANS le bucket (ex: "uploads/mon_audio.mp3").
    // Retourne :
    //   - 'true' si le fichier a été supprimé OU s'il n'existait déjà pas.
    //   - 'false' si une erreur GCS s'est produite pendant la tentative de suppression.
    @Override
    public boolean deleteFile(String objectPath) {
        // Écrit dans les logs qu'on essaie de supprimer ce fichier.
        logger.info("Attempting to delete object '{}' from bucket '{}'", objectPath, bucketName);

        // Vérifie si le chemin fourni est vide ou invalide.
        if (objectPath == null || objectPath.isBlank()) {
            // Si oui, écrit un avertissement dans les logs.
            logger.warn("Deletion skipped: objectPath is null or blank.");
            // Considère que l'objectif (fichier non présent à ce chemin) est atteint. Retourne 'true'.
            return true;
        }

        // Bloc 'try' : Les opérations avec GCS peuvent échouer.
        try {
            // Crée l'identifiant GCS (BlobId) pour le fichier à supprimer, en utilisant le nom du bucket et le chemin de l'objet.
            BlobId blobId = BlobId.of(bucketName, objectPath);

            // Appelle l'API Google pour supprimer le fichier. La méthode renvoie 'true' si ça a marché, 'false' sinon.
            boolean deleted = storage.delete(blobId);

            // Si GCS a confirmé la suppression (retourne 'true')...
            if (deleted) {
                // Écrit dans les logs que la suppression a réussi.
                logger.info("Successfully deleted object '{}' from bucket '{}'", objectPath, bucketName);
                // Sinon (si GCS retourne 'false')...
            } else {
                // Cela signifie généralement que le fichier n'a pas été trouvé.
                // Écrit un avertissement : le fichier n'était peut-être déjà plus là.
                logger.warn("Object '{}' not found in bucket '{}'. It might have been already deleted or the path is incorrect.", objectPath, bucketName);
                // **Décision importante** : On considère "non trouvé" comme un succès car le résultat final (fichier absent) est ce qu'on veut.
                deleted = true;
            }
            // Retourne le résultat ('true' dans les deux cas : supprimé ou non trouvé).
            return deleted;

            // Bloc 'catch' : Si une erreur GCS s'est produite (ex: pas les droits, problème réseau)...
        } catch (StorageException e) {
            // Écrit une erreur grave dans les logs avec les détails de l'erreur GCS.
            logger.error("GCS Deletion Error for object '{}' in bucket '{}': {}", objectPath, bucketName, e.getMessage(), e);

            // **Décision importante** : On retourne 'false' pour signaler qu'il y a eu un vrai problème.
            // Le code qui appelle cette méthode (VersionServiceImpl) verra 'false' et pourra décider quoi faire
            // (ex: loguer un avertissement mais continuer, ou arrêter toute l'opération).
            // Le commentaire explique qu'on pourrait aussi lancer une exception ici pour forcer l'arrêt.
            return false;
        }
    }

    // @Override : Implémente la méthode 'extractObjectPathFromUrl' de l'interface 'StorageService'.
    // Méthode pour extraire le chemin de l'objet (le nom/chemin dans le bucket) à partir d'une URL GCS complète.
    // Paramètre :
    //   - String fileUrl : L'URL complète (ex: "https://storage.googleapis.com/mon-bucket/dossier/fichier.wav").
    // 'throws java.net.URISyntaxException' : Peut échouer si l'URL est mal formée.
    // Retourne : Le chemin de l'objet (ex: "dossier/fichier.wav") ou 'null' si l'extraction échoue.
    @Override
    public String extractObjectPathFromUrl(String fileUrl) throws java.net.URISyntaxException {
        // Écrit dans les logs qu'on tente d'extraire le chemin.
        logger.debug("Attempting to extract object path from URL: {}", fileUrl);

        // Vérifie si l'URL est fournie et si elle commence bien par le préfixe standard des URL publiques GCS.
        if (fileUrl == null || !fileUrl.startsWith("https://storage.googleapis.com/")) {
            // Si non, écrit un avertissement et retourne 'null' (impossible d'extraire).
            logger.warn("URL is null or does not start with 'https://storage.googleapis.com/'.");
            return null;
        }

        // Utilise un outil Java (URI) pour analyser correctement l'URL (gère les caractères spéciaux etc.).
        java.net.URI uri = new java.net.URI(fileUrl);
        // Extrait la partie "chemin" de l'URL (ex: "/mon-bucket/dossier/fichier.wav").
        String path = uri.getPath();
        // Écrit le chemin extrait dans les logs.
        logger.debug("Extracted path component: {}", path);

        // Construit le préfixe attendu en utilisant le nom du bucket stocké dans CETTE classe.
        // Ex: "/" + "mon-bucket" + "/"  -> "/mon-bucket/"
        String expectedPrefix = "/" + this.bucketName + "/";

        // Vérifie si le chemin extrait commence bien par "/nom-du-bucket/".
        if (path.startsWith(expectedPrefix)) {
            // Si oui, prend tout ce qui vient APRES ce préfixe. C'est le chemin de l'objet.
            String objectPath = path.substring(expectedPrefix.length());
            // Écrit le chemin de l'objet trouvé dans les logs.
            logger.info("Extracted object path: '{}'", objectPath);
            // Retourne le chemin de l'objet.
            return objectPath;
            // Sinon (le chemin ne commence pas comme attendu)...
        } else {
            // Écrit un avertissement.
            logger.warn("URL path '{}' does not start with expected prefix '{}'. Trying fallback.", path, expectedPrefix);
            // Tente une méthode de secours moins fiable (couper l'URL en morceaux).
            String[] parts = fileUrl.split("/", 5); // Coupe au max 5 fois sur '/'
            // Vérifie si on a bien 5 morceaux, si le dernier n'est pas vide, et si l'URL commence bien par https://.../bucket/
            if (parts.length == 5 && !parts[4].isEmpty() && fileUrl.startsWith("https://storage.googleapis.com/" + this.bucketName + "/")) {
                // Prend le 5ème morceau comme chemin de l'objet.
                String objectPath = parts[4];
                logger.info("Extracted object path via fallback: '{}'", objectPath);
                return objectPath;
            }
            // Si la méthode de secours échoue aussi, écrit une erreur grave.
            logger.error("Failed to extract object path from URL: {}. No match.", fileUrl);
            // Retourne 'null' car l'extraction a échoué.
            return null;
        }
    }


    // Méthode utilitaire (helper) pour créer un nom de fichier unique.
    // Paramètre :
    //   - String originalFileName : Le nom de fichier original (ex: "mon_super_mix.mp3").
    // Retourne : Un nouveau nom de fichier probablement unique (ex: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx.mp3").
    public String generateUniqueFileName(String originalFileName) {
        // Prépare une variable pour stocker l'extension du fichier (ex: ".mp3").
        String extension = "";
        // Trouve la position du dernier '.' dans le nom de fichier.
        int i = originalFileName.lastIndexOf('.');
        // Si un '.' a été trouvé et n'est pas le premier caractère...
        if (i > 0) {
            // Extrait l'extension (y compris le point).
            extension = originalFileName.substring(i);
        }
        // Génère un identifiant unique universel (UUID, une longue chaîne aléatoire).
        // Le transforme en texte.
        // Ajoute l'extension originale à la fin.
        // Retourne ce nouveau nom unique.
        return UUID.randomUUID().toString() + extension;
    }
}

