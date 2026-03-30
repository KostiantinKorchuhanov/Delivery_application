### Prerequisites
The project requires a **MySQL** database. It is recommended to use **XAMPP** for a quick local environment setup.
### 1. Launching XAMPP
Ensure that any existing system MySQL services are stopped to avoid port conflicts (3306), then start XAMPP:
```bash
sudo xampp start
```
### 2. Create a database
Open the http://localhost do to the phpMyAdmin and crete a database with the name **foodApp**
### 3. Build & Launch Execution
```
cd project
mvn clean install
```
**Launching the Desktop**
```
cd delivery-desktop
mvn javafx:run
```
**Launching the Web**
```
cd delivery-web
mvn spring-boot:run
```
