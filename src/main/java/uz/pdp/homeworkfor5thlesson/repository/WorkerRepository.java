package uz.pdp.homeworkfor5thlesson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.homeworkfor5thlesson.entity.Role;
import uz.pdp.homeworkfor5thlesson.entity.Worker;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID>{
    boolean existsByEmail( String email);
    Optional<Worker> findByEmailAndEmailCode(String email, String emailCode);
    Optional<Worker> findByEmail(@Email String email);
    Optional<Worker> findByTurniketId(UUID turniketId);
    List<Worker> findAllByRoles(Set<Role> roles);
    boolean existsByRoles(Set<Role> roles);
}
