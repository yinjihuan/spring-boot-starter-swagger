# spring-boot-starter-swagger

- 快速集成spring boot
- 直接通过配置定义API分组信息

配置如下，支持多个配置，采用下标方式，从0开始
```
swagger.ui.confs[0].group=loudong
swagger.ui.confs[0].paths=/rest/ld.*
swagger.ui.confs[0].contact=yinjihuan[QQ:1304489315]
swagger.ui.confs[0].version=1.0
swagger.ui.confs[1].group=user_auth
swagger.ui.confs[1].paths=/rest/auth.*
```
