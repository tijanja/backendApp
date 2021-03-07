# Spring Boot Backend App Project
This is a sample Java / Gradle / Spring Boot (version 1.0) application. An appication that has a feature for the creation and managment of a user account.
## How to Run 
This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.


* Clone this repository 
* Make sure you are using JDK 1.8 and Gradle 6.x
* You can build the project and run the tests by running```./gradlew build``` and ```./gradlew test```
* Once successfully built, you can run the application by
```java -jar build/libs/backendApp-0.0.1-SNAPSHOT.jar```

Once the application runs you should see something like this

```
2021-03-07 20:19:43.905  INFO 1933 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 80 (http) with context path ''
2021-03-07 20:19:43.907  INFO 1933 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Context refreshed
2021-03-07 20:19:43.928  INFO 1933 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2021-03-07 20:19:43.965  INFO 1933 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2021-03-07 20:19:44.143  INFO 1933 --- [           main] com.test.test.BackendApplication         : Started BackendApplication in 5.924 seconds (JVM running for 6.366)

 and logs of pre loaded seed data

2021-03-07 23:55:50.172  INFO 4101 --- [           main] com.test.test.data.SeedDataLoader        : Preloading UserDao{id=1, title='null', firstName='Frank', lastName='Eddy', email='eddy@gmail.com', phone='08099934567', password='$2a$10$Se7JdtXXTagPcEL9sciSduJMAI9e1FmA3pou6EXBcaGz3TqaS/DYm', role='USER', dateRegistered=2021-03-07, verified=null, dateDeactivated=null, status=0}
2021-03-07 23:55:50.261  INFO 4101 --- [           main] com.test.test.data.SeedDataLoader        : Preloading UserDao{id=2, title='null', firstName='Adetunji', lastName='Akinde', email='adetunjiakinde@gmail.com', phone='08060774043', password='$2a$10$hUM2OUzftwyy8/3yQWFQUOTsH/L9ASS/kXg5h9AwygX72Uva9FF6u', role='ADMIN', dateRegistered=2021-03-07, verified=null, dateDeactivated=null, status=0}
2021-03-07 23:55:50.352  INFO 4101 --- [           main] com.test.test.data.SeedDataLoader        : Preloading UserDao{id=3, title='null', firstName='Adewale', lastName='Adeniji', email='adewale@gmail.com', phone='0808990987', password='$2a$10$wnZW5id6xH6HHZt0.6OXfOnhsIlb6HhjJvO3bHseXJ88lMjwtH.8i', role='USER', dateRegistered=2021-03-07, verified=null, dateDeactivated=null, status=0}
2021-03-07 23:55:50.444  INFO 4101 --- [           main] com.test.test.data.SeedDataLoader        : Preloading UserDao{id=4, title='null', firstName='Jude', lastName='Elvis', email='judeelvis@gmail.com', phone='08066634567', password='$2a$10$0g9yf6wdEntgXYvl9eUC1.L33mgU6xrceAWq020jSq.hcTh/vr3r.', role='USER', dateRegistered=2021-03-07, verified=null, dateDeactivated=null, status=0}
```
## API Docs
### Authenticaticate
```
POST /api/oauth/token
Accept: application/json
Content-Type: application/json

REQUEST:

{
    "email":"<user email>",
    "password":"<user password>"
}

RESPONSE: 
{
    "token":"JWT token"
}
```
### Create/Regiser a User Account
```
POST /api/user
Accept: application/json
Content-Type: application/json

REQUEST:
{
    "firstName":"<first name>",
    "lastName":"<last name>",
    "password":"<password>",
    "phone":"<phone>",
    "email":"<user email>",
    "title":"<title>"
}

RESPONSE:
{
    "id": 1,
    "title": "Mr",
    "firstName": "user",
    "lastName": "user",
    "email": "user@gmail.com",
    "phone": "08060000000",
    "password": "null",
    "role": "USER",
    "dateRegistered": "2021-03-07",
    "verified": null,
    "dateDeactivated": null,
    "status": 0
}
```
### To view Swagger 2 API docs
Run the server and browse to localhost:80/swagger-ui.html
