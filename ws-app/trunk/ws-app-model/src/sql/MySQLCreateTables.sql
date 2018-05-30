-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Conductor;
DROP TABLE Viaje;

CREATE TABLE Conductor ( idConductor BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) COLLATE latin1_bin NOT NULL,
    ciudad VARCHAR(255) COLLATE latin1_bin NOT NULL,
    modeloCoche VARCHAR(255) COLLATE latin1_bin NOT NULL,
    horaInicio SMALLINT NOT NULL,
    horaFin SMALLINT NOT NULL,
    fechaAlta DATETIME NOT NULL,
    puntuacionAcumulada INTEGER NOT NULL,
    totalViajes INTEGER NOT NULL,
    CONSTRAINT ConductorPK PRIMARY KEY(idConductor),
    CONSTRAINT validHoraInicio CHECK (horaInicio >=0 AND horaInicio<=23)) ENGINE = InnoDB;
    
CREATE TABLE Viaje (idViaje BIGINT NOT NULL AUTO_INCREMENT,
	idConductor BIGINT NOT NULL,
    origen VARCHAR(255) COLLATE latin1_bin NOT NULL,
	destino VARCHAR(255) COLLATE latin1_bin NOT NULL,
	idUsuario VARCHAR(255) COLLATE latin1_bin NOT NULL,
	tarjetaCredito BIGINT NOT NULL,
    fechaReserva DATETIME NOT NULL,
	puntuacion BIGINT NOT NULL,
	CONSTRAINT ViajePK PRIMARY KEY(idViaje)) ENGINE = InnoDB;

    
