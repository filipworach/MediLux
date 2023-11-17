package com.MediLux.MediLux;

import com.MediLux.MediLux.Config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class MediLuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediLuxApplication.class, args);
	}
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new JwtFilter());
		filterRegistrationBean.setUrlPatterns(Collections.singleton("/patient"));
		return filterRegistrationBean;
	}
}
