package academy.jairo.springboot.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Tha idea is to have a base class for the integration tests, but not work
 */
public class IntegrationTestConfiguration {

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
