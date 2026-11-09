package br.ulbra.estacionamento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CobrancaMotoTest {

    @Test
    void deveCobrarSeisCentavosPorMinuto() {
        PoliticaDeCobranca politica = new CobrancaMoto();

        double valor = politica.calcularValor(60);

        assertEquals(3.60, valor, 0.001);
    }
}
