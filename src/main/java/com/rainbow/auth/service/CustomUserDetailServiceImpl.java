package com.rainbow.auth.service;

import com.rainbow.auth.entity.CustomGrantedAuthority;
import com.rainbow.auth.entity.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yanzhihao
 * @since 2022/3/18
 */
@Service
@Qualifier
@Slf4j
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser securityUser = jdbcTemplate.queryForObject(
                "select username, user_id, enabled, password = '' from users where username = ?",
                new String[]{username},
                new BeanPropertyRowMapper<>(CustomUser.class)
        );

        String authStr = jdbcTemplate.queryForObject(
                "select authority from authorities where username = ?",
                new String[]{username}, String.class
        );

        if (StringUtils.isNotBlank(authStr)) {
            String[] authArray = authStr.split(",");
            List<CustomGrantedAuthority> authorities = new ArrayList<>();
            for (int i = 0; i < authArray.length; i++) {
                CustomGrantedAuthority temp = new CustomGrantedAuthority();
                temp.setAuthority(authArray[i]);
                authorities.add(temp);
            }
            securityUser.setAuthorities(authorities);
        }

        return securityUser;
    }
}
