variable "image_name" {
  type    = string
  default = "ghcr.io/ralphdevlab/disney-watchlist"
}

variable "image_tag" {
  type    = string
  default = "e58be8a3cbab8f30d9e3bb9077bf840345e971a1"
}

variable "instance_count" {
  type    = number
  default = 2
}

variable "base_port" {
  type    = number
  default = 8090
}

variable "memory_mb" {
  type    = number
  default = 768
}

variable "log_level" {
  type    = string
  default = "INFO"
}
