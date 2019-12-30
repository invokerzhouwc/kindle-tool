FROM mamohr/centos-java:jdk8
MAINTAINER wenchao.zhou <invoker_zhouwc@163.com>
# 设置服务器编码
ENV LANG en_GB.utf8
COPY target/kindle-tool-0.0.1-SNAPSHOT.jar /root/
CMD ["locale"]
CMD ["java", "-jar","/root/kindle-tool-0.0.1-SNAPSHOT.jar"]