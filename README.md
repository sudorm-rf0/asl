<div align="center">

# ⚡ ASL 期货交易平台

**全栈期货交易系统** · Android 客户端 · Java 微服务后端 · 运营后台 · Web 行情终端

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

基于**事件驱动架构**与**微服务拆分**的完整期货交易平台，覆盖移动端、后端、运营后台与行情终端全链路。

## ✨ 特性

- 📱 **Android 客户端** — Kotlin + Jetpack Compose，Auth → Market → Trading → Positions → Push
- 🔧 **微服务后端** — Java 21 + Spring Boot 3.2，10 个服务按业务域拆分
- ⚡ **高性能撮合** — LMAX Disruptor 无锁撮合引擎
- 🛡 **风控体系** — 独立风控服务：检查、强平、限额
- 📊 **可观测** — Prometheus + Grafana + ELK 全链路监控
- 🔐 **安全加固** — JWT 鉴权、密钥环境变量化、证书轮换

## 架构总览

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

## 🚀 快速启动

```bash
# 1. 启动基础设施
open -a Docker                          # 启动 Docker Desktop
cd backend && docker compose up -d mysql-master redis-master

# 2. 初始化数据库
docker exec -i futures-mysql-master mysql -u root -pfutures123 \
  < infrastructure/scripts/init-schema.sql

# 3. 编译并启动服务
mvn package -Dmaven.test.skip=true -T 4
bash start.sh start

# 4. 启动前端
cd ../web       && tmux new-session -d -s web "vite --port 5173 --host"
cd ../admin-ui  && tmux new-session -d -s admin-ui "vite --port 8090 --host"
```

或者一键启动：
```bash
bash start.sh start
```

## 🧩 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| futures-order | 8081 | 订单生命周期管理 |
| futures-matching | 8082 | 撮合引擎 (LMAX Disruptor) |
| futures-account | 8083 | 账户、认证、KYC |
| futures-fund | 8084 | 保证金、出入金 |
| futures-risk | 8085 | 风控检查、强平 |
| futures-market | 8086 | 行情、K线 |
| futures-settlement | 8087 | 日终结算 |
| futures-gateway | 8088 | API 网关、JWT 鉴权 |
| futures-push | 8093 | WebSocket 推送 |
| futures-admin | 8099 | 运营管理后台 |
| Admin UI | 8090 | Vue 3 管理界面 |
| Web | 5173 | React 行情终端 |
| Grafana | 3000 | 监控面板 |
| Prometheus | 9090 | 指标采集 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |

## 🛠 技术栈

| 层 | 技术 |
|------|------|
| Android | Kotlin, Jetpack Compose, Hilt, Retrofit |
| Web | React 19, TypeScript, Tailwind CSS, Zustand |
| Admin | Vue 3, Element Plus, ECharts, Pinia |
| 后端 | Java 21, Spring Boot 3.2, Spring Cloud Gateway |
| 数据库 | MySQL 8.0, Redis 7, MyBatis-Plus |
| 消息 | RocketMQ, LMAX Disruptor |
| 监控 | Prometheus, Grafana, ELK |
| 容器 | Docker, GHCR |

## 📚 文档

| 文档 | 说明 |
|------|------|
| [架构说明](docs/ARCHITECTURE.md) | 系统架构、数据流、事件驱动 |
| [部署指南](docs/DEPLOYMENT.md) | 从零到生产的完整部署步骤 |
| [API 文档](docs/API.md) | 网关路由、认证、WebSocket 协议 |
| [优化记录](docs/optimization-checklist.md) | Nacos、JVM、数据库优化历史 |
| [运维工具](tools/) | 备份、压测、构建、健康监控 |
| [Knife4j UI](http://localhost:8099/doc.html) | 管理后台 API 交互文档 (112 端点) |
| [健康监控](health.html) | 实时服务状态页面 |
