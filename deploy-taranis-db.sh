#! /bin/sh

docker run --name taranis-db --network=interna --hostname=taranis-db \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASS=admin \
-e POSTGRES_DBNAME=taranis \
-e ALLOW_IP_RANGE='0.0.0.0/0' \
-v /etc/localtime:/etc/localtime:ro \
-p 36701:5432 \
-e POSTGRES_MULTIPLE_EXTENSIONS=postgis,hstore,postgis_topology \
-v /srv/taranis-db/:/var/lib/postgresql/ \
-d kartoza/postgis:12.0

#docker exec -it taranis-db pg_restore -U postgres -h localhost -W -d taranis /opt/taranis-db/taranis.tar

# CREATE EXTENSION pointcloud;
# CREATE EXTENSION pointcloud_postgis;