package com.culflab.jwtauthsb;


import com.culflab.jwtauthsb.member.service.handler.Member;
import com.culflab.jwtauthsb.member.service.handler.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        var member = Member.builder()
                .name(annotation.name())
                .account(annotation.account())
                .password("admin")
                .createdAt(LocalDateTime.now())
                .role(Role.ADMIN)
                .build();

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            member, null, member.getAuthorities()
        );
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
