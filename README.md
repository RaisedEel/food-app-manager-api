# FOOD-APP-MANAGER

Food App Manager API is a streamlined solution for managing users and restaurants on a food platform. It caters to
regular
customers and restaurant owners, offering functionalities such as restaurant subscriptions and menu customization.
The API incorporates JWT token authentication for secure access, ensuring a seamless and secure experience for users.

## Requirements

1. You need to have Java on your machine.
2. You need to have an MySQL instance installed on your machine. I recommend you install [Laragon](https://laragon.org/)
   for an easier process.

## Setting up the API

First download the API by downloading the Zip on this repository. Use the green button with the label Code.

If you are sure your MySQL instance is running and working, you will need the next information:

1. Your username (The majority uses root as a starter name)
2. Your password (If you didn't set a password when installing MySQL then you don't need this)

### Setting the database

Now you need to create a database for the API to pick on and start working.
Depending on your method of installation you will need to find a way to make queries to your database. For example
Laragon comes with tools for this purpose, in any case there are a lot of different ways of doing this. You just need to
run the next query:

CREATE DATABASE food_app;

This query will create the database needed for the API to work.
There are also tools that can do the query for you by only asking the name.

### Configuring the API

Inside the folder of the API go to the next directory: food-app-manager-api\src\main\resources.
There you will find the application.properties file, open it with your text editor of choice and change the username and
password with your own. If you don't have a password just leave it blank. Its important that you don't change anything
else.

### Start the API

Finally, you only have to open a terminal in the main directory of the project and run the command through a terminal:
mvnw clean
spring-boot:run
The API should be running now.
