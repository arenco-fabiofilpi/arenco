# Projeto Arenco - Sistema Integrado
 
Monorepo com todos os serviços e aplicativos da construtora Arenco:

- **Backend (Spring Boot)**: múltiplos microsserviços organizados por domínio.
- **Frontend (Vue + Quasar)**: SPA para uso web.
- **Mobile (Flutter)**: app mobile em desenvolvimento.
- **Deploy manual** via `nohup`, `nginx` e scripts.

## Estrutura

```
/
├── backend/        → Serviços Spring Boot
├── frontend/       → Projeto Vue 3 + Quasar
├── mobile/         → App Flutter
├── deploy/         → Configs de ambiente e scripts de produção
├── scripts/        → Scripts utilitários
├── docker/         → Arquivos para contêineres (opcional)
├── version.txt     → Versão atual do sistema integrado
```

## Comandos úteis

### 🔨 Build completo

```bash
./scripts/build-all.sh
```

### 🚀 Deploy (exemplo produção)

```bash
./deploy/prod/start-backend.sh
./deploy/prod/start-frontend.sh
```

### 🧪 Executar apenas um módulo

```bash
cd backend/arenco-contratos
./mvnw spring-boot:run
```

---

## Versão Atual

📦 `v$(cat version.txt)`

---
