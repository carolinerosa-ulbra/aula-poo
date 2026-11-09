package br.ulbra.estacionamento.dominio;

import br.ulbra.estacionamento.cobranca.*;
import br.ulbra.estacionamento.repositorio.RegistroRepository;
import br.ulbra.estacionamento.repositorio.RegistroRepositoryEmMemoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstacionamentoTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 10, 19, 19, 0);

    private Estacionamento estacionamento;
    private RegistroRepository repositorio;

    @BeforeEach
    void preparar() {
        repositorio = new RegistroRepositoryEmMemoria();
        estacionamento = new Estacionamento(2, repositorio);
    }

    @Test
    void deveCriarAsVagasInformadas() {
        assertEquals(2, estacionamento.getVagas().size());
        assertEquals(2, estacionamento.vagasLivres());
    }

    @Test
    void entradaDeveOcuparUmaVaga() {
        estacionamento.registrarEntrada(new Carro("ABC1D23"), ENTRADA);

        assertEquals(1, estacionamento.vagasLivres());
    }

    @Test
    void saidaDeveLiberarAVagaECobrarPeloTipoDoVeiculo() {
        Ticket ticket = estacionamento.registrarEntrada(new Moto("XYZ9K88"), ENTRADA);

        double valor = estacionamento.registrarSaida(ticket, ENTRADA.plusMinutes(60));

        assertEquals(3.60, valor, 0.001);
        assertEquals(2, estacionamento.vagasLivres());
    }

    @Test
    void naoDevePermitirEntradaSemVagaLivre() {
        estacionamento.registrarEntrada(new Carro("AAA1A11"), ENTRADA);
        estacionamento.registrarEntrada(new Carro("BBB2B22"), ENTRADA);

        assertThrows(IllegalStateException.class,
                () -> estacionamento.registrarEntrada(new Moto("CCC3C33"), ENTRADA));
    }

    @Test
    void listaDeVagasNaoPodeSerAlteradaDeFora() {
        assertThrows(UnsupportedOperationException.class,
                () -> estacionamento.getVagas().clear());
        assertTrue(estacionamento.getVagas().size() > 0);
    }
    @Test
    void saidaDeveSalvarORegistro() {
        Ticket ticket = estacionamento.registrarEntrada(new Carro("ABC1D23"), ENTRADA);

        estacionamento.registrarSaida(ticket, ENTRADA.plusMinutes(60));

        assertEquals(1, repositorio.listarTodos().size());
        assertEquals("ABC1D23", repositorio.listarTodos().getFirst().placa());
        assertEquals(6.0, repositorio.listarTodos().getFirst().valor(), 0.001);
    }}
