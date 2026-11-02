#!/bin/bash
# Prints a timestamped CPU/memory reading for the app pod.
# usage: ./monitoring/sample.sh [samples] [seconds-between]
N=${1:-6}
S=${2:-10}
for i in $(seq 1 $N); do
  echo "$(date +%H:%M:%S)  $(kubectl top pod -l app=disney-watchlist --no-headers)"
  sleep $S
done