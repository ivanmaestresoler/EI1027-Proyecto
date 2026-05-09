-- 1. LIMPIAR DATOS PREVIOS Y REINICIAR CONTADORES (IDs)
TRUNCATE TABLE AssistenciaFormacio, ActivitatFormacio, Formador, RegistreContracte, ComunicacioUsuariOVIPAP, Seleccion, APRequestDisponibilitat, APRequest, AssistentDisponibilitat, AssistentTipusAssistencia, AssistentIdioma, AssistentPersonal, UsuariOVI, Usuario, Idiomas, Pueblos RESTART IDENTITY CASCADE;

-- 2. INSERTAR PUEBLOS
INSERT INTO Pueblos (nombre, codpos) VALUES 
('Castelló de la Plana', '12001'), ('Vila-real', '12540'), ('Borriana', '12530'), 
('La Vall d Uixó', '12600'), ('Vinaròs', '12500'), ('Benicarló', '12580'), 
('Almassora', '12550'), ('Onda', '12200'), ('Benicàssim', '12560'), 
('Nules', '12520'), ('L Alcora', '12110'), ('Orpesa', '12594'), 
('Segorb', '12400'), ('Peníscola', '12598'), ('Moncofa', '12593');

-- 3. INSERTAR IDIOMAS
INSERT INTO Idiomas (nombre) VALUES 
('Valencià'), ('Castellà'), ('Anglés'), ('Francés'), ('Alemany'), 
('Italià'), ('Portugués'), ('Llengua de Signes'), ('Àrab'), ('Romanés'), 
('Xinès'), ('Japonès'), ('Rus'), ('Ucraïnés'), ('Búlgar');

