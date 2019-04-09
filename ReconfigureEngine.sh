directorName=$1
directorPort=$2
INSTALLDIR=$3
cd /opt
tar xvzf DSEngineLinux64.tar.gz

printf "\n10.0.1.6 devhpcca120.centralus.cloudapp.azure.com" > director.txt
cat director.txt >> /etc/hosts

cd $INSTALLDIR
./engine.sh stop
./configure.sh -s ${directorName}:${directorPort}
./engine.sh start

