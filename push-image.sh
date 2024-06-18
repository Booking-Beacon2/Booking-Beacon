PROFILE=${1}

docker build -t booking-beacon --build-arg SPRING_PROFILES_ACTIVE=${ENV} .
docker tag booking-beacon 381492090552.dkr.ecr.ap-northeast-2.amazonaws.com/booking-beacon:booking-beacon

docker push 381492090552.dkr.ecr.ap-northeast-2.amazonaws.com/booking-beacon:booking-beacon
