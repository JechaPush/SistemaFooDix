# 🗄️ Guía para Importar la Base de Datos

## 📦 ¿Qué contiene `backup_db_foodix.sql`?

Este archivo contiene:
- ✅ Estructura completa de todas las tablas
- ✅ Usuario administrador (daniela@FooDix.com.pe / 525224Da!)
- ✅ Todos los usuarios registrados
- ✅ Todos los restaurantes registrados
- ✅ Departamentos, provincias y distritos de Lambayeque
- ✅ Categorías de restaurantes
- ✅ Estados de aprobación
- ✅ Tipos de vehículo
- ✅ Documentos subidos (si existen)

---

## 🚀 Pasos para Importar

### 1️⃣ Asegúrate de que Docker esté corriendo

```powershell
docker ps
```

Deberías ver los contenedores:
- `sistemafoodix-db-1` (MySQL)
- `sistemafoodix-mi-app-1` (Spring Boot)

Si no están corriendo:

```powershell
docker-compose up -d
```

Espera **10-15 segundos** a que MySQL esté completamente listo.

---

### 2️⃣ Importar el backup

**En Windows (PowerShell):**

```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix < backup_db_foodix.sql
```

**En Linux/Mac:**

```bash
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < backup_db_foodix.sql
```

Deberías ver un warning de seguridad (es normal) y luego terminará sin errores.

---

### 3️⃣ Verificar que se importó correctamente

```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix -e "SELECT COUNT(*) as TotalUsuarios FROM usuario;"
```

Debería mostrar el número de usuarios en la base de datos.

```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix -e "SELECT COUNT(*) as TotalRestaurantes FROM restaurante;"
```

Debería mostrar el número de restaurantes registrados.

---

### 4️⃣ Reiniciar la aplicación

```powershell
docker-compose restart mi-app
```

Espera 15 segundos y luego accede a: http://localhost:8080

---

## 👤 Usuarios de Prueba

Después de importar el backup, podrás usar:

### Administrador
- **Email**: daniela@FooDix.com.pe
- **Password**: 525224Da!
- **URL**: http://localhost:8080/menuAdministrador

### Usuarios Clientes
Depende de los usuarios que se hayan registrado. Revisa con:

```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix -e "SELECT codigo, nombre, apellido_paterno, correo_electronico, codigo_rol FROM usuario WHERE codigo_rol = 4;"
```

---

## ⚠️ Solución de Problemas

### Error: "Access denied for user 'root'"
La contraseña de root es `root`. Asegúrate de incluir las comillas simples:
```powershell
-p'root'
```

### Error: "Can't connect to MySQL server"
El contenedor de MySQL no está listo. Espera 15 segundos y vuelve a intentar.

### Error: "Unknown database 'db_foodix'"
La base de datos no existe. Créala primero:
```powershell
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' -e "CREATE DATABASE IF NOT EXISTS db_foodix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

Luego vuelve a importar el backup.

### Error: "ERROR 1049 (42000): Unknown database"
Asegúrate de que el contenedor de MySQL esté corriendo:
```powershell
docker ps | Select-String "db-1"
```

---

## 🔄 ¿Necesitas Resetear la Base de Datos?

Si algo sale mal y quieres empezar de cero:

```powershell
# Detener contenedores
docker-compose down -v

# Levantar de nuevo
docker-compose up -d

# Esperar 15 segundos
Start-Sleep -Seconds 15

# Importar backup
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix < backup_db_foodix.sql

# Reiniciar aplicación
docker-compose restart mi-app
```

---

## 📞 ¿Sigues Teniendo Problemas?

1. Verifica los logs de MySQL:
   ```powershell
   docker logs sistemafoodix-db-1 --tail 50
   ```

2. Verifica los logs de la aplicación:
   ```powershell
   docker logs sistemafoodix-mi-app-1 --tail 50
   ```

3. Contacta al equipo de desarrollo.

---

**¡Listo! Tu base de datos debería estar importada y lista para usar.** 🎉
