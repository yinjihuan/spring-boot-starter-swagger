# spring-boot-starter-swagger

- 快速集成spring boot
- 直接通过配置定义API分组信息

下载源码编译到本地参考即可使用
```
<!-- Swagger -->
<dependency>
	<groupId>com.cxytiandi</groupId>
	<artifactId>spring-boot-starter-swagger</artifactId>
	<version>1.0.0</version>
</dependency>
```

配置如下，支持多个配置，采用下标方式，从0开始

具体配置参考：[https://github.com/yinjihuan/spring-boot-starter-swagger/blob/master/spring-boot-starter-swagger/src/main/java/com/cxytiandi/swagger/SwaggerProperties.java](https://github.com/yinjihuan/spring-boot-starter-swagger/blob/master/spring-boot-starter-swagger/src/main/java/com/cxytiandi/swagger/SwaggerProperties.java)
```
swagger.ui.confs[0].group=loudong
swagger.ui.confs[0].paths=/rest/ld.*
swagger.ui.confs[0].contact=yinjihuan[QQ:1304489315]
swagger.ui.confs[0].version=1.0
swagger.ui.confs[1].group=user_auth
swagger.ui.confs[1].paths=/rest/auth.*
```
