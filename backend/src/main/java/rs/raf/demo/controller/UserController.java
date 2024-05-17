package rs.raf.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.domain.dto.user.UserCreateDto;
import rs.raf.demo.domain.dto.user.UserUpdateDto;
import rs.raf.demo.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto){
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        return  ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable("id") Long id, @RequestBody UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUserById(id, userUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
        userService.deleteUserById(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "ok");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}
