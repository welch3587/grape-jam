directorName=$1
directorPort=$2
INSTALLDIR=$3
cd /opt
tar xvzf DSEngineLinux64.tar.gz

printf "\n10.0.0.4 devhpcca120-3.eastus.cloudapp.azure.com" > director.txt
cat director.txt >> /etc/hosts

cd $INSTALLDIR
./engine.sh stop
./configure.sh -s ${directorName}:${directorPort}
./engine.sh start

