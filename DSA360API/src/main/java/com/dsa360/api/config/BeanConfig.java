package com.dsa360.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class BeanConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean(name = "asyncExecutor")
	public TaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // Minimum number of threads
		executor.setMaxPoolSize(10); // Maximum number of threads
		executor.setQueueCapacity(25); // Queue capacity for incoming tasks
		executor.setThreadNamePrefix("Async-Executor-"); // Thread name prefix
		executor.initialize();
		return executor;
	}

}
