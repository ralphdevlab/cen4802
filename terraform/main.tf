terraform {
  required_version = ">= 1.5.0"
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {
  host = "unix:///Users/ralphalexandre/.docker/run/docker.sock"
}

resource "docker_image" "app" {
  name         = "${var.image_name}:${var.image_tag}"
  keep_locally = true
}

resource "docker_network" "app" {
  name = "disney-net"
}

resource "docker_container" "app" {
  count  = var.instance_count
  name   = "tf-disney-watchlist-${count.index + 1}"
  image  = docker_image.app.image_id
  memory      = var.memory_mb
  memory_swap = var.memory_mb * 2

  env = [
    "SPRING_APPLICATION_JSON=${jsonencode({ "logging.level.com.ralph.disney_watchlist" = var.log_level })}"
  ]

  ports {
    internal = 8080
    external = var.base_port + count.index
  }

  networks_advanced {
    name = docker_network.app.name
  }

  labels {
    label = "managed-by"
    value = "terraform"
  }
}
