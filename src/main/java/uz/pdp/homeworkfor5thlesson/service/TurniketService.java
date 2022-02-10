package uz.pdp.homeworkfor5thlesson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.homeworkfor5thlesson.entity.Turniket;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.repository.TurniketRepository;
import uz.pdp.homeworkfor5thlesson.repository.WorkerRepository;
import uz.pdp.homeworkfor5thlesson.security.JwtProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TurniketService {


    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    TurniketRepository turniketRepository;

    public ApiResponse getIn(UUID turniketId){
        Optional<Worker> optionalWorker = workerRepository.findByTurniketId(turniketId);
       if (optionalWorker.isEmpty()){
           return new ApiResponse("Bunday ishchi mavjud emas",false);
       }
        Optional<Turniket> optionalTurniket = turniketRepository.findByTurniketIdAndClosed(turniketId, false);
       if (optionalTurniket.isPresent()) {
           Turniket errorTurniketTime = optionalTurniket.get();
           errorTurniketTime.setClosed(true);
       }
        Turniket turniket = new Turniket();
       turniket.setTurniketId(turniketId);
       turniket.setClosed(false);
       turniketRepository.save(turniket);
       return new ApiResponse("Hush kelibsiz",true);
    }
    public ApiResponse getOut(UUID turniketId){
        Optional<Worker> optionalWorker = workerRepository.findByTurniketId(turniketId);
        if (optionalWorker.isEmpty()){
            return new ApiResponse("Bunday ishchi mavjud emas",false);
        }
        Optional<Turniket> optionalTurniket = turniketRepository.findByTurniketIdAndClosed(turniketId, false);
        if (optionalTurniket.isEmpty()){
            return new ApiResponse("Kirish vaqti belgilanmagan",false);
        }
        Turniket closingTurniket = optionalTurniket.get();
        closingTurniket.setClosed(true);
        turniketRepository.save(closingTurniket);
        return new ApiResponse("Hayr salomat bo'ling",true);
    }

    public List<Turniket> getAllBYOne(String email){
        Optional<Worker> byEmail = workerRepository.findByEmail(email);
        if (byEmail.isEmpty()){
            return null;
        }
        Worker worker = byEmail.get();
        List<Turniket> turniketList = turniketRepository.findAllByTurniketId(worker.getTurniketId());
        return turniketList ;
    }



}
