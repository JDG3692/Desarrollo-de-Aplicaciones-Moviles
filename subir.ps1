Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "     ACTUALIZAR PROYECTO EN GITHUB" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos dentro de un repositorio Git
git rev-parse --is-inside-work-tree 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Esta carpeta no es un repositorio Git." -ForegroundColor Red
    exit 1
}

# Mostrar cambios actuales
Write-Host "[1/5] Cambios actuales:" -ForegroundColor Yellow
git status --short
Write-Host ""

# Pedir mensaje del commit
$mensaje = Read-Host "Describe los cambios realizados"

if ([string]::IsNullOrWhiteSpace($mensaje)) {
    Write-Host "ERROR: Debes escribir una descripcion." -ForegroundColor Red
    exit 1
}

# Preparar archivos
Write-Host ""
Write-Host "[2/5] Preparando archivos..." -ForegroundColor Yellow
git add .

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al preparar los archivos." -ForegroundColor Red
    exit 1
}

# Comprobar si hay cambios para guardar
git diff --cached --quiet

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "No hay cambios nuevos para guardar." -ForegroundColor Yellow
    exit 0
}

# Crear commit
Write-Host "[3/5] Creando commit..." -ForegroundColor Yellow
git commit -m "$mensaje"

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al crear el commit." -ForegroundColor Red
    exit 1
}

# Sincronizar con GitHub
Write-Host "[4/5] Sincronizando con GitHub..." -ForegroundColor Yellow
git pull --rebase origin main

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "     NO SE PUDO SINCRONIZAR" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Puede existir un conflicto que debe resolverse manualmente." -ForegroundColor Yellow
    Write-Host "Tus cambios ya estan guardados en un commit local." -ForegroundColor Yellow
    exit 1
}

# Subir cambios
Write-Host "[5/5] Subiendo cambios a GitHub..." -ForegroundColor Yellow
git push origin main

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: No se pudieron subir los cambios." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "     CAMBIOS SUBIDOS CORRECTAMENTE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Commit: $mensaje" -ForegroundColor Green
Write-Host ""

git status