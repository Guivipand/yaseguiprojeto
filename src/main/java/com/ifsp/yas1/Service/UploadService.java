package com.ifsp.yas1.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Service
public class UploadService implements WebMvcConfigurer {

    private final Path raiz;

    public UploadService() {
        this("uploads");
    }

    public UploadService(String pasta) {
        this.raiz = Path.of(pasta).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(raiz.toUri().toString(), "classpath:/static/uploads/");
    }

    public String salvar(MultipartFile arquivo, String pasta) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione uma imagem.");
        }
        if (arquivo.getSize() > 2L * 1024 * 1024) {
            throw new IllegalArgumentException("A imagem deve ter no máximo 2 MB.");
        }
        String nome = String.valueOf(arquivo.getOriginalFilename()).toLowerCase(Locale.ROOT);
        String extensao = nome.endsWith(".png") ? "png" : nome.endsWith(".jpg") || nome.endsWith(".jpeg") ? "jpg" : null;
        byte[] bytes = arquivo.getBytes();
        if (extensao == null || !imagemValida(bytes)) {
            throw new IllegalArgumentException("Envie uma imagem JPEG ou PNG.");
        }
        String arquivoFinal = UUID.randomUUID() + "." + extensao;
        Files.write(Files.createDirectories(raiz.resolve(pasta)).resolve(arquivoFinal), bytes);
        return "uploads/" + pasta + "/" + arquivoFinal;
    }

    private static boolean imagemValida(byte[] bytes) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes)) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
