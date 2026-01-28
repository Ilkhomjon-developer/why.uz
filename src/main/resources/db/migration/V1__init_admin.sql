insert into profile (username, password, status, created_date) values ('admin', '123', 'ACTIVE', now());
insert into profile_role (profile_id, roles, created_date) values (1, 'ROLE_ADMIN', now()),
                                                                  (1, 'ROLE_USER', now());

select setval('profile_id_seq', max(id)) from profile;