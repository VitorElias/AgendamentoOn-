package com.AgendamentoOn.Security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.AgendamentoOn.Enum.Role;
import com.AgendamentoOn.Model.Usuario;

public class CustomUserDetails implements UserDetails {
    private final String username;
    private final Role role;

    public CustomUserDetails(Usuario usuario) {
        this.username = usuario.getEmail();
        this.role = usuario.getRole();  // Assumindo que o Role está corretamente mapeado no Usuario
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // A role é convertida para ROLE_ prefixada automaticamente pelo Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return ""; // Aqui deveria vir a senha, mas isso depende do seu sistema
    }

    @Override
    public String getUsername() {
        return username;
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
