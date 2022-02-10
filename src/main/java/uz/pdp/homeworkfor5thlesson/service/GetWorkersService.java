package uz.pdp.homeworkfor5thlesson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;
import uz.pdp.homeworkfor5thlesson.payload.WorkerDtoForSend;
import uz.pdp.homeworkfor5thlesson.repository.RoleRepository;
import uz.pdp.homeworkfor5thlesson.repository.TaskRepository;
import uz.pdp.homeworkfor5thlesson.repository.TurniketRepository;
import uz.pdp.homeworkfor5thlesson.repository.WorkerRepository;
import uz.pdp.homeworkfor5thlesson.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GetWorkersService {
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RoleRepository roleRepository;



    public List<Worker> getAll(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken!=null && roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))){
            return workerRepository.findAll();
        }
        else if (roleFromToken!=null && roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)))){
            return workerRepository.findAllByRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_WORKER)));
        }
        if (roleFromToken!=null && roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_WORKER)))){
            return null;
        }
        return null;

    }
    public WorkerDtoForSend getOne(String email, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null
                && !(roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))
                || roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) )){
            return null;
        }

        Optional<Worker> byEmail = workerRepository.findByEmail(email);
        if (byEmail.isEmpty()){
            return null;
        }
        Worker worker = byEmail.get();
       WorkerDtoForSend workerDto = new WorkerDtoForSend();
       workerDto.setFirstName(worker.getFirstName());
       workerDto.setLastName(worker.getLastName());
       workerDto.setEmail(worker.getEmail());
       workerDto.setRoles(worker.getRoles());
       workerDto.setTurniketList(turniketRepository.findAllByTurniketId(worker.getTurniketId()));



        return workerDto;
    }


}
