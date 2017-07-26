package com.cxytiandi.swagger;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger.ui", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerListProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class SwaggerAutoConfiguration extends WebMvcConfigurerAdapter {
	protected static final Logger log = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);

	@Autowired
	private SwaggerListProperties props;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 注册多个配置
	 * @return
	 * @throws Exception
	 */
	@Bean
	public Runnable dynamicConfiguration() throws Exception {
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		List<SwaggerProperties> list = props.getConfs();
		int index = 0;
		//可以添加多个header或参数
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.parameterType("header") //参数类型支持header, cookie, body, query etc
                .name("Authorization") //参数名
                .defaultValue("") //默认值
                .description("API认证的TOKEN")
                .modelRef(new ModelRef("string"))//指定参数值的类型
                .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(parameterBuilder.build());
		for (SwaggerProperties conf : list) {
			index++;
			Docket doc = new Docket(DocumentationType.SWAGGER_2).groupName(conf.getGroup()).apiInfo(apiInfo(conf))
					.genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false)
					.forCodeGeneration(false).pathMapping("/").select().paths(paths(conf))
					.build().globalOperationParameters(parameters);
			beanFactory.registerSingleton("docket" + index, doc);
		}
		return null;
	}
	
	
	private Predicate<String> paths(SwaggerProperties conf) {
		List<Predicate<CharSequence>> predis = new ArrayList<Predicate<CharSequence>>();
		String[] ps = conf.getPaths().split(",");
		for (String path : ps) {
			predis.add(Predicates.containsPattern(path));
		}
		return Predicates.or(predis);
	}
	
	private ApiInfo apiInfo(SwaggerProperties conf) {
        return new ApiInfoBuilder()
                .title(conf.getTitle())
                .description(conf.getDescription())
                .termsOfServiceUrl(conf.getTermsOfServiceUrl())
                .contact(conf.getContact())
                .version(conf.getVersion())
                .license(conf.getLicense())
                .licenseUrl(conf.getLicenseUrl())
                .build();
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//映射静态文件目录，如果还是不行，需要对项目添加server.context-path
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
