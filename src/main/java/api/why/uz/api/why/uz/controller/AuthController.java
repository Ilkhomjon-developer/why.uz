package api.why.uz.api.why.uz.controller;

import api.why.uz.api.why.uz.dto.*;
import api.why.uz.api.why.uz.dto.auth.AuthDTO;
import api.why.uz.api.why.uz.dto.auth.RegistrationDTO;
import api.why.uz.api.why.uz.dto.auth.ResetPasswordDTO;
import api.why.uz.api.why.uz.dto.sms.SmsVerificationDTO;
import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<AppResponseDTO<String>> registration(@Valid @RequestBody RegistrationDTO dto,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok(authService.registration(dto, lang));
    }

    @GetMapping("/registration/email-verification/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable String token ,
                                                    @RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok(authService.registrationEmailVerification(token, lang));
    }

    @PostMapping("/registration/sms-verification")
    public ResponseEntity<ProfileDTO> smsVerification(@RequestBody SmsVerificationDTO dto,
                                                  @RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok(authService.registrationSmsVerification(dto, lang));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO dto,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok(authService.login(dto, lang));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AppResponseDTO<String>> resetPassword(@RequestBody ResetPasswordDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok(authService.resetPassword(dto, lang));
    }
}
