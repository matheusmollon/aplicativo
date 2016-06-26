-- Testes para o CRUD produto

-- executar estas instruções antes de testar
insert into usuario values ('Teste','1','43 87239033');
insert into conta values(100,'Costela',0,false,'Teste');
insert into usuario_conta values('Teste',100,0);
-- fim 

--Testes para o cálculo da divisão pelo consumo (Cenários):

-- Teste Cálculo funcional divisão por pessoa (Roteiro 1):

insert into usuario values ('Pessoa1','1','43 87239033');
insert into usuario values ('Pessoa2','1','43 87239033');
insert into usuario values ('Pessoa3','1','43 87239033');
insert into usuario values ('Pessoa4','1','43 87239033');
insert into usuario values ('Pessoa5','1','43 87239033');
insert into usuario values ('Pessoa6','1','43 87239033');

insert into conta values(101,'Costela',0,false,'Pessoa1');

insert into usuario_conta values('Pessoa1',101,0);
insert into usuario_conta values('Pessoa2',101,0);
insert into usuario_conta values('Pessoa3',101,0);
insert into usuario_conta values('Pessoa4',101,0);
insert into usuario_conta values('Pessoa5',101,0);
insert into usuario_conta values('Pessoa6',101,0);

insert into produto values(500,'Batata Frita',30.0,101);
insert into produto values(501,'Frango Empanado',35.0,101);
insert into produto values(502,'Torre de Chopp',40.0,101);


insert into produto_usuario values(500,'Pessoa1');
insert into produto_usuario values(500,'Pessoa2');
insert into produto_usuario values(500,'Pessoa3');
insert into produto_usuario values(500,'Pessoa4');
insert into produto_usuario values(500,'Pessoa5');
insert into produto_usuario values(500,'Pessoa6');


insert into produto_usuario values(501,'Pessoa1');
insert into produto_usuario values(501,'Pessoa2');
insert into produto_usuario values(501,'Pessoa3');
insert into produto_usuario values(501,'Pessoa4');
insert into produto_usuario values(501,'Pessoa5');
insert into produto_usuario values(501,'Pessoa6');


insert into produto_usuario values(502,'Pessoa1');
insert into produto_usuario values(502,'Pessoa2');
insert into produto_usuario values(502,'Pessoa3');
insert into produto_usuario values(502,'Pessoa4');
insert into produto_usuario values(502,'Pessoa5');
insert into produto_usuario values(502,'Pessoa6');

------------------ Fim do Roteiro 1------------------

-- Teste Cálculo funcional divisão por pessoa (Roteiro 2):

insert into usuario values ('User1','1','43 87239033');
insert into usuario values ('User2','1','43 87239033');
insert into usuario values ('User3','1','43 87239033');


insert into conta values(102,'Costela 2',0,false,'User1');

insert into usuario_conta values('User1',102,0);
insert into usuario_conta values('User2',102,0);
insert into usuario_conta values('User3',102,0);


insert into produto values(503,'Batata Frita',30.0,102);
insert into produto values(504,'Frango Empanado',35.0,102);
insert into produto values(505,'Torre de Chopp',40.0,102);


insert into produto_usuario values(503,'User1');
insert into produto_usuario values(503,'User3');

insert into produto_usuario values(504,'User2');
insert into produto_usuario values(504,'User3');

insert into produto_usuario values(505,'User1');
insert into produto_usuario values(505,'User2');
insert into produto_usuario values(505,'User3');

------------------ Fim do Roteiro 2------------------


