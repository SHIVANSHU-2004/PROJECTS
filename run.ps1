$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$src = Join-Path $root "src\main\java"
$out = Join-Path $root "out"

if (-not (Test-Path $out)) {
    New-Item -ItemType Directory -Path $out | Out-Null
}

$sources = Get-ChildItem -Path $src -Filter *.java -Recurse | ForEach-Object { $_.FullName }
javac -d $out $sources
java -cp $out com.symptomchecker.Main
