package academy.jairo.springboot.repository;

import academy.jairo.springboot.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

}
