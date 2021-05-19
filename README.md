# hootboard-assignment-code

Simple Flow

First Step is creation of a user which is open to all, And username is the field that is unique  
Read/Update/Delete user can only be done when a user has logged in.  
One a user is logged in, He gets a token for RUD operations on the user. The token has to be inserted in the headers of each requests as **hoot-token**  
And one logged out/ He has to get the token again.  

**Steps to run the Project**  
Java 1.11
mvn install
now you can run the HootboardAssignmentApplication as a Spring boot application
HootBoard Collection.postman_collection.json in the folder --> can be used to import your collection in Postman and run the API's


**Creation of User**  
curl --location --request POST 'http://localhost:8080/user/create' \
--header 'Content-Type: application/json' \  
--data-raw '{  
    "username": "abhishek",  
    "password": "password",  
    "firstName": "abhishek",  
    "lastName": "kumar",  
    "emailAddress": [  
        "abhishek.kumar.virgo@gmail.com",  
        "abhibhaiinfobip@gmail.com"  
    ],  
    "postalAddress": [  
        "E2-1001. Kumar Picasso"  
    ]  
}'  
  
**User login**  
curl --location --request POST 'http://localhost:8080/user/login' \  
--header 'Authorization: Basic YWJoaXNoZWs6cGFzc3dvcmQ='  

**Read User**  
curl --location --request GET 'http://localhost:8080/user/read' \
--header 'hoot-token: 736dfcf4-59fd-40e3-93dd-1f7c3293f54b'

**Delete User**
curl --location --request DELETE 'http://localhost:8080/user/delete' \
--header 'hoot-token: 736dfcf4-59fd-40e3-93dd-1f7c3293f54b'
Please note, One a user is deleted, You are logged out automatically

**Update User**
curl --location --request PATCH 'http://localhost:8080/user/update' \  
--header 'hoot-token: 736dfcf4-59fd-40e3-93dd-1f7c3293f54b' \  
--header 'Content-Type: application/json' \  
--data-raw '{  
    "emailAddress": ["test@test.com"]  
}'  
