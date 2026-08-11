package main.java.br.com.sistemaEstacionamento.model;

public class Veiculo {

    private String placa;
    private String modelo;
    private String cor;
    private boolean mensalista;

    public Veiculo(String placa, String modelo, String cor, boolean mensalista) {
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.mensalista = mensalista;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public boolean ehMensalista() {
        return mensalista;
    }

    @Override
    public String toString() {
        return modelo + " (" + placa + ", " + cor + ")";
    }
}