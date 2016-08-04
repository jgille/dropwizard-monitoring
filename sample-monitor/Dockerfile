FROM java:8

WORKDIR /

COPY target/sample-monitor.jar /opt/sample-monitor/sample-monitor.jar
COPY sample-monitor.yml /opt/sample-monitor/sample-monitor.yml

CMD ["java", "-jar", "/opt/sample-monitor/sample-monitor.jar", "server", "/opt/sample-monitor/sample-monitor.yml" ]

EXPOSE 8067
EXPOSE 9067
