param(
[string]$director,
[string]$directorPort
)

Start-Process msiexec.exe -Wait -ArgumentList "/quiet /i c:\tibco\DSEngineInstall64.msi URL=$($director):$($directorPort)"
