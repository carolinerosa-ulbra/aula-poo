package br.ulbra.estacionamento.dominio;

import br.ulbra.estacionamento.cobranca.CobrancaCarro;
import br.ulbra.estacionamento.cobranca.CobrancaMoto;
import br.ulbra.estacionamento.cobranca.PoliticaDeCobranca;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estacionamento {

    private final List<Vaga> vagas = new ArrayList<>();

    // Composição: o estacionamento cria e controla suas vagas.
    public Estacionamento(int quantidadeDeVagas) {
        for (int numero = 1; numero <= quantidadeDeVagas; numero++) {
            vagas.add(new Vaga(numero));
        }
    }

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
        // Validar antes de alterar: um tipo sem política não pode ocupar a vaga.
        PoliticaDeCobranca politica = escolherPolitica(veiculo); // ponto de DECISÃO
        vaga.ocupar(veiculo);
        return new Ticket(veiculo, politica, entrada);
    }

    public double registrarSaida(Ticket ticket, LocalDateTime saida) {
        // Validar antes de alterar: uma saída inválida mantém a vaga ocupada.
        double valor = ticket.fechar(saida);
        for (Vaga vaga : vagas) {
            if (vaga.getVeiculo() == ticket.getVeiculo()) {
                vaga.liberar();
            }
        }
        return valor;
    }

    private Vaga buscarVagaLivre() {
        for (Vaga vaga : vagas) {
            if (!vaga.isOcupada()) {
                return vaga;
            }
        }
        throw new EstacionamentoLotadoException();
    }

    // Único ponto que escolhe a política pelo tipo do veículo.
    private PoliticaDeCobranca escolherPolitica(Veiculo veiculo) {
        return switch (veiculo.getTipo()) {
            case "CARRO" -> new CobrancaCarro();
            case "MOTO" -> new CobrancaMoto();
            default -> throw new TipoDeVeiculoInvalidoException(veiculo.getTipo());
        };
    }
}
