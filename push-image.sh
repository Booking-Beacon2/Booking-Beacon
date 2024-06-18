docker build -t booking-beacon .
docker tag booking-beacon 381492090552.dkr.ecr.ap-northeast-2.amazonaws.com/booking-beacon:booking-beacon

docker push 381492090552.dkr.ecr.ap-northeast-2.amazonaws.com/booking-beacon:booking-beacon
