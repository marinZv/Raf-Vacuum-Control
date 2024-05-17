package rs.raf.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.domain.dto.vacuum.VacuumScheduleDto;
import rs.raf.demo.domain.entities.vacuum.VacuumStatus;
import rs.raf.demo.domain.exceptions.VacuumException;
import rs.raf.demo.services.VacuumService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/vacuums")
public class VacuumController {

    private final VacuumService vacuumService;

    @Autowired
    public VacuumController(VacuumService vacuumService) {
        this.vacuumService = vacuumService;
    }

    @GetMapping
    public ResponseEntity<?> searchVacuums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<VacuumStatus> statusList,
            @RequestParam(required = false) Long dateFrom,
            @RequestParam(required = false) Long dateTo
            ){


        return ResponseEntity.ok(vacuumService.searchVacuums(name, statusList, dateFrom, dateTo));
    }

    @PostMapping("/add/{name}")
    public ResponseEntity<?> addVacum(@PathVariable("name") String name){
        return ResponseEntity.ok(vacuumService.addVacuum(name));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeVacuum(@PathVariable("id") Long id){
        return ResponseEntity.ok(vacuumService.removeVacuum(id));
    }

    @PostMapping("/start/{id}")
    public ResponseEntity<?> startVacuum(@PathVariable("id") Long id){
        vacuumService.startVacuum(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "action accepted");

        return ResponseEntity.status(HttpStatus.OK).body(body);

    }


    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stopVacuum(@PathVariable("id") Long id){
        vacuumService.stopVacuum(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "action accepted");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/discharge/{id}")
    public ResponseEntity<?> dischargeVacuum(@PathVariable("id") Long id){
        vacuumService.dischargeVacuum(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "action accepted");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleTask(@RequestBody VacuumScheduleDto vacuumScheduleDto){
//        vacuumService.addScheduleTaskForVacuum(vacuumScheduleDto);

        vacuumService.scheduleTaskForVacuum(vacuumScheduleDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/errors")
    public ResponseEntity<?> getError(){
        return ResponseEntity.ok().body(vacuumService.getErrors());
    }

    @ExceptionHandler(VacuumException.class)
    public ResponseEntity<?> handleVacuumException(VacuumException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
