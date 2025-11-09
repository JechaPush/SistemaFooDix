-- Configurar encoding UTF-8 para caracteres especiales
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE db_foodix;

-- Limpiar datos existentes (en orden inverso por las foreign keys)
DELETE FROM usuario WHERE codigo_rol = 1; -- Solo admin
DELETE FROM distrito;
DELETE FROM provincia;
DELETE FROM departamento;
DELETE FROM categoria; -- Limpiar categorías también
DELETE FROM tipo_vehiculo; -- Limpiar tipos de vehículo
DELETE FROM estado_aprobacion; -- Limpiar estados

-- Reiniciar auto_increment
ALTER TABLE departamento AUTO_INCREMENT = 11;
ALTER TABLE provincia AUTO_INCREMENT = 1;
ALTER TABLE distrito AUTO_INCREMENT = 1;
ALTER TABLE categoria AUTO_INCREMENT = 1;
ALTER TABLE tipo_vehiculo AUTO_INCREMENT = 1;
ALTER TABLE estado_aprobacion AUTO_INCREMENT = 1;

-- ============================================
-- 0. USUARIO ADMINISTRADOR
-- ============================================
-- Email: daniela@FooDix.com.pe
-- Password: 525224Da!
-- Rol: 1 = ADMIN (según la lógica de la aplicación)
-- Tipo Documento: 1 = DNI
-- Distrito: 1 (Chiclayo - se insertará después)
INSERT INTO usuario (
    nombre, 
    apellido_paterno, 
    apellido_materno,
    numero_documento,
    fecha_nacimiento,
    correo_electronico, 
    contrasena, 
    telefono,
    direccion,
    codigo_tipo_documento,
    codigo_rol, 
    codigo_distrito,
    fecha_creacion,
    estado
) VALUES (
    'Daniela', 
    'Administrador', 
    'FooDix',
    '99999999',
    '1990-01-01',
    'daniela@FooDix.com.pe', 
    '$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.', 
    '999999999',
    'Admin Address',
    1,
    1, 
    1,
    NOW(),
    TRUE
);

-- ============================================
-- 1. DEPARTAMENTO DE LAMBAYEQUE
-- ============================================
INSERT INTO departamento (nombre, estado) VALUES
('Lambayeque', TRUE);

-- ============================================
-- 2. PROVINCIAS DE LAMBAYEQUE
-- ============================================
-- Lambayeque (departamento codigo 11)
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Chiclayo', 11, TRUE),
('Lambayeque', 11, TRUE),
('Ferreñafe', 11, TRUE);

-- ============================================
-- 3. DISTRITOS POR PROVINCIA
-- ============================================

-- Provincia Chiclayo (código 1)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Chiclayo', 1, TRUE),
('Chongoyape', 1, TRUE),
('Eten', 1, TRUE),
('Eten Puerto', 1, TRUE),
('José Leonardo Ortiz', 1, TRUE),
('La Victoria', 1, TRUE),
('Lagunas', 1, TRUE),
('Monsefú', 1, TRUE),
('Nueva Arica', 1, TRUE),
('Oyotún', 1, TRUE),
('Pátapo', 1, TRUE),
('Picsi', 1, TRUE),
('Pimentel', 1, TRUE),
('Pomalca', 1, TRUE),
('Pucalá', 1, TRUE),
('Reque', 1, TRUE),
('Santa Rosa', 1, TRUE),
('Saña', 1, TRUE),
('Cayaltí', 1, TRUE),
('Tumán', 1, TRUE);

-- Provincia Lambayeque (código 2)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Lambayeque', 2, TRUE),
('Chóchope', 2, TRUE),
('Illimo', 2, TRUE),
('Jayanca', 2, TRUE),
('Mochumi', 2, TRUE),
('Mórrope', 2, TRUE),
('Motupe', 2, TRUE),
('Olmos', 2, TRUE),
('Pacora', 2, TRUE),
('Salas', 2, TRUE),
('San José', 2, TRUE),
('Túcume', 2, TRUE);

-- Provincia Ferreñafe (código 3)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Ferreñafe', 3, TRUE),
('Cañaris', 3, TRUE),
('Incahuasi', 3, TRUE),
('Manuel Antonio Mesones Muro', 3, TRUE),
('Pítipo', 3, TRUE),
('Pueblo Nuevo', 3, TRUE);

-- ============================================
-- 4. CATEGORÍAS DE RESTAURANTES
-- ============================================
INSERT INTO categoria (nombre, descripcion, icono, estado) VALUES
('Pollería', 'Pollo a la brasa y parrillas', 'fa-drumstick-bite', TRUE),
('Cevichería', 'Ceviches y comida marina', 'fa-fish', TRUE),
('Chaufería', 'Comida norteña y chicharrones', 'fa-utensils', TRUE),
('Mariscos', 'Restaurantes de mariscos', 'fa-shrimp', TRUE),
('Comida Criolla', 'Platos tradicionales peruanos', 'fa-plate-wheat', TRUE),
('Chifa', 'Comida fusión chino-peruana', 'fa-bowl-rice', TRUE),
('Pizzería', 'Pizzas y comida italiana', 'fa-pizza-slice', TRUE),
('Hamburguesas', 'Hamburguesas y fast food', 'fa-burger', TRUE),
('Postres', 'Heladerías y postres', 'fa-ice-cream', TRUE),
('Cafetería', 'Cafés y bebidas', 'fa-mug-hot', TRUE),
('Comida Vegetariana', 'Opciones vegetarianas y veganas', 'fa-leaf', TRUE),
('Sushi', 'Comida japonesa', 'fa-fish-fins', TRUE);

-- ============================================
-- 5. TIPOS DE VEHÍCULO
-- ============================================
INSERT INTO tipo_vehiculo (nombre, estado) VALUES
('Bicicleta', TRUE),
('Motocicleta', TRUE),
('Scooter Eléctrico', TRUE),
('Automóvil', TRUE);

-- ============================================
-- 6. ESTADOS DE APROBACIÓN
-- ============================================
INSERT INTO estado_aprobacion (nombre, descripcion, estado) VALUES
('Pendiente', 'Solicitud en revisión', TRUE),
('Aprobado', 'Solicitud aprobada', TRUE),
('Rechazado', 'Solicitud rechazada', TRUE);

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT 'Departamentos insertados:' AS msg, COUNT(*) AS cantidad FROM departamento;
SELECT 'Provincias insertadas:' AS msg, COUNT(*) AS cantidad FROM provincia;
SELECT 'Distritos insertados:' AS msg, COUNT(*) AS cantidad FROM distrito;
SELECT 'Categorías insertadas:' AS msg, COUNT(*) AS cantidad FROM categoria;
SELECT 'Tipos de vehículo insertados:' AS msg, COUNT(*) AS cantidad FROM tipo_vehiculo;
SELECT 'Estados de aprobación insertados:' AS msg, COUNT(*) AS cantidad FROM estado_aprobacion;

-- ============================================
-- CONSULTAS DE PRUEBA
-- ============================================
-- Ver departamento con sus provincias
SELECT d.nombre AS Departamento, p.nombre AS Provincia
FROM departamento d
LEFT JOIN provincia p ON p.codigo_departamento = d.codigo
ORDER BY d.nombre, p.nombre;

-- Ver provincias con sus distritos
SELECT p.nombre AS Provincia, di.nombre AS Distrito
FROM provincia p
LEFT JOIN distrito di ON di.codigo_provincia = p.codigo
ORDER BY p.nombre, di.nombre;