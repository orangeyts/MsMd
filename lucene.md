1 中文分词器安装
https://github.com/blueshen/ik-analyzer
mvn clean install -Dmaven.test.skip=true

curl http://localhost:8080/list?queryString=vivo&price%3E100&page=1