FROM java:8

WORKDIR /

COPY target/dropwizard-monitoring-blackbox-tests.jar /opt/monitoring-blackbox-tests/monitoring-blackbox-tests.jar

CMD ["java", "-jar", "/opt/monitoring-blackbox-tests/monitoring-blackbox-tests.jar" ]

