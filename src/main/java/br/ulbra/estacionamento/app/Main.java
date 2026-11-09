package br.ulbra.estacionamento.app;

import br.ulbra.estacionamento.dominio.Estacionamento;
import br.ulbra.estacionamento.repositorio.RegistroRepository;
import br.ulbra.estacionamento.repositorio.RegistroRepositoryArquivo;
import br.ulbra.estacionamento.servico.ServicoDeEstacionamento;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Scanner;

/** Raiz de composição: é o ÚNICO lugar que escolhe as implementações concretas. */
public class Main {

    public static void main(String[] args) {
        RegistroRepository repositorio = new RegistroRepositoryArquivo(Path.of("registros.csv"));
        Estacionamento estacionamento = new Estacionamento(3);
        ServicoDeEstacionamento servico = new ServicoDeEstacionamento(estacionamento, repositorio);

        Scanner entrada = new Scanner(System.in);
        new MenuConsole(servico, entrada, System.out, LocalDateTime::now).executar();
    }
}
