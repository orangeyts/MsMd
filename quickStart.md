0 a_little_config.txt 拷贝为 a_little_config_db.txt
1 mvn clean install -Pdev -DskipTests
2 运行数据库脚本 init.sql
3 启动DemoConfig.java
4 http://localhost:801/msmd  用户名u 密码 p


自定义开发: com.demo.common.model._JFinalDemoGenerator用于生成基本方法