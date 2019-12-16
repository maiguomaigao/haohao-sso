package xyz.haohao.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "xyz.haohao")
public class SampleWeb2Application {
	public static void main(String[] args) {
        SpringApplication.run(SampleWeb2Application.class, args);
	}
}
