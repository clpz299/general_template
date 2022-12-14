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

        //?????? crsf
        httpSecurity
                .csrf().disable()
                .cors().and()

                //??????????????????????????????Provider
                .authorizeRequests().antMatchers("/signUp", "/signIn", "/captcha", "/mp/signIn").permitAll()

                //swagger???????????????
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()

                // ???????????????????????????????????????
                .antMatchers("/task/**").permitAll()


                .anyRequest().authenticated()
                .and().logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).permitAll()
                .and().formLogin().permitAll()
                //????????????
                .and().exceptionHandling()
                //????????????????????????
                .accessDeniedHandler(accessDeniedHandler)
                //?????????????????????????????????
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                //JWT
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //???????????????
                        o.setAccessDecisionManager(accessDecisionManager);
                        //??????????????????
                        o.setSecurityMetadataSource(securityMetadataSource);
                        return o;
                    }
                });

        //????????????????????????
        httpSecurity.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);


        // ????????????????????????
        httpSecurity.addFilterBefore(new AdminAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // JWT ???????????????
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
