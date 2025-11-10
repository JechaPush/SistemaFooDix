# 📄 Cambios en Documentos de Verificación de Restaurantes

## 🎯 Resumen de Cambios

Se actualizaron los documentos requeridos para el registro de restaurantes según las nuevas especificaciones:

### ❌ Eliminado:
- **Copia del RUC** (ya no necesario, se valida mediante API)

### ✅ Agregado/Modificado:
- **Carta del Restaurante** ⭐ (OBLIGATORIO)
- **Carnet de Sanidad** ⭐ (cambió de opcional a OBLIGATORIO)
- **Logo del Restaurante** (opcional, sin cambios)
- **Licencia de Funcionamiento** (opcional, sin cambios)

---

## 📋 Documentos Actuales

### 1. **Carta del Restaurante** ⭐ OBLIGATORIO
- **Campo**: `cartaRestaurante`
- **Propósito**: Menú o carta de productos del restaurante para que el admin pueda verificar
- **Formatos**: PDF, JPG, PNG
- **Tamaño máximo**: 5MB
- **Descripción**: "Menú o carta de productos - Formato: PDF, JPG, PNG (máx. 5MB)"

### 2. **Carnet de Sanidad** ⭐ OBLIGATORIO
- **Campo**: `carnetSanidad`
- **Propósito**: Documento obligatorio de sanidad
- **Formatos**: PDF, JPG, PNG
- **Tamaño máximo**: 5MB
- **Descripción**: "Documento obligatorio - Formato: PDF, JPG, PNG (máx. 5MB)"

### 3. **Logo del Restaurante** (Opcional)
- **Campo**: `logo`
- **Propósito**: Imagen del logo para la app
- **Formatos**: JPG, PNG
- **Tamaño máximo**: 2MB
- **Descripción**: "Opcional - JPG, PNG (máx. 2MB)"

### 4. **Licencia de Funcionamiento** (Opcional)
- **Campo**: `licenciaFuncionamiento`
- **Propósito**: Licencia oficial de funcionamiento
- **Formatos**: PDF, JPG, PNG
- **Tamaño máximo**: 5MB
- **Descripción**: "Opcional - Formato: PDF, JPG, PNG (máx. 5MB)"

---

## 🔧 Archivos Modificados

### Frontend (HTML)

#### `registro-Restaurante.html`

**Líneas 385-415**: Actualización de campos de documentos
```html
<!-- Antes -->
<label for="documentoRuc" class="form-label">Copia del RUC *</label>
<input type="file" id="documentoRuc" name="documentoRuc" required>

<label for="carnetSanidad" class="form-label">Carnet de Sanidad</label>
<input type="file" id="carnetSanidad" name="carnetSanidad">

<!-- Ahora -->
<label for="cartaRestaurante" class="form-label">Carta del Restaurante *</label>
<input type="file" id="cartaRestaurante" name="cartaRestaurante" required>
<div class="form-text text-muted">Menú o carta de productos - Formato: PDF, JPG, PNG (máx. 5MB)</div>

<label for="carnetSanidad" class="form-label">Carnet de Sanidad *</label>
<input type="file" id="carnetSanidad" name="carnetSanidad" required>
<div class="form-text text-muted">Documento obligatorio - Formato: PDF, JPG, PNG (máx. 5MB)</div>
```

**Líneas 797-802**: Actualización de validaciones JavaScript
```javascript
// Antes
const fileInputs = [
    { input: document.getElementById('documentoRuc'), maxSize: 5 * 1024 * 1024 },
    ...
];

// Ahora
const fileInputs = [
    { input: document.getElementById('cartaRestaurante'), maxSize: 5 * 1024 * 1024 },
    ...
];
```

---

### Backend (Java)

#### `RestauranteRegistroDTO.java`

**Línea 50**: Cambio de nombre de campo
```java
// Antes
private MultipartFile documentoRuc;

// Ahora
private MultipartFile cartaRestaurante;
```

---

#### `DocumentoRestaurante.java` (Modelo)

**Línea 8-10**: Actualización de comentario
```java
// Antes
/**
 * Entidad DocumentoRestaurante - Almacena los documentos legales del restaurante
 * (RUC, Licencia de Funcionamiento, Carnet de Sanidad, etc.)
 */

// Ahora
/**
 * Entidad DocumentoRestaurante - Almacena los documentos legales del restaurante
 * (Carta del Restaurante, Licencia de Funcionamiento, Carnet de Sanidad, etc.)
 */
```

**Líneas 56-60**: Actualización del enum
```java
// Antes
public enum TipoDocumentoRestaurante {
    RUC,
    LicenciaFuncionamiento,
    CarnetSanidad,
    Otros
}

// Ahora
public enum TipoDocumentoRestaurante {
    CARTA_RESTAURANTE,
    LicenciaFuncionamiento,
    CarnetSanidad,
    Otros
}
```

---

#### `RestauranteService.java`

**Línea 88**: Actualización de referencia al documento
```java
// Antes
dto.getDocumentoRuc(), 

// Ahora
dto.getCartaRestaurante(),
```

**Línea 92-96**: Actualización del tipo de documento
```java
// Antes
docRuc.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.RUC);

// Ahora
docRuc.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.CARTA_RESTAURANTE);
```

