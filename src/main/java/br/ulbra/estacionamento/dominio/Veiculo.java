package br.ulbra.estacionamento.dominio;

public abstract class Veiculo {

    private static final String FORMATO_PLACA = "[A-Z]{3}[0-9][A-Z0-9][0-9]{2}";

    private final String placa;

    protected Veiculo(String placa) {
        if (placa == null) {
            throw new PlacaInvalidaException(null);
        }
        String normalizada = placa.trim().toUpperCase();
        if (!normalizada.matches(FORMATO_PLACA)) {
            throw new PlacaInvalidaException(placa);
        }
        this.placa = normalizada;
    }

    public String getPlaca() {
        return placa;
    }

    public abstract String getTipo();
}
