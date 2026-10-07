FROM curlimages/curl:8.12.1 AS download
RUN curl -fsSL \
    https://repo.maven.apache.org/maven2/io/prometheus/jmx/jmx_prometheus_httpserver/1.0.1/jmx_prometheus_httpserver-1.0.1.jar \
    -o /tmp/jmx_prometheus_httpserver.jar

FROM eclipse-temurin:17-jre
COPY --from=download /tmp/jmx_prometheus_httpserver.jar /opt/jmx-exporter.jar
EXPOSE 9404
ENTRYPOINT ["java", "-jar", "/opt/jmx-exporter.jar", "9404", "/etc/jmx-exporter/config.yml"]