-- 4. INSERTAR USUARIOS GENERALES (IDs 1-15: UsuariOVI / IDs 16-30: AssistentPersonal)
INSERT INTO Usuario (nom, cognom1, cognom2, dni, email, contrasenya, genere, data_naixement, tipus_usuari, telefon, nombre_pueblo, direccio) VALUES
('Joan', 'Gomez', 'Perez', '11111111A', 'joan@ovi.com', '123456', 'Masculí', '1990-05-10', 'UsuariOVI', '600111111', 'Castelló de la Plana', 'Carrer Major 1'),
('Maria', 'Lopez', 'Sanz', '22222222B', 'maria@ovi.com', '123456', 'Femení', '1985-08-20', 'UsuariOVI', '600222222', 'Vila-real', 'Avinguda Murà 2'),
('Carles', 'Marti', 'Roig', '33333333C', 'carles@ovi.com', '123456', 'Masculí', '1992-11-15', 'UsuariOVI', '600333333', 'Borriana', 'Plaça Espanya 3'),
('Anna', 'Garcia', 'Moll', '44444444D', 'anna@ovi.com', '123456', 'Femení', '1998-02-25', 'UsuariOVI', '600444444', 'Benicàssim', 'Carrer Sant Tomàs 4'),
('Lluis', 'Vidal', 'Mas', '55555555E', 'lluis@ovi.com', '123456', 'Masculí', '1975-07-30', 'UsuariOVI', '600555555', 'Vinaròs', 'Passeig Marítim 5'),
('Teresa', 'Puig', 'Coll', '66666666F', 'teresa@ovi.com', '123456', 'Femení', '2000-01-05', 'UsuariOVI', '600666666', 'Benicarló', 'Carrer Crist 6'),
('Vicent', 'Ferrer', 'Gali', '77777777G', 'vicent@ovi.com', '123456', 'Masculí', '1980-12-12', 'UsuariOVI', '600777777', 'Onda', 'Plaça del Rei 7'),
('Laura', 'Costa', 'Pi', '88888888H', 'laura@ovi.com', '123456', 'Femení', '1995-04-18', 'UsuariOVI', '600888888', 'Almassora', 'Carrer Sant Jaume 8'),
('Pau', 'Soler', 'Mir', '99999999I', 'pau@ovi.com', '123456', 'Prefereixc no dir-ho', '1991-09-22', 'UsuariOVI', '600999999', 'Nules', 'Carrer Major 9'),
('Elena', 'Serra', 'Vila', '10101010J', 'elena@ovi.com', '123456', 'Femení', '1988-06-14', 'UsuariOVI', '600101010', 'La Vall d Uixó', 'Avinguda Cor de Jesús 10'),
('David', 'Roca', 'Pla', '12121212K', 'david@ovi.com', '123456', 'Masculí', '1982-03-30', 'UsuariOVI', '600121212', 'Orpesa', 'Passeig Conxa 11'),
('Nuria', 'Blasco', 'Pou', '13131313L', 'nuria@ovi.com', '123456', 'Femení', '1997-10-10', 'UsuariOVI', '600131313', 'Segorb', 'Carrer Colón 12'),
('Albert', 'Font', 'Rius', '14141414M', 'albert@ovi.com', '123456', 'Masculí', '1979-05-05', 'UsuariOVI', '600141414', 'Peníscola', 'Avinguda Papa Luna 13'),
('Clara', 'Valles', 'Gros', '15151515N', 'clara@ovi.com', '123456', 'Femení', '1993-08-08', 'UsuariOVI', '600151515', 'L Alcora', 'Carrer Ferrer 14'),
('Josep', 'Marques', 'Gil', '16161616O', 'josep@ovi.com', '123456', 'Masculí', '1986-11-20', 'UsuariOVI', '600161616', 'Moncofa', 'Carrer Mar 15'),
('Sara', 'Navarro', 'Ruiz', '17171717P', 'sara@pap.com', '123456', 'Femení', '1990-01-01', 'AssistentPersonal', '611171717', 'Castelló de la Plana', 'Carrer Navarra 1'),
('Marc', 'Domingo', 'Soto', '18181818Q', 'marc@pap.com', '123456', 'Masculí', '1985-02-02', 'AssistentPersonal', '611181818', 'Vila-real', 'Avinguda Pius XII 2'),
('Alba', 'Gimenez', 'Rios', '19191919R', 'alba@pap.com', '123456', 'Femení', '1992-03-03', 'AssistentPersonal', '611191919', 'Borriana', 'Carrer l Escorredor 3'),
('Jordi', 'Sancho', 'Vera', '20202020S', 'jordi@pap.com', '123456', 'Masculí', '1998-04-04', 'AssistentPersonal', '611202020', 'Benicàssim', 'Gran Avinguda 4'),
('Silvia', 'Mateu', 'Pino', '21212121T', 'silvia@pap.com', '123456', 'Femení', '1975-05-05', 'AssistentPersonal', '611212121', 'Vinaròs', 'Carrer Sant Francesc 5'),
('Xavi', 'Vila', 'Luna', '22222222U', 'xavi@pap.com', '123456', 'Masculí', '2000-06-06', 'AssistentPersonal', '611222222', 'Benicarló', 'Avinguda Maestrat 6'),
('Mireia', 'Llorens', 'Paz', '23232323V', 'mireia@pap.com', '123456', 'Femení', '1980-07-07', 'AssistentPersonal', '611232323', 'Onda', 'Carrer Ceramista 7'),
('Andreu', 'Gargallo', 'Mora', '24242424W', 'andreu@pap.com', '123456', 'Masculí', '1995-08-08', 'AssistentPersonal', '611242424', 'Almassora', 'Carrer Trinitat 8'),
('Laia', 'Sanchis', 'Gómez', '25252525X', 'laia@pap.com', '123456', 'Femení', '1991-09-09', 'AssistentPersonal', '611252525', 'Nules', 'Carrer Sant Bartomeu 9'),
('Sergi', 'Boix', 'Sala', '26262626Y', 'sergi@pap.com', '123456', 'Masculí', '1988-10-10', 'AssistentPersonal', '611262626', 'La Vall d Uixó', 'Carrer Xacó 10'),
('Marina', 'Camps', 'Feliu', '27272727Z', 'marina@pap.com', '123456', 'Femení', '1982-11-11', 'AssistentPersonal', '611272727', 'Orpesa', 'Passeig Marítim 11'),
('Victor', 'Sales', 'Bosc', '28282828A', 'victor@pap.com', '123456', 'Masculí', '1997-12-12', 'AssistentPersonal', '611282828', 'Segorb', 'Carrer de l Aigua 12'),
('Paula', 'Molina', 'Vega', '29292929B', 'paula@pap.com', '123456', 'Femení', '1979-01-13', 'AssistentPersonal', '611292929', 'Peníscola', 'Avinguda Mar 13'),
('Dani', 'Ortiz', 'Gras', '30303030C', 'dani@pap.com', '123456', 'Masculí', '1993-02-14', 'AssistentPersonal', '611356789','Peníscola', 'Avinguda Mar 13');
