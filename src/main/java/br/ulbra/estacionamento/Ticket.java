package br.ulbra.estacionamento;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Ticket {

    private final Veiculo veiculo;
    private final PoliticaDeCobranca politica;
    private final LocalDateTime entrada;

    public Ticket(Veiculo veiculo, PoliticaDeCobranca politica, LocalDateTime entrada) {
        this.veiculo = veiculo;
        this.politica = politica;   // ponto de ARMAZENAMENTO: só uma referência
        this.entrada = entrada;
    }

    // A saída entra por parâmetro para o método ser testável (sem depender do relógio).
    public double fechar(LocalDateTime saida) {
        long minutos = ChronoUnit.MINUTES.between(entrada, saida);
        return politica.calcularValor(minutos);   // ponto de EXECUÇÃO
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }
}
