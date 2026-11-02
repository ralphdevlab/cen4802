#!/bin/bash
# Sends requests to the app nonstop for 60 seconds (10 at a time).
ab -t 60 -n 1000000 -c 10 http://localhost:9090/movies