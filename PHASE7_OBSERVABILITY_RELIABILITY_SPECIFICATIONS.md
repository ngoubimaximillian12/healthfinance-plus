# Phase 7 Implementation Specifications: Observability & Reliability

## Overview

**Timeline:** Weeks 36-42
**Focus:** Production-Ready Infrastructure, Monitoring, Reliability Engineering
**Deliverables:** Full observability stack, SLOs, Canary deployments, Chaos testing, DR plan

---

## 📋 Objectives

### Primary Goals:
1. Deploy complete observability stack (Prometheus, Grafana, ELK, Jaeger)
2. Implement distributed tracing across all 27 services
3. Define and track SLOs (Service Level Objectives)
4. Implement canary deployments with automated rollback
5. Establish chaos engineering practice
6. Configure backup & disaster recovery
7. Conduct production readiness review

### Success Criteria:
- ✅ All services instrumented with metrics, logs, traces
- ✅ Mean time to detect (MTTD) <5 minutes for critical issues
- ✅ Mean time to resolve (MTTR) <1 hour for P1 incidents
- ✅ SLO compliance >99% for critical services
- ✅ Canary deployments automated for all services
- ✅ Monthly chaos testing with zero unplanned downtime
- ✅ RTO (Recovery Time Objective) <4 hours, RPO (Recovery Point Objective) <15 minutes

---

## 📊 Observability Stack

### Architecture:

```
Application Services (27 services)
       ↓           ↓           ↓
   Metrics      Logs       Traces
       ↓           ↓           ↓
  Prometheus  Filebeat    Jaeger
       ↓           ↓           ↓
       └────> Grafana <────┘
                  ↓
           Alertmanager
                  ↓
       PagerDuty / Slack
```

---

## 🔢 Metrics (Prometheus + Grafana)

### Prometheus Deployment:

```yaml
prometheus:
  image: prom/prometheus:latest
  container_name: healthfinance-prometheus
  ports:
    - "9090:9090"
  volumes:
    - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml
    - ./monitoring/rules:/etc/prometheus/rules
    - prometheus-data:/prometheus
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
    - '--storage.tsdb.path=/prometheus'
    - '--web.console.libraries=/etc/prometheus/console_libraries'
    - '--web.console.templates=/etc/prometheus/consoles'
    - '--storage.tsdb.retention.time=90d'
    - '--web.enable-lifecycle'
  networks:
    - healthfinance-network
```

**Prometheus Configuration:**

```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "/etc/prometheus/rules/*.yml"

scrape_configs:
  # Scrape all Spring Boot services
  - job_name: 'spring-boot-services'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets:
        - 'eureka-server:8761'
        - 'user-service:8081'
        - 'appointment-service:8082'
        - 'medical-records-service:8083'
        - 'prescription-service:8084'
        - 'insurance-service:8085'
        - 'billing-service:8086'
        - 'notification-service:8087'
        - 'laboratory-service:8088'
        - 'pharmacy-service:8089'
        - 'intelligence-hub-service:8090'
        - 'consent-service:8091'
        - 'identity-service:8092'
        - 'audit-service:8093'
        - 'risk-scoring-service:8094'
        - 'care-pathway-service:8095'
        - 'triage-service:8096'
        - 'fraud-detection-service:8097'
        - 'pricing-service:8098'
        - 'provider-service:8099'
        - 'inventory-forecast-service:8100'
        - 'claims-orchestrator-service:8101'
        - 'patient-engagement-service:8102'
        - 'document-ai-service:8103'
        - 'observability-service:8104'
        - 'feature-store-service:8105'

  # PostgreSQL databases
  - job_name: 'postgres'
    static_configs:
      - targets: ['postgres-exporter:9187']

  # Kafka
  - job_name: 'kafka'
    static_configs:
      - targets: ['kafka:9101']

  # Redis
  - job_name: 'redis'
    static_configs:
      - targets: ['redis-exporter:9121']

  # Node/container metrics
  - job_name: 'node-exporter'
    static_configs:
      - targets: ['node-exporter:9100']
```

