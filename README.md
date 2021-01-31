# Spring application - Blog

You can add posts with attachments, comments and manage the content. Each guest has limited access to the content, so they can create an account and manage their posts and comments. There is also the role of the admin who manages everything.

### Technologies

* Java 11
* Spring Boot
* Spring Security
* Spring Data
* Thymeleaf
* PostgreSQL
* Bootstrap
* JPA / Hibernate

### Screenshots
#### Posts
![1](https://user-images.githubusercontent.com/52882503/106299027-f6a95700-6254-11eb-8629-942db6ea1d60.png)

#### Comments
![2](https://user-images.githubusercontent.com/52882503/106299030-f7da8400-6254-11eb-8368-1fd4292f3f81.png)

### Configuration
#### src/main/resources/application.properties - main configuration file. 
Here it is possible to set username/password for database and change the settings, as well as change the port number.
You can also set the username/password for the [Mailgun](https://www.mailgun.com).

### How to run
#### You can run application from the command line with Maven.
Once the app starts, visit http://localhost:9494/posts

Admin username: admin123

Admin password: admin123
