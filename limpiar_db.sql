-- ============================================
-- SCRIPT DE LIMPIEZA DE BASE DE DATOS
-- ============================================
-- Este script limpia completamente la base de datos
-- y reinicia los contadores AUTO_INCREMENT
-- ============================================

USE db_foodix;

-- Deshabilitar verificación de foreign keys temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. LIMPIAR TODAS LAS TABLAS
-- ============================================
TRUNCATE TABLE restaurante_categoria;
TRUNCATE TABLE categoria;
TRUNCATE TABLE repartidor;
TRUNCATE TABLE restaurante;
TRUNCATE TABLE usuario;
TRUNCATE TABLE distrito;
TRUNCATE TABLE provincia;
TRUNCATE TABLE departamento;
TRUNCATE TABLE tipo_vehiculo;
TRUNCATE TABLE estado_aprobacion;

-- ============================================
-- 2. REINICIAR AUTO_INCREMENT
-- ============================================
ALTER TABLE categoria AUTO_INCREMENT = 1;
ALTER TABLE departamento AUTO_INCREMENT = 11;
ALTER TABLE provincia AUTO_INCREMENT = 1;
ALTER TABLE distrito AUTO_INCREMENT = 1;
ALTER TABLE usuario AUTO_INCREMENT = 1;
ALTER TABLE restaurante AUTO_INCREMENT = 1;
ALTER TABLE repartidor AUTO_INCREMENT = 1;
ALTER TABLE tipo_vehiculo AUTO_INCREMENT = 1;
ALTER TABLE estado_aprobacion AUTO_INCREMENT = 1;

-- Habilitar verificación de foreign keys nuevamente
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Base de datos limpiada correctamente!' AS Resultado;
SELECT 'Ahora ejecuta: datos_iniciales.sql para cargar los datos base' AS Siguiente_Paso;
