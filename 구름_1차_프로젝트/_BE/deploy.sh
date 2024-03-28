#!/bin/bash

CONTAINER_NAME="decimal"
DOCKER_ACCOUNT="rhrudah"
REPOSITORY="my-spring-app"
TAG="latest"

# 실행 중인 컨테이너가 있으면 정지하고 삭제
sudo docker ps -a -q --filter "name=$CONTAINER_NAME" | grep -q . && sudo docker stop $CONTAINER_NAME && sudo docker rm $CONTAINER_NAME

# 기존 이미지 삭제
sudo docker rmi $DOCKER_ACCOUNT/$REPOSITORY:$TAG

# 도커허브 이미지 pull
sudo docker pull $DOCKER_ACCOUNT/$REPOSITORY:$TAG

# 도커 run (컨테이너를 백그라운드에서 실행)
sudo docker run -d -p 8443:8443 --name $CONTAINER_NAME $DOCKER_ACCOUNT/$REPOSITORY:$TAG

# 사용하지 않는 불필요한 이미지 삭제 (현재 컨테이너가 사용 중인 이미지는 삭제되지 않음)
sudo docker rmi -f $(sudo docker images -f "dangling=true" -q) || true
