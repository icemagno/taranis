FROM openjdk:18-jdk-bullseye
LABEL maintainer "Carlos M. Abreu <magno.mabreu@gmail.com>"

RUN apt update && apt -y install pdal

MAINTAINER magno.mabreu@gmail.com
COPY ./target/taranis-1.0.war /opt/lib/
ENTRYPOINT ["java"]
ENV LANG=pt_BR.utf8 
CMD ["-jar", "/opt/lib/taranis-1.0.war"]