package br.ulbra.estacionamento.dominio;

import br.ulbra.estacionamento.cobranca.PoliticaDeCobranca;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Ticket {

    private final Veiculo veiculo;
    private final PoliticaDeCobranca politica;
    private final LocalDateTime entrada;

    public Ticket(Veiculo veiculo, PoliticaDeCobranca politica, LocalDateTime entrada) {
        this.veiculo = veiculo;
        this.politica = politica; // ponto de ARMAZENAMENTO: referência à interface
        this.entrada = entrada;
    }

    public double fechar(LocalDateTime saida) {
        if (saida.isBefore(entrada)) {
            throw new PeriodoInvalidoException();
        }
        long minutos = ChronoUnit.MINUTES.between(entrada, saida);
        return politica.calcularValor(minutos); // ponto de EXECUÇÃO do polimorfismo
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }
}
