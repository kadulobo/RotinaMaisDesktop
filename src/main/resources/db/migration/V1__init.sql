--pg_dump -U kadu -h localhost -d RotinaMais  -f C:\Users\User\Desktop\planilha\dumpRotinaMais.sql
-- Dropar o schema 'ManicureMarina' se já existir
--DROP SCHEMA IF EXISTS RotinaMais CASCADE;

Begin;

CREATE SCHEMA IF not EXISTS rotinamais;

-- Setar o search_path para o schema 'ManicureMarina'
SET search_path TO rotinamais;


CREATE TABLE IF NOT EXISTS Categoria (
id_categoria serial PRIMARY KEY,
nome text,
descricao text,
foto bytea,
status integer,
data_criacao date
);

CREATE TABLE IF NOT EXISTS Evento (
id_evento serial PRIMARY KEY,
vantagem boolean,
foto bytea,
nome text,
descricao text,
data_criacao date,
status integer,
id_categoria integer,
FOREIGN KEY(id_categoria) REFERENCES Categoria (id_categoria)
);

CREATE TABLE IF NOT EXISTS Lancamento (
id_lancamento serial PRIMARY KEY,
valor numeric(20,2),
fixo boolean,
data_pagamento date,
status integer,
id_movimentacao integer,
id_evento integer,
FOREIGN KEY(id_evento) REFERENCES Evento (id_evento)
);

CREATE TABLE IF NOT EXISTS Exercicio (
id_exercicio serial PRIMARY KEY,
nome text,
carga_leve integer,
carga_media integer,
carga_maxima integer,
foto bytea,
video bytea
);

CREATE TABLE IF NOT EXISTS Documento (
id_documento serial PRIMARY KEY,
nome text,
arquivo bytea,
foto bytea,
video bytea,
data date,
id_usuario integer
);

CREATE TABLE IF NOT EXISTS Movimentacao (
id_movimentacao serial PRIMARY KEY,
desconto numeric(20,2),
vantagem numeric(20,2),
liquido numeric(20,2),
tipo integer,
status integer,
ponto integer,
id_usuario integer,
id_caixa integer,
id_periodo integer
);

CREATE TABLE IF NOT EXISTS Rotina (
id_rotina serial PRIMARY KEY,
nome text,
inicio date,
fim date,
descricao text,
status integer,
ponto integer,
id_usuario integer
);

CREATE TABLE IF NOT EXISTS Fornecedor (
id_fornecedor serial PRIMARY KEY,
nome text,
foto bytea,
endereco text,
online boolean
);

CREATE TABLE IF NOT EXISTS Ingrediente (
foto bytea,
id_ingrediente serial PRIMARY KEY,
nome text,
descricao text
);

CREATE TABLE IF NOT EXISTS Monitoramento (
id_monitoramento serial PRIMARY KEY,
status integer,
nome text,
descricao text,
foto bytea,
id_periodo integer
);

CREATE TABLE IF NOT EXISTS Alimentacao (
status integer,
nome text,
link text,
video bytea,
id_alimentacao serial PRIMARY KEY,
preparo text,
id_rotina integer,
FOREIGN KEY(id_rotina) REFERENCES Rotina (id_rotina)
);

CREATE TABLE IF NOT EXISTS Treino (
id_treino serial PRIMARY KEY,
nome text,
classe text,
id_rotina integer,
FOREIGN KEY(id_rotina) REFERENCES Rotina (id_rotina)
);

CREATE TABLE IF NOT EXISTS Objeto (
id_objeto serial PRIMARY KEY,
nome text,
tipo integer,
valor numeric(20,2),
descricao text,
foto bytea
);

CREATE TABLE IF NOT EXISTS Site (
id_site serial PRIMARY KEY,
url text,
ativo boolean,
logo bytea
);

CREATE TABLE IF NOT EXISTS Caixa (
id_caixa serial PRIMARY KEY,
nome text,
reserva_emergencia numeric(20,2),
salario_medio numeric(20,2),
valor_total numeric(20,2),
id_usuario integer
);

CREATE TABLE IF NOT EXISTS Periodo (
id_periodo serial PRIMARY KEY,
ano integer,
mes integer
);

