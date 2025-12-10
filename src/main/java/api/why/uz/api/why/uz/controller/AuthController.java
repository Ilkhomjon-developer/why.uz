package api.why.uz.api.why.uz.controller;

import api.why.uz.api.why.uz.dto.AppResponseDTO;
import api.why.uz.api.why.uz.dto.AuthDTO;
import api.why.uz.api.why.uz.dto.ProfileDTO;
import api.why.uz.api.why.uz.dto.RegistrationDTO;
import api.why.uz.api.why.uz.service.AuthService;
import jakarta.servlet.Servlet;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<AppResponseDTO<String>> registration(@Valid @RequestBody RegistrationDTO dto ) {

        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/registration/verification/{token}")
    public ResponseEntity<String> regVerification(@PathVariable("token") String token ) {

        return ResponseEntity.ok(authService.regVerification(token));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
