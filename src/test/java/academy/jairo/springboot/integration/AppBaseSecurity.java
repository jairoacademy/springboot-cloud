package academy.jairo.springboot.integration;

import academy.jairo.springboot.domain.AppUser;
import academy.jairo.springboot.repository.AppUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Tha idea is to have a base class for the integration tests, but not work
 */
@Log4j2
public class AppBaseSecurity {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    AppUserRepository appUserRepository;

    static final AppUser USER = AppUser.builder()
            .name("Mary")
            .username("mary")
            .password("{bcrypt}$2a$10$6XaK5xZdCSATyXY3BJWTYuhvYg.qWBUjnWrNCYx5AL6Ndqo272OsW")
            .authorities("ROLE_USER,USER")
            .build();

    static final AppUser ADMIN = AppUser.builder()
            .name("Jairo")
            .username("jairo")
            .password("{bcrypt}$2a$10$6XaK5xZdCSATyXY3BJWTYuhvYg.qWBUjnWrNCYx5AL6Ndqo272OsW")
            .authorities("ROLE_USER,ROLE_ADMIN,USER,ADMIN")
            .build();


    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(
                @Value("${local.server.port}") int port ) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("mary", "321654");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(
                @Value("${local.server.port}") int port ) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("jairo", "321654");
            return new TestRestTemplate(restTemplateBuilder);
        }

    }

}
