package com.todoc.web.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.todoc.web.dto.User;

import lombok.extern.slf4j.Slf4j;

import com.todoc.web.dto.Clinic;
import com.todoc.web.dto.Pharmacy;

// 사용자 인증정보를 관리하는 역할
@Slf4j
public class UserPrincipal implements UserDetails 
{
    private static final long serialVersionUID = 1L;    
    private Object entity;  // 일반 유저거나 의사거나 약사
    private String rolePrefix = "ROLE_";

    // 객체 초기화해서 사용자 정보를 저장
    public UserPrincipal(Object entity) 
    {
        this.entity = entity;
    }

    // 권한 추가
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        String role = rolePrefix;

        if (entity instanceof User) 
        {
            role += ((User) entity).getUserType();
        } 
        else if (entity instanceof Clinic) 
        {
            role += "MEDICAL";
        } 
        else if (entity instanceof Pharmacy) 
        {
            role += "MEDICAL";
        }

        log.info("getAuthorities role : " + role);
        authorities.add(new SimpleGrantedAuthority(role));
        
        return authorities;
    }

    @Override
    public String getPassword() 
    {
        if (entity instanceof User) 
        {
            return ((User) entity).getUserPwd();
        }
        else if (entity instanceof Clinic) 
        {
            return ((Clinic) entity).getUserPwd();
        } 
        else if (entity instanceof Pharmacy) 
        {
            return ((Pharmacy) entity).getUserPwd();
        }
        
        return null; 
    }

    @Override
    public String getUsername() 
    {
        if (entity instanceof User) 
        {
            return ((User) entity).getUserEmail();
        } 
        else if (entity instanceof Clinic) 
        {
            return ((Clinic) entity).getUserEmail();
        } 
        else if (entity instanceof Pharmacy) 
        {
            return ((Pharmacy) entity).getUserEmail();
        }
        
        return null;
    }

    public boolean isAccountNonExpired() 
    {
        return true;
    }

    public boolean isAccountNonLocked() 
    {
        return true;
    }

    public boolean isCredentialsNonExpired() 
    {
        return true;
    }

    public boolean isEnabled() 
    {
    	return true;
    }
}
