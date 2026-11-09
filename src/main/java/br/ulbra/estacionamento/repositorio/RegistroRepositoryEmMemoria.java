package br.ulbra.estacionamento.repositorio;


import java.util.ArrayList;
import java.util.List;

public class RegistroRepositoryEmMemoria implements RegistroRepository {

    private final List<RegistroCobranca> registros = new ArrayList<>();

    @Override
    public void salvar(RegistroCobranca registro) {
        registros.add(registro);
    }

    @Override
    public List<RegistroCobranca> listarTodos() {
        return List.copyOf(registros);
    }
}
