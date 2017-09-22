package com.shanglan.erp.config;

import com.shanglan.erp.base.sms.SmsProperties;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@PropertySource("classpath:erp-core.properties")
public class SmsConfig {
	@Autowired
	private Environment env;

	@Bean
	public SmsProperties smsProperties() {
		return new SmsProperties(env.getProperty("sms.alidayu.templateCode.register"),
				env.getProperty("sms.alidayu.templateCode.password"), env.getProperty("sms.alidayu.templateCode.login"),
				env.getProperty("sms.alidayu.templateCode.createPassword"), env.getProperty("sms.alidayu.signName"));
	}

	@Bean
	public TaobaoClient taobaoClient() {
		String url = env.getProperty("sms.alidayu.url");
		String appKey = env.getProperty("sms.alidayu.app.key");
		String secret = env.getProperty("sms.alidayu.app.secret");
		return new DefaultTaobaoClient(url, appKey, secret);
	}
}
