directorName=$1
directorIP=$2
directorPort=$3
INSTALLDIR=$4
sslenabled=$5

printf "\n${directorIP} ${directorName}" > director.txt
cat director.txt >> /etc/hosts

cd $INSTALLDIR

./engine.sh stop

if [${sslenabled,,} = "true"]
then
    ./configure.sh -s ${directorName}:${directorPort} -l y
else
   ./configure.sh -s ${directorName}:${directorPort}
fi

./engine.sh start

