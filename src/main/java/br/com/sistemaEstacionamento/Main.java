package main.java.br.com.sistemaEstacionamento;
import main.java.br.com.sistemaEstacionamento.model.TipoVaga;
import main.java.br.com.sistemaEstacionamento.model.Vaga;
import main.java.br.com.sistemaEstacionamento.model.Veiculo;
import main.java.br.com.sistemaEstacionamento.model.Ticket;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 1. Criação das vagas do estacionamento
        List<Vaga> vagas = new ArrayList<>();
        vagas.add(new Vaga(1, TipoVaga.COMUM));
        vagas.add(new Vaga(2, TipoVaga.COMUM));
        vagas.add(new Vaga(3, TipoVaga.DEFICIENTE));
        vagas.add(new Vaga(4, TipoVaga.MENSALISTA));

        System.out.println("=== Vagas cadastradas ===");
        for (Vaga v : vagas) {
            System.out.println(v + " - Disponivel: " + v.estaDisponivel());
        }

        // 2. Criação de veículos
        Veiculo carro1 = new Veiculo("ABC-1234", "Fiat Uno", "Branco", false);
        Veiculo carro2 = new Veiculo("XYZ-9876", "Honda Civic", "Preto", true); // mensalista

        // 3. Escolhendo vagas disponíveis
        Vaga vagaEscolhida1 = vagas.get(0);
        Vaga vagaEscolhida2 = vagas.get(3);

        // 4. Entrada dos veículos (abre o ticket)
        Ticket ticket1 = new Ticket(carro1, vagaEscolhida1);
        Ticket ticket2 = new Ticket(carro2, vagaEscolhida2);

        System.out.println("\n=== Entrada registrada ===");
        System.out.println(carro1 + " entrou na " + vagaEscolhida1);
        System.out.println(carro2 + " entrou na " + vagaEscolhida2);
        System.out.println("Vaga 1 disponivel apos entrada? " + vagaEscolhida1.estaDisponivel());

        // 5. Simula o tempo de permanência do veículo no estacionamento
        Thread.sleep(2000); // 2 segundos, apenas para simular passagem de tempo

        // 6. Saída do veículo comum (fecha o ticket e calcula o valor)
        ticket1.fecharTicket();
        System.out.println("\n=== Saida registrada ===");
        System.out.println(carro1 + " saiu.");
        System.out.println("Tempo de permanencia: " + ticket1.tempoPermanencia().toSeconds() + "s");
        System.out.println("Valor a pagar: R$ " + ticket1.calcularValor());
        System.out.println("Vaga 1 disponivel apos saida? " + vagaEscolhida1.estaDisponivel());

        // 7. Saída do veículo mensalista (não paga)
        ticket2.fecharTicket();
        System.out.println("\n=== Saida registrada ===");
        System.out.println(carro2 + " saiu.");
        System.out.println("Valor a pagar (mensalista): R$ " + ticket2.calcularValor());
    }
}