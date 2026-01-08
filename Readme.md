## Create local.properties : 
db.url=jdbc:postgresql://host.docker.internal:5432/mdgtaxi
#db.url=jdbc:postgresql://localhost:5432/mdgtaxi

db.username=postgres
db.password=231205
db.driver=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
tomcat.home=/tmp
#tomcat.home=D:/Tomcat

## Run local : 
# mvn clean package


## Run Docker : 
# docker-compose up --build

