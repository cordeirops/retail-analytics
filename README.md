# retail-analytics — Quarkus + OpenShift Sandbox

Aplicacao de analise de dados para o setor de varejo.
Demo tecnica para a entrevista de TAM Middleware - Red Hat.

## Endpoints

| Metodo | Path                        | Descricao                         |
|--------|-----------------------------|-----------------------------------|
| GET    | /api/v1/info                | Info da aplicacao                 |
| GET    | /api/v1/products            | Lista todos os produtos           |
| GET    | /api/v1/products?category=X | Filtra por categoria              |
| GET    | /api/v1/products/{id}       | Busca produto por ID              |
| GET    | /api/v1/analytics/summary   | Resumo de vendas do mes           |
| GET    | /q/health/live              | Liveness probe (OpenShift)        |
| GET    | /q/health/ready             | Readiness probe (OpenShift)       |
| GET    | /q/metrics                  | Metricas Prometheus               |

---

## Deploy no OpenShift Developer Sandbox

### Pre-requisitos

- Conta no Red Hat Developer Sandbox: https://developers.redhat.com/developer-sandbox
- oc CLI instalado (ou usar o terminal web do console OpenShift)
- Codigo no GitHub (repositorio publico)

---

### Passo 1 — Subir o codigo para o GitHub

```bash
git init
git add .
git commit -m "feat: retail-analytics quarkus app"
git remote add origin https://github.com/SEU_USUARIO/retail-analytics.git
git push -u origin main
```

---

### Passo 2 — Login no OpenShift Sandbox

No console web do Sandbox:
1. Clique no seu nome (canto superior direito)
2. Clique em "Copy login command"
3. Cole no terminal:

```bash
oc login --token=<SEU_TOKEN> --server=https://<SEU_CLUSTER>.openshiftapps.com
oc project <SEU_NAMESPACE>
```

---

### Passo 3 — Deploy via S2I (Source-to-Image)

```bash
oc new-app ubi8/openjdk-21~https://github.com/SEU_USUARIO/retail-analytics.git \
  --name=retail-analytics
```

O OpenShift vai:
- Criar um BuildConfig apontando para o Git
- Iniciar o build S2I com a imagem ubi8/openjdk-21
- Gerar a ImageStream com a imagem final
- Criar o Deployment e subir o pod automaticamente

Acompanhar o build:
```bash
oc logs -f bc/retail-analytics
```

---

### Passo 4 — Expor a aplicacao

```bash
oc expose svc/retail-analytics --port=8080
oc get route retail-analytics
```

Testar:
```bash
curl https://<ROUTE_URL>/api/v1/info
curl https://<ROUTE_URL>/api/v1/products
curl https://<ROUTE_URL>/q/health/ready
```

---

### Passo 5 — Configurar Autoscaling (HPA)

```bash
oc autoscale deployment/retail-analytics \
  --min=1 --max=3 --cpu-percent=70
```

Verificar:
```bash
oc get hpa retail-analytics
```

---

### Passo 6 — Verificar Health e Metricas

```bash
# Health checks
curl https://<ROUTE_URL>/q/health/live
curl https://<ROUTE_URL>/q/health/ready

# Metricas Prometheus
curl https://<ROUTE_URL>/q/metrics
```

No console web do OpenShift:
- Topology view: estado visual do deployment e pods
- Monitoring > Metrics: Prometheus built-in do Sandbox
- Pod logs: oc logs deployment/retail-analytics

---

## Desenvolvimento local

```bash
./mvnw quarkus:dev
```

Acesse: http://localhost:8080/api/v1/info

---

## Estrutura do projeto

```
retail-analytics/
├── .s2i/
│   └── environment          # Variaveis necessarias para o build S2I
├── src/main/java/dev/pedro/tam/
│   ├── model/
│   │   ├── Product.java
│   │   └── SalesSummary.java
│   ├── resource/
│   │   ├── RetailAnalyticsResource.java
│   │   └── DataHealthCheck.java
│   └── service/
│       └── RetailAnalyticsService.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/dev/pedro/tam/
│   └── RetailAnalyticsTest.java
└── pom.xml
```

---

## Referencias

- Quarkus OpenShift extension: https://quarkus.io/guides/deploying-to-openshift
- S2I deploy guide: https://quarkus.io/guides/deploying-to-openshift-s2i-howto
- SmallRye Health: https://quarkus.io/guides/smallrye-health
- OpenShift Developer Sandbox: https://developers.redhat.com/developer-sandbox
