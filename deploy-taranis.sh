#! /bin/sh

cd taranis/
svn update
mvn clean package

docker ps -a | awk '{ print $1,$2 }' | grep magnoabreu/taranis:1.0 | awk '{print $1 }' | xargs -I {} docker rm -f {}
docker rmi magnoabreu/taranis:1.0

docker build --tag=magnoabreu/taranis:1.0 --rm=true .

docker run --name taranis --network=sisgeodef --hostname=taranis \
	-v /etc/localtime:/etc/localtime:ro \
	-v /srv/taranis/:/taranis/ \
	-p 36700:36700 \
	-d magnoabreu/taranis:1.0	



