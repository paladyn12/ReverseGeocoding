package reverseGeocoding.myProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ReverseGeocodingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReverseGeocodingApplication.class, args);
	}

}
