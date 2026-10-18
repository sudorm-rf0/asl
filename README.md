<div align="center">

# ⚡ ASL Futures Trading Platform

**Full-stack futures trading system** · Android client · Java microservices backend · Operations console · Web market terminal

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-purple?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?logo=springboot&logoColor=white)
![Vue](https://img.shields.io/badge/Vue_3-4FC08D?logo=vuedotjs&logoColor=white)
![React](https://img.shields.io/badge/React_19-61DAFB?logo=react&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

![GitHub stars](https://img.shields.io/github/stars/sudorm-rf0/asl)
![GitHub license](https://img.shields.io/github/license/sudorm-rf0/asl)
![GitHub last commit](https://img.shields.io/github/last-commit/sudorm-rf0/asl)

</div>

---

A complete futures trading platform built on an **event-driven architecture** with **microservice decomposition**, covering the full chain across mobile, backend, operations console, and market terminal.

## ✨ Features

- 📱 **Android client** — Kotlin + Jetpack Compose, Auth → Market → Trading → Positions → Push
- 🔧 **Microservices backend** — Java 21 + Spring Boot 3.2, 10 services split by business domain
- ⚡ **High-performance matching** — lock-free matching engine based on LMAX Disruptor
- 🛡 **Risk control** — dedicated risk service: checks, forced liquidation, limits
- 📊 **Observability** — Prometheus + Grafana + ELK end-to-end monitoring
- 🔐 **Hardening** — JWT authentication, secrets externalized to env vars, certificate rotation

## Architecture overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         Android App                             │
│  Auth → Market → Trading → Positions → Push Notifications      │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP/WS
                    ┌──────▼──────┐
                    │  API Gateway │   futures-gateway (:8088)
                    │  JWT Auth   │   Rate Limiting
                    └──┬───┬───┬──┘
          ┌────────────┘   │   └──────────────┐
          ▼                ▼                  ▼
   ┌──────────┐    ┌────────────┐    ┌──────────────┐
   │  Auth    │    │   Order    │    │   Matching   │
   │  Account │    │   Trade    │    │   Disruptor  │
   │  (:8083) │    │  (:8081)   │    │   (:8082)    │
   └──────────┘    └─────┬──────┘    └──────────────┘
                         │               │
                    ┌────▼──────┐  ┌─────▼──────┐
                    │   Fund    │  │   Market   │
                    │  (:8084)  │  │  (:8086)   │
                    └───────────┘  └────────────┘
   ┌──────────┐    ┌────────────┐    ┌──────────────┐
   │   Risk   │    │ Settlement │    │    Push     │
   │  (:8085) │    │  (:8087)   │    │   WS (:8093)│
   └──────────┘    └────────────┘    └──────────────┘

   ┌──────────┐
   │  Admin   │ ◄── Admin UI (Vue 3, :8090)
   │  (:8099) │
   └──────────┘

   Web Terminal (React, :5173) ───► Gateway (:8088)
```

## 🚀 Quick start

```bash
# 1. Start infrastructure
open -a Docker                          # start Docker Desktop
cd backend && docker compose up -d mysql-master redis-master

# 2. Initialize the database
docker exec -i futures-mysql-master mysql -u root -pfutures123 \
  < infrastructure/scripts/init-schema.sql

# 3. Build and start the services
mvn package -Dmaven.test.skip=true -T 4
bash start.sh start

# 4. Start the frontends
cd ../web       && tmux new-session -d -s web "vite --port 5173 --host"
cd ../admin-ui  && tmux new-session -d -s admin-ui "vite --port 8090 --host"
```

Or start everything with one command:
```bash
bash start.sh start
```

## 🧩 Service ports

| Service | Port | Description |
|------|------|------|
| futures-order | 8081 | Order lifecycle management |
| futures-matching | 8082 | Matching engine (LMAX Disruptor) |
| futures-account | 8083 | Accounts, authentication, KYC |
| futures-fund | 8084 | Margin, deposits and withdrawals |
| futures-risk | 8085 | Risk checks, forced liquidation |
| futures-market | 8086 | Market data, candles |
| futures-settlement | 8087 | End-of-day settlement |
| futures-gateway | 8088 | API gateway, JWT authentication |
| futures-push | 8093 | WebSocket push |
| futures-admin | 8099 | Operations console backend |
| Admin UI | 8090 | Vue 3 admin interface |
| Web | 5173 | React market terminal |
| Grafana | 3000 | Monitoring dashboards |
| Prometheus | 9090 | Metrics collection |
| MySQL | 3306 | Database |
| Redis | 6379 | Cache |

## 🛠 Tech stack

| Layer | Technology |
|------|------|
| Android | Kotlin, Jetpack Compose, Hilt, Retrofit |
| Web | React 19, TypeScript, Tailwind CSS, Zustand |
| Admin | Vue 3, Element Plus, ECharts, Pinia |
| Backend | Java 21, Spring Boot 3.2, Spring Cloud Gateway |
| Database | MySQL 8.0, Redis 7, MyBatis-Plus |
| Messaging | RocketMQ, LMAX Disruptor |
| Monitoring | Prometheus, Grafana, ELK |
| Containers | Docker, GHCR |

## 📚 Documentation

| Document | Description |
|------|------|
| [Architecture](docs/ARCHITECTURE.md) | System architecture, data flow, event-driven design |
| [Deployment guide](docs/DEPLOYMENT.md) | Complete steps from zero to production |
| [API reference](docs/API.md) | Gateway routes, authentication, WebSocket protocol |
| [Optimization log](docs/optimization-checklist.md) | Nacos, JVM, and database optimization history |
| [Ops tools](tools/) | Backup, load testing, build, health monitoring |
| [Knife4j UI](http://localhost:8099/doc.html) | Interactive admin API docs (112 endpoints) |
| [Health monitor](health.html) | Live service status page |
