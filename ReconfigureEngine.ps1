param(
[string]$directorName,
[string]$directorIP,
[string]$directorPort,
[string]$dsFolder,
[string]$sslEnabled
)

Stop-Service DataSynapse

#$dsFolder="c:\tibco\datasynapse\engine"
#$directorIP="10.1.0.6"
#$directorPort="8000"

Set-HostsEntry -IPAddress $directorIP -HostName $directorName -Description "GridServer Director"

Remove-Item -Recurse -Force "$($dsFolder)\\profiles"

$datfile="$($dsFolder)\\intranet.dat"

#We originally did the following to capture whether the engine was set up
#for HTTP or HTTPS. Now we're setting it explicitly based on the value
#of sslEnabled.
#$contents = [IO.File]::ReadAllText($datfile)
#$httPrefixDelim="://"
#$endpos=$contents.IndexOf($httPrefixDelim)+$httPrefixDelim.Length
#$newPrefix=$contents.Substring(0,$endpos)

if($sslEnabled.ToLower() -eq "true") {
   $newPrefix="https://"
} else {
   $newPrefix="http://"
}

"$($newPrefix)$($directorIP):$($directorPort)/livecluster/public_html/register/register.jsp".Trim() | Out-File -Encoding Ascii $datfile

Start-Service DataSynapse
