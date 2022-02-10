package uz.pdp.homeworkfor5thlesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.homeworkfor5thlesson.entity.Salary;

import javax.validation.constraints.Email;
import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {
    List<Salary> findAllByWorkerEmail(@Email String worker_email);
}
