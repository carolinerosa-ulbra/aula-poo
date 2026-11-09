package br.ulbra.estacionamento.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ulbra.estacionamento.dominio.Estacionamento;
import br.ulbra.estacionamento.dominio.PlacaInvalidaException;
import br.ulbra.estacionamento.dominio.RegistroCobranca;
import br.ulbra.estacionamento.dominio.TicketNaoEncontradoException;
import br.ulbra.estacionamento.dominio.TipoDeVeiculoInvalidoException;
import br.ulbra.estacionamento.dominio.VeiculoJaEstacionadoException;
import br.ulbra.estacionamento.repositorio.RegistroRepository;
import br.ulbra.estacionamento.repositorio.RegistroRepositoryEmMemoria;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicoDeEstacionamentoTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 11, 9, 19, 0);

    private RegistroRepository repositorio;
    private ServicoDeEstacionamento servico;

    @BeforeEach
    void preparar() {
        repositorio = new RegistroRepositoryEmMemoria();
        servico = new ServicoDeEstacionamento(new Estacionamento(2), repositorio);
    }

    @Test
    void saidaDeveGravarORegistroDeCobranca() {
        servico.registrarEntrada("ABC1D23", "CARRO", ENTRADA);

        RegistroCobranca registro = servico.registrarSaida("abc1d23", ENTRADA.plusMinutes(60));

        assertEquals(6.00, registro.valor(), 0.001);
        assertEquals(1, repositorio.listarTodos().size());
        assertEquals(2, servico.vagasLivres());
    }

    @Test
    void deveSomarOTotalArrecadado() {
        servico.registrarEntrada("ABC1D23", "CARRO", ENTRADA);
        servico.registrarEntrada("XYZ9K88", "MOTO", ENTRADA);
        servico.registrarSaida("ABC1D23", ENTRADA.plusMinutes(60));
        servico.registrarSaida("XYZ9K88", ENTRADA.plusMinutes(60));

        assertEquals(9.60, servico.totalArrecadado(), 0.001);
    }

    @Test
    void naoDevePermitirSaidaSemTicketAberto() {
        TicketNaoEncontradoException erro = assertThrows(TicketNaoEncontradoException.class,
                () -> servico.registrarSaida("ABC1D23", ENTRADA));

        assertEquals("Não há ticket aberto para a placa ABC1D23.", erro.getMessage());
    }

    @Test
    void naoDevePermitirDuasEntradasDaMesmaPlaca() {
        servico.registrarEntrada("ABC1D23", "CARRO", ENTRADA);

        assertThrows(VeiculoJaEstacionadoException.class,
                () -> servico.registrarEntrada("abc1d23", "CARRO", ENTRADA));
    }

    @Test
    void deveRecusarTipoDeVeiculoDesconhecido() {
        assertThrows(TipoDeVeiculoInvalidoException.class,
                () -> servico.registrarEntrada("ABC1D23", "BICICLETA", ENTRADA));
        assertEquals(2, servico.vagasLivres());
    }

    @Test
    void deveRecusarPlacaInvalidaSemOcuparVaga() {
        assertThrows(PlacaInvalidaException.class,
                () -> servico.registrarEntrada("???", "CARRO", ENTRADA));
        assertEquals(2, servico.vagasLivres());
    }
}
