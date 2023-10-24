$param0 = $args[0] # Class name
$param1 = $args[1] # Matrix type
$param2 = $args[2] # No of threads
$param3 = $args[3] # No of runs
$param4 = $args[4] # Input matrix
$param5 = $args[5] # Convolution matrix

$suma = 0
$difference = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = java $param0 $param2 $param4 $param5
    Write-Host $a
    $suma += $a
    Write-Host ""
    $fileA = "$PSScriptRoot\Outputs\sequential.txt"
    $fileB = "$PSScriptRoot\Outputs\parallel.txt"
    if(Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB)) {
        "files are different"
        $difference += 1
    }
    Else {
        "files are the same"
    }

}
$media = $suma / $i

Write-Host "Timp de executie mediu:" $media

if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    Set-Content outJ.csv 'Tip Matrice,Nr threads,Timp executie'
}

if ($difference -eq 0){
    Add-Content outJ.csv "$param1,$param2,$media"
}
else{
    Add-Content outJ.csv "$param1,$param2,Diferente"
}