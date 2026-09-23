package com.ifsp.yas1.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LivroService {

    public static final String endereco_armazenamento_arquivo = new File("src/main/resources/static/uploads").getAbsolutePath();

    public String salvarImagem(MultipartFile arquivo) throws IOException {
        if (arquivo == null) {
            throw new NullPointerException("Arquivo da Imagem do Livro está vazio");
        }

        var enderecoArquivo = new File(endereco_armazenamento_arquivo + File.separator + arquivo.getOriginalFilename());
        System.out.println("Endereço do arquivo: " + enderecoArquivo.getAbsolutePath());
        System.out.println("Nome do arquivo Parent: " + enderecoArquivo.getParent());
        System.out.println("Endereço de armazenamento: " + endereco_armazenamento_arquivo);

        if (!Objects.equals(enderecoArquivo.getParent(), endereco_armazenamento_arquivo)) {
            throw new SecurityException("Nome do arquivo não é viável!");
        }

        Files.copy(arquivo.getInputStream(), enderecoArquivo.toPath(), StandardCopyOption.REPLACE_EXISTING);

        String endereco = new String();
        endereco = "uploads/" + arquivo.getOriginalFilename();
        return endereco;
    }
}
