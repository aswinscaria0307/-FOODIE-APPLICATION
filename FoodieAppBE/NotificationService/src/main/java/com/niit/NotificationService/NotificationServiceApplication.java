package com.niit.NotificationService;

import com.niit.NotificationService.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableEurekaClient
public class NotificationServiceApplication {
	@Autowired
	private	EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
//	@EventListener(ApplicationReadyEvent.class)
//	public void sendEmail(){
//		emailService.sendEmail("aswinscaria0307@gmail.com","am testing","mail sent success");
//	}
//@Bean
//public FilterRegistrationBean filterRegistrationBean(){
//	final CorsConfiguration config=new CorsConfiguration();
//	config.setAllowCredentials(true);
//	config.addAllowedOrigin("http://localhost:4200");
//	config.addAllowedHeader("*");
//	config.addAllowedMethod("*");
//	final UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
//	source.registerCorsConfiguration("/**",config);
//	FilterRegistrationBean bean=new FilterRegistrationBean(new CorsFilter(source));
//	bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//	return bean;
//}

}
