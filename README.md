# Multiple Producers Consumers
Each of the producers and consumers share a counter with an initial value.
The producers increment the value by one, and the consumers decrement the value by one.
Each of the threads continues to run in parallel until the counter reaches to boundary, i.e.,
the value is equal to the lower limit or upper limit.

## Controller
The app exposed the following endpoints:

- `POST /api/v1/producers` to create a producer thread.
- `POST /api/v1/consumers` to create a consumer thread.
- `PATCH /api/v1/value/{value}` to update the counter value.

## Database
Each of the requests for creating a producer and consumer is persisted in the 
`requests` table in the following format:

- `worker_type` is either `producer` or `consumer`.
- `worker_id` is the unique id of the thread.
- `created_at` is the timestamp of the request.

Also, each time the counter value reaches to either lower limit or upper limit, the
event is stored in the `analytics` table in the following format:

- `worker_type` is either `producer` or `consumer`.
- `worker_id` is the unique id of the thread.
- `value` is the counter value.
- `created_at` is the timestamp of the request.

We use the `Flyway` migrations. The migration files are created in the `resources/db/migration` directory.

## Running the Application
Go to `docker/development` and run `docker-compose up`. It will start the `database` and `counter_app` services.
It should build the following two images:
```shell
$ docker image ls
REPOSITORY                TAG       IMAGE ID       CREATED          SIZE
development_counter_app   latest    b71f2e32e1af   39 seconds ago   455MB
mysql                     8.0.25    c0cdc95609f1   39 seconds ago   556MB
```
Also, the following two containers should be started:
```shell
$ docker container ls
CONTAINER ID   IMAGE                     COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
e8d0af0bbc4d   development_counter_app   "java -cp app:app/li…"   53 seconds ago   Up 50 seconds   0.0.0.0:8080->8080/tcp, :::8080->8080/tcp              development_counter_app_1
d567d77da0fd   mysql:8.0.25              "docker-entrypoint.s…"   22 minutes ago   Up 53 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   development_database_1
```

Run the following command to check the app health:
```shell
$ curl localhost:8080/actuator/health
{"status":"UP"}
```

## Sample Run
Suppose we make the following requests:
```shell
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/producers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
curl -XPOST localhost:8080/api/v1/consumers
```

The `requests` table data is:
```shell
mysql> select * from requests;
+----+-------------+--------------------------------------+---------------------+
| id | worker_type | worker_id                            | created_at          |
+----+-------------+--------------------------------------+---------------------+
|  1 | producer    | ccdf9be2-06e7-4258-aa07-aa08384aaf61 | 2021-05-30 17:52:35 |
|  2 | producer    | d95d0299-152f-4c69-8fa4-1be4b0855daf | 2021-05-30 17:52:36 |
|  3 | consumer    | b34e2478-1db4-47c3-81aa-47d797337932 | 2021-05-30 17:52:40 |
|  4 | consumer    | d9611a54-148e-40a5-9800-aafdc2f3f5ab | 2021-05-30 17:52:41 |
|  5 | consumer    | bba20181-e690-4b0b-9310-587a70de5dac | 2021-05-30 17:52:42 |
|  6 | producer    | 908e871a-56a5-40c6-a6b7-187bf81c293f | 2021-05-30 17:52:44 |
|  7 | producer    | b694dc73-ce0c-4e7a-b8f1-2eebb91a0d8e | 2021-05-30 17:52:45 |
|  8 | producer    | 1f6ab780-d08b-42d5-a415-6500601719cd | 2021-05-30 17:52:45 |
|  9 | producer    | beec64ac-1111-4243-857b-570071c0c570 | 2021-05-30 17:52:46 |
| 10 | consumer    | 71f7bcef-c3c2-45ca-80a5-9e99dd7ac624 | 2021-05-30 17:52:48 |
| 11 | consumer    | c1c14c1c-a194-4c68-8cc7-5665230b19ff | 2021-05-30 17:52:48 |
| 12 | consumer    | 77bb9caf-af43-4f00-82ae-f5da360981e2 | 2021-05-30 17:52:49 |
| 13 | consumer    | 19bb2101-36e7-4e8f-8612-5b1f026cc1cb | 2021-05-30 17:53:30 |
| 14 | consumer    | bdaae35a-83f9-49e8-ad6b-30f5f5f6b097 | 2021-05-30 17:53:30 |
| 15 | consumer    | e04f15ba-4f6c-4b50-8e32-e0e95b0bf1e4 | 2021-05-30 17:53:31 |
+----+-------------+--------------------------------------+---------------------+
15 rows in set (0.00 sec)
```

The `analytics` table data is:
```shell
mysql> select * from analytics;
+----+-------------+--------------------------------------+-------+---------------------+
| id | worker_type | worker_id                            | value | created_at          |
+----+-------------+--------------------------------------+-------+---------------------+
|  1 | consumer    | b34e2478-1db4-47c3-81aa-47d797337932 |     0 | 2021-05-30 17:54:08 |
+----+-------------+--------------------------------------+-------+---------------------+
1 row in set (0.01 sec)
```
