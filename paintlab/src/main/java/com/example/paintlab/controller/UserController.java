//package com.example.paintlab.controller;
//
//import com.example.paintlab.domain.user.User;
//import com.example.paintlab.dto.UserDTO;
//import com.example.paintlab.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    //endpoint cadastro
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
//        try {
//            userService.register(userDTO);
//            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // endpoint login
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
//        try {
//            User user = userService.login(userDTO);
//            return ResponseEntity.ok("Login realizado com sucesso! Seja bem-vindo, " + user.getName());
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
