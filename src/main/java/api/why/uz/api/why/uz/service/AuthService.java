package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.dto.*;
import api.why.uz.api.why.uz.dto.auth.AuthDTO;
import api.why.uz.api.why.uz.dto.auth.RegistrationDTO;
import api.why.uz.api.why.uz.dto.auth.ResetPasswordDTO;
import api.why.uz.api.why.uz.dto.sms.SmsVerificationDTO;
import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.enums.GeneralStatus;
import api.why.uz.api.why.uz.enums.ProfileRole;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.repository.ProfileRepository;
import api.why.uz.api.why.uz.repository.ProfileRoleRepository;
import api.why.uz.api.why.uz.service.mail.EmailSendingService;
import api.why.uz.api.why.uz.service.mail.SmsHistoryService;
import api.why.uz.api.why.uz.service.mail.SmsSendingService;
import api.why.uz.api.why.uz.util.JwtUtil;
import api.why.uz.api.why.uz.util.UsernameValidation;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    @Autowired
    private ProfileRoleService profileRoleService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private ResourceBundleService resourceService;
    @Autowired
    private SmsSendingService smsSendingService;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private AttachService attachService;


    public AppResponseDTO<String> registration(RegistrationDTO dto, AppLanguage lang) {

       Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

       if(optional.isPresent()) {
           ProfileEntity profile = optional.get();

           if (profile.getStatus().equals(GeneralStatus.REGISTERING)){

               profileRoleService.deleteRole(profile.getId());
               profileRepository.delete(profile);

           }else {

               throw new AppBadException(resourceService.getMessage("email.phone.exists", lang));
           }
       }

       ProfileEntity entity = new ProfileEntity();
       entity.setName(dto.name());
       entity.setUsername(dto.username());
       entity.setPassword(bCryptPasswordEncoder.encode(dto.password()));
       entity.setStatus(GeneralStatus.REGISTERING);
       entity.setVisible(true);
       entity.setCreatedDate(LocalDateTime.now());
       profileRepository.save(entity);

       profileRoleService.create(entity.getId(), ProfileRole.ROLE_USER);

       if(UsernameValidation.isPhone(dto.username())){
           smsSendingService.sendRegistrationSms(dto.username());
           return new AppResponseDTO<>(resourceService.getMessage("sms.activation.code", lang));
       } else if (UsernameValidation.isEmail(dto.username())) {

           emailSendingService.sendRegistrationEmail(dto.username(), entity.getId(), lang);
           return new AppResponseDTO<>(resourceService.getMessage("email.activation.link", lang));
       }

        throw new AppBadException(resourceService.getMessage("email.or.username.incorrect", lang));
    }


    public String registrationEmailVerification(String token, AppLanguage lang) {

        Integer profileId = JwtUtil.decodeRegVerToken(token);

        try {

            ProfileEntity profile = profileService.getById(profileId, lang);

            if (profile.getStatus().equals(GeneralStatus.REGISTERING)){
                profileRepository.updateStatus(profileId, GeneralStatus.ACTIVE);
                return resourceService.getMessage("email.verification", lang);
            }

        }catch (JwtException ignored){

        }
        throw new AppBadException(resourceService.getMessage("email.verification.failed", lang));

    }


    public ProfileDTO login(AuthDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

        if(optional.isEmpty()){
            throw new AppBadException(resourceService.getMessage("email.or.username.incorrect", lang));
        }


        if(!bCryptPasswordEncoder.matches(dto.password(),optional.get().getPassword())){
            throw new AppBadException(resourceService.getMessage("email.or.username.incorrect", lang));
        }

        if(!optional.get().getStatus().equals(GeneralStatus.ACTIVE)){
            throw new AppBadException(resourceService.getMessage("user.not.active", lang));
        }

        return getLogInResponse(optional);
    }

    public ProfileDTO registrationSmsVerification(SmsVerificationDTO dto, AppLanguage lang) {

        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

        if (optional.isEmpty()) {
            throw new AppBadException(resourceService.getMessage("profile.not.found", lang));
        }

        if(!optional.get().getStatus().equals(GeneralStatus.REGISTERING)){
            throw new AppBadException(resourceService.getMessage("profile.in.wrong.state", lang));
        }

        if(UsernameValidation.isPhone(dto.username()) && smsHistoryService.isSmsSendToAccount(dto.username(), dto.code())){

            profileRepository.updateStatus(optional.get().getId(), GeneralStatus.ACTIVE);
            return getLogInResponse(optional);
        }
        throw new AppBadException( resourceService.getMessage("sms.verification.failed", lang));

    }

    public AppResponseDTO<String> resetPassword(ResetPasswordDTO dto, AppLanguage lang) {

        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

        if(optional.isEmpty()){
            throw new AppBadException(resourceService.getMessage("profile.not.found", lang));
        }
        if(!optional.get().getStatus().equals(GeneralStatus.ACTIVE)){
            throw new AppBadException(resourceService.getMessage("profile.in.wrong.status", lang));
        }

        ProfileEntity entity = optional.get();
        if(UsernameValidation.isPhone(dto.username())){
            smsSendingService.sendResetPasswordSms(dto.username(), lang);

        } else if (UsernameValidation.isEmail(dto.username())) {

            emailSendingService.sendResetPasswordEmail(dto.username(), lang);

        }
        return new AppResponseDTO(resourceService.getMessage("sms.activation.code", lang));
    }

    private ProfileDTO getLogInResponse(Optional<ProfileEntity> optional) {
        if (optional.isEmpty()) throw new RuntimeException("User not found");
        ProfileDTO response = new ProfileDTO();
        response.setName(optional.get().getName());
        response.setUsername(optional.get().getUsername());
        response.setRoleList(profileRoleRepository.getAllRolesListByProfileId(optional.get().getId()));
        response.setToken(JwtUtil.encode(optional.get().getUsername(), optional.get().getId(), response.getRoleList()));
        response.setPhoto(attachService.attachDto(optional.get().getPhotoId()));
        return response;
    }
}
