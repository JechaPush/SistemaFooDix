# 🗄️ Gestión de Base de Datos - FooDix

## 📋 Scripts Disponibles

### 1. `datos_iniciales.sql`
Script principal que carga los datos iniciales en la base de datos:
- ✅ Usuario administrador (daniela@FooDix.com.pe)
- ✅ Departamento de Lambayeque
- ✅ 3 Provincias (Chiclayo, Lambayeque, Ferreñafe)
- ✅ 38 Distritos
- ✅ 12 Categorías de restaurantes
- ✅ 4 Tipos de vehículo
- ✅ 3 Estados de aprobación

### 2. `limpiar_db.sql`
Script que **limpia completamente** la base de datos y reinicia los contadores AUTO_INCREMENT.

⚠️ **CUIDADO:** Este script borra TODOS los datos de todas las tablas.

### 3. `reset_db.ps1` (PowerShell)
Script automatizado que:
1. Verifica que Docker esté corriendo
2. Limpia la base de datos
3. Carga los datos iniciales
4. Muestra un resumen de lo cargado

## 🚀 Cómo Usar

### Opción 1: Reset Automático (Recomendado)
```powershell
# Ejecutar el script de PowerShell
.\reset_db.ps1
```

### Opción 2: Manual
```powershell
# 1. Limpiar base de datos
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < limpiar_db.sql

# 2. Cargar datos iniciales
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < datos_iniciales.sql
```

### Opción 3: Solo cargar datos iniciales (sin limpiar)
```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < datos_iniciales.sql
```

## 🔍 Verificar Datos

### Ver todas las categorías
```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT * FROM categoria;"
```

### Contar registros
```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT 
    (SELECT COUNT(*) FROM categoria) as categorias,
    (SELECT COUNT(*) FROM distrito) as distritos,
    (SELECT COUNT(*) FROM usuario) as usuarios;"
```

### Ver el usuario administrador
```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT codigo, nombre, correo_electronico, codigo_rol FROM usuario WHERE codigo_rol = 1;"
```

## 📊 Datos Iniciales Cargados

### 🏷️ Categorías (12)
1. Pollería
2. Cevichería
3. Chaufería
4. Mariscos
5. Comida Criolla
6. Chifa
7. Pizzería
8. Hamburguesas
9. Postres
10. Cafetería
11. Comida Vegetariana
12. Sushi

### 👤 Usuario Administrador
- **Email:** daniela@FooDix.com.pe
- **Contraseña:** 525224Da!
- **Rol:** Administrador (código 1)

### 📍 Ubicaciones
- **Departamento:** Lambayeque
- **Provincias:** 3 (Chiclayo, Lambayeque, Ferreñafe)
- **Distritos:** 38 (todos los de Lambayeque)

### 🚗 Tipos de Vehículo
1. Bicicleta
2. Motocicleta
3. Scooter Eléctrico
4. Automóvil

### ✅ Estados de Aprobación
1. Pendiente
2. Aprobado
3. Rechazado

## 🐛 Solución de Problemas

### Error: "Access denied"
Verifica que la contraseña de MySQL sea correcta. En el `docker-compose.yml` debe ser:
```yaml
MYSQL_ROOT_PASSWORD=root
```

### Error: "Container not found"
Asegúrate de que Docker esté corriendo:
```powershell
docker-compose up -d
```

### Categorías duplicadas
Si ves categorías duplicadas, ejecuta el reset completo:
```powershell
.\reset_db.ps1
```

## 📝 Notas

- Los scripts usan codificación UTF-8 para caracteres especiales (ñ, tildes, etc.)
- El script `datos_iniciales.sql` ahora incluye limpieza automática antes de insertar
- El AUTO_INCREMENT de departamento inicia en 11 (código UBIGEO de Lambayeque)
- Las contraseñas se almacenan con BCrypt (algoritmo $2a$10$)

## 🔄 Cuándo Usar Cada Script

| Escenario | Script a Usar |
|-----------|---------------|
| Primera instalación | `datos_iniciales.sql` |
| Datos duplicados | `reset_db.ps1` |
| Reset completo | `reset_db.ps1` |
| Actualizar solo datos base | `datos_iniciales.sql` (con limpieza incluida) |
| Limpiar sin recargar | `limpiar_db.sql` |

## ⚠️ Advertencias

1. **NO ejecutes `limpiar_db.sql` en producción** sin backup
2. El reset elimina TODOS los datos, incluyendo usuarios registrados
3. Solo el usuario admin se recrea automáticamente
4. Los restaurantes y repartidores registrados se perderán