### Spring Boot Actuator Configuration:

**Add to all service application.properties:**

```properties
# Enable Prometheus endpoint
management.endpoints.web.exposure.include=health,info,prometheus,metrics
management.metrics.export.prometheus.enabled=true
management.metrics.tags.application=${spring.application.name}
management.metrics.tags.environment=production

# Custom metrics
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.sla.http.server.requests=50ms,100ms,200ms,500ms,1s,2s
```

### Grafana Deployment:

```yaml
grafana:
  image: grafana/grafana:latest
  container_name: healthfinance-grafana
  ports:
    - "3000:3000"
  environment:
    - GF_SECURITY_ADMIN_PASSWORD=admin123
    - GF_SERVER_ROOT_URL=http://localhost:3000
    - GF_INSTALL_PLUGINS=grafana-piechart-panel
  volumes:
    - grafana-data:/var/lib/grafana
    - ./monitoring/dashboards:/etc/grafana/provisioning/dashboards
    - ./monitoring/datasources.yml:/etc/grafana/provisioning/datasources/datasources.yml
  networks:
    - healthfinance-network
```

### Grafana Dashboards:

#### 1. Service Health Dashboard

**Metrics:**
- Request rate (requests/second)
- Error rate (%)
- Latency (p50, p95, p99)
- Uptime (%)
- Active connections

**Panels:**
```
┌─────────────────────┬─────────────────────┐
│  Request Rate       │  Error Rate         │
│  (Line Chart)       │  (Line Chart)       │
├─────────────────────┼─────────────────────┤
│  Latency Percentiles│  Uptime             │
│  (Line Chart)       │  (Gauge)            │
├─────────────────────┴─────────────────────┤
│  Top 10 Slowest Endpoints                 │
│  (Table)                                  │
└───────────────────────────────────────────┘
```

**Prometheus Queries:**

```promql
# Request rate
rate(http_server_requests_seconds_count{application="appointment-service"}[5m])

# Error rate
rate(http_server_requests_seconds_count{application="appointment-service",status=~"5.."}[5m])
/
rate(http_server_requests_seconds_count{application="appointment-service"}[5m])

# Latency p99
histogram_quantile(0.99,
  rate(http_server_requests_seconds_bucket{application="appointment-service"}[5m])
)

# Uptime
up{job="spring-boot-services",instance="appointment-service:8082"}
```

#### 2. JVM Metrics Dashboard

**Metrics:**
- Heap memory usage
- Non-heap memory usage
- GC pause time
- Thread count
- CPU usage

#### 3. Database Performance Dashboard

**Metrics:**
- Connection pool usage
- Query latency
- Transaction rate
- Slow queries (>1s)
- Deadlocks

#### 4. Kafka Metrics Dashboard

**Metrics:**
- Messages produced/consumed per second
- Consumer lag
- Topic size
- Broker CPU/memory

#### 5. ML Model Performance Dashboard

**Metrics:**
- Predictions per second
- Inference latency (p50, p95, p99)
- Model confidence distribution
- Prediction drift
- Human override rate

---

## 📝 Logging (ELK Stack)

### Elasticsearch Deployment:

```yaml
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
  container_name: healthfinance-elasticsearch
  environment:
    - discovery.type=single-node
    - xpack.security.enabled=false
    - "ES_JAVA_OPTS=-Xms2g -Xmx2g"
  ports:
    - "9200:9200"
  volumes:
    - elasticsearch-data:/usr/share/elasticsearch/data
  networks:
    - healthfinance-network
```

### Logstash Deployment:

```yaml
logstash:
  image: docker.elastic.co/logstash/logstash:8.11.0
  container_name: healthfinance-logstash
  ports:
    - "5000:5000"
  volumes:
    - ./monitoring/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
  networks:
    - healthfinance-network
```

**Logstash Configuration:**

