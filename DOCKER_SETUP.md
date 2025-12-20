# Tricol Supplier Chain - Docker Setup Guide

## 📋 Prerequisites
- Docker Desktop installed
- Docker Hub account
- Git repository on GitHub

## 🚀 Quick Start

### 1. **Run with Docker Compose (Recommended)**

This will start both the application and MySQL database:

```bash
# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes (clean database)
docker-compose down -v
```

Access the application:
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **MySQL**: localhost:3307 (exposed port)

### 2. **Build and Run Docker Image Only**

If you have MySQL running locally:

```bash
# Build the image
docker build -t tricol-supplier-chain .

# Run with connection to host MySQL
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/supplier_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  tricol-supplier-chain
```

### 3. **Push to Docker Hub**

```bash
# Login to Docker Hub
docker login

# Tag your image
docker tag tricol-supplier-chain YOUR_DOCKERHUB_USERNAME/tricol-supplier-chain:latest

# Push to Docker Hub
docker push YOUR_DOCKERHUB_USERNAME/tricol-supplier-chain:latest
```

## ⚙️ GitHub Actions CI/CD Setup

### Configure GitHub Secrets

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Add these secrets:

| Secret Name | Value |
|-------------|-------|
| `DOCKER_USERNAME` | Your Docker Hub username |
| `DOCKER_PASSWORD` | Your Docker Hub access token |

**Creating Docker Hub Access Token:**
1. Go to https://hub.docker.com/settings/security
2. Click **New Access Token**
3. Name it (e.g., "GitHub Actions")
4. Copy the token
5. Use it as `DOCKER_PASSWORD` in GitHub Secrets

### Trigger CI/CD

The workflow runs automatically on push to:
- `auth-feat` branch (currently configured)
- Update `.github/workflows/ci.yml` to change branches

The CI/CD pipeline will:
1. ✅ Build the project with Maven
2. ✅ Run tests
3. ✅ Build Docker image
4. ✅ Push to Docker Hub with tags

## 🔧 Configuration

### Environment Variables

You can override these in docker-compose.yml or docker run:

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/supplier_db` | Database connection URL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `""` | Database password |
| `JWT_SECRET` | Auto-generated | JWT secret key |

### Docker Compose Services

- **mysql**: MySQL 8.0 database
  - Port: 3307 (host) → 3306 (container)
  - Volume: `mysql_data` for persistence
  
- **app**: Spring Boot application
  - Port: 8080
  - Depends on: mysql (with health check)

## 🧪 Testing

```bash
# Check if services are running
docker-compose ps

# View application logs
docker-compose logs app

# View database logs
docker-compose logs mysql

# Execute commands in running container
docker-compose exec app sh
docker-compose exec mysql mysql -uroot -prootpassword supplier_db
```

## 🛠️ Troubleshooting

### Application won't start
```bash
# Check logs
docker-compose logs app

# Restart services
docker-compose restart

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

### Database connection issues
```bash
# Check if MySQL is healthy
docker-compose ps

# Test database connection
docker-compose exec mysql mysqladmin ping -h localhost -u root -prootpassword

# Access MySQL shell
docker-compose exec mysql mysql -uroot -prootpassword
```

### Clean everything
```bash
# Remove all containers, networks, and volumes
docker-compose down -v

# Remove images
docker rmi tricol-supplier-chain
docker rmi tricol_supplier_chain_tests-app
```

## 📦 Files Created

- `Dockerfile` - Multi-stage Docker build
- `docker-compose.yml` - Complete stack (app + database)
- `.dockerignore` - Exclude unnecessary files
- `.github/workflows/ci.yml` - GitHub Actions CI/CD
- `DOCKER_SETUP.md` - This guide

## 🎯 Next Steps

1. ✅ Test locally with `docker-compose up`
2. ✅ Configure GitHub Secrets
3. ✅ Push to GitHub to trigger CI/CD
4. ✅ Verify image on Docker Hub
5. ✅ Deploy to production (Azure, AWS, etc.)
