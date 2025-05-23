# SMS Filter Service

## Run with Docker Compose

To start the application along with PostgreSQL and WireMock, use:


```bash
docker compose up
```
### Example Usage

###  Enable protection for a number

```
curl -X POST http://localhost:8080/sms \
  -H "Content-Type: application/json" \
  -d '{
        "sender": "48700111222",
        "recipient": "48123456789",
        "message": "START"
      }'
```
Response:
Protection enabled


### Send a message that contains a phishing link
```
curl -X POST http://localhost:8080/sms \
  -H "Content-Type: application/json" \
  -d '{
        "sender": "487001112233",
        "recipient": "48700111222",
        "message": "http://www.m-bonk.pl"
      }'
```
Response:
Message blocked (phishing detected)
###  Configuration and Tests

The phishing service is mocked with WireMock and its base URL is configured via the `app.webrisk.url` property.

In production, this would be replaced with the real  Web Risk API.

## Running Tests

```bash
./gradlew test
```

## License
MIT