```conf
# logstash.conf
input {
  beats {
    port => 5044
  }
}

filter {
  if [application] == "appointment-service" {
    grok {
      match => { "message" => "%{TIMESTAMP_ISO8601:timestamp} %{LOGLEVEL:level} %{NUMBER:pid} --- \[%{DATA:thread}\] %{DATA:class} : %{GREEDYDATA:log_message}" }
    }
    date {
      match => [ "timestamp", "ISO8601" ]
    }
  }

  # Extract correlation ID for request tracing
  grok {
    match => { "message" => "correlationId=%{UUID:correlation_id}" }
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "healthfinance-logs-%{+YYYY.MM.dd}"
  }
}
```

### Filebeat Deployment (on each service host):

```yaml
filebeat:
  image: docker.elastic.co/beats/filebeat:8.11.0
  user: root
  volumes:
    - ./monitoring/filebeat.yml:/usr/share/filebeat/filebeat.yml:ro
    - /var/lib/docker/containers:/var/lib/docker/containers:ro
    - /var/run/docker.sock:/var/run/docker.sock:ro
  networks:
    - healthfinance-network
```

**Filebeat Configuration:**

```yaml
# filebeat.yml
filebeat.inputs:
  - type: container
    paths:
      - '/var/lib/docker/containers/*/*.log'
    processors:
      - add_docker_metadata:
          host: "unix:///var/run/docker.sock"

output.logstash:
  hosts: ["logstash:5044"]
```

### Kibana Deployment:

```yaml
kibana:
  image: docker.elastic.co/kibana/kibana:8.11.0
  container_name: healthfinance-kibana
  ports:
    - "5601:5601"
  environment:
    - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
  networks:
    - healthfinance-network
```

### Structured Logging in Spring Boot:

**Add Logback configuration:**

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <customFields>{"application":"${spring.application.name}"}</customFields>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

**Add dependency:**

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

### Log Correlation with MDC:

```java
import org.slf4j.MDC;

@Component
public class CorrelationIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String correlationId = httpRequest.getHeader("X-Correlation-ID");
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put("correlationId", correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
        }
    }
}
```

---

## 🔍 Distributed Tracing (Jaeger)

### Jaeger Deployment:

```yaml
jaeger:
  image: jaegertracing/all-in-one:latest
  container_name: healthfinance-jaeger
  environment:
    - COLLECTOR_ZIPKIN_HOST_PORT=:9411
  ports:
    - "5775:5775/udp"
    - "6831:6831/udp"
    - "6832:6832/udp"
    - "5778:5778"
    - "16686:16686"  # Jaeger UI
    - "14268:14268"
    - "14250:14250"
    - "9411:9411"
  networks:
    - healthfinance-network
```

### OpenTelemetry Configuration (Spring Boot):

**Add dependencies:**

```xml
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-api</artifactId>
    <version>1.32.0</version>
</dependency>
<dependency>
    <groupId>io.opentelemetry.instrumentation</groupId>
    <artifactId>opentelemetry-spring-boot-starter</artifactId>
    <version>1.32.0-alpha</version>
</dependency>
```

**application.properties:**

```properties
# OpenTelemetry
otel.service.name=${spring.application.name}
otel.traces.exporter=jaeger
otel.exporter.jaeger.endpoint=http://jaeger:14250
otel.metrics.exporter=prometheus
otel.instrumentation.common.default-enabled=true
```

### Trace Visualization:

**Example Trace Flow:**

```
Patient makes appointment request (web app)
  ├─ api-gateway (1ms)
  │   └─ JWT validation (0.5ms)
  ├─ appointment-service (45ms)
  │   ├─ Check patient exists → user-service (12ms)
  │   ├─ Check provider availability → provider-service (18ms)
  │   ├─ Create appointment (DB write) (8ms)
  │   ├─ Publish AppointmentScheduledEvent → Kafka (5ms)
  │   └─ Send notification → notification-service (2ms, async)
  └─ risk-scoring-service (consumer, 23ms)
      ├─ Calculate risk score → intelligence-hub (18ms)
      │   └─ ML model inference → tensorflow-serving (15ms)
      └─ Update risk score (DB write) (3ms)

Total: ~70ms (user-facing), ~93ms (background)
```

