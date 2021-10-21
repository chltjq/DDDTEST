FROM 237655567475.dkr.ecr.ap-northeast-2.amazonaws.com/aip-devops-all/amzn-openjdk:latest
ENV JAVA_HEAP_XMS 512m
ENV JAVA_HEAP_XMX 512m
ENV AWS_XRAY_CONTEXT_MISSING LOG_ERROR
ENV LC_ALL=C.UTF-8

RUN mkdir deploy
COPY build/libs/gep-admin-0.0.1-SNAPSHOT.jar /source001/boot.jar
RUN chmod +x source001/boot.jar
COPY docker-entrypoint.sh .
COPY agent.java/ agent.java/
COPY ba_scp/ ba_scp/
COPY motd .
RUN chmod +x docker-entrypoint.sh

ENTRYPOINT ["./docker-entrypoint.sh"]
