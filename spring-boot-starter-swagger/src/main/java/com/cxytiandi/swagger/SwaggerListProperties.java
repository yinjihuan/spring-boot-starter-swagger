package com.cxytiandi.swagger;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger.ui")
public class SwaggerListProperties {
	private List<SwaggerProperties> confs;

	public List<SwaggerProperties> getConfs() {
		return confs;
	}

	public void setConfs(List<SwaggerProperties> confs) {
		this.confs = confs;
	}
	
}
