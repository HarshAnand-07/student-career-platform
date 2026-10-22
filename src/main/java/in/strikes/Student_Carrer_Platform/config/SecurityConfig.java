package in.strikes.Student_Carrer_Platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/opportunities/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/opportunities/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/opportunities/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/opportunities/**")
                        .hasRole("ADMIN")


                        .requestMatchers(HttpMethod.PUT, "/api/applications/*/status")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/applications/student/*")
                        .hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/applications/opportunity/*")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/applications")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/applications")
                        .hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/applications/*")
                        .hasAnyRole("STUDENT", "ADMIN")


                        .requestMatchers(HttpMethod.GET, "/api/students")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/students")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE , "/api/students/me")
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/students/*")
                        .hasRole("ADMIN")


                        .requestMatchers(HttpMethod.POST, "/api/projects/**")
                        .hasAnyRole("STUDENT" , "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/projects/**")
                        .authenticated()


                        .requestMatchers(HttpMethod.POST, "/api/certification/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/certification/**")
                        .authenticated()

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();

    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${jwt.secret}") String secret) {

        byte[] keyBytes = Base64.getDecoder().decode(secret);

        SecretKey key = new SecretKeySpec(
                keyBytes,
                "HmacSHA256"
        );

        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;

    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Carrer PlatForm")
                        .description("By Harsh Anand"))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearerAuth"));
    }

}
