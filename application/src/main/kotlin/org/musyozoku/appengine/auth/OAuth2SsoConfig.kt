package org.musyozoku.appengine.auth

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * OAuth2 Single Sign On Configuration
 */
@Configuration
@EnableOAuth2Sso
open class OAuth2SsoConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        // for Admin Console
        http.authorizeRequests()
                .antMatchers("/_ah/**").permitAll()

        // for Application
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/fonts/**").permitAll()
                .anyRequest().authenticated()
        http.logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
    }
}
