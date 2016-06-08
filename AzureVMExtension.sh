#!/bin/bash
#$1 = userID
#$2 = Director IP
#$3 = Director FQDN
#$4 = directorHTTPPort
#$5 = Broker IP
#$6 = Broker FQDN
printf "\n$2 $3" > director.txt
sudo bash -c \'cat director.txt >> /etc/hosts\'
printf "\n$5 $6" > broker.txt
sudo bash -c \'cat broker.txt >> /etc/hosts\'
sudo chown -hR $1 /opt
cd /opt/datasynapse/engine
./configure.sh -s $3:$4
./engine.sh start
