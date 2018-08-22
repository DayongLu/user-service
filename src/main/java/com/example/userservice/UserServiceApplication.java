package com.example.userservice;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initialized(UserRepository repository) {
        return args -> {
            Stream.of("Dayong", "Mia", "Luke").map(User::new).forEach(user -> repository.save(user));
            repository.findAll().stream().map(user -> user.toString()).forEach(str -> {
                System.out.println(str);
            });
        };
    }
}


@RestController
class UserRestController {




    private UserRepository repository;

    private String port;

    UserRestController(UserRepository repository, @Value("${server.port}") String port) {
        this.repository = repository;
        this.port = port;
    }

    @GetMapping(value = "/allUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    List<User> getAllUser() {
        return this.repository.findAll();

    }

    @GetMapping(value = "/message")
    String getMessage(){
        return "This is coming from "+ this.port;
    }
}


@RepositoryRestResource
interface UserRepository extends JpaRepository<User, Long> {

}


@Data
//@NoArgsConstructor
//@AllArgsConstructor
@ToString
@Entity
class User {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    public User(String name) {
        this.name = name;
    }


}
