param(
[string]$dsFolder,
[string]$director,
[string]$directorPort
)

Stop-Service DataSynapse

#$dsFolder="c:\tibco\datasynapse\engine"
#$director="10.1.0.6"
#$directorPort="8000"

Remove-Item -Recurse -Force "$($dsFolder)\\profiles"

$datfile="$($dsFolder)\\intranet.dat"
$contents = [IO.File]::ReadAllText($datfile)
$httPrefixDelim="://"
$endpos=$contents.IndexOf($httPrefixDelim)+$httPrefixDelim.Length
$newPrefix=$contents.Substring(0,$endpos)

"$($newPrefix)$($director):$($directorPort)/livecluster/public_html/register/register.jsp".Trim() | Out-File -Encoding Ascii $datfile

Start-Service DataSynapse