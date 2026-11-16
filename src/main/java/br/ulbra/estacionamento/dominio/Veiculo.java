package br.ulbra.estacionamento.dominio;

public abstract class Veiculo {

    private static final String FORMATO_PLACA = "[A-Z]{3}[0-9][A-Z0-9][0-9]{2}";

    private final String placa;

    protected Veiculo(String placa) {
        if (placa == null) {
            throw new PlacaInvalidaException(null);
        }
        String normalizada = normalizarPlaca(placa);
        if (!normalizada.matches(FORMATO_PLACA)) {
            throw new PlacaInvalidaException(placa);
        }
        this.placa = normalizada;
    }

    /** Mantém a mesma chave de placa na entrada e na busca do ticket na saída. */
    public static String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }

    /** Padroniza o tipo antes de escolher o veículo, sem esconder o valor original do erro. */
    public static String normalizarTipo(String tipo) {
        return tipo == null ? "" : tipo.trim().toUpperCase();
    }
    public String getPlaca() {
        return placa;
    }

    public abstract String getTipo();
}
