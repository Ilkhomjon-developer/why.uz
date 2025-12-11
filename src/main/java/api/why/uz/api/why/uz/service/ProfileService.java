package api.why.uz.api.why.uz.service;

import api.why.uz.api.why.uz.entity.ProfileEntity;
import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.exps.AppBadException;
import api.why.uz.api.why.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResourceBundleService resourceService;


    public ProfileEntity getById(Integer id, AppLanguage lang){
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(()-> new AppBadException(resourceService.getMessage("profile.not.found", lang)));
    }

   



}
