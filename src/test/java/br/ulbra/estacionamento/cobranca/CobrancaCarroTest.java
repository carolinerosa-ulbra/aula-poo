package br.ulbra.estacionamento.cobranca;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CobrancaCarroTest {

    @Test
    void deveCobrarDezCentavosPorMinuto() {
        PoliticaDeCobranca politica = new CobrancaCarro();

        double valor = politica.calcularValor(60);

        assertEquals(6.00, valor, 0.001);
    }

    @Test
    void deveCobrarZeroQuandoNaoHouveTempo() {
        PoliticaDeCobranca politica = new CobrancaCarro();

        assertEquals(0.0, politica.calcularValor(0), 0.001);
    }
}
