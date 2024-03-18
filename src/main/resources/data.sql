
-- inserting hashed value for password: pass1234
INSERT INTO races_user (id, type, username, full_name, password, email) VALUES
                                           (1, 'CUSTOMER', 'john_doe', 'John Doe', '$2a$10$/81TtTPU36hF78KPXXZpNeOx1CxZBJ.3hZsX8vXp8F06a9SBY.NW2', 'john.doe@example.com');

INSERT INTO dog (id, name, age, breed) VALUES
                                           (1, 'Buddy', 3, 'Greyhound'),
                                           (2, 'Max', 2, 'Whippet'),
                                           (3, 'Bella', 4, 'Italian Greyhound'),
                                           (4, 'Lucy', 5, 'Saluki'),
                                           (5, 'Charlie', 2, 'Borzoi'),
                                           (6, 'Molly', 3, 'Afghan Hound'),
                                           (7, 'Daisy', 4, 'Labrador'),
                                           (8, 'Bailey', 1, 'Beagle'),
                                           (9, 'Lola', 2, 'Bulldog'),
                                           (10, 'Sadie', 3, 'Poodle');

INSERT INTO race (id, name, race_time) VALUES
                                           (1, 'Spring Derby', '2024-04-05 15:00:00'),
                                           (2, 'Summer Sprint', '2024-06-20 15:00:00'),
                                           (3, 'Autumn Run', '2024-10-11 15:00:00'),
                                           (4, 'Winter Chase', '2024-12-15 15:00:00'),
                                           (5, 'Holiday Marathon', '2024-12-25 15:00:00'),
                                           (6, 'New Year Race', '2025-01-01 15:00:00'),
                                           (7, 'Valentine Dash', '2025-02-14 15:00:00'),
                                           (8, 'Easter Hunt', '2025-04-05 15:00:00'),
                                           (9, 'Freedom Gallop', '2025-07-04 15:00:00'),
                                           (10, 'Fall Classic', '2025-11-05 15:00:00');

INSERT INTO race_participation (id, race_id, dog_id, odds) VALUES
                                                               (1, 1, 1, 5.0),
                                                               (2, 1, 2, 6.0),
                                                               (3, 2, 3, 4.5),
                                                               (4, 2, 4, 3.2),
                                                               (5, 3, 5, 7.0),
                                                               (6, 3, 6, 8.0),
                                                               (7, 4, 7, 5.5),
                                                               (8, 4, 8, 9.0),
                                                               (9, 5, 9, 4.3),
                                                               (10, 5, 10, 2.5);

INSERT INTO bet (id, race_participation_id, amount, user_id) VALUES
                                                                 (1, 1, 100.00, 1),
                                                                 (2, 2, 150.00, 1),
                                                                 (3, 3, 200.00, 1),
                                                                 (4, 4, 250.00, 1),
                                                                 (5, 5, 300.00, 1),
                                                                 (6, 6, 350.00, 1),
                                                                 (7, 7, 400.00, 1),
                                                                 (8, 8, 450.00, 1),
                                                                 (9, 9, 500.00, 1),
                                                                 (10, 10, 550.00, 1);


SELECT setval('races_user_seq', (SELECT MAX(id) FROM races_user));
SELECT setval('dog_seq', (SELECT MAX(id) FROM dog));
SELECT setval('race_seq', (SELECT MAX(id) FROM race));
SELECT setval('race_participation_seq', (SELECT MAX(id) FROM race_participation));
SELECT setval('bet_seq', (SELECT MAX(id) FROM bet));
