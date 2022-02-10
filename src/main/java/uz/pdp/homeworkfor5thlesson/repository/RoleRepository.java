package uz.pdp.homeworkfor5thlesson.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.homeworkfor5thlesson.entity.Role;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
