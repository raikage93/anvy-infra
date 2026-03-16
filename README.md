# AnVy Clinic - Infrastructure

Repo chứa cấu hình infrastructure và DevOps cho dự án AnVy Clinic.

## 📁 Cấu trúc

```
ANVY/
├── docker-compose.yml          # Docker Compose config
├── .env.example                # Template biến môi trường
├── dockerfiles/
│   ├── Dockerfile.frontend     # Build image frontend
│   └── Dockerfile.backend      # Build image backend
├── config/
│   ├── nginx/nginx.conf        # Nginx reverse proxy
│   └── mysql/init/             # MySQL init scripts
├── scripts/                    # Deploy & utility scripts
├── logs/                       # Log files
└── repos/
    ├── anvy_frontend/          # [submodule] Frontend React
    └── anvy_backend/           # [submodule] Backend Node.js
```

## 🚀 Quick Start

```bash
# 1. Clone repo + submodules
git clone --recurse-submodules git@github.com:raikage93/anvy-infrastructure.git
cd anvy-infrastructure

# 2. Setup environment
cp .env.example .env

# 3. Start all services
docker compose up -d --build

# 4. Check status
docker compose ps
```

## 🔧 Useful Commands

```bash
# Rebuild a specific service
docker compose up -d --build backend

# View logs
docker compose logs -f backend

# Stop all
docker compose down

# Stop + remove volumes (⚠️ deletes data)
docker compose down -v
```
