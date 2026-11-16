## Monitoring (Assignment 8)

The app is monitored in Kubernetes with `kubectl top`, which needs metrics-server. Docker Desktop doesn't
include it, so it was installed with:

```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl patch deployment metrics-server -n kube-system --type='json' -p='[{"op":"add","path":"/spec/template/spec/containers/0/args/-","value":"--kubelet-insecure-tls"}]'
```

Scripts in `monitoring/`:
- `sample.sh [samples] [seconds]` prints timestamped CPU and memory for the app pod
- `workload.sh` sends 60 seconds of requests with ApacheBench (`ab`) through the port-forward on port 9090

```bash
kubectl port-forward service/disney-watchlist-service 9090:8080
./monitoring/sample.sh 6 10     # baseline
./monitoring/workload.sh        # in another tab, while sampling again
```

## Infrastructure as Code

Terraform (v1.17.2, `kreuzwerker/docker` provider) in `terraform/` defines the Docker image, a `disney-net` network, and `instance_count` app containers (ports 8090+).

```bash
cd terraform
terraform init
terraform validate
terraform plan
terraform apply      # app at http://localhost:8090/movies
terraform destroy
```

Change `instance_count` in `variables.tf` to scale out. State files are not committed.
