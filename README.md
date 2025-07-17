
# 📦 Project Dependencies

This document outlines the **system requirements**, **dependencies**, and **Docker-specific setups** required to run this project smoothly across different environments.

---

## 🖥️ System Requirements

| Component            | Requirement                                     |
|---------------------|--------------------------------------------------|
| Operating System     | Windows 10/11 (with WSL2), or Linux |
| CPU                  | x86_64 or ARM64 (for compatible containers)     |
| GPU                  | NVIDIA GPU with CUDA support (for model inference acceleration) |
| RAM                  | ≥ 8 GB                                          |
| Disk Space           | ≥ 25 GB free for Docker images/volumes         |

---

## 🐳 Required Software

### 1. **Docker**
- [Docker Desktop](https://www.docker.com/products/docker-desktop) (Windows) with:
  - ✅ WSL2 backend enabled on Windows
  - ✅ GPU integration enabled (if using NVIDIA GPU)

- Or:
  ```bash
  sudo apt install docker docker-compose
  ```

### 2. **NVIDIA Container Toolkit** (for GPU support)

- **Linux**:
  [Install NVIDIA Container Toolkit](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/install-guide.html)

- **Windows (WSL2)**:
  - Install [CUDA WSL Driver](https://developer.nvidia.com/cuda/wsl)
  - Enable GPU support in Docker Desktop → Settings → Resources → **Enable GPU**

---

## 🧪 Docker Compose Setup

This project uses `docker-compose` to orchestrate two services:

| Service | Description                         | Ports  | Depends On |
|---------|-------------------------------------|--------|------------|
| `web`   | Frontend web app (Node + Nginx)     | 3000 → 80 | `server`   |
| `server`| Backend ML API (Python + TensorFlow) | 5000    | –          |

---

## 📂 Required Directory Structure

Ensure the following **host directories exist** if running with volume bindings:

```
server/
├── data/      # Input datasets (mounted to /app/data)
├── out/       # Output .task or model files (mounted to /app/out)
```

If not present, Docker will mount empty directories.

---

## 📥 Required Model Weights

To train or fine-tune the model, you must manually download the base model checkpoint.

1. Visit the Hugging Face repository:
   [https://huggingface.co/google/gemma-3-1b-it/tree/main](https://huggingface.co/google/gemma-3-1b-it/tree/main)

2. Download the file:
   - `model.safetensors`

3. Place the downloaded file into the following directory inside your project:
   ```
   server/
   └── trainer/
       └── checkpoints/
           └── base/
               └── model.safetensors
   ```

> ⚠️ Ensure the `base` folder exists, and contains all other files in the repository.

---

## 🧠 Optional `.env` File (Recommended)

```env
WEB_PORT=3000
SERVER_PORT=5000
```

Reference it in `docker-compose.yml` for custom port configs.

---

## 🧱 Python Dependencies (Server)

These are already installed via the Dockerfile, but for local development:

- Python 3.12
- `tensorflow` (nightly dev version: `tf-nightly==2.20.0.dev20250515`)
- `flask`, `numpy`, etc. as listed in `requirements.txt`

> ⚠️ Note: This version of TensorFlow requires **CUDA 12.6** and may not be supported on all hardware. Prefer to run inside the container.

---

## 🐳 Running the App

### GPU-enabled System

```bash
docker compose --profile gpu up --build
```

### CPU-only System (fallback)

1. Comment out the `runtime: nvidia` and `deploy:` blocks in `docker-compose.yml`
2. Or use:
   ```bash
   DOCKER_BUILDKIT=0 docker compose up --build
   ```

---

## 📤 Optional: Export Images for Offline Use

To transfer between machines:

```bash
docker save -o web.tar your-web-image
docker save -o server.tar your-server-image

# Load on target machine:
docker load -i web.tar
docker load -i server.tar
```

---

## 🔒 Security & Credentials

This setup assumes:
- No authentication is enforced by default.
- API keys, dataset access, or other secrets should be passed via environment variables or `.env` file.

> **Never commit sensitive credentials to Git.**
