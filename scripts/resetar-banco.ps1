# Apaga e recria o banco de DESENVOLVIMENTO "mapa_financeira".
# ATENCAO: todos os dados (contas, progresso, diagnosticos) sao perdidos.
# Depois de rodar, suba o backend: ele recria modulos, perguntas e cenarios do simulador.

$banco   = "mapa_financeira"
$usuario = "postgres"

# Procura o psql.exe na instalacao do PostgreSQL (usa a versao mais nova encontrada).
$psql = Get-ChildItem "C:\Program Files\PostgreSQL" -Directory -ErrorAction SilentlyContinue |
    Sort-Object Name -Descending |
    ForEach-Object { Join-Path $_.FullName "bin\psql.exe" } |
    Where-Object { Test-Path $_ } |
    Select-Object -First 1

if (-not $psql) {
    Write-Host "psql.exe nao encontrado em C:\Program Files\PostgreSQL. Rode os comandos SQL manualmente."
    exit 1
}

Write-Host "Isto vai APAGAR todos os dados do banco '$banco'."
$resposta = Read-Host "Digite SIM para continuar"
if ($resposta -ne "SIM") {
    Write-Host "Cancelado. Nada foi alterado."
    exit 0
}

Write-Host "Digite a senha do usuario '$usuario' do PostgreSQL quando for pedida."

# WITH (FORCE) derruba conexoes abertas (por exemplo, o backend ligado).
& $psql -U $usuario -d postgres -c "DROP DATABASE IF EXISTS $banco WITH (FORCE);"
if ($LASTEXITCODE -ne 0) { Write-Host "Falha ao apagar o banco."; exit 1 }

& $psql -U $usuario -d postgres -c "CREATE DATABASE $banco;"
if ($LASTEXITCODE -ne 0) { Write-Host "Falha ao criar o banco."; exit 1 }

Write-Host ""
Write-Host "Banco recriado e vazio."
Write-Host "Proximos passos: (1) reinicie o backend; (2) limpe a sessao do navegador (veja abaixo)."
Write-Host "Navegador: F12 > Application > Local Storage > clicar com o botao direito > Clear, ou use janela anonima."
