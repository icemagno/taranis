#! /bin/sh
mvn install:install-file -Dfile=./3dcitydb-impexp.jar -DgroupId=org.citydb -DartifactId=impexp -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true

