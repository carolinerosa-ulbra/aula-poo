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

    /** Recebe as dependências para trocar o armazenamento sem alterar as regras de domínio. */
    public ServicoDeEstacionamento(Estacionamento estacionamento, RegistroRepository repositorio) {
        this.estacionamento = estacionamento;
        this.repositorio = repositorio;
    }

    /** Recusa duplicidade antes de ocupar a vaga e só guarda o ticket após uma entrada válida. */
    public Ticket registrarEntrada(String placa, String tipo, LocalDateTime entrada) {
        Veiculo veiculo = criarVeiculo(placa, tipo);
        if (ticketsAbertos.containsKey(veiculo.getPlaca())) {
            throw new VeiculoJaEstacionadoException(veiculo.getPlaca());
        }
        Ticket ticket = estacionamento.registrarEntrada(veiculo, entrada);
        ticketsAbertos.put(veiculo.getPlaca(), ticket);
        return ticket;
    }

    /** Valida e grava antes de liberar a vaga, preservando o ticket se o repositório falhar. */
    public RegistroCobranca registrarSaida(String placa, LocalDateTime saida) {
        String chave = Veiculo.normalizarPlaca(placa);
        Ticket ticket = ticketsAbertos.get(chave);
        if (ticket == null) {
            throw new TicketNaoEncontradoException(placa);
        }
        double valor = estacionamento.calcularValorDaSaida(ticket, saida);
        RegistroCobranca registro = new RegistroCobranca(chave, ticket.getVeiculo().getTipo(),
                ticket.getEntrada(), saida, valor);
        repositorio.salvar(registro);
        estacionamento.liberarVaga(ticket);
        ticketsAbertos.remove(chave);
        return registro;
    }

    /** Consulta o contrato de armazenamento para não depender de memória ou arquivo. */
    public List<RegistroCobranca> listarRegistros() {
        return repositorio.listarTodos();
    }

    /** Usa os mesmos registros do histórico para manter uma única fonte para o total. */
    public double totalArrecadado() {
        double total = 0;
        for (RegistroCobranca registro : repositorio.listarTodos()) {
            total += registro.valor();
        }
        return total;
    }

    /** Delega ao domínio para que a apresentação não conheça a coleção de vagas. */
    public int vagasLivres() {
        return estacionamento.vagasLivres();
    }

    private Veiculo criarVeiculo(String placa, String tipo) {
        String tipoNormalizado = Veiculo.normalizarTipo(tipo);
        return switch (tipoNormalizado) {
            case Carro.TIPO -> new Carro(placa);
            case Moto.TIPO -> new Moto(placa);
            default -> throw new TipoDeVeiculoInvalidoException(tipo);
        };
    }
}
