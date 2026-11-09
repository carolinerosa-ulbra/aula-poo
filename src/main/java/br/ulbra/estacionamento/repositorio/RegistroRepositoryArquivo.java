package br.ulbra.estacionamento.repositorio;

import br.ulbra.estacionamento.dominio.RegistroCobranca;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RegistroRepositoryArquivo implements RegistroRepository {

    private final Path arquivo;

    public RegistroRepositoryArquivo(Path arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public void salvar(RegistroCobranca registro) {
        String linha = String.join(";", registro.placa(), registro.tipo(),
                registro.entrada().toString(), registro.saida().toString(),
                Double.toString(registro.valor()));
        try {
            Files.writeString(arquivo, linha + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível gravar o registro em " + arquivo, e);
        }
    }

    @Override
    public List<RegistroCobranca> listarTodos() {
        if (!Files.exists(arquivo)) {
            return List.of();
        }
        try {
            List<RegistroCobranca> registros = new ArrayList<>();
            for (String linha : Files.readAllLines(arquivo)) {
                if (!linha.isBlank()) {
                    registros.add(converter(linha));
                }
            }
            return List.copyOf(registros);
        } catch (IOException e) {
            throw new RepositorioException("Não foi possível ler os registros de " + arquivo, e);
        }
    }

    private RegistroCobranca converter(String linha) {
        String[] partes = linha.split(";");
        if (partes.length != 5) {
            throw new RepositorioException("Linha inválida no arquivo: " + linha);
        }
        try {
            return new RegistroCobranca(partes[0], partes[1],
                    LocalDateTime.parse(partes[2]), LocalDateTime.parse(partes[3]),
                    Double.parseDouble(partes[4]));
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new RepositorioException("Linha inválida no arquivo: " + linha, e);
        }
    }
}
