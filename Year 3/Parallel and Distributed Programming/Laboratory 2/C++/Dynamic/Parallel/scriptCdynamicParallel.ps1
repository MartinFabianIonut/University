
$param1 = $args[0] # Nume fisier exe
$param2 = $args[1] # Matrix type
$param3 = $args[2] # No of threads
$param4 = $args[3] # No of runs
$param5 = $args[4] # No of rows
$param6 = $args[5] # No of cols
$param7 = $args[6] # No of rows/cols convolution matrix

# Get the grandparent folder of the script's folder
$grandparentFolder = Split-Path -Path (Split-Path -Path $PSScriptRoot -Parent) -Parent

# Specify the path for outCstatic.csv in the grandparent folder
$outCsvPath = Join-Path -Path $grandparentFolder -ChildPath 'outC.csv'

# Creare fisier .csv
if (!(Test-Path $outCsvPath)) {
    New-Item $outCsvPath -ItemType File
    Set-Content $outCsvPath 'Tip Matrice,Tip alocare,Nr threads,Timp executie'
}

$suma = 0
$difference = 0

for ($i = 0; $i -lt $param4; $i++) {
   
    $a = & "${param1}" $param3 $param5 $param6 $param7 2>&1
    
    $suma += $a
    
    $fileA = "$grandparentFolder\Outputs\sequential.txt"
    $fileB = "$grandparentFolder\Outputs\parallel.txt"
    if (Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB)) {
       
        $difference += 1
    }
}

$media = $suma / $param4

if ($difference -eq 0) {
    Add-Content $outCsvPath "$($param2),Dynamic,$($param3),$($media)"
}
else {
    Add-Content $outCsvPath "$($param2),Dynamic,$($param3),Diferente"
    Write-Host "Diferente"
}

Write-Host "Timp de executie mediu:" $media
Write-Host ""