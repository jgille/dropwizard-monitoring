Dropwizard Monitoring
=====================

This project contains a Dropwizard bundle that will extend the health checking functionality of Dropwizard.

It adds a couple of features:

* An endpoint on the admin port where you can get metadata about the service
* A new health endpoint where each health check contains more information than the original Dropwizard ones
* The possibility to report/push service health and metadata, similar to how metrics can be reported

Additional information about the health checks
--------------------------------------------------

A default Dropwizard health check only contains the name, a boolean flag (healthy or not) and an optional message and error.

With the bundle you can add more information that can be valuable when doing monitoring/alerting:

* level - WARNING/CRITICAL
* type - An optional string, e.g. "INFRASTRUCTURE_COMPONENT" or "EXTERNAL_DEPENDENCY"
* description - An optional string describing the health check
* dependent_on - An optional string with the name of the service this check depends on
* link - An optional string linking to more information, perhaps to the status page of the service the check depends on

Health checks can be decorated with this information by adding an annotation on you health check class and/or by providing them
explicitly when registering the health check.

The endpoint host:adminPort/healthcheck will be behave as it always has, but host:adminPort/service/health
will return this extra information.

The only thing you need to do to get this is to register your health checks in the bundle instead of directly in the Dropwizard
health check registry.

Example response from the endpoint:

<pre>
{
  "unhealthy": [
    {
      "name": "broken",
      "status": "CRITICAL",
      "description": "I will always fail",
      "message": "Fail!",
      "error": "java.lang.RuntimeException: Fail!",
      "type": "SELF"
    }
  ],
  "healthy": [
    {
      "name": "google_something",
      "status": "HEALTHY",
      "message": "Search succeeded",
      "type": "EXTERNAL_DEPENDENCY",
      "dependent_on": "google"
    },
    {
      "name": "deadlocks",
      "status": "HEALTHY"
    }
  ]
}
</pre>

Service metadata
----------------

In a distributed environment, being able to ask a service to tell a little about itself can be really valuable. The bundle
will add an endpoint on host:adminPort/service/metadata that will return things like the service name, the version it's running,
the id of the instance it's running on (perhaps en EC2 instance id) etc.

Example response:

<pre>
{
  "service": {
    "service_name": "Sample App",
    "service_version": "1.0-SNAPSHOT"
  },
  "instance": {
    "instance_id": "d32501e364c0",
    "host_address": "172.17.2.61"
  }
}
</pre>

Reporting service health
------------------------

Apart from providing endpoints which a monitoring tool can poll, you can also push service health and metadata on a regular basis. It works
pretty much the same way as the Dropwizard metrics reporters, you configure a list of reporters each having a frequency
(how often to check health and report it).

A simple console reporter and one that will push json via http (POST) is implemented, but you can also easily create your own reporter(s). Perhaps publishing to Kafka or sending events to Riemann?

Caching health check results
----------------------------

Some health checks may be expensive and you don't want them to execute too frequently. You can avoid this by adding an @Cached annotation
on your health class.

Usage
-----

Documentation needed, but have a look in the sample-app module.

Building and testing
--------------------

Tests are written using Spock and will run just as any old Junit test. Just do a maven install.

There are also black box tests where a sample service and a sample monitoring service are spun up in Docker containers.

To build and run all tests:

mvn clean install -Pfatjar

./blackbox.sh verify
