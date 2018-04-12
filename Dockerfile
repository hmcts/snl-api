FROM openjdk:8-jre

COPY build/install/snl-api /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" curl --silent --fail http://localhost:8090/health

EXPOSE 8090

ENTRYPOINT ["/opt/app/bin/snl-api"]
