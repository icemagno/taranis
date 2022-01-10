
FROM hiparco/geoprocessor-core-bullseye:1.0

LABEL maintainer "Carlos M. Abreu <magno.mabreu@gmail.com>"


MAINTAINER magno.mabreu@gmail.com
COPY ./target/taranis-1.0.war /opt/lib/
ENTRYPOINT ["java"]
ENV LANG=pt_BR.utf8 
CMD ["-jar", "/opt/lib/taranis-1.0.war"]