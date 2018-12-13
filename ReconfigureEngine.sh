directorName=$1
directorPort=$2
INSTALLDIR=$3
cd /opt
tar xvzf DSEngineLinux64.tar.gz

printf "\n$1 lloydstestmanager.eastus.cloudapp.azure.com" > director.txt
cat director.txt >> /etc/hosts

cd $INSTALLDIR
./engine.sh stop
./configure.sh -s ${directorName}:${directorPort}
./engine.sh start

