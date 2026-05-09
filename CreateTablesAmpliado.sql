-- ENUMS
CREATE TYPE enum_tipus_usuari AS ENUM ('UsuariOVI', 'AssistentPersonal');
CREATE TYPE enum_estat_usuari AS ENUM ('Pendent', 'Acceptat', 'Rebutjat');
CREATE TYPE enum_genere AS ENUM ('Masculí', 'Femení', 'Prefereixc no dir-ho');
CREATE TYPE enum_formacio AS ENUM ('ESO', 'BATXILLERAT', 'FPGM', 'FPGS', 'GRAU UNIVERSITARI');
CREATE TYPE enum_tipus_assistent AS ENUM ('PAP', 'PATI');
CREATE TYPE enum_estat_assistent AS ENUM ('Candidat', 'Acceptat', 'Rebutjat');
CREATE TYPE enum_estat_request AS ENUM ('En revisió', 'Aprovada', 'Tancada amb contracte', 'Tancada amb contracte finalitzat', 'Rebutjada');
CREATE TYPE enum_tipus_activitat AS ENUM ('Limitada', 'Divulgacio');
CREATE TYPE enum_tipus_assistencia AS ENUM ('Higiene personal', 'Mobilitat', 'Suport emocional', 'Acompanyament mèdic', 'Tasques de la llar', 'Altres');
CREATE TYPE enum_tipus_disponibilitat AS ENUM ('Vehicle prop', 'Transport públic');
CREATE TYPE enum_dia_setmana AS ENUM ('Dilluns', 'Dimarts', 'Dimecres', 'Dijous', 'Divendres', 'Dissabte', 'Diumenge');

CREATE TABLE Pueblos (
    nombre VARCHAR(100) PRIMARY KEY,
    codpos VARCHAR(5) NOT NULL
);

CREATE TABLE Idiomas (
    id_idioma SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Usuario (
    id_usuario SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    cognom1 VARCHAR(50) NOT NULL,
    cognom2 VARCHAR(50),
    dni VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    genere enum_genere NOT NULL,
    data_naixement TIMESTAMP NOT NULL,
    tipus_usuari enum_tipus_usuari NOT NULL,
    telefon VARCHAR(20),
    nombre_pueblo VARCHAR(100),
    direccio VARCHAR(50) NOT NULL,

    CONSTRAINT fk_usuario_pueblo FOREIGN KEY (nombre_pueblo) REFERENCES Pueblos(nombre)
);

CREATE TABLE UsuariOVI (
    id_usuari INT PRIMARY KEY,
    pla_vida TEXT,
    tipus_assistencia enum_tipus_assistencia,
    consentiment_LOPD BOOLEAN DEFAULT FALSE NOT NULL,
    estat_usuari enum_estat_usuari DEFAULT 'Pendent',

    CONSTRAINT fk_usuariovi_usuario FOREIGN KEY (id_usuari) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE AssistentPersonal (
    id_assistent INT PRIMARY KEY,
    formacio_academica enum_formacio NOT NULL,
    disponibilitat enum_tipus_disponibilitat,
    cv TEXT,
    tipus enum_tipus_assistent NOT NULL,
    estat_acceptat enum_estat_assistent DEFAULT 'Candidat',
    actiu BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_assistent_usuario FOREIGN KEY (id_assistent) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE AssistentIdioma (
    id_assistent INT NOT NULL,
    id_idioma INT NOT NULL,

    PRIMARY KEY (id_assistent, id_idioma),
    CONSTRAINT fk_ai_assistent FOREIGN KEY (id_assistent) REFERENCES AssistentPersonal(id_assistent) ON DELETE CASCADE,
    CONSTRAINT fk_ai_idioma FOREIGN KEY (id_idioma) REFERENCES Idiomas(id_idioma) ON DELETE RESTRICT
);

CREATE TABLE AssistentTipusAssistencia (
    id_assistent INT NOT NULL,
    tipus_assistencia enum_tipus_assistencia NOT NULL,

    PRIMARY KEY (id_assistent, tipus_assistencia),
    CONSTRAINT fk_ata_assistent FOREIGN KEY (id_assistent) 
        REFERENCES AssistentPersonal(id_assistent) ON DELETE CASCADE
);

CREATE TABLE AssistentDisponibilitat (
    id_disponibilitat SERIAL PRIMARY KEY,
    id_assistent INT NOT NULL,
    dia_setmana enum_dia_setmana NOT NULL,
    hora_inici TIME NOT NULL,
    hora_fi TIME NOT NULL,

    CONSTRAINT fk_disp_assistent FOREIGN KEY (id_assistent)
        REFERENCES AssistentPersonal(id_assistent) ON DELETE CASCADE,
    CONSTRAINT ck_hores CHECK (hora_fi > hora_inici),
    CONSTRAINT uq_assistent_franja UNIQUE (id_assistent, dia_setmana, hora_inici)
);


CREATE TABLE APRequest (
    id_request SERIAL PRIMARY KEY,
    id_usuari INT NOT NULL,
    id_idioma INT,                                     
    data_solicitud DATE DEFAULT CURRENT_DATE,
    experiencia_previa TEXT,
    formacio_academica TEXT,
    genere_assistent enum_genere,
    localitat VARCHAR(100),
    estat_request enum_estat_request DEFAULT 'En revisió',
    tipus_assistencia enum_tipus_assistencia,          

    CONSTRAINT fk_request_usuari FOREIGN KEY (id_usuari)
        REFERENCES UsuariOVI(id_usuari) ON DELETE CASCADE,
    CONSTRAINT fk_request_idioma FOREIGN KEY (id_idioma)
        REFERENCES Idiomas(id_idioma) ON DELETE SET NULL
);

CREATE TABLE APRequestDisponibilitat (
    id_request INT NOT NULL,
    dia_setmana enum_dia_setmana NOT NULL,
    hora_inici TIME NOT NULL,
    hora_fi TIME NOT NULL,

    PRIMARY KEY (id_request, dia_setmana, hora_inici),
    CONSTRAINT fk_reqdisp_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request) ON DELETE CASCADE,
    CONSTRAINT ck_hores_request CHECK (hora_fi > hora_inici)
);

CREATE TABLE Seleccion (
    id_seleccion SERIAL PRIMARY KEY,
    id_request INT NOT NULL,
    id_assistent INT NOT NULL,
    data_proposta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_seleccion_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request) ON DELETE CASCADE,
    CONSTRAINT fk_seleccion_assistent FOREIGN KEY (id_assistent)
        REFERENCES AssistentPersonal(id_assistent) ON DELETE CASCADE,
    CONSTRAINT uq_seleccion_unica UNIQUE (id_request, id_assistent)
);

CREATE TABLE ComunicacioUsuariOVIPAP (
    id_comunicacio SERIAL PRIMARY KEY,
    id_seleccion INT NOT NULL,
    missatge TEXT NOT NULL,
    id_from INT NOT NULL,
    id_to INT NOT NULL,
    data_enviament TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comunicacio_seleccion FOREIGN KEY (id_seleccion)
        REFERENCES Seleccion(id_seleccion) ON DELETE CASCADE
);

CREATE TABLE RegistreContracte (
    id_contracte SERIAL PRIMARY KEY,
    id_request INT NOT NULL,
    id_assistent INT NOT NULL,
    data_inici DATE NOT NULL,
    data_fi DATE,
    ruta_pdf VARCHAR(255),

    CONSTRAINT fk_contracte_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request),
    CONSTRAINT fk_contracte_assistent FOREIGN KEY (id_assistent)
        REFERENCES AssistentPersonal(id_assistent),
    CONSTRAINT ck_fechas_contracte CHECK (data_fi IS NULL OR data_fi >= data_inici),
    CONSTRAINT uq_contracte_request UNIQUE (id_request)
);

