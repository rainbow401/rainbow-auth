//package com.rainbow.auth.config;
//
//import com.rainbow.auth.common.TokenConstant;
//import com.rainbow.auth.entity.SecurityUser;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//import org.springframework.util.StringUtils;
//
//import java.util.Collection;
//import java.util.Map;
//
///**
// * @author yanzhihao
// * @since 2022/3/18
// */
//public class MyUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
//
//    @Override
//    public Authentication extractAuthentication(Map<String, ?> map) {
//        if (map.containsKey(USERNAME)) {
//            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
//            String username = (String) map.get(USERNAME);
//            String userId = map.get(TokenConstant.USER_ID).toString();
//            SecurityUser user = new SecurityUser(userId,username,"",authorities);
//            return new UsernamePasswordAuthenticationToken(user, "", authorities);
//        }
//        return null;
//    }
//
//    /**
//     * 提取权限
//     */
//    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
//        Object authorities = map.get(AUTHORITIES);
//        if (authorities instanceof String) {
//            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
//        }
//        if (authorities instanceof Collection) {
//            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
//                    .collectionToCommaDelimitedString((Collection<?>) authorities));
//        }
//        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
//    }
//}