**Jaeger UI allows:**
- Drill down into each span
- Identify bottlenecks
- See error spans (red)
- Analyze service dependencies

---

## 🎯 Service Level Objectives (SLOs)

### SLO Framework:

**SLI (Service Level Indicator):** Quantitative measure of service performance
**SLO (Service Level Objective):** Target value for an SLI
**Error Budget:** Allowable amount of unreliability (1 - SLO)

### Defined SLOs:

| Service | SLI | SLO | Error Budget (30 days) | Burn Rate Alert |
|---------|-----|-----|------------------------|-----------------|
| API Gateway | Availability | 99.9% | 43.2 min downtime | >2x burn rate |
| API Gateway | Latency p99 <500ms | 99.5% | 21.6 min | >2x burn rate |
| User Service | Availability | 99.9% | 43.2 min | >2x burn rate |
| Appointment Service | Availability | 99.5% | 3.6 hours | >2x burn rate |
| Appointment Service | Latency p95 <200ms | 99% | 7.2 hours | >2x burn rate |
| Medical Records | Query latency p95 <300ms | 99% | 7.2 hours | >2x burn rate |
| Intelligence Hub | Decision latency p99 <2s | 99.5% | 21.6 min | >2x burn rate |
| Risk Scoring | Latency <150ms | 99% | 7.2 hours | >2x burn rate |
| Notification | Delivery within 5 min | 99% | 7.2 hours | >1.5x burn rate |
| Database | Query latency p95 <100ms | 99.5% | 3.6 hours | >2x burn rate |
| Kafka | Message delivery | 99.99% | 4.32 min | >5x burn rate |

### SLO Tracking:

**Prometheus Recording Rules:**

```yaml
# prometheus-slo-rules.yml
groups:
  - name: slo-rules
    interval: 30s
    rules:
      # API Gateway Availability SLI
      - record: sli:availability:api_gateway
        expr: |
          sum(rate(http_server_requests_seconds_count{application="api-gateway",status!~"5.."}[5m]))
          /
          sum(rate(http_server_requests_seconds_count{application="api-gateway"}[5m]))

      # API Gateway Latency SLI (p99 <500ms)
      - record: sli:latency:api_gateway:p99
        expr: |
          histogram_quantile(0.99,
            rate(http_server_requests_seconds_bucket{application="api-gateway"}[5m])
          ) < 0.5

      # Error Budget Calculation
      - record: error_budget:api_gateway:availability:30d
        expr: |
          1 - (
            1 - 0.999  # SLO
          ) - (
            1 - avg_over_time(sli:availability:api_gateway[30d])
          )

      # Error Budget Burn Rate
      - record: error_budget_burn_rate:api_gateway:1h
        expr: |
          (1 - sli:availability:api_gateway)
          /
          (1 - 0.999)  # SLO
```

**Grafana SLO Dashboard:**

```
┌──────────────────────────────────────────────┐
│  Service: API Gateway                        │
├──────────────────────────────────────────────┤
│  SLO: 99.9% Availability                     │
│  Current SLI: 99.95% ✅                      │
│  Error Budget Remaining: 80% ✅              │
│                                              │
│  [████████████████░░░░] 80% remaining        │
│                                              │
│  Burn Rate (1h): 0.5x (Good)                 │
│  Burn Rate (6h): 0.8x (Good)                 │
│                                              │
│  Projected SLO Compliance: ✅ PASS           │
├──────────────────────────────────────────────┤
│  SLO: p99 Latency <500ms                     │
│  Current SLI: 98.2% ⚠️                       │
│  Error Budget Remaining: 20% ⚠️              │
│                                              │
│  [████░░░░░░░░░░░░░░░░] 20% remaining        │
│                                              │
│  Burn Rate (1h): 3.2x ⚠️ ALERT!             │
│  Burn Rate (6h): 2.1x ⚠️ ALERT!             │
│                                              │
│  Projected SLO Compliance: ⚠️ AT RISK        │
└──────────────────────────────────────────────┘
```

