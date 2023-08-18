# README #

Digest Prototype Manager

### What is this repository for? ###

* Digest Prototype Manager is a very basic webapp that allows a user to authenticate against Salesforce
and generate an Email Digest of their Chatter Feed.
* 1.5


### How do I get set up? ###

* This is a self-contained Heroku project that keeps a local copy of its dependencies
* Build it by running 'mvn clean package'
* All of the dependencies are managed in the Maven POM.xml
* You can run it locally with the embedded Jetty servlet: 'mvn jetty:run'
* It runs on https://localhost:8443 or http://locahost:8080 by default
* 
* To deploy it to Heroku:
* 1) heroku app:create <your_app_name>
* 2) git push heroku 
* 3) heroku config:set consumer_key:<the_connected_app_key>
  4) heroku config:set consumer_secret:<the_connected_app_secret>
  5) heroku open
  