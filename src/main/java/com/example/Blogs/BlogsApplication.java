package com.example.Blogs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class BlogsApplication{

	private static final Logger log = LoggerFactory.getLogger(BlogsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BlogsApplication.class, args);
	}


}
