output "container_names" {
  value = docker_container.app[*].name
}

output "urls" {
  value = [for i in range(var.instance_count) : "http://localhost:${var.base_port + i}/movies"]
}
