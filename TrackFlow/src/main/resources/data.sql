


INSERT INTO users (id, first_name, last_name, login, password, creation_date, update_date, suppression_date, user_role) VALUES (1, 'David', 'Bowie', 'dbowie@music.com', 'passwordBowie123!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'ARTIST');
INSERT INTO musicians (id, address, mobile, biography, picture) VALUES (1, '123 Stardust Lane, London', '0601020304', 'Iconic singer, songwriter, and actor.', 'https://example.com/images/bowie.jpg');

INSERT INTO users (id, first_name, last_name, login, password, creation_date, update_date, suppression_date, user_role) VALUES (2, 'Joni', 'Mitchell', 'jmitchell@music.com', 'passwordJoni456!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'ARTIST');
INSERT INTO musicians (id, address, mobile, biography, picture) VALUES (2, '456 Blue Motel Way, CA', '0698765432', 'Influential singer-songwriter and painter.', 'https://example.com/images/mitchell.jpg');



INSERT INTO projects (id, title, description, illustration, is_archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (1, 'Mon Premier Album Rock', 'Un album concept sur les voyages temporels.', 'http://example.com/images/album1.jpg', false, 1, '2024-03-15 10:00:00', '2024-04-01 11:30:00', NULL, NULL, 1, 'EN_COURS', 'ALBUM', 'EXCLUSIF');
INSERT INTO projects (id, title, description, illustration, is_archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (2, 'EP Electro Chill', 'Quelques tracks pour se détendre.', NULL, false, 2, '2024-03-20 14:00:00', '2024-03-28 09:00:00', NULL, NULL, 1, 'FINALISE', 'EP', 'STREAMING');
INSERT INTO projects (id, title, description, illustration, is_archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (3, 'Single Pop Été 2024', 'Le prochain hit de l\'été !', 'http://example.com/images/single_summer.png', false, 1, '2024-04-01 08:00:00', '2024-04-01 08:00:00', NULL, NULL, 2, 'EN_COURS', 'SINGLE', 'LABEL');
INSERT INTO projects (id, title, description, illustration, is_archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (4, 'Musique pour Jeu Vidéo "Galaxy Quest"', 'Bande originale pour un jeu indépendant.', 'http://example.com/images/game_gq.jpg', false, 3, '2023-11-01 16:45:00', '2024-02-10 18:00:00', NULL, NULL, 1, 'FINALISE', 'VIDEOGAME', 'COMMANDE');
INSERT INTO projects (id, title, description, illustration, is_archived, project_order, created_date, last_update_date, supression_date, definitiv_supression_date, user_id, project_status, project_type, project_commercial_status) VALUES (5, 'Projet perso Lo-Fi', NULL, NULL, false, 4, '2024-04-02 12:00:00', '2024-04-02 12:00:00', NULL, NULL, 2, 'EN_COURS', 'MAQUETTE', 'PERSONNEL');

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


INSERT INTO compositions (id, title, description, composition_order, illustration, created_date, last_update_date, suppression_date, definitiv_supression_date, project_id, composition_status) VALUES (1, 'Mon Premier Thème', 'Idée initiale de la composition pour le projet rock', 1, '/images/default_comp.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, 1, 'EBAUCHE');

