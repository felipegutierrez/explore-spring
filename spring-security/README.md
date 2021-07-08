

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



