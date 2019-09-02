package org.sid.Entities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 510L;
    private static final Log logger = LogFactory.getLog(User.class);
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private String username;
    private String password;
    private Set<GrantedAuthority> roles;
    private Set<GrantedAuthority> modules;
    private Set<GrantedAuthority> features;

    public CustomUserDetails(String username, String password, Set<GrantedAuthority> roles, Set<GrantedAuthority> modules, Set<GrantedAuthority> features) {
        this(username, password, true, true, true, true, roles, modules, features);
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> roles, Set<GrantedAuthority> modules, Set<GrantedAuthority> features) {
        if (username != null && !"".equals(username) && password != null) {
            this.username = username;
            this.password = password;
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.roles = roles;
            this.modules = modules;
            this.features = features;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");
        if (!this.roles.isEmpty()) {
            sb.append("Granted Roles: ");
            boolean first1 = true;
            Iterator var3 = this.roles.iterator();

            while (var3.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority) var3.next();
                if (!first1) {
                    sb.append(",");
                }

                first1 = false;
                sb.append(auth);
            }
        } else {
            sb.append("Not granted any roles");
        }

        if (!this.modules.isEmpty()) {
            sb.append("Granted Modules: ");
            boolean first2 = true;
            Iterator var4 = this.modules.iterator();

            while (var4.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority) var4.next();
                if (!first2) {
                    sb.append(",");
                }

                first2 = false;
                sb.append(auth);
            }
        } else {
            sb.append("Not granted any modules");
        }

        if (!this.features.isEmpty()) {
            sb.append("Granted Features: ");
            boolean first3 = true;
            Iterator var5 = this.features.iterator();

            while (var5.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority) var5.next();
                if (!first3) {
                    sb.append(",");
                }

                first3 = false;
                sb.append(auth);
            }
        } else {
            sb.append("Not granted any features");
        }

        return sb.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Set<GrantedAuthority> getRoles() {
        return this.roles;
    }

    public Set<GrantedAuthority> getModules() {
        return this.modules;
    }

    public Set<GrantedAuthority> getFeatures() {
        return this.features;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
