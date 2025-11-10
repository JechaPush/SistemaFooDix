# ============================================
# SCRIPT PARA ACTUALIZAR BASE DE DATOS
# ============================================
# Uso: .\importar_backup.ps1
#
# Para companeros que YA TIENEN el proyecto corriendo
# Solo actualiza la BD (no toca Docker)
# ============================================

Write-Host "Actualizando base de datos..." -ForegroundColor Cyan
Write-Host ""

# Verificar que Docker este corriendo
$dockerRunning = docker ps 2>&1 | Select-String "sistemafoodix-db-1"
if (-not $dockerRunning) {
    Write-Host "ERROR: Docker no esta corriendo" -ForegroundColor Red
    Write-Host "Ejecuta: docker-compose up -d" -ForegroundColor Yellow
    exit 1
}

# Verificar que exista el archivo de backup
if (-not (Test-Path "backup_db_foodix.sql")) {
    Write-Host "ERROR: No se encontro el archivo backup_db_foodix.sql" -ForegroundColor Red
    Write-Host "Primero haz: git pull" -ForegroundColor Yellow
    exit 1
}

$tamano = (Get-Item "backup_db_foodix.sql").Length / 1KB
Write-Host "Backup encontrado: $([math]::Round($tamano, 2)) KB" -ForegroundColor Green
Write-Host ""

# Preguntar confirmacion
Write-Host "ADVERTENCIA: Esto actualizara tu BD con los cambios de Daniela" -ForegroundColor Yellow
$confirmacion = Read-Host "Continuar? (S/N)"
if ($confirmacion -ne "S" -and $confirmacion -ne "s") {
    Write-Host "Actualizacion cancelada" -ForegroundColor Red
    exit 0
}

Write-Host ""
Write-Host "Importando cambios..." -ForegroundColor Cyan

# Importar el backup
Get-Content "backup_db_foodix.sql" | docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix 2>$null

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK: Base de datos actualizada" -ForegroundColor Green
    Write-Host ""
    
    # Verificar datos
    $usuarios = docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) FROM usuario;" 2>$null | Select-Object -Last 1
    $restaurantes = docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) FROM restaurante;" 2>$null | Select-Object -Last 1
    
    Write-Host "Usuarios: $usuarios | Restaurantes: $restaurantes" -ForegroundColor Cyan
    Write-Host ""
    
    # Reiniciar app
    Write-Host "Reiniciando aplicacion..." -ForegroundColor Yellow
    docker-compose restart mi-app | Out-Null
    Start-Sleep -Seconds 2
    
    Write-Host "LISTO! La BD esta sincronizada" -ForegroundColor Green
    Write-Host "Accede a: http://localhost:8080" -ForegroundColor Cyan
    
} else {
    Write-Host "ERROR al importar" -ForegroundColor Red
}

Write-Host ""
