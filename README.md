# GraphQLServer
A GraphQLServer written in Kotlin

This GraphQLServer is written in Kotlin. It has a PostgreSQL database combined with a Redis cache.
It uses "graphql-kotlin-spring-server" a handy library which turns Kotlin functions straight into GraphQL queries, 
mutations and subscriptions. 

Further info:
https://opensource.expediagroup.com/graphql-kotlin/docs/server/spring-server/spring-overview/

When starting the server the current schema is printed. Use registerUser and loginUser mutations and queries to get a 
JsonWebToken, which needs to be added to Authorization header in all other queries and mutations.

To use this server, first fire up the docker-compose: (make sure its installed of course.)

```
docker-compose up --build
``` 
If you download this repository as is, the default settings apply and running is done most easily from IntelliJ IDEA. 

