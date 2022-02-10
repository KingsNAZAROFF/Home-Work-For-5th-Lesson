package uz.pdp.homeworkfor5thlesson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.homeworkfor5thlesson.entity.Turniket;
import uz.pdp.homeworkfor5thlesson.payload.ApiResponse;
import uz.pdp.homeworkfor5thlesson.payload.TurniketDto;
import uz.pdp.homeworkfor5thlesson.service.TurniketService;

import java.util.List;

@RestController
@RequestMapping("/api/turniket")
public class TurniketController {

    @Autowired
    TurniketService turniketService;


    @PostMapping("/in")
    public ResponseEntity<?> getIn(@RequestBody TurniketDto turniketDto){
        ApiResponse response = turniketService.getIn(turniketDto.getId());
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @PatchMapping("/out")
    public ResponseEntity<?> getOut(@RequestBody TurniketDto turniketDto){
        ApiResponse response = turniketService.getOut(turniketDto.getId());
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
    @GetMapping("/byOneWorker/{email}")
    public ResponseEntity<?> getAllByOne(@PathVariable String email){

        List<Turniket> all = turniketService.getAllBYOne(email);
        return ResponseEntity.status(all!=null?200:409).body(all);
    }
}
