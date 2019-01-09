package irina.rabbit.restGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class RestGatewayApplication {

    public static void main(String[] args){

        SpringApplication.run(RestGatewayApplication.class, args);
    }
}
