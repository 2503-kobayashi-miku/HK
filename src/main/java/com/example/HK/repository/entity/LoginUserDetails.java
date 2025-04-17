package com.example.HK.repository.entity;

import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Table(name = "users")
public class LoginUserDetails implements UserDetails {
    private User user;
    private Collection<GrantedAuthority> authorities;

    public LoginUserDetails(User user) {
        this.user = user;
    }

    // ユーザーに付与された権限を返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getAccount(){
        return user.getAccount();
    }

    // ユーザー名を返す
    @Override
    public String getUsername() {
        return user.getAccount();
    }

    // ユーザーパスワードを返す
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // アカウントが期限切れでないかを示す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // アカウントがロックされていないかを示す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 資格情報が期限切れでないかを示す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // アカウントが有効かを示す
    @Override
    public boolean isEnabled() {
        return true;
    }
}
