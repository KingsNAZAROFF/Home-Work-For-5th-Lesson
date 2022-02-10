package uz.pdp.homeworkfor5thlesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.homeworkfor5thlesson.entity.Tasks;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, UUID> {


    List<Tasks> findAllByWorker_EmailAndDone(@Email String worker_email, boolean done);
    Optional<Tasks> findByTaskCode(String taskCode);
}
