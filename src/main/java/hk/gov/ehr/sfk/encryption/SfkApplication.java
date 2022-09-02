package hk.gov.ehr.sfk.encryption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SfkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfkApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate2() {
        return new RestTemplate();
    }
}
