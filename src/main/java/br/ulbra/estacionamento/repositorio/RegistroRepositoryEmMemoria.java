package br.ulbra.estacionamento.repositorio;

import br.ulbra.estacionamento.dominio.RegistroCobranca;
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
