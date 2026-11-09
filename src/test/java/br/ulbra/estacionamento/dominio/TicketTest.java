package br.ulbra.estacionamento.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ulbra.estacionamento.cobranca.CobrancaCarro;
import br.ulbra.estacionamento.cobranca.CobrancaMoto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TicketTest {

    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 11, 9, 19, 0);

    @Test
    void deveCobrarSegundoAPoliticaRecebida() {
        Ticket ticket = new Ticket(new Carro("ABC1D23"), new CobrancaCarro(), ENTRADA);

        assertEquals(6.00, ticket.fechar(ENTRADA.plusMinutes(60)), 0.001);
    }

    @Test
    void mesmoTicketComPoliticaDiferenteDeveCobrarDiferente() {
        Ticket ticket = new Ticket(new Moto("XYZ9K88"), new CobrancaMoto(), ENTRADA);

        assertEquals(3.60, ticket.fechar(ENTRADA.plusMinutes(60)), 0.001);
    }

    @Test
    void deveRecusarSaidaAnteriorAEntrada() {
        Ticket ticket = new Ticket(new Carro("ABC1D23"), new CobrancaCarro(), ENTRADA);

        assertThrows(PeriodoInvalidoException.class, () -> ticket.fechar(ENTRADA.minusMinutes(1)));
    }
}
