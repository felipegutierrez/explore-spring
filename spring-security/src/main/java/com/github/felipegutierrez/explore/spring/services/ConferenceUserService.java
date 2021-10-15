package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.domain.ConferenceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ConferenceUserService {
    public final static class ConferenceUserDetails extends ConferenceUser implements UserDetails {

        public ConferenceUserDetails(ConferenceUser user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList("ROLE_ATTENDEE");
            if (this.isSpeaker()) {
                roles.add(new SimpleGrantedAuthority("ROLE_SPEAKER"));
            }
            if (this.isAdmin()) {
                roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            return roles;
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
}
