package com.ifsp.yas1.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LivroService {

    // Pasta "uploads" dentro do projeto, separada de onde o usuário escolhe o arquivo
    public static final Path endereco_armazenamento_arquivo = Paths.get("uploads").toAbsolutePath().normalize();
    // Recebimento de dados de arquivo para salvar no computador

    public String salvarImagem(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new NullPointerException("Arquivo da Imagem do Livro está vazio");
        }

        Files.createDirectories(endereco_armazenamento_arquivo);

        // Nome único para não sobrescrever (nem travar) arquivos já existentes
        String nomeArquivo = UUID.randomUUID() + "_" + Paths.get(arquivo.getOriginalFilename()).getFileName();
        Path enderecoArquivo = endereco_armazenamento_arquivo.resolve(nomeArquivo).normalize();

        if (!enderecoArquivo.getParent().equals(endereco_armazenamento_arquivo)) {
            throw new SecurityException("Nome do arquivo não é válido!");
        }

        Files.copy(arquivo.getInputStream(), enderecoArquivo);

        return nomeArquivo;
    }
}
