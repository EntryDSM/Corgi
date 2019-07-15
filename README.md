# Corgi: Docker registry observer

## How to build it?
```
sbt assembly
```

## How to use Corgi?

    sudo java -jar Corgi.jar --config config.json

Config file example:

    {
      "registryProtocol": "https",
      "registryURL": "<registry url>",
      "userID": "<username>",
      "password": "<password>",
      "pollingRatePerMinute": 5,
      "runScripts": {
        "hermes": "<launch script>"
      },
      "dockerEngineSocket": "unix:///var/run/docker.sock"
    }
    
