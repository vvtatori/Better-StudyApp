package team.project.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import team.project.team.entity.EmailSenderService;

@SpringBootApplication
public class TeamProjectApplication {
    


	public static void main(String[] args) {
		SpringApplication.run(TeamProjectApplication.class, args);
	}
}
//http://localhost:8080/sendmail?toEmail=sam123ashe@gmail.com