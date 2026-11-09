package br.ulbra.estacionamento;

public class Carro extends Veiculo {

    public Carro(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "CARRO";
    }
}
