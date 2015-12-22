# indigorestclient
This repository contains RESTfull client for Indigo based Web Services

To build it call following

	mvn compile
	mvn test
	mvn package

This will produce indigo-fg-api and put jar into local Maven repository.

If you don't want to perform tests, call:

	mvn package -DskipTests

Note that for testing you need access to Futuge Gateway Web Services.

