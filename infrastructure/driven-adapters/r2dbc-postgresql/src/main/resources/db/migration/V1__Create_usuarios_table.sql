-- Migración V1: Creación de tabla usuarios para HU1 - Sistema CrediYa

CREATE TABLE usuarios (
    id VARCHAR(36) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    correo_electronico VARCHAR(150) NOT NULL,
    salario_base DECIMAL(12,2) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_usuarios_correo UNIQUE (correo_electronico),
    CONSTRAINT ck_usuarios_nombres_no_vacio CHECK (LENGTH(TRIM(nombres)) > 0),
    CONSTRAINT ck_usuarios_apellidos_no_vacio CHECK (LENGTH(TRIM(apellidos)) > 0),
    CONSTRAINT ck_usuarios_correo_formato CHECK (correo_electronico ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT ck_usuarios_salario_rango CHECK (salario_base >= 0 AND salario_base <= 15000000),
    CONSTRAINT ck_usuarios_fecha_nacimiento CHECK (fecha_nacimiento <= CURRENT_DATE)
);

CREATE INDEX idx_usuarios_correo_electronico ON usuarios(LOWER(correo_electronico));
