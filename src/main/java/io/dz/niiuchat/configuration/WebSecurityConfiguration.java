package io.dz.niiuchat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("!development")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        /**/.authorizeRequests()
        /**/.antMatchers("/admin/**").hasRole("ADMIN")
        /**/.antMatchers("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.svg").permitAll()
        /**/.antMatchers("/favicon.ico").permitAll()
        /**/.antMatchers("/register").permitAll()
        /**/.antMatchers("/public/**").permitAll()
        /**/.anyRequest().authenticated()
        .and()
        /**/.formLogin()
        /**/.loginPage("/login")
        /**/.loginProcessingUrl("/login")
        /**/.defaultSuccessUrl("/app", true)
        /**/.permitAll()
        .and()
        /**/.logout()
        /**/.permitAll()
        .and()
        /**/.exceptionHandling()
        /**/.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"))
    ;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
