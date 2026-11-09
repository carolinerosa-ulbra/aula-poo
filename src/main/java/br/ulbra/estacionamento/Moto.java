package br.ulbra.estacionamento;

public class Moto extends Veiculo {

    public Moto(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "MOTO";
    }
}
