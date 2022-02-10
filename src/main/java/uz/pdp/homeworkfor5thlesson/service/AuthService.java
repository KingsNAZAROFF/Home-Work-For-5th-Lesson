package uz.pdp.homeworkfor5thlesson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.homeworkfor5thlesson.entity.Worker;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;
import uz.pdp.homeworkfor5thlesson.payload.*;
import uz.pdp.homeworkfor5thlesson.repository.RoleRepository;

import uz.pdp.homeworkfor5thlesson.repository.WorkerRepository;
import uz.pdp.homeworkfor5thlesson.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerDirector(DirectorDto directorDto){
        boolean byEmail = workerRepository.existsByEmail(directorDto.getEmail());
        if (byEmail){
            return new ApiResponse("Bunday email ro'yxatdan o'tgan",false);
        }

        boolean byRoles = workerRepository.existsByRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
        if (byRoles){
            return new ApiResponse("DIRECTOR ro'yxatdan o'tgan ",false);
        }

        Worker worker = new Worker();
        worker.setFirstName(directorDto.getFirstName());
        worker.setLastName(directorDto.getLastName());
        worker.setEmail(directorDto.getEmail());
        worker.setPassword(directorDto.getPassword());
        worker.setEnabled(true);
        worker.setEmailCode(null);
        workerRepository.save(worker);
        return new ApiResponse("DIRECTOR HR OFFICE DAN REGISTRATSIYA QILINDI",true);
    }
    public ApiResponse registerManager(RegisterDto registerDto, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null && !roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))){
            return new ApiResponse("MANAGER ni faqat DIRECTOR qo'sha oladi",false);
        }

        boolean byEmail = workerRepository.existsByEmail(registerDto.getEmail());
        if (byEmail){
            return new ApiResponse("Bunday email ro'yxatdan o'tgan",false);
        }
        Worker worker = new Worker();
        worker.setFirstName(registerDto.getFirstName());
        worker.setLastName(registerDto.getLastName());
        worker.setEmail(registerDto.getEmail());
        worker.setEmailCode(UUID.randomUUID().toString());
        worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));
        workerRepository.save(worker);
        sendEMail(worker.getEmail(), worker.getEmailCode());



        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tkazildi, MAANGER accountiga tasdiqlash kodi yuborildi",true);



    }

    public ApiResponse registerWorker(RegisterDto registerDto,HttpServletRequest request){

        String token = request.getHeader("Authorization");
        token=token.substring(7);

        Object roleFromToken = jwtProvider.getRoleFromToken(token);

        if (roleFromToken==null
                && !(roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)))
                || roleFromToken.equals(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) )){
            return new ApiResponse("MANAGER ni faqat DIRECTOR qo'sha oladi",false);
        }

        boolean byEmail = workerRepository.existsByEmail(registerDto.getEmail());
        if (byEmail){
            return new ApiResponse("Bunday email ro'yxatdan o'tgan",false);
        }
        Worker worker = new Worker();
        worker.setFirstName(registerDto.getFirstName());
        worker.setLastName(registerDto.getLastName());
        worker.setEmail(registerDto.getEmail());
        worker.setEmailCode(UUID.randomUUID().toString());
        worker.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_WORKER)));
        workerRepository.save(worker);
        sendEMail(worker.getEmail(), worker.getEmailCode());



        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tkazildi, MAANGER accountiga tasdiqlash kodi yuborildi",true);



    }









    public Boolean sendEMail(String sendingEmail,String emailCode){
        try {


            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testpdp@mail.com");
            message.setTo(sendingEmail);
            message.setSubject("Accountni tasdiqlash");
            message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'> Tasdiqlang  </a>");
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email, PasswordAdd passwordAdd) {
        Optional<Worker> optionalUser = workerRepository.findByEmailAndEmailCode(email, emailCode);
        if (!passwordAdd.getPassword().equals(passwordAdd.getPasswordCheck())){
            return new ApiResponse("Passwordlar bir biriga mos emas",false);
        }
        if (optionalUser.isPresent()){
            Worker editingWorker = optionalUser.get();
            editingWorker.setPassword(passwordEncoder.encode(passwordAdd.getPassword()));
            editingWorker.setEnabled(true);
            editingWorker.setEmailCode(null);
            workerRepository.save(editingWorker);
            return new ApiResponse("Accoun tasdiqlandi",true);
        }
        return new ApiResponse("Accoun allaqachon tasdiqlangan",false);


    }


    public ApiResponse login(LoginDto loginDto) {
        try {


            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));
            Worker worker = (Worker) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), worker.getRoles());

            return new ApiResponse("Token = ",true,token);
        }catch (BadCredentialsException badCredentialsException){
            return new ApiResponse("Parol yoki login xato",false);

        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> optionalUser = userRepository.findByEmail(username);
//        if (optionalUser.isPresent()){
//            return optionalUser.get();
//        }
//        throw  new UsernameNotFoundException(username +" topilmadi");

        return workerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "user topilmadi ! ! !"));
    }
}
