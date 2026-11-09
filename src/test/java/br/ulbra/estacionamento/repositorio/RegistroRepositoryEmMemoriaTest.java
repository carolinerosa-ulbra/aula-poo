package br.ulbra.estacionamento.repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ulbra.estacionamento.dominio.RegistroCobranca;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RegistroRepositoryEmMemoriaTest {

    @Test
    void deveSalvarEListarOsRegistrosNaOrdem() {
        RegistroRepository repositorio = new RegistroRepositoryEmMemoria();
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 26, 19, 0);
        RegistroCobranca carro = new RegistroCobranca("ABC1D23", "CARRO", entrada, entrada.plusMinutes(60), 6.0);
        RegistroCobranca moto = new RegistroCobranca("XYZ9K88", "MOTO", entrada, entrada.plusMinutes(60), 3.6);

        repositorio.salvar(carro);
        repositorio.salvar(moto);

        assertEquals(java.util.List.of(carro, moto), repositorio.listarTodos());
    }

    @Test
    void listaRetornadaNaoDevePermitirAlterarORepositorio() {
        RegistroRepository repositorio = new RegistroRepositoryEmMemoria();

        assertThrows(UnsupportedOperationException.class, () -> repositorio.listarTodos().clear());
        assertEquals(0, repositorio.listarTodos().size());
    }
}