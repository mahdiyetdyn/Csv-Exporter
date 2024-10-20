package com.tadayon.csvexporter;

import com.tadayon.csvexporter.config.CsvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CsvConfig.class)
public class CsvExporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvExporterApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(Repository repository) {
//        return args -> {
//            System.out.println("Fetching 10 entries from blacklist:");
//            repository.findAll().stream().limit(10).forEach(System.out::println);
//        };
//    }
}
