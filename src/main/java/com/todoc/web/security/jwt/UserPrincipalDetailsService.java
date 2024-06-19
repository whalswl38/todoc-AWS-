package com.todoc.web.security.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.todoc.web.dao.UserDao;
import com.todoc.web.security.dto.UserPrincipal;

// 로그인 요청 회원 찾기
// 사용자의 상세 정보를 로드, 사용자 인증 과정에서 사용자 정보를 조회 후 -> security 가 이해할 수 있는 형태로 제공
@Service
public class UserPrincipalDetailsService implements UserDetailsService 
{
	@Autowired
	private UserDao userDao;
	
	@SuppressWarnings("unused")
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	// 사용자의 이메일을 매개변수로 받고, 해당 이메일을 사용하여 사용자 정보를 조회
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
        Object entity = userDao.findByEmail(username);
        
        if (entity == null) 
        {
            entity = userDao.findClinicEmail(username);
        }
        
        if (entity == null) 
        {
            entity = userDao.findPharmEmail(username);
        }
        
        if (entity == null) 
        {
            throw new UsernameNotFoundException("해당 회원이 존재하지 않습니다 , " + username);
        }
                       
        return new UserPrincipal(entity);
	}
}