---

## 🗄️ Impacto en Base de Datos

La tabla `DocumentoRestaurante` ya existe y no requiere cambios estructurales. Solo se actualizó el enum `TipoDocumento` a nivel de código Java:

```sql
-- La columna TipoDocumento almacena strings, por lo que acepta:
-- 'CARTA_RESTAURANTE' (nuevo)
-- 'LicenciaFuncionamiento' 
-- 'CarnetSanidad'
-- 'Otros'
```

**No se requieren migraciones SQL** porque el campo es `VARCHAR` y acepta cualquier string.

---

## ✅ Validaciones Implementadas

### Frontend (JavaScript)
```javascript
// Validación de tamaño de archivos
const fileInputs = [
    { input: document.getElementById('cartaRestaurante'), maxSize: 5 * 1024 * 1024 },  // 5MB
    { input: document.getElementById('licenciaFuncionamiento'), maxSize: 5 * 1024 * 1024 },
    { input: document.getElementById('carnetSanidad'), maxSize: 5 * 1024 * 1024 },
    { input: document.getElementById('logo'), maxSize: 2 * 1024 * 1024 }  // 2MB
];

// Cada archivo se valida al seleccionar:
// - Tamaño máximo
// - Formato permitido (HTML accept attribute)
```

### Backend (Spring)
```java
// Configuración en application.properties
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=25MB

// Los archivos se validan automáticamente por Spring Boot
```

---

## 🎨 Experiencia de Usuario

### Formulario de Registro

```
┌─────────────────────────────────────────────────────┐
│         📄 Documentos de Verificación               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐ │
│  │ Carta del Restaurante* │  │ Licencia Funcionam. │ │
│  │ [Seleccionar archivo] │  │ [Seleccionar archivo]│ │
│  │ ✅ Menú o carta      │  │ Opcional             │ │
│  └──────────────────────┘  └──────────────────────┘ │
│                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐ │
│  │ Carnet de Sanidad *  │  │ Logo del Restaurante│ │
│  │ [Seleccionar archivo] │  │ [Seleccionar archivo]│ │
│  │ ✅ Documento oblig.  │  │ Opcional             │ │
│  └──────────────────────┘  └──────────────────────┘ │
│                                                     │
└─────────────────────────────────────────────────────┘

* = Campos obligatorios
```

---

## 🚀 Para Implementar en Producción

### 1. Compilar y Reconstruir
```bash
.\mvnw.cmd clean package -DskipTests
docker-compose up -d --build
```

### 2. Verificar Logs
```bash
docker logs sistemafoodix-mi-app-1 --tail 20
```

### 3. Probar Registro
1. Ir a http://localhost:8080/registro-restaurante
2. Llenar el formulario completo
3. Subir:
   - ✅ **Carta del Restaurante** (obligatorio)
   - ✅ **Carnet de Sanidad** (obligatorio)
   - Logo del Restaurante (opcional)
   - Licencia de Funcionamiento (opcional)
4. Hacer clic en "Registrar mi Restaurante"

---

## 📌 Notas Importantes

### ⚠️ Problema del RUC API

**DETECTADO**: El RUC no se está cargando automáticamente porque:
```
📥 [RUC] Respuesta RAW: {"success":true,"datos":{"ruc":"","razon_social":"",...}}
```

**Causa**: Los datos están vacíos en la respuesta de la API.

**Solución REQUERIDA**: 
```
1. Verificar que el token API sea válido
2. Renovar el token si expiró
3. Actualizar docker-compose.yml con el nuevo token:
   
   - API_TOKEN=TU_NUEVO_TOKEN_AQUI
```

**Token Actual** (en docker-compose.yml):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo1NTksImV4cCI6MTc2MzMxODEzNH0.bzVljvbLILWrSrhsLV8sI7c-6I1gw7URf6R0lV0lToU
```

**Fecha de expiración**: 30 de noviembre de 2025 (según JWT payload)

---

## 🔍 Logs de Debug

Para verificar el funcionamiento:

```bash
# Ver logs completos
docker logs sistemafoodix-mi-app-1 -f

# Buscar logs de RUC
docker logs sistemafoodix-mi-app-1 | Select-String "RUC"

# Buscar respuestas RAW
docker logs sistemafoodix-mi-app-1 | Select-String "RAW"
```

---

## ✨ Próximos Pasos Sugeridos

1. ✅ **Resolver problema del token API**
   - Generar nuevo token en https://miapi.cloud
   - Actualizar docker-compose.yml
   - Reconstruir: `docker-compose up -d --build`

2. ⏳ **Implementar almacenamiento de archivos**
   - Descomentar código en `RestauranteService.java`
   - Implementar `FileStorageService`
   - Configurar ruta de uploads en servidor

3. ⏳ **Panel de administración**
   - Vista para revisar documentos subidos
   - Aprobar/rechazar restaurantes
   - Descargar documentos para verificación

---

## 📅 Fecha de Implementación
**10 de Noviembre, 2025**

## 👥 Estado
✅ **Código completado y compilando correctamente**
⚠️ **Token API requiere renovación para funcionar completamente**

---

**¿Alguna pregunta sobre los cambios?** 🤔
