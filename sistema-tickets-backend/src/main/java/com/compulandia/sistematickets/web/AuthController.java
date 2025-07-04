package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compulandia.sistematickets.entities.Usuario;
import com.compulandia.sistematickets.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario loginRequest){
        String username = loginRequest.getUsername() == null ? null : loginRequest.getUsername().trim();
        String password = loginRequest.getPassword() == null ? null : loginRequest.getPassword().trim();
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario != null && usuario.getPassword().equals(password)){
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<Usuario> getProfile(@PathVariable String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }
}
