package main.java.br.com.sistemaEstacionamento.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Ticket {

    private static final double VALOR_POR_HORA = 6.0;
    private static final double VALOR_FRACAO_MINIMA = 4.0; // primeira hora ou fração

    private LocalDateTime horarioEntrada;
    private LocalDateTime horarioSaida;
    private Veiculo veiculo;
    private Vaga vaga;

    public Ticket(Veiculo veiculo, Vaga vaga) {
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.horarioEntrada = LocalDateTime.now();
        this.vaga.ocupar();
    }

    public void fecharTicket() {
        this.horarioSaida = LocalDateTime.now();
        this.vaga.liberar();
    }

    public Duration tempoPermanencia() {
        LocalDateTime fim = (horarioSaida != null) ? horarioSaida : LocalDateTime.now();
        return Duration.between(horarioEntrada, fim);
    }

    public double calcularValor() {
        if (veiculo.ehMensalista()) {
            return 0.0;
        }

        Duration duracao = tempoPermanencia();
        long minutos = duracao.toMinutes();

        if (minutos <= 60) {
            return VALOR_FRACAO_MINIMA;
        }

        long horasExtras = (long) Math.ceil((minutos - 60) / 60.0);
        return VALOR_FRACAO_MINIMA + (horasExtras * VALOR_POR_HORA);
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }
}