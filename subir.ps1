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
Write-Host "Cambios actuales:" -ForegroundColor Yellow
git status --short
Write-Host ""

# Pedir mensaje del commit
$mensaje = Read-Host "Describe los cambios realizados"

if ([string]::IsNullOrWhiteSpace($mensaje)) {
    Write-Host "ERROR: Debes escribir una descripcion." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "[1/4] Preparando archivos..." -ForegroundColor Yellow
git add .

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al preparar los archivos." -ForegroundColor Red
    exit 1
}

# Verificar si realmente hay algo para commit
git diff --cached --quiet

if ($LASTEXITCODE -eq 0) {
    Write-Host "No hay cambios nuevos para guardar." -ForegroundColor Yellow
    exit 0
}

Write-Host "[2/4] Creando commit..." -ForegroundColor Yellow
git commit -m "$mensaje"

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR al crear el commit." -ForegroundColor Red
    exit 1
}

Write-Host "[3/4] Sincronizando con GitHub..." -ForegroundColor Yellow
git pull --rebase origin main

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ATENCION: No se pudo sincronizar con GitHub." -ForegroundColor Red
    Write-Host "Puede existir un conflicto que debe resolverse manualmente." -ForegroundColor Yellow
    exit 1
}

Write-Host "[4/4] Subiendo cambios..." -ForegroundColor Yellow
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