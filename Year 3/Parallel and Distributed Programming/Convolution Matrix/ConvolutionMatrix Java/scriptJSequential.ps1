$param0 = $args[0] # Class name
$param1 = $args[1] # Matrix type
$param2 = $args[2] # No of runs
$param3 = $args[3] # Input matrix
$param4 = $args[4] # Convolution matrix

$suma = 0

if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    Set-Content outJ.csv 'Tip Matrice,Nr threads,Timp executie'
}

for ($i = 0; $i -lt 5*$param2; $i++){
    if($i % $param2 -eq 0 -and $i -ne 0)
    {
        $media = $suma / $param2
        Write-Host "Timp de executie mediu:" $media
        Write-Host ""
        Add-Content outJ.csv "$param1,Sequential,$media"
        $suma = 0
    }

    # Write-Host "Rulare" ($i+1)
    $a = java $param0 $param3 $param4
    # Write-Host $a
    $suma += $a
    # Write-Host ""

}
$media = $suma / $param2

Write-Host "Timp de executie mediu:" $media
Write-Host ""
Add-Content outJ.csv "$param1,Sequential,$media"
