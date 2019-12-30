# 删除未打包成功的镜像
docker rmi -f $(docker images | grep '<none>' | awk -F ' ' '{print $3}')
# 使用maven打包
mvn clean install -Dmaven.test.skip=true
# 打包镜像为kindle-tool
docker build -t kindle-tool .
# 获取之前启动的kindle-tool
pid=$(docker ps -a | grep kindle-tool | awk -F ' ' '{print $1}')
# 停止之前启动的镜像，并且删除之前启动的镜像
docker stop "$pid" && docker rm "$pid"
# 启动镜像
docker run -i -t -d --name kindle-tool -p 8084:8084 kindle-tool:latest
