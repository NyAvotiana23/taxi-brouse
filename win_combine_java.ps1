# Define the base directory
$baseDir = "D:\S5\Mme Baovola\taxi-brouse\src\main\java\com\mdgtaxi"

# Combine entity files
Get-Content "$baseDir\entity\*.java" | Out-File "$baseDir\entity\entities.txt" -Encoding UTF8

# Combine service files
Get-Content "$baseDir\service\*.java" | Out-File "$baseDir\service\services.txt" -Encoding UTF8

# Combine dto files
Get-Content "$baseDir\dto\*.java" | Out-File "$baseDir\dto\dtos.txt" -Encoding UTF8

# Combine view files
Get-Content "$baseDir\view\*.java" | Out-File "$baseDir\view\views.txt" -Encoding UTF8

Write-Host "All files combined successfully."