### SLO Alerting Rules:

```yaml
# prometheus-alerts.yml
groups:
  - name: slo-alerts
    rules:
      - alert: HighErrorBudgetBurnRate
        expr: error_budget_burn_rate:api_gateway:1h > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High error budget burn rate for API Gateway"
          description: "Burning error budget at {{ $value }}x the acceptable rate"

      - alert: ErrorBudgetExhausted
        expr: error_budget:api_gateway:availability:30d < 0
        labels:
          severity: critical
        annotations:
          summary: "Error budget exhausted for API Gateway"
          description: "SLO will be violated if no action taken"
```

---

## 🚨 Alerting (Alertmanager + PagerDuty)

### Alertmanager Deployment:

```yaml
alertmanager:
  image: prom/alertmanager:latest
  container_name: healthfinance-alertmanager
  ports:
    - "9093:9093"
  volumes:
    - ./monitoring/alertmanager.yml:/etc/alertmanager/alertmanager.yml
  networks:
    - healthfinance-network
```

**Alertmanager Configuration:**

```yaml
# alertmanager.yml
global:
  resolve_timeout: 5m
  pagerduty_url: 'https://events.pagerduty.com/v2/enqueue'

route:
  group_by: ['alertname', 'cluster', 'service']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 12h
  receiver: 'team-platform'
  routes:
    - match:
        severity: critical
      receiver: 'pagerduty-critical'
      continue: true
    - match:
        severity: warning
      receiver: 'slack-warnings'

receivers:
  - name: 'team-platform'
    email_configs:
      - to: 'platform-team@healthfinance.com'

  - name: 'pagerduty-critical'
    pagerduty_configs:
      - service_key: '<PAGERDUTY_SERVICE_KEY>'
        description: '{{ .GroupLabels.alertname }}: {{ .CommonAnnotations.summary }}'

  - name: 'slack-warnings'
    slack_configs:
      - api_url: '<SLACK_WEBHOOK_URL>'
        channel: '#alerts'
        title: '{{ .GroupLabels.alertname }}'
        text: '{{ .CommonAnnotations.description }}'
```

### Alert Routing Matrix:

| Severity | Destination | Response Time |
|----------|-------------|---------------|
| CRITICAL | PagerDuty + Slack + Email | Immediate (on-call engineer) |
| WARNING | Slack + Email | Within 1 hour |
| INFO | Slack only | Next business day |

### Alert Examples:

```yaml
groups:
  - name: critical-alerts
    rules:
      - alert: ServiceDown
        expr: up{job="spring-boot-services"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Service {{ $labels.instance }} is down"
          description: "{{ $labels.instance }} has been down for more than 1 minute"

      - alert: HighErrorRate
        expr: |
          rate(http_server_requests_seconds_count{status=~"5.."}[5m])
          /
          rate(http_server_requests_seconds_count[5m])
          > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate in {{ $labels.application }}"
          description: "Error rate is {{ $value | humanizePercentage }}"

      - alert: DatabaseConnectionPoolExhausted
        expr: |
          hikaricp_connections_active / hikaricp_connections_max > 0.9
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "Database connection pool near exhaustion"

  - name: warning-alerts
    rules:
      - alert: HighMemoryUsage
        expr: |
          (jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"}) > 0.85
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "High heap memory usage in {{ $labels.application }}"

      - alert: KafkaConsumerLag
        expr: kafka_consumer_lag > 10000
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Kafka consumer lag is high for {{ $labels.topic }}"
```

---

## 🔄 Canary Deployments (Argo Rollouts)

### Architecture:

```
Production Traffic (100%)
       ↓
   Kubernetes Service
       ↓
   ┌─────────────┐
   │  Argo Rollout│
   └─────────────┘
       ↓      ↓
    90%      10%
       ↓      ↓
   Stable  Canary
   (v1.0)  (v1.1)
```