CREATE TABLE Formador (
    id_formador SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    cognom1 VARCHAR(50) NOT NULL,
    cognom2 VARCHAR(50),
    email_contacte VARCHAR(100),
    especialitat VARCHAR(100)
);

CREATE TABLE ActivitatFormacio (
    id_activitat SERIAL PRIMARY KEY,
    id_formador INT NOT NULL,
    titol VARCHAR(150) NOT NULL,
    descripcio TEXT,
    data_hora TIMESTAMP NOT NULL,
    tipus_activitat enum_tipus_activitat NOT NULL,
    aforament_maxim INT,

    CONSTRAINT fk_activitat_formador FOREIGN KEY (id_formador)
        REFERENCES Formador(id_formador) ON DELETE RESTRICT
);

CREATE TABLE AssistenciaFormacio (
    id_assistencia SERIAL PRIMARY KEY,
    id_activitat INT NOT NULL,
    id_usuari INT,
    id_assistent INT,
    inscripcio_previa BOOLEAN DEFAULT FALSE NOT NULL,
    ha_assistit BOOLEAN DEFAULT FALSE NOT NULL,
    certificat_emes BOOLEAN DEFAULT FALSE NOT NULL,
    ruta_certificat VARCHAR(255),

    CONSTRAINT fk_assistencia_activitat FOREIGN KEY (id_activitat)
        REFERENCES ActivitatFormacio(id_activitat) ON DELETE CASCADE,
    CONSTRAINT fk_assistencia_usuari FOREIGN KEY (id_usuari)
        REFERENCES UsuariOVI(id_usuari) ON DELETE CASCADE,
    CONSTRAINT fk_assistencia_assistent FOREIGN KEY (id_assistent)
        REFERENCES AssistentPersonal(id_assistent) ON DELETE CASCADE,
    CONSTRAINT ck_alguien_asiste CHECK (id_usuari IS NOT NULL OR id_assistent IS NOT NULL),
    CONSTRAINT uq_assistencia_usuari UNIQUE (id_activitat, id_usuari),
    CONSTRAINT uq_assistencia_assistent UNIQUE (id_activitat, id_assistent)
);