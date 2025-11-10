package com.example.SistemaDePromociones.security;

import com.example.SistemaDePromociones.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetails para Spring Security
 */
public class CustomUserDetails implements UserDetails {
    
    private final Usuario usuario;
    
    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapear el código de rol a un authority
        String role = getRoleFromCodigo(usuario.getCodigoRol());
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
    
    private String getRoleFromCodigo(Long codigoRol) {
        // 1 = ADMIN, 2 = RESTAURANT, 3 = DELIVERY, 4 = CUSTOMER
        return switch (codigoRol.intValue()) {
            case 1 -> "ADMIN";
            case 2 -> "RESTAURANT";
            case 3 -> "DELIVERY";
            case 4 -> "CUSTOMER";
            default -> "CUSTOMER";
        };
    }
    
    @Override
    public String getPassword() {
        return usuario.getContrasena();
    }
    
    @Override
    public String getUsername() {
        return usuario.getCorreoElectronico();
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
        // Si el estado es null, considerar como habilitado por defecto
        return usuario.getEstado() != null ? usuario.getEstado() : true;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Long getCodigoRol() {
        return usuario.getCodigoRol();
    }
}
