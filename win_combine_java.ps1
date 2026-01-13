# Define the base directory
$baseJavaDir = "D:\S5\Mme Baovola\taxi-brouse\src\main\java\com\mdgtaxi"
$baseWebAppDir = "D:\S5\Mme Baovola\taxi-brouse\src\main\webapp"
# Combine entity files
Get-Content "$baseJavaDir\entity\*.java" | Out-File "$baseJavaDir\entity\entities.txt" -Encoding UTF8

# Combine service files
Get-Content "$baseJavaDir\service\*.java" | Out-File "$baseJavaDir\service\services.txt" -Encoding UTF8

# Combine dto files
Get-Content "$baseJavaDir\dto\*.java" | Out-File "$baseJavaDir\dto\dtos.txt" -Encoding UTF8

# Combine view files
Get-Content "$baseJavaDir\view\*.java" | Out-File "$baseJavaDir\view\views.txt" -Encoding UTF8


# Combine servlet files
Get-Content "$baseJavaDir\servlet\*.java" | Out-File "$baseJavaDir\servlet\servlets.txt" -Encoding UTF8


# Combine servlet files
Get-Content "$baseJavaDir\util\*.java" | Out-File "$baseJavaDir\util\utils.txt" -Encoding UTF8


# Jsp
Get-Content "$baseWebAppDir\**.jsp" | Out-File "$baseWebAppDir\jsps.txt" -Encoding UTF8




Write-Host "All files combined successfully."