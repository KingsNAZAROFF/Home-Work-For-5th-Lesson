package uz.pdp.homeworkfor5thlesson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.homeworkfor5thlesson.payload.FeedbackText;
import uz.pdp.homeworkfor5thlesson.entity.Tasks;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.payload.TaskDto;
import uz.pdp.homeworkfor5thlesson.repository.RoleRepository;
import uz.pdp.homeworkfor5thlesson.repository.TaskRepository;
import uz.pdp.homeworkfor5thlesson.repository.WorkerRepository;
import uz.pdp.homeworkfor5thlesson.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;


    public ApiResponse giveTaskToWorker(TaskDto taskDto , HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null
                && !(roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))
                || roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) )){
            return new ApiResponse("Wokrerga faqat DIREKTOR va MANAGER vazifa bera oladi",false);
        }

        Optional<Worker> optionalWorker = workerRepository.findByEmail(taskDto.getWorkerEmail());
        if (optionalWorker.isEmpty()){
            return new ApiResponse("Bunday Ishchi mavjud emas",false);
        }

        Tasks newTask = new Tasks();
        newTask.setTaskName(taskDto.getTaskName());
        newTask.setTaskText(taskDto.getTaskText());
        newTask.setTaskCode(UUID.randomUUID().toString());
        newTask.setEndTime((Timestamp) new Date(System.currentTimeMillis()+taskDto.getEndTime()));
        newTask.setWorkerEmail(taskDto.getWorkerEmail());

        taskRepository.save(newTask);
        sendTask(newTask.getWorkerEmail(), newTask.getTaskName(), newTask.getTaskText(), newTask.getTaskCode(),newTask.getEndTime());

        return new ApiResponse("Vazifa jo'natildi",true);


    }
    public ApiResponse giveTaskToManager(TaskDto taskDto, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null && !roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))){
            return new ApiResponse("MANAGER ga faqat DIRECTOR vazifa yuklay oladi",false);
        }
        Optional<Worker> optionalWorker = workerRepository.findByEmail(taskDto.getWorkerEmail());
        if (optionalWorker.isEmpty()){
            return new ApiResponse("Bunday Ishchi mavjud emas",false);
        }
        Tasks newTask = new Tasks();
        newTask.setTaskName(taskDto.getTaskName());
        newTask.setTaskText(taskDto.getTaskText());
        newTask.setTaskCode(UUID.randomUUID().toString());
        newTask.setEndTime((Timestamp) new Date(System.currentTimeMillis()+taskDto.getEndTime()));
        newTask.setWorkerEmail(taskDto.getWorkerEmail());

        taskRepository.save(newTask);
        sendTask(newTask.getWorkerEmail(), newTask.getTaskName(), newTask.getTaskText(), newTask.getTaskCode(),newTask.getEndTime());

        return new ApiResponse("Vazifa jo'natildi",true);

    }
    public Boolean sendTask(String sendingEmail,String subject, String text,String taskCode, Timestamp endTime){
        try {


            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testpdp@mail.com");
            message.setTo(sendingEmail);
            message.setSubject(subject);
            message.setText(text+"\n Vazifa muddati :  "+endTime+" gacha"+"\n Vazifa yakunlangandan so'ng ushbu link ustiga bosing : \n "+ "<a href='http://localhost:8080/api/task/endTask?taskCode="+taskCode);
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            return false;
        }
    }



    public ApiResponse endTask(String taskCode){
        Optional<Tasks> optionalTasks = taskRepository.findByTaskCode(taskCode);
        if (optionalTasks.isEmpty()){
            return new ApiResponse("Bunday vazifa mavjud emas  ",false);
        }
        Tasks task = optionalTasks.get();
        Optional<Worker> optionalWorker = workerRepository.findById(task.getGivenBy());
        Worker worker = optionalWorker.get();
        sendMessage(worker.getEmail(),taskCode,task.getWorkerEmail());
        return new ApiResponse("Habar jo'natildi  ",true);

    }
    public ApiResponse verifyTask(String taskCOde, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null
                && !(roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))
                || roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) )){
            return new ApiResponse("Vazifani worker tasdiqlay olmaydi ! ! !",false);
        }
        Optional<Tasks> byTaskCode = taskRepository.findByTaskCode(taskCOde);
        Tasks editingTask = byTaskCode.get();
        editingTask.setDone(true);
        taskRepository.save(editingTask);
        sendMFeedback(editingTask.getWorkerEmail(),new FeedbackText("Vazifa bajarildi "));
         return new ApiResponse("Vazifa yakunlanganlik tasdiqlandi",true);


    }


    public boolean sendMessage(String sendingEmail, String taskCode,String workerEmail){
        try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("TestPdp@gmail.com");
        message.setTo(sendingEmail);
        message.setSubject("Vazifa yakunlandi ");
        message.setText("Vazifa yakunlanganlik haqida habar \n Tasdiqlash uchun : "+"http://localhost:8080/api/task/verifyTask?taskCode="+taskCode+ "       link ustiga obsing" +"\n"+
                "Agar vazifa to'liq bajarilmagan bo'lsa  :"+ " http://localhost:8080/api/task/sendMessageToWorker?sendingEmail="+workerEmail +" link orqali ishchiga habar jo'nating" );
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            return false;
        }

    }


    public boolean sendMFeedback(String sendingEmail, FeedbackText text){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("TestPdp@gmail.com");
            message.setTo(sendingEmail);
            message.setSubject("Vazifa uchun Fedback  ");
            message.setText(text.getText());
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            return false;
        }

    }


}
