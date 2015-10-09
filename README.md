= boot-ups-ribbon

This project is a small shim to allow the use of user provided services to populate Ribbon load balancer server lists in a Spring Cloud Netflix based project.

To use, simple build this project and place the resulting jar on the classpath of your application using your favorite method.  Then, use `cf cups my-service -p "listOfServers"` and put in the comma separated list of server:port names that map to the given service.

In your application, anything that looks up the "my-service" service name from Ribbon (explicit lookups, Ribbon enabled Feign clients, Ribbon enabled RestTemplate, etc.) will use your server list as the static list of servers for that service name.