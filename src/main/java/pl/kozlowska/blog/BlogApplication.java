package pl.kozlowska.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import pl.kozlowska.blog.helpers.WriterToXML;
import pl.kozlowska.blog.service.interfaces.StorageService;
import pl.kozlowska.blog.storage.StorageProperties;

import java.io.*;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
@EnableConfigurationProperties(StorageProperties.class)
public class BlogApplication {
    public static void main(String[] args) throws IOException {
        WriterToXML.createXMLFromCSV();
        SpringApplication.run(BlogApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}


