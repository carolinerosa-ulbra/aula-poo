package br.ulbra.estacionamento.dominio;

public class Moto extends Veiculo {

    public static final String TIPO = "MOTO";

    public Moto(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
