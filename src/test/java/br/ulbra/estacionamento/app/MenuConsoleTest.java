package br.ulbra.estacionamento.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import br.ulbra.estacionamento.dominio.Estacionamento;
import br.ulbra.estacionamento.repositorio.RegistroRepositoryEmMemoria;
import br.ulbra.estacionamento.servico.ServicoDeEstacionamento;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class MenuConsoleTest {

    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 9, 19, 0);

    private String executarComEntrada(String digitado) {
        ServicoDeEstacionamento servico =
                new ServicoDeEstacionamento(new Estacionamento(1), new RegistroRepositoryEmMemoria());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new MenuConsole(servico, new Scanner(digitado), new PrintStream(buffer), () -> AGORA).executar();
        return buffer.toString();
    }

    @Test
    void erroPrevisivelNaoDeveEncerrarOPrograma() {
        String tela = executarComEntrada("1\n???\nCARRO\n9\n0\n");

        assertTrue(tela.contains("Erro: Placa inválida"));
        assertTrue(tela.contains("Opção inválida"));
        assertTrue(tela.contains("Encerrado."));
    }

    @Test
    void fluxoNormalDeveRegistrarEntradaESaida() {
        String tela = executarComEntrada("1\nABC1D23\nCARRO\n2\nABC1D23\n0\n");

        assertTrue(tela.contains("Entrada registrada. Vagas livres: 0"));
        assertTrue(tela.contains("Saída registrada."));
    }

    @Test
    void lotacaoDeveMostrarMensagemCompreensivel() {
        String tela = executarComEntrada("1\nAAA1A11\nCARRO\n1\nBBB2B22\nMOTO\n0\n");

        assertTrue(tela.contains("Erro: Não há vaga livre no momento."));
    }
}
