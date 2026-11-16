package br.ulbra.estacionamento.cobranca;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CobrancaMotoTest {

    @Test
    void deveCobrarSeisCentavosPorMinuto() {
        PoliticaDeCobranca politica = new CobrancaMoto();

        double valor = politica.calcularValor(60);

        assertEquals(3.60, valor, 0.001);
    }

    @ParameterizedTest
    @CsvSource({"0, 0.0", "1, 0.06", "60, 3.6"})
    void deveCobrarMotoConformeOsMinutosCompletos(long minutos, double esperado) {
        PoliticaDeCobranca politica = new CobrancaMoto();

        assertEquals(esperado, politica.calcularValor(minutos), 0.001);
    }
}
