package io.batch.SpringBatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


//@EnableBatchProcessing
@SpringBootApplication
@EnableBatchAdmin
public class SpringBatchApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(SpringBatchApplication.class);
    }
}
