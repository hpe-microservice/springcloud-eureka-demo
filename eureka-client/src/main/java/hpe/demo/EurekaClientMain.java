package hpe.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientMain {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientMain.class, args);
	}

}

@RestController
class ServiceInstanceRestController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@RequestMapping("/get-time")
	public String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}

	@RequestMapping("/invoke-api")
	public String ss() {
		List<ServiceInstance> list = this.discoveryClient.getInstances("eureka-client");
		if (list.size() > 0) {
			String uri = list.get(0).getUri().toString();
			return (new RestTemplate()).getForObject(uri + "/get-time", String.class);
		}
		return "No instance for '/get-time'";
	}

}