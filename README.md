# Create and manage Sales Offer
### Assumption
Create offer request will accept offer name, price, currency and expiryTime in minutes as below. All these parameters are mandatory and expiryTime in minutes should be equals are more than 1 minutes
``` json
{
  "name": "LG TV",
  "price": 299.9,
  "currency": "GBP",
  "expiryTimeInMin": 5
}
```
In-memory H2 DB has been used to store offers.

A scheduler are craeted to find any expired offer and mark as inactive. Scheduler run time can be configured in application.properties and should be less than a a offer created with expiryTimeInMin. Default should be 40 seconds in this case as minimum expiry time is 1 minute.



### Steps to run this project
Checkout/download this maven project from github (https://github.com/sanjaykushwaha83/fisglobletest.git)

1. Open command prompt and go to project checkout/download directory 
2. Clean and build this Maven project using below command
   mvn clean install
3. Run project using below command
   mvn spring-boot:run
4. Go to browser and open swagger url: http://localhost:8888/fisglobal-test/swagger-ui.html

5. Following are the rest api are exposed to add,update,get, delete:
  
  * Create offer 
  
     POST http://localhost:8888/fisglobal-test/api/salesoffer
  * Update offer 


     PUT http://localhost:8888/fisglobal-test/api/salesoffer
  * Get offer by Id

     GET http://localhost:8888/fisglobal-test/api/salesoffer/{id}
  * Get all offers 
  
     GET http://localhost:8888/fisglobal-test/api/salesoffer/getAll
  * Delete offer by Id
  
     DELETE http://localhost:8888/fisglobal-test/api/salesoffer/{id}
     
Now you will be able to add,update,get,delete and get all offers.

- In-memory H2 DB can be access here while runnig the application http://localhost:8888/fisglobal-test/h2-console/

Note: I tried to add test all possible tests. I was not able to write a good unit test for scheduler so created and Integration test to test it. Integration test can be more improved to test all possible scenario.
javadoc and code quality can be improved as well.
