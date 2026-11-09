package br.ulbra.estacionamento.servico;

import br.ulbra.estacionamento.dominio.Carro;
import br.ulbra.estacionamento.dominio.Estacionamento;
import br.ulbra.estacionamento.dominio.Moto;
import br.ulbra.estacionamento.dominio.RegistroCobranca;
import br.ulbra.estacionamento.dominio.Ticket;
import br.ulbra.estacionamento.dominio.TicketNaoEncontradoException;
import br.ulbra.estacionamento.dominio.TipoDeVeiculoInvalidoException;
import br.ulbra.estacionamento.dominio.Veiculo;
import br.ulbra.estacionamento.dominio.VeiculoJaEstacionadoException;
import br.ulbra.estacionamento.repositorio.RegistroRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Orquestra os casos de uso. A apresentação chama o serviço; o serviço usa domínio e repositório. */
public class ServicoDeEstacionamento {

    private final Estacionamento estacionamento;
    private final RegistroRepository repositorio;
    private final Map<String, Ticket> ticketsAbertos = new HashMap<>();

    public ServicoDeEstacionamento(Estacionamento estacionamento, RegistroRepository repositorio) {
        this.estacionamento = estacionamento;
        this.repositorio = repositorio;
    }

    public Ticket registrarEntrada(String placa, String tipo, LocalDateTime entrada) {
        Veiculo veiculo = criarVeiculo(placa, tipo);
        if (ticketsAbertos.containsKey(veiculo.getPlaca())) {
            throw new VeiculoJaEstacionadoException(veiculo.getPlaca());
        }
        Ticket ticket = estacionamento.registrarEntrada(veiculo, entrada);
        ticketsAbertos.put(veiculo.getPlaca(), ticket);
        return ticket;
    }

    public RegistroCobranca registrarSaida(String placa, LocalDateTime saida) {
        String chave = placa == null ? "" : placa.trim().toUpperCase();
        Ticket ticket = ticketsAbertos.get(chave);
        if (ticket == null) {
            throw new TicketNaoEncontradoException(placa);
        }
        double valor = estacionamento.registrarSaida(ticket, saida);
        RegistroCobranca registro = new RegistroCobranca(chave, ticket.getVeiculo().getTipo(),
                ticket.getEntrada(), saida, valor);
        repositorio.salvar(registro);
        ticketsAbertos.remove(chave);
        return registro;
    }

    public List<RegistroCobranca> listarRegistros() {
        return repositorio.listarTodos();
    }

    public double totalArrecadado() {
        double total = 0;
        for (RegistroCobranca registro : repositorio.listarTodos()) {
            total += registro.valor();
        }
        return total;
    }

    public int vagasLivres() {
        return estacionamento.vagasLivres();
    }

    private Veiculo criarVeiculo(String placa, String tipo) {
        String tipoNormalizado = tipo == null ? "" : tipo.trim().toUpperCase();
        return switch (tipoNormalizado) {
            case "CARRO" -> new Carro(placa);
            case "MOTO" -> new Moto(placa);
            default -> throw new TipoDeVeiculoInvalidoException(tipo);
        };
    }
}
