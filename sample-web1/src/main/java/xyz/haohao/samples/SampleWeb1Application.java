package xyz.haohao.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "xyz.haohao")
public class SampleWeb1Application {
	public static void main(String[] args) {
        SpringApplication.run(SampleWeb1Application.class, args);
	}
}
