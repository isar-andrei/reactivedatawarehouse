package com.ai.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class EtlApplication {

	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(EtlApplication.class, args);
	}

}
