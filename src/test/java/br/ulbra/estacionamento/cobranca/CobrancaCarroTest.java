package br.ulbra.estacionamento.cobranca;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({"0, 0.0", "1, 0.10", "60, 6.0"})
    void deveCobrarCarroConformeOsMinutosCompletos(long minutos, double esperado) {
        PoliticaDeCobranca politica = new CobrancaCarro();

        assertEquals(esperado, politica.calcularValor(minutos), 0.001);
    }
}
