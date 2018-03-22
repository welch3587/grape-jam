directorName=$1
directorPort=$2
INSTALLDIR=$3

cd $INSTALLDIR
./engine.sh stop
./configure.sh -s ${directorName}:${directorPort}
./engine.sh start

