ALTER TABLE topicos
ADD CONSTRAINT uk_topicos_titulo_mensaje UNIQUE (titulo, mensaje);
