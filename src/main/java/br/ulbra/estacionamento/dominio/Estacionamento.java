package br.ulbra.estacionamento.dominio;

import br.ulbra.estacionamento.cobranca.*;
import br.ulbra.estacionamento.repositorio.RegistroRepository;
import br.ulbra.estacionamento.repositorio.RegistroCobranca;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estacionamento {

    private final List<Vaga> vagas = new ArrayList<>();
    private final RegistroRepository repositorio;

    // COMPOSIÇÃO: as vagas nascem aqui dentro e não existem sem o estacionamento.
    public Estacionamento(int quantidadeDeVagas, RegistroRepository repositorio) {
        this.repositorio = repositorio;
        for (int numero = 1; numero <= quantidadeDeVagas; numero++) {
            vagas.add(new Vaga(numero));
        }
    }

    // Devolve uma visão somente leitura: quem chama não consegue alterar a lista interna.
    public List<Vaga> getVagas() {
        return Collections.unmodifiableList(vagas);
    }

    public int vagasLivres() {
        int livres = 0;
        for (Vaga vaga : vagas) {
            if (!vaga.isOcupada()) {
                livres++;
            }
        }
        return livres;
    }

    public Ticket registrarEntrada(Veiculo veiculo, LocalDateTime entrada) {
        Vaga vaga = buscarVagaLivre();
        vaga.ocupar(veiculo);
        PoliticaDeCobranca politica = escolherPolitica(veiculo);   // ponto de DECISÃO
        return new Ticket(veiculo, politica, entrada);
    }

    public double registrarSaida(Ticket ticket, LocalDateTime saida) {
        for (Vaga vaga : vagas) {
            if (vaga.getVeiculo() == ticket.getVeiculo()) {
                vaga.liberar();
            }
        }
        double valor = ticket.fechar(saida);
        repositorio.salvar(new RegistroCobranca(ticket.getVeiculo().getPlaca(),
                ticket.getVeiculo().getTipo(), ticket.getEntrada(), saida, valor));
        return valor;
    }

    private Vaga buscarVagaLivre() {
        for (Vaga vaga : vagas) {
            if (!vaga.isOcupada()) {
                return vaga;
            }
        }
        throw new IllegalStateException("Não há vaga livre.");
    }

    // Único lugar do sistema que olha o tipo do veículo.
    private PoliticaDeCobranca escolherPolitica(Veiculo veiculo) {
        return switch (veiculo.getTipo()) {
            case "CARRO" -> new CobrancaCarro();
            case "MOTO" -> new CobrancaMoto();
            default -> throw new IllegalArgumentException("Tipo sem política: " + veiculo.getTipo());
        };
    }
}
