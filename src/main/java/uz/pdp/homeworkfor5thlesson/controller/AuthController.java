package uz.pdp.homeworkfor5thlesson.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.homeworkfor5thlesson.payload.*;
import uz.pdp.homeworkfor5thlesson.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    @PostMapping("/regDirector")
    public ResponseEntity<?> addDirector(@RequestBody DirectorDto directorDto){
        ApiResponse response = authService.registerDirector(directorDto);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }



    @PostMapping("/registerManager")
    public ResponseEntity<?> registerManager(@RequestBody RegisterDto registerDto, HttpServletRequest request){

        ApiResponse response = authService.registerManager(registerDto,request);
        return ResponseEntity.status(response.isSuccess()?201:409).body(response);
    }
    @PostMapping("/registerWorker")
    public ResponseEntity<?> registerWorker(@RequestBody RegisterDto registerDto, HttpServletRequest request){
        ApiResponse response = authService.registerWorker(registerDto, request);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @GetMapping("/veirfyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email, @RequestBody PasswordAdd passwordAdd){
        ApiResponse response =  authService.verifyEmail(emailCode,email,passwordAdd);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse response = authService.login(loginDto);
        return ResponseEntity.status(response.isSuccess()?200:401).body(response);
    }
}
