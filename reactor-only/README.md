

Using swagger to visualize the endpoints [http://localhost:8080/movies/swagger-ui.html](http://localhost:8080/movies/swagger-ui.html).

### Create docker image: 
```
$ ./gradlew jar docker

$ docker images
REPOSITORY               TAG                  IMAGE ID       CREATED             SIZE
reactor-only             0.1.0                e940fd36338e   25 seconds ago      440MB
```

### Running the docker image
```
./gradlew jar docker dockerRun
```
or
```
docker run -i -t reactor-only:0.1.0
```

### Logs:
```
docker logs reactor-only
docker inspect reactor-only
```

