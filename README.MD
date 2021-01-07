
[![Build Status](https://travis-ci.com/LukasBangStoltz/myStartCode_Backend.svg?branch=master)](https://travis-ci.com/LukasBangStoltz/myStartCode_Backend)
Clone this startcode, and the matching startcode for the client-side: https://github.com/fiske-halsen/CA3_Frontend
Before you begin, make sure that your docker-containers are running locally. Go to pom file and change the remote-server, to ready your ci-pipeline



1. Open the project, in your preffered editor. Security and JWT is already implementet, and dosen't need to be editet
2. Modify the persistance file, if nescesary, to match your desirable database
3. Create a repo, and push up the project. Setup Travis, and remember to add a remote password and username. If travis fails, use command: [mvn clean test -Dremote.user=USER -Dremote.password=PW tomcat7:deploy]
4. Run your given API in a browser, to check what attributes is returned in JSON. 
5. Now you can change your DTO classes, from the data you recived from the API.
6. Rename the StarWarsFetcher class, to something relevant. Change all URLS to the ones you were given
7. Change the names of endpoints, to something relevant for your API's.
8. Finally, edit your rest-assured and facade test 
