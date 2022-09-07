package com.stella.rememberall.security;

import com.stella.rememberall.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private User user;

    public UserAdapter(User user){
        super(user.getEmail(), user.getPassword(),  authorities(user.getRoles()));
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> authorities(List<String> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toSet());
    }

}
