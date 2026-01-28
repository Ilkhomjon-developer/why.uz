    package api.why.uz.api.why.uz.controller;

    import api.why.uz.api.why.uz.dto.*;
    import api.why.uz.api.why.uz.enums.AppLanguage;
    import api.why.uz.api.why.uz.service.ProfileService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/profile")
    @RequiredArgsConstructor
    public class ProfileController {

        private final ProfileService profileService;


        @PutMapping("/update-profile-name")
        public ResponseEntity<AppResponseDTO<String>> updateProfile(@Valid @RequestBody UpdateProfileDetailsDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
            return ResponseEntity.ok(profileService.updateProfile(dto, lang));
        }

        @PutMapping("/update-password")
        public ResponseEntity<AppResponseDTO<String>> updatePassword(@Valid @RequestBody UpdatePasswordDetailDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
            return ResponseEntity.ok(profileService.updatePassword(dto, lang));
        }

        @PutMapping("update-username")
        public ResponseEntity<String> updateUsername(@Valid @RequestBody UpdateProfileUsernameDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
            return ResponseEntity.ok(profileService.updateUsername(dto, lang));
        }

        @PutMapping(value = "/update-profile-photo")
        public ResponseEntity<String> updateProfilePhoto(@Valid @RequestBody UpdateProfilePhoto dto, @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
            return ResponseEntity.ok(profileService.updateProfilePhoto(dto, lang));
        }
    }
