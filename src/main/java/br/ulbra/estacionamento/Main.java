package br.ulbra.estacionamento;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Estacionamento estacionamento = new Estacionamento(3);
        LocalDateTime entrada = LocalDateTime.of(2026, 10, 19, 19, 0);

        System.out.println("Vagas livres: " + estacionamento.vagasLivres());

        Ticket ticketCarro = estacionamento.registrarEntrada(new Carro("ABC1D23"), entrada);
        Ticket ticketMoto = estacionamento.registrarEntrada(new Moto("XYZ9K88"), entrada);
        System.out.println("Vagas livres apos 2 entradas: " + estacionamento.vagasLivres());

        double valorCarro = estacionamento.registrarSaida(ticketCarro, entrada.plusMinutes(60));
        System.out.printf("Carro %s | 60 min | R$ %.2f%n", ticketCarro.getVeiculo().getPlaca(), valorCarro);

        double valorMoto = estacionamento.registrarSaida(ticketMoto, entrada.plusMinutes(60));
        System.out.printf("Moto  %s | 60 min | R$ %.2f%n", ticketMoto.getVeiculo().getPlaca(), valorMoto);

        System.out.printf("Total arrecadado: R$ %.2f%n", valorCarro + valorMoto);
        System.out.println("Vagas livres no fim: " + estacionamento.vagasLivres());
    }
}
