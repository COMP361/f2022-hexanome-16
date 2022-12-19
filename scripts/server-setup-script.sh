#!/bin/bash

# Setup Docker
sudo apt-get remove -y docker docker-engine docker.io containerd runc
sudo apt-get update
sudo apt-get install -y \
    ca-certificates \
    curl \
    wget \
    gnupg \
    lsb-release
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --batch --yes --dearmor -o /etc/apt/keyrings/docker.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo systemctl enable --now docker.service
sudo systemctl enable --now containerd.service

# Login to private GitHub package repo
if [[ -z $CR_PAT ]]; then
  CR_PAT=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
fi
echo $CR_PAT | docker login ghcr.io -u USERNAME --password-stdin

# Download docker-compose.yml and start our game server + LS
wget https://gist.github.com/ConstBur/d715e15f7ee4035ce45cde5a67a37d58/raw/1daeea4bcda844369741c97822e26ed9fc5d6b72/docker-compose.yml
docker compose --profile with-server up -d --no-build --force-recreate --remove-orphans
