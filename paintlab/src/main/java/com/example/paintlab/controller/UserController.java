package com.example.paintlab.controller;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.dto.LoginResponseDTO;
import com.example.paintlab.dto.RegisterDTO;
import com.example.paintlab.dto.UserDTO;
import com.example.paintlab.repositories.UserRepository;
import com.example.paintlab.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    //endpoint cadastro
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {

        try{

        if(this.repository.findByEmail(data.email())!=null) return ResponseEntity.badRequest().build();

        String encryptedPassword= new BCryptPasswordEncoder().encode(data.password());
        User newUser= new User (data.name(),data.email(),encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");

    } catch (Exception e) {
        e.printStackTrace(); // Mostra o erro no console
        return ResponseEntity.badRequest().body("Erro ao cadastrar: " + e.getMessage());
    }
}

    // endpoint login
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDTO data) {
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth=this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(403).body("Erro no login: " + e.getMessage());
    }

}
}
