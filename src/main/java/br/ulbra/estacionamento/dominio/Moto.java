package br.ulbra.estacionamento.dominio;

public class Moto extends Veiculo {

    public Moto(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "MOTO";
    }
}
