package br.ulbra.estacionamento.repositorio;

import java.time.LocalDateTime;

public record RegistroCobranca(String placa, String tipo,
                               LocalDateTime entrada, LocalDateTime saida, double valor) { }
