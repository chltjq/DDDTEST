#!/bin/sh
### env 설정 ###
for SECRET in $(ls /etc/secret/); do export $SECRET=$(cat /etc/secret/$SECRET); done

### app 설정 ###
JAVA_OPTS="${JAVA_OPTS} -Xms${JAVA_HEAP_XMS} -Xmx${JAVA_HEAP_XMX}"
JAVA_OPTS="${JAVA_OPTS} -XX:MaxMetaspaceSize=256m"
# JAVA_OPTS="${JAVA_OPTS} ${JAVA_AGENT}"
JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="${JAVA_OPTS} -Dspring.backgroundpreinitializer.ignore=true"
JAVA_OPTS="${JAVA_OPTS} -Xlog:gc*=debug:file=/var/log/gc.log:time,level,tags"


### 운영환경 개인정보접속기록 INFOSAFER 설정 ###
if [ ${PROFILE} == "prd" ]; then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/agent.java/wastracer/wastracer-javaagent.jar -Xbootclasspath/a:/agent.java/wastracer/wastracer.jar"
    cp -arp /ba_scp/prd/* /ba_scp/
else
    cp -arp /ba_scp/dev/* /ba_scp/
fi
chmod -s /usr/bin/newgrp
chmod -s /sbin/unix_chkpwd
chmod 644 /var/log/wtmp
export LD_LIBRARY_PATH=/usr/local:/ba_scp

mv motd /etc/motd
sed -i -e 5,7s/\#auth/auth/g /etc/pam.d/su

### app 실행 ###
exec java ${JAVA_OPTS} -Dspring.profiles.active=${PROFILE} -jar /source001/boot.jar