param(
    [string]$MainClass = "main.Main",
    [string[]]$SourceDirs = @("accessory", "entity", "fenetre", "main")
)

rm **/*.class -ErrorAction SilentlyContinue;

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$binDir = Join-Path $projectRoot ".bin"

if (-not (Test-Path -LiteralPath $binDir)) {
    New-Item -ItemType Directory -Path $binDir | Out-Null
}

# Clean old compiled classes to avoid stale bytecode issues.
Get-ChildItem -Path $binDir -Recurse -File -Filter "*.class" -ErrorAction SilentlyContinue |
    Remove-Item -Force

$javaFiles = @()
foreach ($dir in $SourceDirs) {
    $sourcePath = Join-Path $projectRoot $dir
    if (-not (Test-Path -LiteralPath $sourcePath)) {
        continue
    }

    $javaFiles += Get-ChildItem -Path $sourcePath -Recurse -File -Filter "*.java" |
        ForEach-Object { $_.FullName }
}

if (-not $javaFiles -or $javaFiles.Count -eq 0) {
    throw "Aucun fichier .java trouve dans le projet."
}

Write-Host "Compilation vers: $binDir"
& javac -d $binDir $javaFiles
if ($LASTEXITCODE -ne 0) {
    throw "La compilation a echoue."
}

Write-Host "Execution: $MainClass"
& java -cp $binDir $MainClass
if ($LASTEXITCODE -ne 0) {
    throw "L'execution du programme a echoue."
}
