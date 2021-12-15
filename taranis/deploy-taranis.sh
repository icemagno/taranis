#! /bin/sh

svn update
mvn clean package

docker ps -a | awk '{ print $1,$2 }' | grep magnoabreu/taranis:1.0 | awk '{print $1 }' | xargs -I {} docker rm -f {}
docker rmi magnoabreu/taranis:1.0

docker build --tag=magnoabreu/taranis:1.0 --rm=true .

docker run --name taranis --hostname=taranis \
	-v /etc/localtime:/etc/localtime:ro \
	-p 36700:36700 \
	-v /srv/pointgen/:/pointgen/ \
	-d magnoabreu/taranis:1.0	

docker network connect sisgeodef taranis
docker network connect apolo taranis

