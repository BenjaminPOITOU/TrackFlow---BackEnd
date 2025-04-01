package com.eql.cda.track.flow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // Active la configuration de sécurité web de Spring
public class SecurityConfig {

    // Déclare un Bean SecurityFilterChain pour définir les règles de sécurité HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver CSRF (Cross-Site Request Forgery) - Courant pour les API REST stateless
                .csrf(csrf -> csrf.disable())

                // 2. Configurer les règles d'autorisation des requêtes HTTP
                .authorizeHttpRequests(authz -> authz
                        // Autoriser l'accès sans authentification aux endpoints d'API pour l'enregistrement et l'authentification
                        .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()

                        // ----> AUTORISER SWAGGER UI <----
                        .requestMatchers(
                                new AntPathRequestMatcher("/"), // Si la racine redirige vers Swagger
                                new AntPathRequestMatcher("/swagger-ui.html"), // La page HTML principale
                                new AntPathRequestMatcher("/swagger-ui/**"), // Ressources de l'UI (JS, CSS...)
                                new AntPathRequestMatcher("/v3/api-docs/**") // La spec OpenAPI JSON/YAML
                        ).permitAll() // Autoriser l'accès sans authentification

                        // Toutes les AUTRES requêtes doivent être authentifiées (sera appliqué plus tard)
                        // Pour l'instant, on pourrait autoriser tout pour faciliter le développement SANS sécurité réelle
                        // .anyRequest().authenticated() // C'est ce que tu auras en prod
                        .anyRequest().permitAll()      // !!! TEMPORAIRE : Autorise tout le reste sans auth !!!
                )

                // 3. Gérer la session (pour API REST/JWT, on la veut généralement STATELESS)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Ne pas créer de session HTTP
                )

                // 4. Désactiver la connexion par formulaire par défaut (on utilise l'API /api/auth/authenticate)
                .formLogin(form -> form.disable())
                // 5. Désactiver HTTP Basic par défaut
                .httpBasic(basic -> basic.disable());

        // **TODO (Sécurité):** Plus tard, tu ajouteras ici :
        // - La configuration du AuthenticationProvider (avec PasswordEncoder).
        // - Le filtre JWT pour valider les tokens sur les requêtes authentifiées.
        // - Des règles plus strictes dans authorizeHttpRequests (ex: .anyRequest().authenticated()).

        return http.build();
    }

    // **TODO (Sécurité):** Plus tard, tu ajouteras un Bean pour PasswordEncoder :
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // **TODO (Sécurité):** Plus tard, tu ajouteras un Bean pour AuthenticationManager :
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    //    return config.getAuthenticationManager();
    // }
}