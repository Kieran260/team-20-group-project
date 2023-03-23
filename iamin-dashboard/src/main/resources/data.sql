-- SamplePerson data
INSERT INTO sample_person(id, version, first_name, last_name) VALUES (1, 1, 'John', 'Doe');

-- CheckInOut data
INSERT INTO check_in_out(id, version, person_id, check_in_time, check_out_time, date) VALUES (1, 1, 1, '09:00:00', '17:00:00', '2023-03-07');
INSERT INTO check_in_out(id, version, person_id, check_in_time, check_out_time, date) VALUES (2, 1, 1, '09:15:00', '17:30:00', '2023-03-08');
INSERT INTO check_in_out(id, version, person_id, check_in_time, check_out_time, date) VALUES (3, 1, 1, '08:45:00', '16:45:00', '2023-03-09');
