# Spring Cloud Task Test App

### Installation

1. After cloning the current repository, make sure to do a `mvn package`
2. Build a docker image by providing the needed params in the following command:
For any Database setup:
```
docker build --build-arg SCTTA_DB_HOST_ARG=#{the_host} --build-arg SCTTA_DB_PORT_ARG=#{port_num} --build-arg SCTTA_DB_ADAPTER_ARG=#{adapter_name} --build-arg SCTTA_HBT_DIALECT_PACKAGE_ARG=#{package.address.here} --build-arg SCTTA_DB_USER_ARG=#{user} --build-arg SCTTA_DB_PASS_ARG=#{pass} --build-arg SCTTA_DB_NAME_ARG=#{db_name} --build-arg JAR_FILE=target/#{build_name} -t #{image_name}:latest .
```

There are provided defaults for Postgres DB clients, so you can omit those if your's are the same:
```
SCTTA_DB_HOST_ARG="192.168.99.100"
SCTTA_DB_PORT_ARG="5432"
SCTTA_DB_ADAPTER_ARG="postgresql"
SCTTA_HBT_DIALECT_PACKAGE_ARG="org.hibernate.dialect.PostgreSQLDialect"
```

respectively leaving our `docker build` command as follows:
```
docker build --build-arg SCTTA_DB_USER_ARG=#{user} --build-arg SCTTA_DB_PASS_ARG=#{pass} --build-arg SCTTA_DB_NAME_ARG=#{db_name} --build-arg JAR_FILE=target/#{build_name} -t #{image_name}:latest .
```


### Run the application
To run the application execute:
```
docker run -it --rm #{image_name} -type #{message_type(e.g. emotion or text)} -payload "#{some payload here"
```

Docker container will automatically stop if the Cloud Task Test App exit code is `0` but if an exception is thrown
the container will not stop. So using a `--rm` is recommended.

### Failure persistence

In order for the `Spring Cloud Task` to persist any failed tasks as such the `#run` method
should throw an `Exception` regardless if its `catch`ed later or not.

### Retry mechanism

`Spring Retry 1.2.2.RELEASE` is used for the retry mechanism. [It](https://docs.spring.io/spring-batch/trunk/reference/html/retry.html)
supports handle definitions for separate exception types, as well as `maxAttempts`, `backoff` period as well as exponential backoff with
`multiplier` option.

Current (sample) definition is:
```
@Retryable(
    value = { InvalidParameterException.class },
    maxAttempts = 4,
    backoff = @Backoff(delay = 5000)
)
```
which retries 4 times with a period of 5 seconds between retries.
Only the last unsuccessful retry is logged as a failure in the DB.
In order to log every try, a Persistent Retry Policy must be implemented.


### Project Reasoning

**Spring Cloud Task aims to bring functionality required to support short lived microservices to Spring Boot based applications.**

**The goals of Spring Cloud Task**
```
In most cases, the modern cloud environment is designed around the execution of processes that are not expected to end (think web applications or stream modules). If they do, it's considered a failure by the platform and they are typically restarted. While many platforms do have some method to execute a process that is expected to end (a batch job for example), the results of that execution are typically not maintained in a consumable way. Yet for mission critical applications, even though they are short lived, they still have the same non-functional requirements long lived processes have.

While this functionality is useful in a cloud environment, the same issues can arise in a traditional deployment model as well. When executing Spring Boot applications via a scheduler like cron, it can be useful to be able to monitor the results of the application after it’s completion.

A Spring Cloud Task takes the approach that a Spring Boot application can have a start and an end and still be production grade. Batch applications are just one example of where short lived processes can be helpful.
```
Source [Introducing Spring Clud Task (Spring Blog) ](https://spring.io/blog/2016/01/27/introducing-spring-cloud-task)


### Sources
  * [Spring Cloud Task Reference Guide](https://docs.spring.io/spring-cloud-task/docs/2.0.0.RELEASE/reference/htmlsingle/#features-configuration)
  * [An Intro to Spring Cloud Task](https://www.baeldung.com/spring-cloud-task)