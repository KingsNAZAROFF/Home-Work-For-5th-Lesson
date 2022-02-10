package uz.pdp.homeworkfor5thlesson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.payload.SalaryDto;
import uz.pdp.homeworkfor5thlesson.service.SalaryService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @PostMapping("/giveSalary")
    public ResponseEntity<?> giveSalary(HttpServletRequest request, @RequestBody SalaryDto salaryDto){
        ApiResponse response = salaryService.giveSalary(request, salaryDto);
        return ResponseEntity.status(response.isSuccess()?200:403).body(response);
    }
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getOne(HttpServletRequest request,@PathVariable String email){
        ApiResponse response = salaryService.getAll(request, email);
        return ResponseEntity.status(response.isSuccess()?200:403).body(response);
    }
}
