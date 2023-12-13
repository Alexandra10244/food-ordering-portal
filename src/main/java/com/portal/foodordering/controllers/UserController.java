package com.portal.foodordering.controllers;

import com.portal.foodordering.models.dtos.EditUserDTO;
import com.portal.foodordering.models.dtos.UserDTO;
import com.portal.foodordering.serivces.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,@Valid @RequestBody EditUserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @GetMapping("/id")
    public ResponseEntity<UserDTO> findUserById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> findUserByMail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }
}
