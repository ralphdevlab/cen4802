#!/bin/bash
ab -t 20 -c 10 http://localhost:8080/movies
ab -t 20 -c 10 "http://localhost:8080/movies?search=toy"
ab -t 20 -c 10 "http://localhost:8080/movies?filter=watched"
