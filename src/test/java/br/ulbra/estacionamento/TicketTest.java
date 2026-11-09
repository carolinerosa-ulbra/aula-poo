package br.ulbra.estacionamento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TicketTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 10, 19, 19, 0);

    @Test
    void deveCobrarSegundoAPoliticaRecebida() {
        Ticket ticket = new Ticket(new Carro("ABC1D23"), new CobrancaCarro(), ENTRADA);

        double valor = ticket.fechar(ENTRADA.plusMinutes(60));

        assertEquals(6.00, valor, 0.001);
    }

    @Test
    void mesmoTicketComPoliticaDiferenteDeveCobrarDiferente() {
        Ticket ticket = new Ticket(new Moto("XYZ9K88"), new CobrancaMoto(), ENTRADA);

        double valor = ticket.fechar(ENTRADA.plusMinutes(60));

        assertEquals(3.60, valor, 0.001);
    }
}
