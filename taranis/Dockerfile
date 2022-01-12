FROM openjdk:18-jdk-bullseye
LABEL maintainer "Carlos M. Abreu <magno.mabreu@gmail.com>"

apt update && apt -y upgrade && apt -y install pdal

MAINTAINER magno.mabreu@gmail.com
COPY ./target/taranis-1.0.war /opt/lib/
ENTRYPOINT ["java"]
ENV LANG=pt_BR.utf8 
CMD ["-jar", "/opt/lib/taranis-1.0.war"]