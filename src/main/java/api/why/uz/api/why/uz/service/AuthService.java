package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.dto.AppResponseDTO;
import api.why.uz.api.why.uz.dto.AuthDTO;
import api.why.uz.api.why.uz.dto.ProfileDTO;
import api.why.uz.api.why.uz.dto.RegistrationDTO;
import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.GeneralStatus;
import api.why.uz.api.why.uz.enums.ProfileRole;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.repository.ProfileRepository;
import api.why.uz.api.why.uz.repository.ProfileRoleRepository;
import api.why.uz.api.why.uz.util.JwtUtil;
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


    public AppResponseDTO<String> registration(RegistrationDTO dto) {

       Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

       if(optional.isPresent()) {
           ProfileEntity profile = optional.get();

           if (profile.getStatus().equals(GeneralStatus.REGISTERING)){

               profileRoleService.deleteRole(profile.getId());
               profileRepository.delete(profile);

           }else {

               throw new AppBadException("User already exists");
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

        emailSendingService.sendRegistrationEmail(dto.username(), entity.getId());
       return new AppResponseDTO<>("Successfully registered");

    }


    public String regVerification(String token) {

        Integer profileId = JwtUtil.decodeRegVerToken(token);

        try {

            ProfileEntity profile = profileService.getById(profileId);

            if (profile.getStatus().equals(GeneralStatus.REGISTERING)){
                profileRepository.updateStatus(profileId, GeneralStatus.ACTIVE);
                return "Successfully verified";
            }

        }catch (JwtException ignored){

        }
        throw new AppBadException("Verification failed");

    }


    public ProfileDTO login(AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.username());

        if(optional.isEmpty()){
            throw new AppBadException("Username or Password is incorrect");
        }


        if(!bCryptPasswordEncoder.matches(dto.password(),optional.get().getPassword())){
            throw new AppBadException("Username or Password is incorrect");
        }

        if(!optional.get().getStatus().equals(GeneralStatus.ACTIVE)){
            throw new AppBadException("User is not active");
        }

        ProfileDTO response = new ProfileDTO();
        response.setName(optional.get().getName());
        response.setUsername(optional.get().getUsername());
        response.setRoleList(profileRoleRepository.getAllRolesListByProfileId(optional.get().getId()));
        response.setToken(JwtUtil.encode(optional.get().getUsername(), optional.get().getId(), response.getRoleList()));


        return response;
    }
}
