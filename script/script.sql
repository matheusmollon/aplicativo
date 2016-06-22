--DROP TABLE conta;
CREATE TABLE conta(
	c_id 		SERIAL 		NOT NULL,
	c_nome 		VARCHAR(30) 	NOT NULL,
	c_valor		FLOAT		NOT NULL,
	CONSTRAINT c_pk 
		PRIMARY KEY(c_id)
);

--DROP TABLE produto;
CREATE TABLE produto(
	p_id 		SERIAL		NOT NULL,
	p_nome		VARCHAR(30)	NOT NULL,
	p_valor		FLOAT		NOT NULL,
	c_id		INTEGER		NOT NULL,
	CONSTRAINT p_pk
		PRIMARY KEY(p_id),
	CONSTRAINT c_id_fk
		FOREIGN KEY (c_id) REFERENCES conta(c_id)
);

--DROP TABLE usuario_conta;
CREATE TABLE usuario_conta(
	u_nome 		VARCHAR(30) 	NOT NULL,
	c_id		INTEGER		NOT NULL,
	u_c_gerente	VARCHAR(30)	NOT NULL,
	u_c_valor	FLOAT		NOT NULL,
	CONSTRAINT u_c_pk 
		PRIMARY KEY(u_nome, c_id),
	CONSTRAINT u_nome_fk 
		FOREIGN KEY (u_nome) REFERENCES usuario(u_nome),
	CONSTRAINT c_id_fk 
		FOREIGN KEY (c_id) REFERENCES conta(c_id)
);

--DROP TABLE produto_usuario;
CREATE TABLE produto_usuario(
	p_id		INTEGER		NOT NULL,
	u_nome		VARCHAR(30)	NOT NULL,
	CONSTRAINT p_u_pk
		PRIMARY KEY(p_id, u_nome),
	CONSTRAINT p_id_fk
		FOREIGN KEY (p_id) REFERENCES produto(p_id),
	CONSTRAINT u_nome_fk
		FOREIGN KEY (u_nome) REFERENCES usuario(u_nome)
);
