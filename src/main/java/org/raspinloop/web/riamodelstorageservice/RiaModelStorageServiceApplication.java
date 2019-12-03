package org.raspinloop.web.riamodelstorageservice;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@SpringBootApplication
public class RiaModelStorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiaModelStorageServiceApplication.class, args);
	}
	
	  @Bean
	  public Filter OpenFilter() {
	    return new OpenEntityManagerInViewFilter();
	  }
}
