#!/bin/bash
SERVER_HASH=$(docker images | grep server | awk '{print $3}')
DB_HASH=$(docker images | grep database | awk '{print $3}')
LS_HASH=$(docker images | grep lobby | awk '{print $3}')
docker tag "$SERVER_HASH" ghcr.io/comp361/f2022-hexanome-16/splendor-server:latest
docker tag "$DB_HASH" ghcr.io/comp361/f2022-hexanome-16/database:latest
docker tag "$LS_HASH" ghcr.io/comp361/f2022-hexanome-16/lobby-service:latest
docker push ghcr.io/comp361/f2022-hexanome-16/splendor-server:latest
docker push ghcr.io/comp361/f2022-hexanome-16/database:latest
docker push ghcr.io/comp361/f2022-hexanome-16/lobby-service:latest
