package com.general.template.config;

import com.general.template.config.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomizeAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomizeAccessDecisionManager accessDecisionManager;

    @Autowired
    private CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    private CustomizeAbstractSecurityInterceptor securityInterceptor;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Resource
    private JwtRequestFilter jwtRequestFilter;

    @Resource(name = "adminUserDetailsServiceImpl")
    private UserDetailsService adminProgramUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new AdminAuthenticationProvider(passwordEncoder(), adminProgramUserDetailsService));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //禁用 crsf
        httpSecurity
                .csrf().disable()
                .cors().and()

                //注册、登录，允许访问Provider
                .authorizeRequests().antMatchers("/signUp", "/signIn", "/captcha", "/mp/signIn").permitAll()

                //swagger，允许访问
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()

                // 开放定时任务接口，允许访问
                .antMatchers("/task/**").permitAll()


                .anyRequest().authenticated()
                .and().logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).permitAll()
                .and().formLogin().permitAll()
                //异常处理
                .and().exceptionHandling()
                //访问拒绝处理逻辑
                .accessDeniedHandler(accessDeniedHandler)
                //匿名用户无权限处理逻辑
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                //JWT
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //决策管理器
                        o.setAccessDecisionManager(accessDecisionManager);
                        //安全元数据源
                        o.setSecurityMetadataSource(securityMetadataSource);
                        return o;
                    }
                });

        //自定义权限拦截器
        httpSecurity.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);


        // 管理端登录过滤器
        httpSecurity.addFilterBefore(new AdminAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // JWT 处理过滤器
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
