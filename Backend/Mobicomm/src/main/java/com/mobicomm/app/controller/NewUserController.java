package com.mobicomm.app.controller;

import com.mobicomm.app.model.NewUser;
import com.mobicomm.app.service.NewUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")

@RestController
@RequestMapping("/api/newusers")
@RequiredArgsConstructor
public class NewUserController {

    private final NewUserService newUserService;

    @GetMapping("/{id}")
    public ResponseEntity<NewUser> getNewUserById(@PathVariable String id) {
        return newUserService.getNewUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<NewUser> createNewUser(@RequestBody NewUser newUser) {
        return ResponseEntity.ok(newUserService.saveNewUser(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewUser> updateNewUser(@PathVariable String id, @RequestBody NewUser updatedNewUser) {
        return ResponseEntity.ok(newUserService.updateNewUser(id, updatedNewUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewUser(@PathVariable String id) {
        newUserService.deleteNewUser(id);
        return ResponseEntity.noContent().build();
    }
}
