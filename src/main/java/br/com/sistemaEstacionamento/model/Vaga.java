package main.java.br.com.sistemaEstacionamento.model;

public class Vaga {

    private int numero;
    private TipoVaga tipo;
    private boolean ocupada;

    public Vaga(int numero, TipoVaga tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupada = false;
    }

    public void ocupar() {
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
    }

    public boolean estaDisponivel() {
        return !ocupada;
    }

    public int getNumero() {
        return numero;
    }

    public TipoVaga getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Vaga " + numero + " (" + tipo + ")";
    }
}