### Argo Rollouts Installation (Kubernetes):

```bash
kubectl create namespace argo-rollouts
kubectl apply -n argo-rollouts -f https://github.com/argoproj/argo-rollouts/releases/latest/download/install.yaml
```

### Rollout Configuration Example:

```yaml
# appointment-service-rollout.yaml
apiVersion: argoproj.io/v1alpha1
kind: Rollout
metadata:
  name: appointment-service
spec:
  replicas: 5
  strategy:
    canary:
      steps:
        - setWeight: 10     # Route 10% traffic to canary
        - pause: {duration: 5m}
        - setWeight: 25     # Increase to 25%
        - pause: {duration: 5m}
        - setWeight: 50     # Increase to 50%
        - pause: {duration: 5m}
        - setWeight: 75     # Increase to 75%
        - pause: {duration: 5m}
        # If no issues, promote canary to stable (100%)
      analysis:
        templates:
          - templateName: error-rate-analysis
          - templateName: latency-analysis
        args:
          - name: service-name
            value: appointment-service
  revisionHistoryLimit: 5
  selector:
    matchLabels:
      app: appointment-service
  template:
    metadata:
      labels:
        app: appointment-service
    spec:
      containers:
        - name: appointment-service
          image: healthfinance/appointment-service:v1.1
          ports:
            - containerPort: 8082
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: production
          resources:
            requests:
              memory: "1Gi"
              cpu: "500m"
            limits:
              memory: "2Gi"
              cpu: "1000m"
```

### Analysis Templates:

```yaml
# error-rate-analysis.yaml
apiVersion: argoproj.io/v1alpha1
kind: AnalysisTemplate
metadata:
  name: error-rate-analysis
spec:
  args:
    - name: service-name
  metrics:
    - name: error-rate
      interval: 1m
      successCondition: result < 0.01  # Error rate <1%
      failureLimit: 3                   # Fail after 3 consecutive failures
      provider:
        prometheus:
          address: http://prometheus:9090
          query: |
            rate(http_server_requests_seconds_count{application="{{args.service-name}}",status=~"5.."}[1m])
            /
            rate(http_server_requests_seconds_count{application="{{args.service-name}}"}[1m])

# latency-analysis.yaml
apiVersion: argoproj.io/v1alpha1
kind: AnalysisTemplate
metadata:
  name: latency-analysis
spec:
  args:
    - name: service-name
  metrics:
    - name: latency-p95
      interval: 1m
      successCondition: result < 0.3  # p95 latency <300ms
      failureLimit: 3
      provider:
        prometheus:
          address: http://prometheus:9090
          query: |
            histogram_quantile(0.95,
              rate(http_server_requests_seconds_bucket{application="{{args.service-name}}"}[1m])
            )
```

**Automated Rollback:**
- If error rate >1% or latency >300ms for 3 consecutive minutes → Automatic rollback to stable version
- Engineers notified via PagerDuty

---

## 🌪️ Chaos Engineering

### Chaos Toolkit Installation:

```bash
pip install chaostoolkit chaostoolkit-kubernetes chaostoolkit-prometheus
```

### Experiment 1: Service Pod Termination

**Purpose:** Verify that service can recover from pod crashes

```yaml
# chaos-experiments/pod-termination.yaml
version: 1.0.0
title: Appointment Service survives pod termination
description: Verify service remains available when a pod is killed

steady-state-hypothesis:
  title: Service is healthy and responding
  probes:
    - name: service-responds
      type: probe
      tolerance: 200
      provider:
        type: http
        url: http://appointment-service:8082/actuator/health
        timeout: 5
        headers:
          Content-Type: application/json

method:
  - type: action
    name: terminate-pod
    provider:
      type: python
      module: chaosk8s.pod.actions
      func: terminate_pods
      arguments:
        label_selector: app=appointment-service
        ns: healthfinance
        qty: 1
    pauses:
      after: 10

rollbacks:
  - type: action
    name: scale-back-up
    provider:
      type: python
      module: chaosk8s.deployment.actions
      func: scale_deployment
      arguments:
        name: appointment-service
        replicas: 5
        ns: healthfinance
```

