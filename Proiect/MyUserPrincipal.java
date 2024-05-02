package com.ESN_Poliapp.Proiect;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class MyUserPrincipal implements UserDetails {
    private VolunteerUser volunteerUser;
    private AdminUser adminUser;

    public MyUserPrincipal(VolunteerUser volunteerUser) {
        this.volunteerUser = volunteerUser;
    }

    public MyUserPrincipal(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (volunteerUser != null) {
            return volunteerUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
        } else if (adminUser != null) {
            // Returnează rolurile pentru utilizatorii de tip admin
            return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        if (volunteerUser != null) {
            return volunteerUser.getPassword();
        } else if (adminUser != null) {
            return adminUser.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (volunteerUser != null) {
            return volunteerUser.getUsername();
        } else if (adminUser != null) {
            return adminUser.getUsername();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (volunteerUser != null) {
            return volunteerUser.isAccountNonExpired();
        } else if (adminUser != null) {
            return adminUser.isAccountNonExpired();
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (volunteerUser != null) {
            return volunteerUser.isAccountNonLocked();
        } else if (adminUser != null) {
            return adminUser.isAccountNonLocked();
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (volunteerUser != null) {
            return volunteerUser.isCredentialsNonExpired();
        } else if (adminUser != null) {
            return adminUser.isCredentialsNonExpired();
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (volunteerUser != null) {
            return volunteerUser.isEnabled();
        } else if (adminUser != null) {
            return adminUser.isEnabled();
        }
        return false;
    }
}
