# Futures Trading Platform - Infrastructure Submodule

## Directory layout

```
infrastructure/
├── README.md                           # this file
├── version-matrix.md                   # version compatibility matrix
├── architecture.md                     # architecture topology (data flow and port allocation)
├── resource-specs.md                   # recommended resource specs (dev/prod/containers/JVM)
├── nacos-cluster.md                    # Nacos cluster setup and service registration guide
├── scripts/                            # config file scripts
│   ├── mysql-master.cnf                # MySQL primary config
│   ├── mysql-slave.cnf                # MySQL replica config
│   ├── redis-sentinel.conf            # Redis sentinel config
│   ├── rocketmq-broker.conf           # RocketMQ broker config
│   ├── seata-registry.conf            # Seata registry config
│   ├── prometheus.yml                 # Prometheus scrape config
│   └── grafana-datasources.yml        # Grafana datasource config
└── k8s/                                # Kubernetes deployment skeleton
    ├── namespace.yaml                  # namespace
    ├── deployment-template.yaml        # generic deployment template
    ├── gateway-deployment.yaml         # Gateway-specific deployment
    └── matching-deployment.yaml        # matching engine deployment (anti-affinity)
```

## One-command infrastructure startup

```bash
# Start all middleware (MySQL, Redis, Nacos, RocketMQ, Kafka, Seata, ES, SkyWalking, Prometheus, Grafana)
cd /Users/fangfang/Documents/黑期/futures-platform   # local futures-platform checkout
docker compose up -d

# Check startup status
docker compose ps

# View logs
docker compose logs -f nacos
docker compose logs -f skywalking-oap
```

## Registering services with Nacos

To make the microservices use Nacos service discovery:

```bash
# 1. Edit bootstrap.yml in each service
#    set nacos.discovery.enabled=true
#    set nacos.config.enabled=true

# 2. Edit the Gateway routes
#    change uri: http://localhost:8081 to uri: lb://futures-order

# 3. Edit the Feign clients
#    remove the hardcoded url= and use the service name
#    @FeignClient(name = "futures-order")

# 4. Start and smoke-test
cd futures-order && mvn spring-boot:run
```

## Access URLs

| Component | URL | Credentials |
|------|------|------|
| Nacos | http://localhost:8848/nacos | nacos/nacos |
| Nacos (gRPC) | localhost:9848 | - |
| RocketMQ Dashboard | http://localhost:8088 | - |
| SkyWalking UI | http://localhost:8089 | - |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin/futures123 |
| MySQL | localhost:3306 | futures/futures123 |
| MySQL Slave | localhost:3307 | futures/futures123 |
| Redis | localhost:6379 | futures123 |
| Kafka | localhost:9092 | - |
| Seata | localhost:8091 | - |
| Elasticsearch | localhost:9200 | - |

## Infrastructure startup recommendations

### Development (current)
- Use `docker compose up -d mysql redis nacos rocketmq-namesrv rocketmq-broker`
- Run the microservices directly from the local IDE or in screen sessions
- See the "minimal dev set" note at the top of `docker-compose.yml`

### Test / CI
- Full `docker compose up -d` (all middleware)
- Microservices run in containers

### Production
- Follow the configs under `infrastructure/k8s/`
- Deploy on a Kubernetes cluster
- Nacos 3 nodes + MySQL master/slave + Redis sentinel

## Tech stack map

```
┌───────────────────────────────────────────────────────────────────────┐
│                  Futures Trading Platform Tech Stack                  │
├──────────────────────┼─────────────────────────────────────────────────┤
│ Frontend             │ React 18 + TypeScript + Vite + Ant Design       │
│                      │ Lightweight Charts + WebSocket                  │
│ API Gateway          │ Spring Cloud Gateway 2023.0.x                   │
│ Discovery / Config   │ Nacos 2.3.x (gRPC)                              │
│ Business services    │ Spring Boot 3.2.x + MyBatis-Plus 3.5.x          │
│                      │ OpenFeign + Sentinel + Seata                    │
│ Messaging            │ RocketMQ 5.x (primary) + Kafka 3.x (fallback)   │
│ Data layer           │ MySQL 8.0 (master/slave) + Redis 7 (sentinel)   │
│ Observability        │ SkyWalking + Prometheus + Grafana               │
└───────────────────────────────────────────────────────────────────────┘
```
