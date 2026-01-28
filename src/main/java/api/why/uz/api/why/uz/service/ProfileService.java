package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.dto.*;
import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.enums.SmsType;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.repository.ProfileRepository;
import api.why.uz.api.why.uz.service.mail.EmailSendingService;
import api.why.uz.api.why.uz.service.mail.SmsHistoryService;
import api.why.uz.api.why.uz.service.mail.SmsSendingService;
import api.why.uz.api.why.uz.util.RandomNumberGenerator;
import api.why.uz.api.why.uz.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSendingService emailSendingService;
    private final SmsHistoryService smsHistoryService;


    public AppResponseDTO<String> updateProfile(UpdateProfileDetailsDTO dto, AppLanguage lang) {
        profileRepository.update(dto.name(), SpringSecurityUtil.getCurrentUserId());
        return new AppResponseDTO<>(resourceService.getMessage("profile.detail.successfully.updated", lang));
    }

    public AppResponseDTO<String> updatePassword(UpdatePasswordDetailDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findById(SpringSecurityUtil.getCurrentUserId());
        if(optional.isEmpty()){
            throw new AppBadException(resourceService.getMessage("profile.not.found", lang));
        }
        if (!bCryptPasswordEncoder.matches(dto.oldPassword(), optional.get().getPassword())) {
            throw new AppBadException(resourceService.getMessage("old.password.incorrect", lang));
        }
        profileRepository.updatePassword(optional.get().getId(), bCryptPasswordEncoder.encode(dto.newPassword()));
        return new AppResponseDTO<>(resourceService.getMessage("password.successfully.updated", lang));
    }

    public ProfileEntity getById(Integer id, AppLanguage lang){
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(()-> new AppBadException(resourceService.getMessage("profile.not.found", lang)));
    }

    public String updateUsername(@Valid UpdateProfileUsernameDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> emailOptional = profileRepository.findByUsernameAndVisibleTrue(dto.username());
        if (emailOptional.isPresent()){
            throw new AppBadException(resourceService.getMessage("username.already.exists", lang));
        }
        int code = RandomNumberGenerator.generate();
        emailSendingService.sendUpdateEmailCode(dto.username(), code,lang, SmsType.CHANGE_USERNAME_CONFIRMATION_CODE);
        smsHistoryService.save(dto.username(),code,"Change username confirmation code",SmsType.CHANGE_USERNAME_CONFIRMATION_CODE);
        return new AppResponseDTO<>(resourceService.getMessage("email.update.code.sent", lang)).getData();
    }

    public String updateProfilePhoto(@Valid UpdateProfilePhoto dto, AppLanguage lang) {
        profileRepository.updatePhoto(SpringSecurityUtil.getCurrentUserId(), dto.photoId());
        return new AppResponseDTO<>(resourceService.getMessage("photo.updated",lang)).getData();
    }
}
