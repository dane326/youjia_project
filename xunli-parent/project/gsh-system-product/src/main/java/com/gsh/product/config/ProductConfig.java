package com.gsh.product.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.wxpay.sdk.WXPay;
import com.google.common.io.ByteStreams;

/**
 * 配置
 * @author LCS
 *
 */
@Configuration
public class ProductConfig {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	@ConditionalOnMissingBean(AlipayConfig.class)
	public AlipayConfig alipayConfig() {
		return new AlipayConfig();
	}
	
	@Bean
	@ConditionalOnMissingBean(AlipayClient.class)
	public AlipayClient alipayClient() {
		AlipayConfig alipayConfig = alipayConfig();
		return new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", alipayConfig.getCharset(), alipayConfig.getAlipayPublicKey(), alipayConfig.getSignType());
	}

	@Bean
	@ConditionalOnMissingBean(WinxinPayConfig.class)
	public WinxinPayConfig winxinPayConfig() {
		WinxinPayConfig winxinPayConfig = new WinxinPayConfig();
		try {
			winxinPayConfig.setCertData(ByteStreams.toByteArray(ProductConfig.class.getResourceAsStream("/cert/apiclient_cert.p12")));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return winxinPayConfig;
	}
	
	@Bean
	@ConditionalOnMissingBean(WXPay.class)
	public WXPay wxPay() {
		WinxinPayConfig winxinPayConfig = winxinPayConfig();
		return new WXPay(winxinPayConfig, winxinPayConfig.getSignType());
	}
}
