

### Starting KeyCloak docker image
```
docker-compose -f docker-compose.yml up
```
Create the administrator account
```
docker exec local_keycloak \
    /opt/jboss/keycloak/bin/add-user-keycloak.sh \
    -u admin \
    -p admin \
&& docker restart local_keycloak
```
Then access at [http://localhost:28080/auth/](http://localhost:28080/auth/). 
The administrator default account and password is `admin`/`admin`. 
Create a user and access [http://localhost:28080/auth/realms/appsdeveloperblog/account](http://localhost:28080/auth/realms/appsdeveloperblog/account). 

Send the GET request
```
curl --location --request GET \
'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/auth?client_id=photo-app-code-flow-client&response_type=code&scope=openid profile&redirect_uri=http://localhost:8083/callback&state=afwerfgqawerf'
```
Get the callback response with the `code`:
```
http://localhost:8083/callback?state=afwerfgqawerf&session_state=b3f87ee9-91d2-4c60-b0e8-7b9f3c0d1ce4&code=5e5d41dd-9c22-4ea5-b351-a122cdc3c03a.b3f87ee9-91d2-4c60-b0e8-7b9f3c0d1ce4.c9a39c20-15af-4caa-97f4-139806bdca80
```
Send the POST request
```
curl --location --request POST 'http://localhost:28080/auth/realms/appsdeveloperblog/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: JSESSIONID=CF73A7ACF929345F601AE960AE64194D.9e12cb34caaa; JSESSIONID=CF73A7ACF929345F601AE960AE64194D' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=photo-app-code-flow-client' \
--data-urlencode 'client_secret=68ed3abb-2fc2-4edc-a734-2fca3d93e12f' \
--data-urlencode 'code=5e5d41dd-9c22-4ea5-b351-a122cdc3c03a.b3f87ee9-91d2-4c60-b0e8-7b9f3c0d1ce4.c9a39c20-15af-4caa-97f4-139806bdca80' \
--data-urlencode 'redirect_uri=http://localhost:8083/callback' \
--data-urlencode 'scope=openid profile'
```


