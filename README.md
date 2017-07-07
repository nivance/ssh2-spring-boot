# ssh2-spring-boot
现在很多创业公司都不再自建机房，更多地选择云主机，如阿里云和腾讯云等。为了安全考虑，他们提供的关系数据库、nosql数据库等服务器都是不能直接访问的，只能通过云主机访问。因此开发和测试会比较麻烦。

在Java环境中，可以使用[JSch](http://www.jcraft.com/jsch/)和[GANYMED](https://www.cleondris.com/opensource/ssh2/)解决这个问题。本地程序通过JSch或GANYMED连接到云主机，通过端口转发访问云数据库等受限的服务器，相当于本地操作，很方便。

本项目为方便使用[JSch](http://www.jcraft.com/jsch/)和[GANYMED](https://www.cleondris.com/opensource/ssh2/)而建，可以随意选择一种使用。


**注意**：本地的转发端口有可能会退出监听，JSch不能重新建立连接，GANYMED可以重新建立连接，您可视情况用。


### 下载打包 

```
git clone https://github.com/nivance/ssh2-spring-boot.git
```

因为没有将ssh-spring-boot放到任何maven公共仓库，所以需要将ssh-spring-boot的jar包安装到本地repository中。
```
cd ssh-spring-boot

mvn clean install

cd ganymed-spring-boot/ganymed-spring-boot-starter/target

mvn install:install-file -Dfile=ganymed-spring-boot-starter-1.0.0.jar \
	-DgroupId=ssh2.spring.boot \
	-DartifactId=ganymed-spring-boot-starter \ 
	-Dversion=1.0.0 \
	-Dpackaging=jar
	
cd ../../ganymed-spring-boot-autoconfigure/target

mvn install:install-file -Dfile=ganymed-spring-boot-autoconfigure-1.0.0.jar \
	-DgroupId=ssh2.spring.boot \
	-DartifactId=ganymed-spring-boot-autoconfigure \ 
	-Dversion=1.0.0 \
	-Dpackaging=jar

或者：---------------------------------------------------
cd jsch-spring-boot/jsch-spring-boot-starter/target

mvn install:install-file -Dfile=jsch-spring-boot-starter-1.0.0.jar \
	-DgroupId=ssh2.spring.boot \
	-DartifactId=jsch-spring-boot-starter \
	-Dversion=1.0.0 \
	-Dpackaging=jar
	
cd ../../jsch-spring-boot-autoconfigure/target

mvn install:install-file -Dfile=jsch-spring-boot-autoconfigure-1.0.0.jar \
	-DgroupId=ssh2.spring.boot \
	-DartifactId=jsch-spring-boot-autoconfigure \ 
	-Dversion=1.0.0 \
	-Dpackaging=jar
		
```

### 引用（2选1）
```
<dependency>
	<groupId>ssh2.spring.boot</groupId>
	<artifactId>jsch-spring-boot-starter</artifactId>
	<version>1.0.0</version>
</dependency>

<dependency>
	<groupId>ssh2.spring.boot</groupId>
	<artifactId>ganymed-spring-boot-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```