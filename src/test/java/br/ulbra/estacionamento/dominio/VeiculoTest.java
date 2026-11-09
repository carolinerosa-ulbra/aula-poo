package br.ulbra.estacionamento.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class VeiculoTest {

    @Test
    void deveNormalizarAPlacaParaMaiusculas() {
        Veiculo carro = new Carro(" abc1d23 ");

        assertEquals("ABC1D23", carro.getPlaca());
    }

    @Test
    void deveRecusarPlacaForaDoFormato() {
        PlacaInvalidaException erro = assertThrows(PlacaInvalidaException.class,
                () -> new Carro("123"));

        assertEquals("Placa inválida: \"123\". Use o formato ABC1D23.", erro.getMessage());
    }

    @Test
    void deveRecusarPlacaNula() {
        assertThrows(PlacaInvalidaException.class, () -> new Moto(null));
    }
}
