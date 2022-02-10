package uz.pdp.homeworkfor5thlesson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.payload.WorkerDtoForSend;
import uz.pdp.homeworkfor5thlesson.service.GetWorkersService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/get")
public class GetController {


    @Autowired
    GetWorkersService getWorkersService;

    @GetMapping("/all")
    public List<Worker> getAll(HttpServletRequest request){
        List<Worker> all = getWorkersService.getAll(request);
        return all;
    }

    @GetMapping("/one/{email}")
    public WorkerDtoForSend getOne(@PathVariable String email,HttpServletRequest request){
        WorkerDtoForSend one = getWorkersService.getOne(email, request);
        return one;
    }
}
