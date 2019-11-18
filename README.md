#### jenkins线上中了一次病毒(并不是说jenkins不强大,正式因为太强大和代码开放，和用的人多导致hacker惦记上了)

## 设计原则
1. 尽量把工作放在maven中,这里只是触发一下构建而已,例如edas:deploy等自定义maven mojo插件

## 使用步骤：
1. 创建数据库脚本；
2. 创建项目；
3. 部署项目；

## more: 网关
1. 下线,上线通知网关

## more: rpc
1. 下线,上线通知<注册中心>

## 访问路径
    http://localhost

## https://gitee.com/xiaoxustudent/jfinal-tio  WebSocket 参考

## jfinal部署tomcat最佳实践
<Context path="" docBase="/var/www/my_project" reloadable="false" />
https://my.oschina.net/jfinal/blog/353062

但是对于一个tocmat要放多个应用的话,还是设置好ctx变量

## mvn授权
/cihome/apache-maven-3.5.4/bin
chmod u+x *

## 转码
需要运行linux命令的服务器 要安装 yum -y install dos2unix,否则在window上生成的文件,由于格式问题，无法执行
dos2unix -q spring-mvc-chat.sh

## 前端项目记得在环境安装 nodejs
[nodejs](https://www.cnblogs.com/emao/p/5511394.html)
wget https://nodejs.org/download/release/v9.9.0/node-v9.9.0-linux-x64.tar.gz

### nodejs 下载是 tar.xz
可以看到这个压缩包也是打包后再压缩，外面是xz压缩方式，里层是tar打包方式。
```
$xz -d ***.tar.xz
$tar -xvf  ***.tar
mv node-v10.15.1-linux-x64 nodePackage 这样建立软连接-如果更换了node版本,只需要下载新的版本，改成这个名字就可以了
ln -s /mnt/cihome/nodePackage/bin/npm /usr/bin/npm (bash目录)
ln -s /mnt/cihome/nodePackage/bin/node /usr/bin/node（bash 目录）

删除已有的软连接
rm -rf /usr/bin/npm
rm -rf /usr/bin/node

node -v
npm -v

1.临时使用
npm --registry https://registry.npm.taobao.org install express
2.持久使用
npm config set registry https://registry.npm.taobao.org
3.配置后可通过下面方式来验证是否成功
npm config get registry
```

### edas API 接入指南
edas_config.yaml无法配置私有内部地址上传，可以使用oss sdk(internal地址)和 edas java sdk(部署api)配合使用
https://help.aliyun.com/document_detail/62123.html?spm=a2c4g.11186623.6.747.4328d29aS5dHC0

.ssh多git账户修改,下载的时候要用git协议,不能用https协议,还要配置项目私有配置
git config user.email "112260085@qq.com"

