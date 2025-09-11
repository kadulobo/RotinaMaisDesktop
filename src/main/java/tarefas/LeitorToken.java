package tarefas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilitário para leitura do token a partir de um arquivo de texto.
 *
 * <p>O caminho pode ser informado de forma relativa, como em
 * {@code "pasta\\token.txt"} no Windows. O conteúdo é retornado
 * sem espaços em branco nas extremidades.</p>
 */
public final class LeitorToken {

    private LeitorToken() {
        // Utilidade, não instanciável
    }

    /**
     * Lê o conteúdo do arquivo informado e o retorna como token.
     *
     * @param caminhoRelativo caminho relativo do arquivo que contém o token
     * @return token lido do arquivo
     * @throws IOException se ocorrer problema de leitura
     */
    public static String ler(String caminhoRelativo) throws IOException {
        Path caminho = Paths.get(caminhoRelativo);
        byte[] bytes = Files.readAllBytes(caminho);
        return new String(bytes, StandardCharsets.UTF_8).trim();
    }
}

