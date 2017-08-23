package com.cultur.eventmanager.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 *
 * @author Shantanu Singh
 * @since 23-May-16
 */
@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = { "com.cultur.eventmanager", "com.cultur.eventmanager.repositories", "com.cultur.eventmanager.services", "com.cultur.eventmanager.controllers" })
@PropertySource("classpath:application.properties")
public class EventManagerWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
    private Environment environment;
	
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
    }
    
    /*@Bean
    public JavaMailSender javaMailSenderImpl(){
    	Properties javaMailProperties = new Properties();

    	javaMailProperties.setProperty("mail.transport.protocol", environment.getProperty("mail.transport.protocol"));
    	javaMailProperties.setProperty("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
    	javaMailProperties.setProperty("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
    	javaMailProperties.setProperty("mail.debug", environment.getProperty("mail.debug"));

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(environment.getRequiredProperty("mail.smtp.host"));
        sender.setPort(Integer.parseInt(environment.getRequiredProperty("mail.smtp.port")) );

        if (environment.getProperty("mail.smtp.auth").equalsIgnoreCase("true")) {
            sender.setUsername(environment.getRequiredProperty("mail.user"));
            sender.setPassword(environment.getRequiredProperty("mail.password"));
        }

		sender.setJavaMailProperties(javaMailProperties );

    	return sender;
    }*/

    /*@Bean
    public VelocityEngine velocityEngine() throws VelocityException, IOException {
        VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("directive.set.null.allowed", "true");
        props.put("dateToolAttribute", "org.apache.velocity.tools.generic.DateTool");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);

        return factory.createVelocityEngine();
    }*/
}
