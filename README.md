## Logging (Assignment 7)

The app uses Logback through SLF4J (included with Spring Boot). Configuration is in
`src/main/resources/application.properties`: root level INFO, `com.ralph.disney_watchlist` at DEBUG,
console output with timestamp, level, thread, logger and message.

Levels used: DEBUG (request details), INFO (completed actions), WARN (odd input), ERROR (failed toggle).

View logs in Kubernetes:
```bash
kubectl logs deployment/disney-watchlist
kubectl logs deployment/disney-watchlist | grep -E ' (WARN|ERROR) '
```

Change the level without rebuilding:
```bash
kubectl set env deployment/disney-watchlist SPRING_APPLICATION_JSON='{"logging.level.com.ralph.disney_watchlist":"INFO"}'
```
