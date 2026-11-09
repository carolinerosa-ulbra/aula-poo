package br.ulbra.estacionamento.app;

import br.ulbra.estacionamento.dominio.EstacionamentoException;
import br.ulbra.estacionamento.dominio.RegistroCobranca;
import br.ulbra.estacionamento.repositorio.RepositorioException;
import br.ulbra.estacionamento.servico.ServicoDeEstacionamento;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.function.Supplier;

/** Camada de APRESENTAÇÃO: lê o que o usuário digita, chama o serviço e mostra mensagens. Sem regras de negócio. */
public class MenuConsole {

    private final ServicoDeEstacionamento servico;
    private final Scanner entrada;
    private final PrintStream saida;
    private final Supplier<LocalDateTime> relogio;

    public MenuConsole(ServicoDeEstacionamento servico, Scanner entrada,
                       PrintStream saida, Supplier<LocalDateTime> relogio) {
        this.servico = servico;
        this.entrada = entrada;
        this.saida = saida;
        this.relogio = relogio;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcao = entrada.nextLine().trim();
            try {
                continuar = tratar(opcao);
            } catch (EstacionamentoException e) {
                saida.println("Erro: " + e.getMessage());          // regra de negócio violada
            } catch (RepositorioException e) {
                saida.println("Falha ao acessar os registros. Tente novamente.");  // erro técnico
            }
        }
        saida.println("Encerrado.");
    }

    private boolean tratar(String opcao) {
        switch (opcao) {
            case "1" -> registrarEntrada();
            case "2" -> registrarSaida();
            case "3" -> listarRegistros();
            case "0" -> {
                return false;
            }
            default -> saida.println("Opção inválida: \"" + opcao + "\". Digite 0, 1, 2 ou 3.");
        }
        return true;
    }

    private void registrarEntrada() {
        saida.print("Placa: ");
        String placa = entrada.nextLine();
        saida.print("Tipo (CARRO ou MOTO): ");
        String tipo = entrada.nextLine();
        servico.registrarEntrada(placa, tipo, relogio.get());
        saida.println("Entrada registrada. Vagas livres: " + servico.vagasLivres());
    }

    private void registrarSaida() {
        saida.print("Placa: ");
        String placa = entrada.nextLine();
        RegistroCobranca registro = servico.registrarSaida(placa, relogio.get());
        saida.printf("Saída registrada. Valor: R$ %.2f%n", registro.valor());
    }

    private void listarRegistros() {
        for (RegistroCobranca registro : servico.listarRegistros()) {
            saida.printf("%s | %s | R$ %.2f%n", registro.placa(), registro.tipo(), registro.valor());
        }
        saida.printf("Total arrecadado: R$ %.2f%n", servico.totalArrecadado());
    }

    private void mostrarMenu() {
        saida.println();
        saida.println("1 - Registrar entrada | 2 - Registrar saída | 3 - Listar registros | 0 - Sair");
        saida.print("Opção: ");
    }
}
