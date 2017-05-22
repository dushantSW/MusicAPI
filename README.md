# MusicAPI
Mashup of three api MusicBrainz, Wikipedia &amp; Cover Art Archive

## How to install
Redis on docker are optional to run the application if you want cache. Otherwise jump to **run app**

### Redis cache

``` 
docker run redis 
docker network create -d bridge music-redis
docker run -v "$(pwd)"/redis.conf:/usr/local/etc/redis/redis.conf --name music-redis --net music-redis -p 6379:6379 -d redis redis-server /usr/local/etc/redis/redis.conf

```

### Run app
```
java -jar ${JAVA_OPTS:--Xmx1024m} music-api.jar

```



