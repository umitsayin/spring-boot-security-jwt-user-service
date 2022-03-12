package com.springsecurity.userservice.controller;

import com.springsecurity.userservice.busniess.UserService;
import com.springsecurity.userservice.entities.User;
import com.springsecurity.userservice.results.DataResult;
import com.springsecurity.userservice.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/role/save")
    public ResponseEntity<Result> saveRole(@RequestParam String roleName){
        return ResponseEntity.ok().body(this.userService.saveRole(roleName));
    }

    @GetMapping("/users")
    public ResponseEntity<DataResult<List<User>>> getUsers(){
        return ResponseEntity.ok().body(this.userService.getUsers());
    }

    @PostMapping("/addRoleToUser")
    public ResponseEntity<Result> addRoleToUser(@RequestParam String username,@RequestParam String roleName){
        return ResponseEntity.ok().body(this.userService.addRoleToUser(username,roleName));
    }

    @PostMapping("/deleteRoleToUser")
    public ResponseEntity<Result> deleteRoleToUser(@RequestParam String username,@RequestParam String roleName){
        return ResponseEntity.ok().body(this.userService.deleteRoleToUser(username,roleName));
    }

    @PostMapping("/delete")
    public ResponseEntity<Result> deleteUser(@RequestParam String username){
        return ResponseEntity.ok().body(this.userService.deleteUser(username));
    }
}
