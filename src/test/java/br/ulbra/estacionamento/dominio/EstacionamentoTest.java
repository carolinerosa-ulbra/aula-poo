package br.ulbra.estacionamento.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstacionamentoTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 11, 9, 19, 0);

    private Estacionamento estacionamento;

    @BeforeEach
    void preparar() {
        estacionamento = new Estacionamento(2);
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

        EstacionamentoLotadoException erro = assertThrows(EstacionamentoLotadoException.class,
                () -> estacionamento.registrarEntrada(new Moto("CCC3C33"), ENTRADA));

        assertEquals("Não há vaga livre no momento.", erro.getMessage());
    }

    @Test
    void saidaInvalidaNaoDeveLiberarAVaga() {
        Ticket ticket = estacionamento.registrarEntrada(new Carro("ABC1D23"), ENTRADA);

        assertThrows(PeriodoInvalidoException.class,
                () -> estacionamento.registrarSaida(ticket, ENTRADA.minusMinutes(5)));

        assertEquals(1, estacionamento.vagasLivres());
    }

    @Test
    void listaDeVagasNaoPodeSerAlteradaDeFora() {
        assertThrows(UnsupportedOperationException.class,
                () -> estacionamento.getVagas().clear());
    }
}