CREATE TABLE IF NOT EXISTS Meta (
id_meta serial PRIMARY KEY,
ponto_minimo integer,
ponto_medio integer,
ponto_maximo integer,
status integer,
foto bytea,
id_periodo integer,
FOREIGN KEY(id_periodo) REFERENCES Periodo (id_periodo)
);

CREATE TABLE IF NOT EXISTS Usuario (
nome text,
senha text,
foto bytea,
id_usuario serial PRIMARY KEY,
email text,
cpf text
);

CREATE TABLE IF NOT EXISTS Caderno (
id_caderno serial PRIMARY KEY,
nome_ia text,
titulo text,
objetivo text,
comando text,
resultado text,
data date,
resultado_imagem bytea,
resultado_video bytea,
id_usuario integer,
id_categoria integer,
FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario),
FOREIGN KEY(id_categoria) REFERENCES Categoria (id_categoria)
);


CREATE TABLE IF NOT EXISTS Cofre (
id_cofre serial PRIMARY KEY,
login text,
senha text,
tipo integer,
foto bytea,
plataforma text,
id_usuario integer,
FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario)
);

CREATE TABLE IF NOT EXISTS Treino_Exercicio (
qtd_repeticao integer,
tempo_descanso text,
ordem integer,
feito boolean,
id_treino_exercicio serial PRIMARY KEY,
id_exercicio integer,
id_treino integer,
FOREIGN KEY(id_exercicio) REFERENCES Exercicio (id_exercicio),
FOREIGN KEY(id_treino) REFERENCES Treino (id_treino)
);

CREATE TABLE IF NOT EXISTS Rotina_Periodo (
id_rotina_periodo serial PRIMARY KEY,
id_rotina integer,
id_periodo integer,
FOREIGN KEY(id_rotina) REFERENCES Rotina (id_rotina),
FOREIGN KEY(id_periodo) REFERENCES Periodo (id_periodo)
);

CREATE TABLE IF NOT EXISTS Alimentacao_Ingrediente (
quantidade integer,
id_alimentacao_ingrediente serial PRIMARY KEY,
id_alimentacao integer,
id_ingrediente integer,
FOREIGN KEY(id_alimentacao) REFERENCES Alimentacao (id_alimentacao),
FOREIGN KEY(id_ingrediente) REFERENCES Ingrediente (id_ingrediente)
);

CREATE TABLE IF NOT EXISTS Ingrediente_fornecedor (
id_fornecedor_ingrediente serial PRIMARY KEY,
valor numeric(20,2),
data date,
id_fornecedor integer,
id_ingrediente integer,
FOREIGN KEY(id_fornecedor) REFERENCES Fornecedor (id_fornecedor),
FOREIGN KEY(id_ingrediente) REFERENCES Ingrediente (id_ingrediente)
);

CREATE TABLE IF NOT EXISTS Site_Objeto (
id_site_objeto serial PRIMARY KEY,
id_site integer,
id_objeto integer,
FOREIGN KEY(id_site) REFERENCES Site (id_site),
FOREIGN KEY(id_objeto) REFERENCES Objeto (id_objeto)
);

CREATE TABLE IF NOT EXISTS Monitoramento_Objeto (
data date,
id_monitoramento_objeto serial PRIMARY KEY,
id_monitoramento integer,
id_objeto integer,
FOREIGN KEY(id_monitoramento) REFERENCES Monitoramento (id_monitoramento),
FOREIGN KEY(id_objeto) REFERENCES Objeto (id_objeto)
);

CREATE TABLE IF NOT EXISTS Papel (
codigo Varchar (10),
id_papel serial PRIMARY KEY,
tipo Varchar (50),
vencimento Date
);

CREATE TABLE IF NOT EXISTS Carteira (
nome Varchar (50),
tipo Varchar (50),
id_carteira serial PRIMARY KEY,
dataInicio Date,
foto bytea,
id_usuario Integer,
FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario)
);

