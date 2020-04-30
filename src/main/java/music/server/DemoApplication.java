package music.server;

import music.server.entities.User;
import music.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	@Value("${admin_username}")
	private String admin_username;
	@Value("${admin_password}")
	private String admin_password;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init (UserRepository userRepository){
		return args -> {
			User user = userRepository.findByUsername(admin_username);
			if(user == null){
				System.out.println("here");
				User temp = new User(admin_username, passwordEncoder.encode(admin_password), "admin");
				userRepository.save(temp);
			}
		};
	}

}
