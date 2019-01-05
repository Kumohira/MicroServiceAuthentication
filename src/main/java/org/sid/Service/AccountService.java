package org.sid.Service;

import org.sid.Entities.AppRole;
import org.sid.Entities.AppUser;

public interface AccountService {
    public AppUser saveUser(String username, String password, String confirmedPassword);

    public AppRole saveRole(AppRole role);

    public AppUser loadUserByUsername(String username);

    public void addRoleToUser(String username, String rolename);
}
