package com.ecommerce.microcommerce.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailsService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if ("admin".equals(username)) {
      User user = new User("admin", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
      List<GrantedAuthority> authorities = buildUserAuthority(username);
      return buildUserForAuthentication(user, authorities);
    } else if ("client".equals(username)) {
      User user = new User("client", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
      List<GrantedAuthority> authorities = buildUserAuthority(username);
      return buildUserForAuthentication(user, authorities);
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

  private User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
    String username = user.getUsername();
    String password = user.getPassword();
    boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;

    return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
  }

  private List<GrantedAuthority> buildUserAuthority(String username) {

    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
    if ("admin".equals(username)) {
      setAuths.add(new SimpleGrantedAuthority("ADMIN"));
    } else {
      setAuths.add(new SimpleGrantedAuthority("CLIENT"));
    }
    return new ArrayList<GrantedAuthority>(setAuths);
  }
}