**Run Experiment:**

```bash
chaos run chaos-experiments/pod-termination.yaml
```

**Expected Outcome:**
- Service continues to respond (other pods handle traffic)
- Kubernetes automatically recreates terminated pod
- No user-facing downtime

### Experiment 2: Database Connection Failure

```yaml
# chaos-experiments/database-failure.yaml
version: 1.0.0
title: Services handle database unavailability gracefully

steady-state-hypothesis:
  title: Service health checks pass
  probes:
    - name: service-healthy
      type: probe
      tolerance: 200
      provider:
        type: http
        url: http://appointment-service:8082/actuator/health

method:
  - type: action
    name: block-database-connections
    provider:
      type: process
      path: docker
      arguments: ["exec", "appointment-service", "iptables", "-A", "OUTPUT", "-p", "tcp", "--dport", "5432", "-j", "DROP"]
    pauses:
      after: 30

rollbacks:
  - type: action
    name: restore-database-connections
    provider:
      type: process
      path: docker
      arguments: ["exec", "appointment-service", "iptables", "-D", "OUTPUT", "-p", "tcp", "--dport", "5432", "-j", "DROP"]
```

### Experiment 3: Network Latency Injection

```yaml
# chaos-experiments/network-latency.yaml
version: 1.0.0
title: Service remains functional under network latency

method:
  - type: action
    name: add-network-delay
    provider:
      type: process
      path: tc
      arguments:
        - qdisc
        - add
        - dev
        - eth0
        - root
        - netem
        - delay
        - 200ms
    pauses:
      after: 60

rollbacks:
  - type: action
    name: remove-network-delay
    provider:
      type: process
      path: tc
      arguments: ["qdisc", "del", "dev", "eth0", "root"]
```

### Chaos Testing Schedule:

**Monthly Chaos Day:**
- 1st Monday of month (during business hours, with team present)
- Run 3-5 experiments
- Document learnings
- Update runbooks

**Automated Chaos (Production):**
- Random pod terminations (1 pod/service/week, off-hours)
- Network latency injection (once/month, 5 minutes)

---

## 💾 Backup & Disaster Recovery

### Backup Strategy:

#### 1. Database Backups (PostgreSQL)

**Automated Daily Backups:**

```bash
#!/bin/bash
# backup-postgres.sh

DATE=$(date +%Y-%m-%d-%H-%M)
BACKUP_DIR="/backups/postgres"

# Backup each database
for DB in healthfinance_users healthfinance_appointments healthfinance_medical_records healthfinance_prescriptions healthfinance_insurance healthfinance_billing healthfinance_notifications healthfinance_laboratory healthfinance_pharmacy healthfinance_intelligence_hub healthfinance_consent healthfinance_identity healthfinance_audit healthfinance_risk_scoring healthfinance_care_pathways healthfinance_triage healthfinance_fraud healthfinance_pricing healthfinance_providers healthfinance_engagement healthfinance_claims healthfinance_documents
do
  docker exec healthfinance-postgres pg_dump -U healthfinance $DB | gzip > $BACKUP_DIR/$DB-$DATE.sql.gz
done

# Upload to S3
aws s3 sync $BACKUP_DIR/ s3://healthfinance-backups/postgres/ --storage-class STANDARD_IA

# Retain local backups for 7 days
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

# Log backup completion
echo "$DATE: PostgreSQL backups completed" >> /var/log/backups.log
```

**Cron Schedule:**

```
0 2 * * * /scripts/backup-postgres.sh
```

**Retention Policy:**
- Daily backups: Retained for 30 days
- Weekly backups (Sunday): Retained for 90 days
- Monthly backups (1st Sunday): Retained for 1 year

#### 2. MinIO / S3 Backups (Data Lake)

**Versioning Enabled:**
- All S3 buckets have versioning enabled
- Accidental deletes can be recovered

