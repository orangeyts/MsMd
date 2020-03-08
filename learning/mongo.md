learning

-  下载安装包

curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.2.1.tgz
tar -xvf mongodb-linux-x86_64-rhel70-4.2.1.tgz

- 环境变量

export PATH=$PATH:/mnt/data/soft/mongodb-linux-x86_64-rhel70-4.2.3/bin
vi /etc/profile
source /etc/profile

- 启动
mongod --dbpath /mnt/data/soft/mongodata --port 27017 --logpath /mnt/data/soft/mongodata/mongod.log --fork

--bind_ip 不能加 会报错,估计是默认 安装的mongo没有用户名和密码，绑定所有IP有风险，设置一个 密码，并以 --auth启动试试
mongod --dbpath /mnt/data/soft/mongodata --port 27017 --logpath /mnt/data/soft/mongodata/mongod.log --fork --bind_ip 0.0.0.0
mongod --dbpath /mnt/data/soft/mongodata --port 27017 --logpath /mnt/data/soft/mongodata/mongod.log --fork --bind_ip_all

- 关闭数据库

mongod --shutdown --dbpath /mnt/data/soft/mongodata

- 创建账户

mongo

use admin

db.createUser(
  {
    user: "root",
    pwd: "123456as",
    roles: [ { role: "root", db: "admin" } ]
  }
)

mongod --auth --dbpath /mnt/data/soft/mongodata --port 27017 --logpath /mnt/data/soft/mongodata/mongod.log --fork --bind_ip 0.0.0.0

mongo
use admin
db.auth("root", "123456as" )

- 导入测试数据
mongorestore dump



https://www.cnblogs.com/meiriyixiaobu/p/10114888.html

https://www.cnblogs.com/wadeyu/p/7928625.html

db.system.users.find()


db.createUser(
    {
        user:"admin",
        pwd:"irootech123",
        roles:[
            {role:"root", db:"admin"}
        ]
    }
)


db.createUser({user:"admin",pwd:"123",roles:["readWrite"]})

db.updateUser('root',{pwd:'654321',roles:[{role:'root',db:'admin'}]})

mongo localhost:27017/admin -u admin -p 123

#全局查看
mongostat -h localhost:27017 -u admin -p 123 --authenticationDatabase=admin

# 查看具体的集合的读写情况
mongotop -h localhost:27017 -u admin -p 123 --authenticationDatabase=admin

# 查看日志的 执行计划
tail -f /mnt/data/soft/mongodata/mongod.log