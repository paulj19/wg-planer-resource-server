package com.wgplaner.security;

import com.wgplaner.core.entity.UserProfile;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@Getter
public class MyAuthenticationToken extends AbstractAuthenticationToken {
  private Jwt jwt;
  private UserProfile userProfile;

  public MyAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
  }

  public MyAuthenticationToken(Jwt jwt, UserProfile userProfile) {
    super(null);
    this.userProfile = userProfile;
    this.jwt = jwt;
  }

  @Override
  public Object getCredentials() {
    return jwt;
  }

  @Override
  public UserProfile getPrincipal() {
    return this.userProfile;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }
}
