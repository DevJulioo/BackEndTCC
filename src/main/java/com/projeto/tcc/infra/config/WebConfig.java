package com.projeto.tcc.infra.config; // Mantive seu pacote

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Este é o seu método existente para servir as imagens de perfil.
     * Ele está mantido e continua funcionando.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }

    /**
     * MÉTODO ADICIONADO PARA CORRIGIR O CORS
     * Este método permite que o front-end faça requisições PATCH, PUT, DELETE, etc.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os endpoints
                .allowedOrigins("*") // Permite requisições de qualquer origem
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Permite todos os métodos
                .allowedHeaders("*"); // Permite todos os cabeçalhos
    }
}