package com.rainbow.auth.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication != null) {
            Object principal = authentication.getUserAuthentication().getPrincipal();
            try {
                String s = objectMapper.writeValueAsString(principal);
                Map<String,Object> map = objectMapper.readValue(s, new TypeReference<Map<String,Object>>(){});
                //把用户标识以userDetails这个Key加入到JWT的额外信息中去
                map.remove("password");
                map.remove("authorities");
                map.remove("accountNonExpired");
                map.remove("accountNonLocked");
                map.remove("credentialsNonExpired");
                map.remove("enabled");
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("user_info", map);
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return accessToken;
    }
}