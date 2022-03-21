package com.rainbow.auth.config;

import com.rainbow.auth.service.CustomUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailServiceImpl customUserDetailService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private CustomTokenEnhancer customTokenEnhancer;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 客户端数据源配置
     *
     * 我们配置了使用数据库来维护客户端信息。虽然在各种Demo中我们经常看到的是在内存中维护客户端信息，通过配置直接写死在这里。
     * 但是，对于实际的应用我们一般都会用数据库来维护这个信息，甚至还会建立一套工作流来允许客户端自己申请ClientID。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     * 这里干了两个事情，首先打开了验证Token的访问权限（以便之后我们演示）。
     * 然后允许ClientSecret明文方式保存并且可以通过表单提交（而不仅仅是Basic Auth方式提交），之后会演示到这个。
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()").allowFormAuthenticationForClients().passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    /**
     * 干了几个事情：
     * 1、配置我们的Token存放方式不是内存、数据库或Redis方式，而是JWT方式。
     * JWT是Json Web Token缩写也就是使用JSON数据格式包装的Token，由.句号把整个JWT分隔为头、数据体、签名三部分。
     * JWT保存Token虽然易于使用但是不是那么安全，一般用于内部，并且需要走HTTPS+配置比较短的失效时间。
     * 2、配置了JWT Token的非对称加密来进行签名
     * 3、配置了一个自定义的Token增强器，把更多信息放入Token中
     * 4、配置了使用JDBC数据库方式来保存用户的授权批准记录
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        //配置端点
        endpoints.approvalStore(approvalStore())
                .authorizationCodeServices(authorizationCodeServices())
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices());
    }


    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, accessTokenConverter()));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }

    /**
     * 使用JDBC数据库方式来保存授权码
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 使用JWT令牌存储
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 使用JDBC数据库方式来保存用户的授权批准记录
     * @return
     */
    @Bean
    public JdbcApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     *
     * token增强器
     *
     * 自定义的Token增强器，把更多信息放入Token中
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }


    /**
     * 验证token配置
     *
     * 配置JWT令牌使用非对称加密方式来验证
     * @return
     */
    @Bean
    protected JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        //设置密钥
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));

        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

        DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
        userTokenConverter.setUserDetailsService(customUserDetailService);

        tokenConverter.setUserTokenConverter(userTokenConverter);

        converter.setAccessTokenConverter(tokenConverter);

        return converter;
    }


}
