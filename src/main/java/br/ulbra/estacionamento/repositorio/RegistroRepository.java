package br.ulbra.estacionamento.repositorio;

import br.ulbra.estacionamento.dominio.RegistroCobranca;
import java.util.List;

public interface RegistroRepository {

    void salvar(RegistroCobranca registro);

    List<RegistroCobranca> listarTodos();
}