CREATE TABLE IF NOT EXISTS Operacao (
id_operacao serial PRIMARY KEY,
fechamento Decimal (10,2),
tempo_operacao Time,
qtd_compra Integer,
abertura Decimal (10,2),
qtd_venda Integer,
lado Char (1),
preco_compra Decimal (10,2),
preco_venda Decimal (10,2),
preco_medio Decimal (10,2),
res_intervalo Varchar (20),
numero_operacao Numeric,
res_operacao Varchar (20),
drawdon Decimal (10,2),
ganhoMax Decimal (10,2),
perdaMax Decimal (10,2),
tet Varchar (10),
total Numeric,
id_carteira Integer,
id_papel Integer,
FOREIGN KEY(id_carteira) REFERENCES Carteira (id_carteira),
FOREIGN KEY(id_papel) REFERENCES Papel (id_papel)
);

ALTER TABLE Lancamento ADD FOREIGN KEY(id_movimentacao) REFERENCES Movimentacao (id_movimentacao);
ALTER TABLE Documento ADD FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario);
ALTER TABLE Movimentacao ADD FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario);
ALTER TABLE Movimentacao ADD FOREIGN KEY(id_caixa) REFERENCES Caixa (id_caixa);
ALTER TABLE Movimentacao ADD FOREIGN KEY(id_periodo) REFERENCES Periodo (id_periodo);
ALTER TABLE Rotina ADD FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario);
ALTER TABLE Monitoramento ADD FOREIGN KEY(id_periodo) REFERENCES Periodo (id_periodo);
ALTER TABLE Caixa ADD FOREIGN KEY(id_usuario) REFERENCES Usuario (id_usuario);

CREATE INDEX if not exists idx_evento_id_categoria ON Evento(id_categoria);
CREATE INDEX if not exists  idx_lancamento_id_evento ON Lancamento(id_evento);
CREATE INDEX if not exists  idx_lancamento_id_movimentacao ON Lancamento(id_movimentacao);
CREATE INDEX if not exists  idx_mov_id_usuario ON Movimentacao(id_usuario);
CREATE INDEX if not exists  idx_mov_id_caixa ON Movimentacao(id_caixa);
CREATE INDEX if not exists  idx_mov_id_periodo ON Movimentacao(id_periodo);
CREATE INDEX if not exists  idx_rotina_id_usuario ON Rotina(id_usuario);
CREATE INDEX if not exists  idx_alim_id_rotina ON Alimentacao(id_rotina);
CREATE INDEX if not exists  idx_treino_id_rotina ON Treino(id_rotina);
CREATE INDEX if not exists  idx_cofre_id_usuario ON Cofre(id_usuario);
CREATE INDEX if not exists  idx_meta_id_periodo ON Meta(id_periodo);
CREATE INDEX if not exists  idx_monitoramento_id_periodo ON Monitoramento(id_periodo);
CREATE INDEX if not exists  idx_doc_id_usuario ON Documento(id_usuario);
CREATE INDEX if not exists  idx_caixa_id_usuario ON Caixa(id_usuario);
CREATE INDEX if not exists  idx_te_id_treino ON Treino_Exercicio(id_treino);
CREATE INDEX if not exists  idx_te_id_exercicio ON Treino_Exercicio(id_exercicio);
CREATE INDEX if not exists  idx_rp_id_rotina ON Rotina_Periodo(id_rotina);
CREATE INDEX if not exists  idx_rp_id_periodo ON Rotina_Periodo(id_periodo);
CREATE INDEX if not exists  idx_ai_id_alimentacao ON Alimentacao_Ingrediente(id_alimentacao);
CREATE INDEX if not exists  idx_ai_id_ingrediente ON Alimentacao_Ingrediente(id_ingrediente);
CREATE INDEX if not exists  idx_if_id_fornecedor ON Ingrediente_fornecedor(id_fornecedor);
CREATE INDEX if not exists  idx_if_id_ingrediente ON Ingrediente_fornecedor(id_ingrediente);
CREATE INDEX if not exists  idx_so_id_site ON Site_Objeto(id_site);
CREATE INDEX if not exists  idx_so_id_objeto ON Site_Objeto(id_objeto);
CREATE INDEX if not exists  idx_mo_id_monitoramento ON Monitoramento_Objeto(id_monitoramento);
CREATE INDEX if not exists  idx_mo_id_objeto ON Monitoramento_Objeto(id_objeto);

commit;
