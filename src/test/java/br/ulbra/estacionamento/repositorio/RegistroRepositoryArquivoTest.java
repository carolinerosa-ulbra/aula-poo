package br.ulbra.estacionamento.repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.ulbra.estacionamento.dominio.RegistroCobranca;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RegistroRepositoryArquivoTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 11, 9, 19, 0);

    @TempDir
    Path pasta;

    @Test
    void arquivoInexistenteDeveListarVazio() {
        RegistroRepository repositorio = new RegistroRepositoryArquivo(pasta.resolve("registros.csv"));

        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    void dadosDevemSobreviverAUmaNovaInstancia() {
        Path arquivo = pasta.resolve("registros.csv");
        RegistroRepository primeiro = new RegistroRepositoryArquivo(arquivo);
        primeiro.salvar(new RegistroCobranca("ABC1D23", "CARRO", ENTRADA, ENTRADA.plusMinutes(60), 6.0));
        primeiro.salvar(new RegistroCobranca("XYZ9K88", "MOTO", ENTRADA, ENTRADA.plusMinutes(60), 3.6));

        RegistroRepository segundo = new RegistroRepositoryArquivo(arquivo);

        assertEquals(2, segundo.listarTodos().size());
        assertEquals("XYZ9K88", segundo.listarTodos().get(1).placa());
        assertEquals(3.6, segundo.listarTodos().get(1).valor(), 0.001);
    }

    @Test
    void arquivoCorrompidoDeveLancarErroTecnico() throws IOException {
        Path arquivo = pasta.resolve("registros.csv");
        Files.writeString(arquivo, "linha;quebrada" + System.lineSeparator());
        RegistroRepository repositorio = new RegistroRepositoryArquivo(arquivo);

        assertThrows(RepositorioException.class, repositorio::listarTodos);
    }
}
