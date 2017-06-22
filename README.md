# jsch-spring-boot
现在很多创业公司都不再自建机房，更多地选择云主机，如阿里云和腾讯云等。为了安全考虑，他们提供的关系数据库、nosql数据库等服务器都是不能直接访问的，只能通过云主机访问。因此开发和测试会比较麻烦。

在Java环境中，可以使用[JSch](http://www.jcraft.com/jsch/)解决这个问题。本地程序通过JSch连接到云主机，通过端口转发访问云数据库等受限的服务器，相当于本地操作，很方便。

本项目jsch-spring-boot为方便使用[JSch](http://www.jcraft.com/jsch/)而建。

##### 1.下载和打包

```
git clone https://github.com/nivance/jsch-spring-boot.git
```

因为没有将jsch-spring-boot放到任何maven公共仓库，所以需要将jsch-spring-boot的jar包安装到本地repository中。
```
cd jsch-spring-boot
mvn clean install
cd jsch-spring-boot-starter\target
mvn install:install-file -Dfile=jsch-spring-boot-starter-1.0.0.jar -DgroupId=jsch.spring.boot -DartifactId=jsch-spring-boot-starter -Dversion=1.0.0 -Dpackaging=jar
cd ..\..\jsch-spring-boot-autoconfigure\target
mvn install:install-file -Dfile=jsch-spring-boot-autoconfigure-1.0.0.jar -DgroupId=jsch.spring.boot -DartifactId=jsch-spring-boot-autoconfigure -Dversion=1.0.0 -Dpackaging=jar
```

##### 2. 引用
<dependency>
	<groupId>jsch.spring.boot</groupId>
	<artifactId>jsch-spring-boot-starter</artifactId>
	<version>1.0.0</version>
</dependency>

