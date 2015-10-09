# boot-ups-ribbon

This project is a small shim to allow the use of user provided services to populate Ribbon load balancer server lists in a Spring Cloud Netflix based project.  This jar expects that you are using Spring Boot, as it is implemented via an EnvironmentPostProcessor.

To use, simply build this project and place the resulting jar on the classpath of your Spring Boot based application using your favorite dependency management tool.  

Then, use `cf cups my-service -p "listOfServers"` and put in the comma separated list of server:port names that map to the given service.  Push your app to Cloud Foundry without starting it, then bind the user provided service to your app, and start it.

In your application, anything that looks up the "my-service" service name from Ribbon (explicit lookups, Ribbon enabled Feign clients, Ribbon enabled RestTemplate, etc.) will use your server list as the static list of servers for that service name.