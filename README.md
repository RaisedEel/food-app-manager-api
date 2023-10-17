# FOOD-APP-MANAGER

Food App Manager API is a streamlined solution for managing users and restaurants on a food platform. It caters to
regular
customers and restaurant owners, offering functionalities such as restaurant subscriptions and menu customization.
The API incorporates JWT token authentication for secure access, ensuring a seamless and secure experience for users.

## Requirements

Its recommended that you have some background with programming otherwise this setup is going to be very hard for you.
This is not like the programs you install that only requires clicking, if you want to correctly configure the API to
your needs, some programming is required. With that in mind:

1. You need to have JDK (version 17 onwards) installed on your machine. There are different versions and platforms
   available for use. I recommend checking this website and selecting the one that fits you
   best: https://adoptium.net/es/temurin/releases/.
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
Laragon comes with tools for this purpose, in any case there are a lot of different ways of doing this and not on the
scope of this text. To set the database, you just need to run the next query in the console:

```CREATE DATABASE food_app;```

This query will create the database needed for the API to work.
There are also tools that can create and execute the query for you, only requiring the name of the database.

> **Note:** To personalize the database name you would require to change the properties file on the project. Check
> details below.

### Configuring the API

Inside the folder of the API go to the next directory:`food-app-manager-api\src\main\resources\`.
There you will find the application.properties file, open it with your text editor of choice and change the username and
password with your own. If you don't have a password just leave it blank. Its important that you don't change anything
else.

Other configuration is customizing the admin profile. For this you can modify the user creation on the
FoodAppManagerApplication on the path `food-app-manager-api\src\main\java`. Change the password to a secure one for
example.

### Test the API

If you want to check that the API is functioning correctly you can do so by running the tests already packaged in the
API, however have in mind that the tests run on a custom database, so don't use them to check if your database is
correctly set after all they don't use it.

Just run the command: `mvnw test`

And the main application will be tested. If everything is correct no errors should pop up and the message "BUILD
SUCCESS" should appear on the screen.

There is also an additional test that can be executed to check if the average of ratings is being
calculated properly:

`mvnw test -Dtest=SubscriptionRepositoryTest`

This test is separated from the rest because is more intensive than the others.

## Start the API

Finally, you only have to open a terminal in the main directory of the project and run the command through a terminal:

`mvnw clean spring-boot:run`

Alternatively, you can execute the class FoodAppManagerApplication using Java to start the application.

Give it some minutes to compile and EUREKA!

## Use the API

Check the documentation by going to:

`http://localhost:8080/swagger-ui/index.html`

There you will find all the information you need to use all endpoints on the API, like if the
endpoint is protected, the data you need to send, parameters, responses and some errors cases.

## Next Steps

This little guide had the purpose of showing off this API, but as of right now, this application only works locally,
however in order to use it freely on the internet you would need to have a domain and set the database on the cloud
for example. Sadly that is outside the scope of this little guide, but at least now you know how to install and use this
API! So good job!