package uz.pdp.homeworkfor5thlesson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.homeworkfor5thlesson.payload.FeedbackText;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.payload.TaskDto;
import uz.pdp.homeworkfor5thlesson.service.TaskService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/giveToWorker")
    public ResponseEntity<?> giveTaskToWorker(TaskDto taskDto , HttpServletRequest request){
        ApiResponse response = taskService.giveTaskToWorker(taskDto, request);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
    @PostMapping("/giveToManager")
    public ResponseEntity<?> giveTaskToManger(TaskDto taskDto , HttpServletRequest request){
        ApiResponse response = taskService.giveTaskToManager(taskDto, request);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
    @GetMapping("/endTask")
    public ResponseEntity<?> endTask(@RequestParam String taskCode){
        ApiResponse response = taskService.endTask(taskCode);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }


    @PatchMapping("/verifyTask")
    public ResponseEntity<?> verifyTask(@RequestParam String verifyTask, HttpServletRequest request){
        ApiResponse response = taskService.verifyTask(verifyTask, request);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PostMapping("/sendMessageToWorker")
    public ResponseEntity<?> sendFeedback(@RequestParam String sendingEmail, FeedbackText text){
        boolean feedback = taskService.sendMFeedback(sendingEmail, text);
        return ResponseEntity.status(feedback?200:409).body(feedback);
    }

}
