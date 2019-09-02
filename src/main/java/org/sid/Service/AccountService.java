package org.sid.Service;

import org.sid.Entities.AppRole;
import org.sid.Entities.AppUser;
import org.sid.Entities.ModuleFeature;

import java.util.List;

public interface AccountService {
    public AppUser saveUser(String username, String password, String confirmedPassword);

    public AppRole saveRole(AppRole role);

    public AppUser loadUserByUsername(String username);

    public void addRoleToUser(String username, String rolename);

    public ModuleFeature saveModuleFeature(ModuleFeature moduleFeature);

    public void addModuleFeatureToRolee(ModuleFeature moduleFeature, String roleName);

    public void addModuleFeatureToRole(String moduleName, String featureName, String roleName);

    public List<ModuleFeature> getModuleFeatureByRole(String roleName);

    public List<String> getAllModuleByRole(String roleName);

    public List<String> getAllFeatureByModuleAndRole(String moduleName, String roleName);
}
