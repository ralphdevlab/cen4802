 HEAD
# Disney Movie Watchlist: CEN4802 Midterm

Spring Boot app with a Watched / Unwatched / All filter, built and shipped by CI.

## Links
- Issue: #3 (Watched/Unwatched filter)
- Pull request: #4 (merged to main)
- Workflow: `.github/workflows/ci.yml`
- Image: `ghcr.io/ralphdevlab/disney-watchlist`

## What CI does
1. Checks out the commit
2. Sets up Java 17
3. Runs `./gradlew clean build` (compile, unit tests, package the jar)
4. Uploads the jar
5. On pushes to `main` only, and only if step 3 passed: builds a Docker image from that jar and pushes it to GHCR, tagged `latest` and the commit SHA

## Controlled failure
- CI #8 (`e9b0669`): the Watched filter was changed to query unwatched movies. The code still compiled, but `listMovies_withWatchedFilter_callsFindByWatchedTrue` failed, `build-and-test` went red, and `docker-image` was skipped.
- CI #9 (`4e1edf4`): bug fixed, both jobs green, new image published.

## Run the CI-built image
    docker pull ghcr.io/ralphdevlab/disney-watchlist:latest
    docker run -d -p 8081:8080 --name disney-watchlist-ci ghcr.io/ralphdevlab/disney-watchlist:latest
Then open http://localhost:8081/movies

## Kubernetes deployment (Assignment 6)

The app runs on a local Kubernetes cluster (Docker Desktop) from the CI-built image
`ghcr.io/ralphdevlab/disney-watchlist` (pinned to commit tag `4e1edf420211d3cb2493428fa22833f72d70610a`).

Manifests are in `k8s/`:
- `deployment.yaml` - 2 replicas, image, containerPort 8080, readiness probe
- `service.yaml` - NodePort Service (8080 -> pods, nodePort 30080)

```bash
kubectl config use-context docker-desktop
kubectl apply -f k8s/
kubectl get deployments
kubectl get pods
kubectl get service
# Docker Desktop doesn't expose NodePorts to localhost, so use port-forward:
kubectl port-forward service/disney-watchlist-service 9090:8080
# open http://localhost:9090/movies

kubectl scale deployment disney-watchlist --replicas=4   # management
kubectl delete -f k8s/                                    # cleanup
```
 2858c08 (Add Kubernetes manifests (Deployment + Service) and README section)
