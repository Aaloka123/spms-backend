# Always run SPMS backend with JDK 26 + local .env
$ErrorActionPreference = "Stop"

$jdk = "C:\Program Files\Java\jdk-26.0.1"
if (-not (Test-Path $jdk)) {
    Write-Error "JDK not found at $jdk"
    exit 1
}

$env:JAVA_HOME = $jdk
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path

# Load .env into this process (optional; DotEnvLoader also loads at startup)
$envFile = Join-Path $PSScriptRoot ".env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^\s*#' -or $_ -match '^\s*$') { return }
        $name, $value = $_.Split('=', 2)
        Set-Item -Path "Env:$($name.Trim())" -Value $value.Trim()
    }
    Write-Host "Loaded .env"
}

Write-Host "JAVA_HOME = $env:JAVA_HOME"
java -version

Set-Location $PSScriptRoot
.\mvnw.cmd spring-boot:run
