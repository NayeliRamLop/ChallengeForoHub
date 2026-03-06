CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(100) NOT NULL,
    clave VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_login UNIQUE (login)
);

INSERT INTO usuarios (login, clave)
VALUES ('admin', '{noop}admin123');
