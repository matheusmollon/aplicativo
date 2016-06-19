CREATE TABLE usuario(
	u_nome 		VARCHAR(30) 	NOT NULL,
	u_senha 	VARCHAR(30) 	NOT NULL,
	u_celular 	VARCHAR(20) 	NOT NULL,
	CONSTRAINT u_pk
		PRIMARY KEY(u_nome)
);

CREATE TABLE conta(
	c_id 		SERIAL 		NOT NULL,
	c_nome 		VARCHAR(30) 	NOT NULL,
	c_valor		MONEY		NOT NULL,
	CONSTRAINT c_pk 
		PRIMARY KEY(c_id)
);

CREATE TABLE produto(
	p_id 		SERIAL		NOT NULL,
	p_nome		VARCHAR(30)	NOT NULL,
	p_valor		MONEY		NOT NULL,
	c_id		INTEGER		NOT NULL,
	CONSTRAINT p_pk
		PRIMARY KEY(p_id),
	CONSTRAINT c_id_fk
		FOREIGN KEY (c_id) REFERENCES conta(c_id)
);

CREATE TABLE usuario_conta(
	u_nome 		VARCHAR(30) 	NOT NULL,
	c_id		INTEGER		NOT NULL,
	u_c_gerente	VARCHAR(30)	NOT NULL,
	u_c_valor	MONEY		NOT NULL,
	CONSTRAINT u_c_pk 
		PRIMARY KEY(u_nome, c_id),
	CONSTRAINT u_nome_fk 
		FOREIGN KEY (u_nome) REFERENCES usuario(u_nome),
	CONSTRAINT c_id_fk 
		FOREIGN KEY (c_id) REFERENCES conta(c_id)
);

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
