docker-compose -f mobile-sample-docker-compose.yml up -d hub samsung_s7_9_0
sleep 1m
VERSION=3.2.1.1 docker-compose -f mobile-sample-docker-compose.yml up --abort-on-container-exit --exit-code-from lippia
 
