#! /bin/sh

docker ps -a | awk '{ print $1,$2 }' | grep magnoabreu/taranis-db:1.0 | awk '{print $1 }' | xargs -I {} docker rm -f {}
docker rmi magnoabreu/taranis-db:1.0
docker build --tag=magnoabreu/taranis-db:1.0 --rm=true .

docker run --name taranis-db --hostname=taranis-db \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASS=admin \
-e POSTGRES_DBNAME=taranis \
-e ALLOW_IP_RANGE='0.0.0.0/0' \
-v /etc/localtime:/etc/localtime:ro \
-p 36701:5432 \
-e POSTGRES_MULTIPLE_EXTENSIONS=postgis,hstore,postgis_topology \
-v /srv/taranis-db/:/var/lib/postgresql/ \
-d magnoabreu/taranis-db:1.0

#docker exec -it taranis-db pg_restore -U postgres -h localhost -W -d taranis /opt/taranis-db/taranis.tar

