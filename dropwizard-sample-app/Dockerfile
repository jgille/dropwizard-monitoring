FROM java:8

WORKDIR /

COPY target/dropwizard-sample-app.jar /opt/sample-app/sample-app.jar
COPY sample-app.yml /opt/sample-app/sample-app.yml

CMD ["java", "-jar", "/opt/sample-app/sample-app.jar", "server", "/opt/sample-app/sample-app.yml" ]

EXPOSE 8066
EXPOSE 9066
