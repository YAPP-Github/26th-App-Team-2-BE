#!/bin/bash

# ===============================
# 설정
# ===============================
NGINX_CONF_DIR="./nginx"
MAX_RETRIES=10
HEALTHCHECK_WAIT=3

# ===============================
# 함수: 헬스 체크
# ===============================
health_check() {
  local NAME=$1  # blue 또는 green
  local RETRIES=0

  while [[ $RETRIES -lt $MAX_RETRIES ]]; do
    echo "[HealthCheck] Checking $NAME (Attempt $((RETRIES+1))/$MAX_RETRIES)..."
    sleep $HEALTHCHECK_WAIT

    local RESPONSE=$(docker exec "$NAME" curl -s http://127.0.0.1:8080/health)
    if [[ -n "$RESPONSE" ]]; then
      echo "[HealthCheck] $NAME is healthy."
      return 0
    fi

    ((RETRIES++))
  done

  echo "[HealthCheck] $NAME failed health check after $MAX_RETRIES attempts."
  return 1
}

# ===============================
# 함수: 서비스 시작
# ===============================
start_service() {
  local NAME=$1
  echo "[Service] Pulling image for $NAME..."
  docker compose pull "$NAME"

  echo "[Service] Starting $NAME container..."
  docker compose up -d "$NAME"
}

# ===============================
# 함수: 서비스 중지
# ===============================
stop_service() {
  local NAME=$1
  echo "[Service] Stopping $NAME container..."
  docker compose stop "$NAME"
}

# ===============================
# 함수: Nginx 설정 적용
# ===============================
reload_nginx() {
  local CONF_NAME=$1
  echo "[Nginx] Switching to $CONF_NAME..."
  sudo cp "$NGINX_CONF_DIR/$CONF_NAME" "$NGINX_CONF_DIR/nginx.conf"
  sudo docker compose restart nginx
  echo "[Nginx] Reload complete."
}

# ===============================
# 배포 시작 (Blue <-> Green 스위칭)
# ===============================
IS_GREEN_RUNNING=$(docker compose ps | grep green)

if [[ -z "$IS_GREEN_RUNNING" ]]; then
  echo "[Deploy] Switching from BLUE to GREEN..."

  start_service green
  if ! health_check green; then
    echo "[Abort] GREEN failed health check. Deployment cancelled."
    exit 1
  fi

  reload_nginx "nginx-green.conf"
  stop_service blue
  echo "[Deploy] GREEN is now live."

else
  echo "[Deploy] Switching from GREEN to BLUE..."

  start_service blue
  if ! health_check blue; then
    echo "[Abort] BLUE failed health check. Deployment cancelled."
    exit 1
  fi

  reload_nginx "nginx-blue.conf"
  stop_service green
  echo "[Deploy] BLUE is now live."
fi
