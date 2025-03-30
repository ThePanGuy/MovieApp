-- Sequences
CREATE SEQUENCE movie_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE movie_user_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE reaction_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE movie_role_id_seq START WITH 1 INCREMENT BY 1;

-- Movie User Table
CREATE TABLE movie_user (
                            id BIGINT NOT NULL DEFAULT nextval('movie_user_id_seq'),
                            username VARCHAR(255) NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id)
);

-- Movie Table
CREATE TABLE movie (
                       id BIGINT NOT NULL DEFAULT nextval('movie_id_seq'),
                       title VARCHAR(255),
                       description TEXT,
                       creation_date TIMESTAMP,
                       uploaded_by BIGINT,
                       PRIMARY KEY (id),
                       FOREIGN KEY (uploaded_by) REFERENCES movie_user(id) ON DELETE SET NULL
);

-- Movie Role Table
CREATE TABLE movie_role (
                            id SERIAL NOT NULL ,
                            name VARCHAR(255),
                            PRIMARY KEY (id)
);

-- Movie User Role Table (Many-to-Many)
CREATE TABLE movie_user_role (
                                 user_id BIGINT NOT NULL DEFAULT nextval('movie_role_id_seq'),
                                 role_id BIGINT NOT NULL,
                                 PRIMARY KEY (user_id, role_id),
                                 FOREIGN KEY (user_id) REFERENCES movie_user(id) ON DELETE CASCADE,
                                 FOREIGN KEY (role_id) REFERENCES movie_role(id) ON DELETE CASCADE
);

-- Reaction Table
CREATE TABLE reaction (
                          id BIGINT NOT NULL DEFAULT nextval('reaction_id_seq'),
                          user_id BIGINT NOT NULL,
                          movie_id BIGINT NOT NULL,
                          type VARCHAR(10) NOT NULL,
                          PRIMARY KEY (id),
                          FOREIGN KEY (user_id) REFERENCES movie_user(id) ON DELETE CASCADE,
                          FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);