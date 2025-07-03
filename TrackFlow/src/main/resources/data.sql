


-- Utilisateur musicien avec profil ARTIST
INSERT INTO users (first_name, last_name, login, password, security_role, profile_type, creation_date, update_date, suppression_date) VALUES ('David', 'Bowie', 'dbowie@music.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P1ISdqdbQ2sURS', 'ROLE_MUSICIAN', 'ARTIST', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- Utilisateur musicien avec profil PRODUCER
INSERT INTO users (first_name, last_name, login, password, security_role, profile_type, creation_date, update_date, suppression_date) VALUES ('Max', 'Martin', 'mmartin@music.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P1ISdqdbQ2sURS', 'ROLE_MUSICIAN', 'PRODUCER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- Utilisateur musicien avec profil ENGINEER
INSERT INTO users (first_name, last_name, login, password, security_role, profile_type, creation_date, update_date, suppression_date) VALUES ('Rick', 'Rubin', 'rrubin@music.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P1ISdqdbQ2sURS', 'ROLE_MUSICIAN', 'ENGINEER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- Utilisateur admin (sans profile_type)
INSERT INTO users (first_name, last_name, login, password, security_role, profile_type, creation_date, update_date, suppression_date) VALUES ('Admin', 'System', 'admin@trackflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P1ISdqdbQ2sURS', 'ROLE_ADMIN', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);


INSERT INTO projects (id, title, description, illustration, archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (1, 'Mon Premier Album Rock', 'Un album concept sur les voyages temporels.', 'http://example.com/images/album1.jpg', false, 1, '2024-03-15 10:00:00', '2024-04-01 11:30:00', NULL, NULL, 1, 'EN_COURS', 'ALBUM', 'EXCLUSIF');
INSERT INTO projects (id, title, description, illustration, archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (2, 'EP Electro Chill', 'Quelques tracks pour se détendre.', NULL, false, 2, '2024-03-20 14:00:00', '2024-03-28 09:00:00', NULL, NULL, 1, 'FINALISE', 'EP', 'STREAMING');
INSERT INTO projects (id, title, description, illustration, archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (3, 'Single Pop Été 2024', 'Le prochain hit de l\'été !', 'http://example.com/images/single_summer.png', false, 1, '2024-04-01 08:00:00', '2024-04-01 08:00:00', NULL, NULL, 2, 'EN_COURS', 'SINGLE', 'LABEL');
INSERT INTO projects (id, title, description, illustration, archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (4, 'Musique pour Jeu Vidéo "Galaxy Quest"', 'Bande originale pour un jeu indépendant.', 'http://example.com/images/game_gq.jpg', false, 3, '2023-11-01 16:45:00', '2024-02-10 18:00:00', NULL, NULL, 1, 'FINALISE', 'VIDEOGAME', 'COMMANDE');
INSERT INTO projects (id, title, description, illustration, archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (5, 'Projet perso Lo-Fi', NULL, NULL, false, 4, '2024-04-02 12:00:00', '2024-04-02 12:00:00', NULL, NULL, 2, 'EN_COURS', 'MAQUETTE', 'PERSONNEL');

-- Insertion des buts pour les projets (table 'project_purpose')
-- Projet 1 (ID=1)
INSERT INTO project_purpose (project_id, purpose) VALUES (1, 'LABEL');
INSERT INTO project_purpose (project_id, purpose) VALUES (1, 'STREAMING');
-- Projet 2 (ID=2)
INSERT INTO project_purpose (project_id, purpose) VALUES (2, 'STREAMING');
INSERT INTO project_purpose (project_id, purpose) VALUES (2, 'PERSONNEL');
-- Projet 3 (ID=3)
INSERT INTO project_purpose (project_id, purpose) VALUES (3, 'LABEL');
INSERT INTO project_purpose (project_id, purpose) VALUES (3, 'RADIO');
-- Projet 4 (ID=4)
INSERT INTO project_purpose (project_id, purpose) VALUES (4, 'VIDEOGAME');
INSERT INTO project_purpose (project_id, purpose) VALUES (4, 'COMMANDE');
-- Projet 5 (ID=5)
INSERT INTO project_purpose (project_id, purpose) VALUES (5, 'PERSONNEL');

-- Insertion des genres musicaux prédéfinis pour les projets (table 'project_musical_gender_predefined')
-- Projet 1 (ID=1)
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (1, 'ROCK');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (1, 'PROGRESSIVE_ROCK');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (1, 'ALTERNATIVE');
-- Projet 2 (ID=2)
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (2, 'ELECTRONIC');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (2, 'AMBIENT');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (2, 'LOUNGE');
-- Projet 3 (ID=3)
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (3, 'POP');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (3, 'SYNTHPOP');
-- Projet 4 (ID=4)
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (4, 'CLASSICAL');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (4, 'ELECTRONIC');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (4, 'AMBIENT');
-- Projet 5 (ID=5)
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (5, 'LO_FI');
INSERT INTO project_musical_gender_predefined (project_id, gender) VALUES (5, 'HIP_HOP');

-- =================================================================
-- PAS D'ABORD : Vider les tables dépendantes pour repartir à zéro
-- L'ORDRE EST IMPORTANT POUR RESPECTER LES CLÉS ÉTRANGÈRES.
-- =================================================================
DELETE FROM version_instruments;
DELETE FROM annotations;
DELETE FROM versions;
DELETE FROM branches;
DELETE FROM composition_sub_genders;
DELETE FROM compositions;

-- =================================================================
-- 1. COMPOSITIONS
-- =================================================================
INSERT INTO compositions (id, title, description, composition_order, illustration, project_id, composition_status, created_date, last_update_date) VALUES (1, 'Voyage Temporel - Intro', 'La pièce d''ouverture de l''album concept.', 1, 'http://example.com/images/compo1.jpg', 1, 'EN_COURS', '2024-04-10 10:00:00', '2024-04-15 11:00:00');
INSERT INTO compositions (id, title, description, composition_order, illustration, project_id, composition_status, created_date, last_update_date) VALUES (2, 'Le Paradoxe du Grand-père', 'Morceau central, plus complexe et rock progressif.', 2, 'http://example.com/images/compo2.jpg', 1, 'EBAUCHE', '2024-04-12 14:00:00', '2024-04-12 14:00:00');
INSERT INTO compositions (id, title, description, composition_order, illustration, project_id, composition_status, created_date, last_update_date) VALUES (3, 'Sunset Drive', 'Le premier titre de l''EP Electro Chill.', 1, 'http://example.com/images/compo3.jpg', 2, 'TERMINE', '2024-03-22 09:00:00', '2024-03-25 18:00:00');
INSERT INTO compositions (id, title, description, composition_order, illustration, project_id, composition_status, created_date, last_update_date) VALUES (4, 'Plage Oubliée', 'Ambiance Lo-fi pour un projet perso.', 1, NULL, 5, 'EBAUCHE', '2024-04-03 15:00:00', '2024-04-03 15:00:00');

-- Sous-genres pour les compositions
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (1, 'Rock Alternatif');
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (1, 'Space Rock');
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (2, 'Rock Progressif');
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (3, 'Chillwave');
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (3, 'Downtempo');
INSERT INTO composition_sub_genders (composition_id, sub_gender) VALUES (4, 'Lo-Fi Hip-Hop');

-- =================================================================
-- 2. BRANCHES
-- =================================================================
INSERT INTO branches (id, name, description, composition_id, parent_branch_id, created_date, last_update_date) VALUES (1, 'main', 'Branche principale pour "Voyage Temporel - Intro"', 1, NULL, '2024-04-10 10:05:00', '2024-04-10 10:05:00');
INSERT INTO branches (id, name, description, composition_id, parent_branch_id, created_date, last_update_date) VALUES (2, 'main', 'Branche principale pour "Le Paradoxe du Grand-père"', 2, NULL, '2024-04-12 14:05:00', '2024-04-12 14:05:00');
INSERT INTO branches (id, name, description, composition_id, parent_branch_id, created_date, last_update_date) VALUES (3, 'main', 'Branche principale pour "Sunset Drive"', 3, NULL, '2024-03-22 09:05:00', '2024-03-22 09:05:00');
INSERT INTO branches (id, name, description, composition_id, parent_branch_id, created_date, last_update_date) VALUES (4, 'main', 'Branche principale pour "Plage Oubliée"', 4, NULL, '2024-04-03 15:05:00', '2024-04-03 15:05:00');

-- =================================================================
-- 3. VERSIONS
-- =================================================================
INSERT INTO versions (id, name, author, bpm, `key`, duration_seconds, audio_file_url, branch_id, parent_version_id, created_date) VALUES (1, 'V1.0', 'David Bowie', '120', 'Am', 185, 'https://storage.googleapis.com/my-audio-file-bucket/musiques/166a1f3b-b713-4b0b-a085-02b07b865e6e.mp3', 1, NULL, '2024-04-11 12:00:00');
INSERT INTO versions (id, name, author, bpm, `key`, duration_seconds, audio_file_url, branch_id, parent_version_id, created_date) VALUES (2, 'V1.1', 'David Bowie', '122', 'Am', 190, 'https://storage.googleapis.com/my-audio-file-bucket/musiques/166a1f3b-b713-4b0b-a085-02b07b865e6e.mp3', 1, 1, '2024-04-14 09:30:00');
INSERT INTO versions (id, name, author, bpm, `key`, duration_seconds, audio_file_url, branch_id, parent_version_id, created_date) VALUES (5, 'V1.2', 'David Bowie', '120', 'Am', 45,  'https://storage.googleapis.com/my-audio-file-bucket/musiques/166a1f3b-b713-4b0b-a085-02b07b865e6e.mp3', 1, 2, '2024-04-18 16:30:00');
INSERT INTO versions (id, name, author, bpm, `key`, duration_seconds, audio_file_url, branch_id, parent_version_id, created_date) VALUES (3, 'V1.0', 'David Bowie', '95', 'Dm', 240, 'https://storage.googleapis.com/my-audio-file-bucket/musiques/166a1f3b-b713-4b0b-a085-02b07b865e6e.mp3', 2, NULL, '2024-04-13 11:00:00');
INSERT INTO versions (id, name, author, bpm, `key`, duration_seconds, audio_file_url, branch_id, parent_version_id, created_date) VALUES (4, 'V1.0', 'David Bowie', '100', 'C#m', 210, 'https://storage.googleapis.com/my-audio-file-bucket/musiques/166a1f3b-b713-4b0b-a085-02b07b865e6e.mp3', 3, NULL, '2024-03-23 10:00:00');

-- =================================================================
-- 4. INSTRUMENTS DES VERSIONS
-- =================================================================
INSERT INTO version_instruments (id, version_id, instrument) VALUES (1, 1, 'GUITARE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (2, 1, 'GUITARE_BASSE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (3, 1, 'BATTERIE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (4, 1, 'SYNTHETISEUR');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (5, 2, 'GUITARE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (6, 2, 'GUITARE_BASSE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (7, 2, 'BATTERIE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (8, 2, 'SYNTHETISEUR');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (9, 2, 'CHANT');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (10, 3, 'PIANO');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (11, 3, 'VIOLONCELLE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (12, 3, 'BATTERIE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (13, 4, 'SYNTHETISEUR');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (14, 4, 'SAMPLER');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (15, 4, 'GUITARE_BASSE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (16, 5, 'GUITARE');
INSERT INTO version_instruments (id, version_id, instrument) VALUES (17, 5, 'SYNTHETISEUR');
-- =================================================================
-- 5. ANNOTATIONS DES VERSIONS
-- =================================================================
INSERT INTO annotations (id, content, time_position, version_id, is_resolved, annotation_category, annotation_status, creation_date) VALUES (1, 'La ligne de basse pourrait être plus présente à ce moment.', 32.5, 1, false, 'MIXAGE', 'A_TRAITER', '2024-04-12 15:00:00');
INSERT INTO annotations (id, content, time_position, version_id, is_resolved, annotation_category, annotation_status, creation_date) VALUES (2, 'Le solo de guitare est parfait, ne rien changer !', 121.0, 1, true, 'GENERAL', 'RESOLUE', '2024-04-12 15:02:00');
INSERT INTO annotations (id, content, time_position, version_id, is_resolved, annotation_category, annotation_status, creation_date) VALUES (3, 'Idée : Ajouter une nappe de synthé discrète ici.', 90.2, 1, false, 'MELODIC_IDEA', 'EN_REFLEXION', '2024-04-13 10:20:00');