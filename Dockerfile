FROM mamohr/centos-java:jdk8
MAINTAINER wenchao.zhou <invoker_zhouwc@163.com>
# 设置服务器编码
ENV LANG en_GB.utf8
ENV STORAGE_MESSAGE false
ENV STORAGE_MESSAGE_PATH /root/message
COPY target/kindle-tool-0.0.1-SNAPSHOT.jar /root/
COPY application-storage.properties /root/
CMD ["java", "-jar","-Dstorage.message=${STORAGE_MESSAGE}","-Dstorage.message.path=${STORAGE_MESSAGE_PATH}","/root/kindle-tool-0.0.1-SNAPSHOT.jar"]