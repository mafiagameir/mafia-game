package co.mafiagame.bot;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class ApplicationConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

	@Bean
	public CacheManager cacheManager() {
		CacheManager cacheManager
				= CacheManagerBuilder.newCacheManagerBuilder()
				.build();
		cacheManager.init();
		return cacheManager;
	}

	@ConditionalOnProperty(name = "proxy.enable", havingValue = "true")
	@Bean
	public RestTemplate restTemplateWithProxy() {
		SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8123));
		clientHttpReq.setProxy(proxy);
		RestTemplate restTemplate = new RestTemplate(clientHttpReq);
		setErrorHandler(restTemplate);
		return restTemplate;
	}

	@ConditionalOnProperty(name = "proxy.enable", havingValue = "false")
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		setErrorHandler(restTemplate);
		return restTemplate;
	}

	private void setErrorHandler(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
				return !clientHttpResponse.getStatusCode().equals(HttpStatus.OK);
			}

			@Override
			public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
				logger.error("error calling telegram getUpdate\n code:{}\n{}",
						clientHttpResponse.getStatusCode(),
						org.apache.commons.io.IOUtils.toString(clientHttpResponse.getBody()));
			}
		});
	}
}
