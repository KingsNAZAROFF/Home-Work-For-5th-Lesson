package uz.pdp.homeworkfor5thlesson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.homeworkfor5thlesson.entity.Salary;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.payload.SalaryDto;
import uz.pdp.homeworkfor5thlesson.repository.RoleRepository;
import uz.pdp.homeworkfor5thlesson.repository.SalaryRepository;
import uz.pdp.homeworkfor5thlesson.repository.WorkerRepository;
import uz.pdp.homeworkfor5thlesson.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse giveSalary(HttpServletRequest request, SalaryDto salaryDto){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken!=null && roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))){
            Optional<Worker> byEmail = workerRepository.findByEmail(salaryDto.getEmail());
            if (byEmail.isEmpty()){
                return new ApiResponse("Bunday worker mavjud emas",false);
            }
            Worker worker =byEmail.get();
            Salary salary = new Salary();
            salary.setSalaryPrice(salaryDto.getSalaryPrice());
            salary.setWorker(worker);
            salaryRepository.save(salary);
            return new ApiResponse("Workerga oylik berildi",true);
        }
        return new ApiResponse("Bu yo'lga kirishga ruxsat yo'q",false);

    }
    public ApiResponse getAll(HttpServletRequest request, String email) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken != null && roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))) {

            Optional<Worker> optionalWorker = workerRepository.findByEmail(email);
            if (optionalWorker.isEmpty()){
                return new ApiResponse("Bunday Worker mavjud emas",false);
            }
            List<Salary> salaryList = salaryRepository.findAllByWorkerEmail(email);
            return new ApiResponse("Natija",true,salaryList);
        }
        return new ApiResponse("Bu yo'lga kirishga ruxsat yo'q",false);
    }
}
