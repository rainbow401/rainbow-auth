//package com.rainbow.auth.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.*;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    public CustomUserDetailsService customUserDetailsService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        //定义一个客户端支持自定义的授权类型
//        clients.inMemory()
//                .withClient("demo")
//                .secret(passwordEncoder().encode("demo"))
//                .authorizedGrantTypes("custom_phone_pwd","custom_phone_sms")
//                .scopes("all");
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.allowFormAuthenticationForClients()
//                .tokenKeyAccess("isAuthenticated()")
//                .checkTokenAccess("permitAll()");
//    }
//
//@Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
//
//        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
//        endpoints.tokenEnhancer(new TokenEnhancer() {
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//                DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
//                CustomUser user = (CustomUser) oAuth2Authentication.getPrincipal();
//                Map<String, Object> map = new LinkedHashMap<>();
//                map.put("nickname", user.getNickname());
//                map.put("mobile", user.getMobile());
//                map.put("avatar",user.getAvatar());
//                token.setAdditionalInformation(map);
//                return oAuth2AccessToken;
//            }
//        });
//
//    }
//
//    private List<TokenGranter> getTokenGranters(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
//        return new ArrayList<>(Arrays.asList(
//                new PhoneSmsCustomTokenGranter(tokenServices, clientDetailsService, requestFactory, customUserDetailsService),
//                new PhonePasswordCustomTokenGranter(tokenServices, clientDetailsService, requestFactory, customUserDetailsService)
//        ));
//    }
//}