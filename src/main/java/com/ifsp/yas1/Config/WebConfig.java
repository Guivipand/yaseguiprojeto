package com.ifsp.yas1.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ifsp.yas1.Service.LivroService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Deixa as imagens da pasta "uploads" acessíveis em /uploads/nome-do-arquivo
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pasta = LivroService.endereco_armazenamento_arquivo.toUri().toString();
        if (!pasta.endsWith("/")) {
            pasta += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(pasta);
    }
}
