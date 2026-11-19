package com.ifsp.yas1.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class UploadServiceTest {

    @TempDir
    Path pasta;

    private static byte[] imagem(String formato) throws Exception {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(30, 45, BufferedImage.TYPE_INT_RGB), formato, saida);
        return saida.toByteArray();
    }

    private static MockMultipartFile arquivo(String nome, byte[] conteudo) {
        return new MockMultipartFile("imagem", nome, "image/png", conteudo);
    }

    @Test
    void salvaImagemValidaComNomeNovo() throws Exception {
        UploadService servico = new UploadService(pasta.toString());
        String caminho = servico.salvar(arquivo("../../capa.png", imagem("png")), "capas");

        assertTrue(caminho.matches("uploads/capas/[0-9a-f-]{36}\\.png"));
        assertTrue(Files.exists(pasta.resolve(caminho.substring("uploads/".length()))));
        assertTrue(servico.salvar(arquivo("foto.JPEG", imagem("jpg")), "fotos").endsWith(".jpg"));
    }

    @Test
    void recusaArquivosInvalidos() throws Exception {
        UploadService servico = new UploadService(pasta.toString());
        byte[] png = imagem("png");

        assertThrows(IllegalArgumentException.class, () -> servico.salvar(arquivo("x.php", png), "capas"));
        assertThrows(IllegalArgumentException.class, () -> servico.salvar(arquivo("x.png", "<?php echo 1;".getBytes()), "capas"));
        assertThrows(IllegalArgumentException.class, () -> servico.salvar(arquivo("x.png", new byte[3 * 1024 * 1024]), "capas"));
        assertThrows(IllegalArgumentException.class, () -> servico.salvar(arquivo("x.png", new byte[0]), "capas"));
    }
}
