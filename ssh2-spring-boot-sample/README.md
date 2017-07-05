# ssh2-spring-boot-sample使用说明

本项目以访问远程MySQL为例。

默认使用dev配置文件，可在application-dev.yml配置要访问的MySQL服务，然后在此数据库执行doc/book.sql文件。

JSch和GANYMED只能选择其中一个。

```
		<dependency>
			<groupId>ssh2.spring.boot</groupId>
			<artifactId>ganymed-spring-boot-autoconfigure</artifactId>
			<version>${ssh2.spring.boot.version}</version>
		</dependency> 
		
		<dependency>
			<groupId>ssh2.spring.boot</groupId>
			<artifactId>jsch-spring-boot-autoconfigure</artifactId>
			<version>${ssh2.spring.boot.version}</version>
		</dependency> 
```

