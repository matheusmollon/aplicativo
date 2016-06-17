CREATE TABLE usuario(
	u_nome 		varchar(30) 	not null,
	u_senha 	varchar(30) 	not null,
	u_celular 	varchar(20) 	not null,
	constraint u_pk
		primary key(u_nome)
);

CREATE TABLE conta(
	c_id 		serial 		not null,
	c_nome 		varchar(30) 	not null,
	c_valor_total 	money		not null,
	constraint c_pk 
		primary key(c_id)
);

CREATE TABLE usuario_conta(
	u_nome 			varchar(30) 	not null,
	c_id			integer		not null,
	u_c_gerente		varchar(30)	not null,
	u_c_valor_usuario	money		not null,
	constraint u_c_pk 
		primary key(u_nome, c_id),
	constraint u_nome_fk 
		foreign key (u_nome) references usuario(u_nome),
	constraint c_id_fk 
		foreign key (c_id) references conta(c_id)
);
