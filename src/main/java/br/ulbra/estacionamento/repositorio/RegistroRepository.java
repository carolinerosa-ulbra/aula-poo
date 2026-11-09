package br.ulbra.estacionamento.repositorio;


import java.util.List;

public interface RegistroRepository {

    void salvar(RegistroCobranca registro);

    List<RegistroCobranca> listarTodos();
}
