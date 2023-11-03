
$param1 = $args[0] # Nume fisier exe
$param2 = $args[1] # Matrix type
$param3 = $args[2] # No of runs
$param4 = $args[3] # No of rows
$param5 = $args[4] # No of cols
$param6 = $args[5] # No of rows/cols convolution matrix

# Get the grandparent folder of the script's folder
$grandparentFolder = Split-Path -Path (Split-Path -Path $PSScriptRoot -Parent) -Parent

# Specify the path for outCstatic.csv in the grandparent folder
$outCsvPath = Join-Path -Path $grandparentFolder -ChildPath 'outC.csv'

# Creare fisier .csv
if (!(Test-Path $outCsvPath)) {
    New-Item $outCsvPath -ItemType File
    #Scrie date in csv
    Set-Content $outCsvPath 'Tip Matrice,Tip alocare,Nr threads,Timp executie'
}

$suma = 0

for ($i = 0; $i -lt $param3; $i++) {
    # Write-Host "Rulare" ($i + 1)
    $a = & "${param1}" $param4 $param5 $param6 2>&1
    # Write-Host $a
    $suma += $a
    # Write-Host ""
}
$media = $suma / $param3
Write-Host "Timp de executie mediu:" $media
Write-Host ""
Add-Content $outCsvPath "$($param2),Dynamic,Sequential,$($media)"