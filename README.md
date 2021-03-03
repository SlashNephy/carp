# carp

🧭 Cydia APT repository proxy

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.30-blue)](https://kotlinlang.org)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/SlashNephy/carp)](https://github.com/SlashNephy/carp/releases)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/SlashNephy/carp/Docker)](https://hub.docker.com/r/slashnephy/carp)
[![Docker Image Size (tag)](https://img.shields.io/docker/image-size/slashnephy/carp/latest)](https://hub.docker.com/r/slashnephy/carp)
[![Docker Pulls](https://img.shields.io/docker/pulls/slashnephy/carp)](https://hub.docker.com/r/slashnephy/carp)
[![license](https://img.shields.io/github/license/SlashNephy/carp)](https://github.com/SlashNephy/carp/blob/master/LICENSE)
[![issues](https://img.shields.io/github/issues/SlashNephy/carp)](https://github.com/SlashNephy/carp/issues)
[![pull requests](https://img.shields.io/github/issues-pr/SlashNephy/carp)](https://github.com/SlashNephy/carp/pulls)

## Requirements

- Java 8 or later

## Get Started

### Docker

There are some image tags.

- `slashnephy/carp:latest`  
  Automatically published every push to `master` branch.
- `slashnephy/carp:dev`  
  Automatically published every push to `dev` branch.
- `slashnephy/carp:<version>`  
  Coresponding to release tags on GitHub.

`docker-compose.yml`

```yaml
version: '3.8'

services:
  carp:
    container_name: carp
    image: slashnephy/carp:latest
    restart: always
    environment:
      HTTP_HOST: 0.0.0.0
      HTTP_PORT: 20503

volumes:
  data:
    driver: local
```
