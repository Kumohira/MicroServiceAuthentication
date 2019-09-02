package org.sid.Service;

import org.sid.Dao.AppRoleRepository;
import org.sid.Dao.AppUserRepository;
import org.sid.Dao.ModuleFeatureRepository;
import org.sid.Entities.AppRole;
import org.sid.Entities.AppUser;
import org.sid.Entities.ModuleFeature;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private ModuleFeatureRepository moduleFeatureRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, ModuleFeatureRepository moduleFeatureRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.moduleFeatureRepository = moduleFeatureRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {
        AppUser user = appUserRepository.findByUsername(username);
        if (user != null) throw new RuntimeException("User already exists");
        if (!password.equals(confirmedPassword)) throw new RuntimeException("Please confirm your password !");
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setActived(true);
        appUserRepository.save(appUser);
        addRoleToUser(username, "USER");
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(rolename);
        appUser.getRoles().add(appRole);
    }

    @Override
    public ModuleFeature saveModuleFeature(ModuleFeature moduleFeature) {
        return moduleFeatureRepository.save(moduleFeature);
    }

    @Override
    public void addModuleFeatureToRolee(ModuleFeature moduleFeature, String roleName) {
        ModuleFeature mf = moduleFeatureRepository.findByModuleAndFeature(moduleFeature.getModule(), moduleFeature.getFeature());
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appRole.getModuleFeatures().add(mf);
    }

    @Override
    public void addModuleFeatureToRole(String moduleName, String featureName, String roleName) {
        ModuleFeature mf = moduleFeatureRepository.findByModuleAndFeature(moduleName, featureName);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appRole.getModuleFeatures().add(mf);
        mf.setRole(appRole);
    }

    @Override
    public List<ModuleFeature> getModuleFeatureByRole(String roleName) {
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        return moduleFeatureRepository.findAllByRole(appRole);
    }

    @Override
    public List<String> getAllModuleByRole(String roleName) {
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        return moduleFeatureRepository.findAllModuleByRole(appRole.getId());
    }

    @Override
    public List<String> getAllFeatureByModuleAndRole(String moduleName, String roleName) {
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        return moduleFeatureRepository.findAllFeatureByModuleAndRole(moduleName, appRole.getId());
    }
}
