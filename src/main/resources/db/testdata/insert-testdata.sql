INSERT INTO user_data (username, password, password_last_updated, full_name, email, reset_required) VALUES ('officer1','$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy',CURRENT_TIMESTAMP,'Listing Officer 1','snl_officer1@HMCTS.net',false);
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer1'), 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer1'), 'ROLE_OFFICER');
INSERT INTO user_data (username, password, password_last_updated, full_name, email) VALUES ('officer_reset_required','$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy',CURRENT_TIMESTAMP,'Listing Officer 1','snl_officer1@HMCTS.net');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer_reset_required'), 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer_reset_required'), 'ROLE_OFFICER');
INSERT INTO user_data (username, password, password_last_updated, full_name, email, enabled, reset_required) VALUES ('officer_not_enabled','$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy',CURRENT_TIMESTAMP,'Listing Officer 1','snl_officer1@HMCTS.net',false,false);
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer_not_enabled'), 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='officer_not_enabled'), 'ROLE_OFFICER');
INSERT INTO user_data (username, password, password_last_updated, full_name, email, reset_required) VALUES ('judge1','$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy',CURRENT_TIMESTAMP,'John Harris','snl_judge1@HMCTS.net',false);
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='judge1'), 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='judge1'), 'ROLE_JUDGE');
INSERT INTO user_data (username, password, password_last_updated, full_name, email, reset_required) VALUES ('admin','$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy',CURRENT_TIMESTAMP,'Administrator man','snl_admin@HMCTS.net',false);
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='admin'), 'ROLE_USER');
INSERT INTO user_roles (user_id, role) VALUES ((SELECT id from user_data where username='admin'), 'ROLE_ADMIN');