**Cross-Region Replication:**
- Primary: us-east-1
- Replica: us-west-2
- Asynchronous replication (near real-time)

#### 3. Configuration Backups

**Git Repository:**
- All configuration files (docker-compose.yml, prometheus.yml, etc.) in Git
- Tagged with version numbers
- Automated daily commits

### Disaster Recovery Plan:

**Recovery Time Objective (RTO):** 4 hours
**Recovery Point Objective (RPO):** 15 minutes

#### DR Scenarios:

**1. Single Service Failure**
- **Detection:** <1 minute (Prometheus alerts)
- **Recovery:** Automatic (Kubernetes restarts pod)
- **RTO:** <5 minutes

**2. Database Corruption**
- **Detection:** Data quality checks, application errors
- **Recovery:**
  1. Restore from latest backup (pg_restore)
  2. Replay WAL logs if available (point-in-time recovery)
  3. Verify data integrity
- **RTO:** 1-2 hours
- **RPO:** 15 minutes (with WAL archiving)

**3. Complete Data Center Failure**
- **Detection:** All services down
- **Recovery:**
  1. Failover to DR data center (us-west-2)
  2. Update DNS to point to DR
  3. Verify services operational
  4. Restore from cross-region replicated data
- **RTO:** 4 hours
- **RPO:** 1 hour (replication lag)

**4. Ransomware Attack**
- **Detection:** Unusual file modifications, encrypted files
- **Recovery:**
  1. Isolate infected systems
  2. Restore from offline backups (immutable S3 Glacier backups)
  3. Scan for vulnerabilities
  4. Rebuild systems from known-good images
- **RTO:** 8-24 hours
- **RPO:** 24 hours (offline backups)

### DR Testing:

**Quarterly DR Drill:**
- Simulate complete failure
- Practice recovery procedures
- Time each step
- Document improvements

---

## 🎯 Phase 7 Success Metrics

### Observability:
- ✅ All 27 services instrumented (metrics, logs, traces)
- ✅ MTTD (Mean Time to Detect) <5 minutes
- ✅ MTTR (Mean Time to Resolve) <1 hour for P1 incidents
- ✅ 100% of incidents have complete trace data

### Reliability:
- ✅ SLO compliance >99% for all critical services
- ✅ Zero unplanned downtime from deployments
- ✅ Canary rollbacks automated (no manual intervention)
- ✅ Monthly chaos testing with no production impact

### Recovery:
- ✅ RTO <4 hours, RPO <15 minutes achieved
- ✅ Quarterly DR drill completed successfully
- ✅ Database backups verified (test restore monthly)

---

## 📁 Deliverables

### Infrastructure:
- Prometheus + Grafana + Alertmanager
- ELK Stack (Elasticsearch, Logstash, Kibana, Filebeat)
- Jaeger (distributed tracing)
- Argo Rollouts (canary deployments)
- Backup scripts and schedules
- DR runbooks

### Dashboards (Grafana):
- Service Health Dashboard (all 27 services)
- JVM Metrics Dashboard
- Database Performance Dashboard
- Kafka Metrics Dashboard
- ML Model Performance Dashboard
- SLO Tracking Dashboard
- Data Quality Dashboard

### Documentation:
- Runbooks for common incidents
- SLO definitions and error budgets
- Alerting matrix and escalation procedures
- DR plan and recovery procedures
- Chaos experiment catalog
- Backup and restore procedures

---

## 🚀 Next Phase Preview

**Phase 8 (Weeks 42-48): Revenue & Go-to-Market**

Will implement:
- Subscription management (Stripe integration)
- API marketplace portal
- Billing and invoicing system
- Customer support (Zendesk integration)
- Marketing website
- Demo environment
- Sales CRM integration
- Revenue analytics dashboard
- Public API documentation

---

**Document Version:** 1.0
**Last Updated:** 2026-01-08
**Status:** ✅ Ready for Implementation
**Dependencies:** Phase 1, 2, 3, 4, 5, 6 Complete
**Estimated Duration:** 6 weeks (Weeks 36-42)
