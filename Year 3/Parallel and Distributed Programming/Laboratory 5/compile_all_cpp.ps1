# compile_all_cpp.ps1
$cppFiles = Get-ChildItem -Recurse -Filter *.cpp

foreach ($cppFile in $cppFiles) {
    $outputName = Join-Path $cppFile.Directory.FullName ($cppFile.BaseName + ".exe")
    $compileCommand = "g++ -o `"$outputName`" `"$($cppFile.FullName)`""

    Write-Host "Compiling $($cppFile.FullName) to $outputName"
    Write-Host ""
    Invoke-Expression -Command $compileCommand
}
