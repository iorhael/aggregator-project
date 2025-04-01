# Price Aggregator project

#### Web application for monitoring prices of various goods

## Running application:

1. Copy contents of `.env.sample` file into `.env`:

```shell
cp .env.sample .env
```

2. Then build images and run containers by command:

```shell
docker compose up --build --watch
```

*`compose.override.yaml` helps to launch the application in a comfortable development environment: you do not need to
rebuild the application container on changes in the source code, because hot reload is enabled. To launch, you need to
change the name of the network of `pgdmin`, or, if you do not plan to supplement the application, you can simply
delete `compose.override.yaml`.*