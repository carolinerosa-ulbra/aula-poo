# Sistema de Estacionamento — Encontro 14

Exemplo de código da professora Caroline Pacheco da Rosa para Fundamentos de Programação Orientada a Objetos — ADS, ULBRA, 2026/2.

O sistema registra entradas e saídas de carros e motos, controla vagas, calcula a cobrança por minuto e guarda o histórico em CSV. A interface é um menu de console. O projeto completo está na raiz deste repositório; os alunos não precisam dos materiais locais da professora.

## Regras de negócio

| Regra | Comportamento |
|---|---|
| RN1 | O valor cobrado depende do tipo do veículo. |
| RN2 | Carro paga R$ 0,10 por minuto. |
| RN3 | Moto paga R$ 0,06 por minuto. |
| RN4 | A entrada ocupa uma vaga livre; sem vaga, a entrada é recusada. |
| RN5 | A saída cobra pela política escolhida na entrada e libera a vaga após a gravação da cobrança. |
| RN6 | A placa segue o formato `ABC1D23`: três letras, um dígito, uma letra ou dígito e dois dígitos, sem hífen. Espaços nas pontas são removidos e letras passam para maiúsculas. O padrão `ABC1234` também é aceito. |
| RN7 | O mesmo veículo, identificado pela placa, não pode entrar duas vezes enquanto houver ticket aberto. |
| RN8 | A saída não pode ser anterior à entrada. Saída igual à entrada é válida e custa zero. |
| RN9 | Somente os tipos `CARRO` e `MOTO` são aceitos. Espaços nas pontas e diferenças entre maiúsculas e minúsculas são normalizados. |

A saída exige um ticket aberto para a placa. O tempo usa minutos completos, sem arredondar para cima: em 60 minutos, carro paga R$ 6,00 e moto paga R$ 3,60. O exemplo mantém `double` e compara os valores dos testes com margem 0.001.

No v14, uma falha ao salvar mantém a vaga ocupada e o ticket aberto para permitir nova tentativa. A ordem do serviço é **calcular e validar → salvar → liberar vaga → remover ticket**.

## Como executar em uma máquina limpa

Pré-requisitos:

- Git para clonar o projeto.
- JDK **25** (LTS), conferido com `java -version`.
- Maven **3.9.x**, conferido com `mvn -version`.
- Acesso ao Maven Central na primeira compilação para baixar os plugins e o JUnit Jupiter **6.0.0**.

Depois de publicar a atualização no GitHub, execute no PowerShell ou Bash:

```sh
git clone https://github.com/carolinerosa-ulbra/aula-poo.git
cd aula-poo
mvn test
mvn compile
java -cp target/classes br.ulbra.estacionamento.app.Main
```

O menu aceita `1` para registrar entrada, `2` para registrar saída, `3` para listar o histórico e o total e `0` para encerrar. Na entrada, informe a placa e o tipo. Na saída, informe a placa. Erros previstos mostram uma mensagem e o menu continua disponível.

Para reproduzir a execução abaixo, use uma pasta sem histórico anterior. No PowerShell:

```powershell
"1`nABC1D23`nCARRO`n1`n123`nCARRO`n2`nZZZ9Z99`n0" | java -cp target/classes br.ulbra.estacionamento.app.Main
```

No Bash:

```sh
printf '1\nABC1D23\nCARRO\n1\n123\nCARRO\n2\nZZZ9Z99\n0\n' | java -cp target/classes br.ulbra.estacionamento.app.Main
```

`Main` cria três vagas, escolhe `RegistroRepositoryArquivo(Path.of("registros.csv"))` e monta o serviço e o menu. O CSV usa `;`, datas ISO e valor decimal com ponto. Cada saída concluída acrescenta um registro ao arquivo no diretório de execução. O histórico sobrevive a novas instâncias; vagas e tickets abertos ficam apenas na memória da execução atual.

## Estrutura de pacotes e dependências

As fontes estão em `src/main/java/br/ulbra/estacionamento`; os testes seguem a mesma organização em `src/test/java`.

| Pacote | Responsabilidade |
|---|---|
| `app` | `Main` monta as dependências; `MenuConsole` lê entradas, chama o serviço e apresenta resultados e erros. |
| `servico` | `ServicoDeEstacionamento` controla os tickets por placa e coordena as operações do domínio com a gravação dos registros. |
| `dominio` | `Veiculo`, `Carro`, `Moto`, `Vaga`, `Ticket`, `Estacionamento`, `RegistroCobranca` e as exceções de negócio. |
| `cobranca` | `PoliticaDeCobranca`, `CobrancaCarro` e `CobrancaMoto`. |
| `repositorio` | `RegistroRepository`, implementações em memória e arquivo e `RepositorioException`. |

As dependências são `app → servico → dominio`, `servico → repositorio → dominio` e `dominio → cobranca`. A apresentação também importa tipos para compor os objetos, mostrar registros e tratar erros. **O domínio não importa `repositorio`.**

`Vaga` e `Estacionamento` permanecem no mesmo pacote para usar o construtor e os métodos de alteração com acesso de pacote. A coleção de vagas é não modificável para quem a consulta.

O polimorfismo tem três pontos comentados no código: decisão em `Estacionamento.escolherPolitica`, armazenamento da referência à interface no `Ticket` e execução em `Ticket.fechar`. `Carro.TIPO` e `Moto.TIPO` concentram os nomes dos tipos; `Veiculo.normalizarPlaca` e `Veiculo.normalizarTipo` concentram sua normalização.

Confira os [diagramas de classes e dependências em Mermaid](docs/diagrama-classes/README.md), atualizados com as 24 classes, interfaces, record e exceções de produção.

## Erros e tratamento

| Situação | Exceção | Camada que lança | Camada que trata | Mensagem ao usuário |
|---|---|---|---|---|
| Contrato comum das violações de negócio | `EstacionamentoException` | Domínio ou serviço, por meio das subclasses; a base não é lançada diretamente | Apresentação (`MenuConsole`) | `Erro: <mensagem da subclasse>` |
| Placa nula ou fora do formato | `PlacaInvalidaException` | Domínio (`Veiculo`) | Apresentação | `Erro: Placa inválida: "<placa>". Use o formato ABC1D23.` |
| Tipo diferente de carro ou moto | `TipoDeVeiculoInvalidoException` | Serviço (criação do veículo) ou domínio (escolha da política) | Apresentação | `Erro: Tipo de veículo não aceito: "<tipo>". Use CARRO ou MOTO.` |
| Nenhuma vaga livre | `EstacionamentoLotadoException` | Domínio (`Estacionamento`) | Apresentação | `Erro: Não há vaga livre no momento.` |
| Saída sem ticket aberto | `TicketNaoEncontradoException` | Serviço | Apresentação | `Erro: Não há ticket aberto para a placa <placa>.` |
| Nova entrada da mesma placa | `VeiculoJaEstacionadoException` | Serviço | Apresentação | `Erro: O veículo de placa <placa> já está estacionado.` |
| Saída anterior à entrada | `PeriodoInvalidoException` | Domínio (`Ticket`) | Apresentação | `Erro: A saída não pode ser anterior à entrada.` |
| Falha de leitura, gravação ou linha inválida no CSV | `RepositorioException` | Repositório | Apresentação | `Falha ao acessar os registros. Tente novamente.` |

As exceções de negócio são não verificadas e herdam de `EstacionamentoException`. O erro técnico `RepositorioException` tem hierarquia separada e preserva a causa quando ela existe. O `try/catch` fica dentro do laço do menu. A opção desconhecida é tratada diretamente na apresentação: `Opção inválida: "<opcao>". Digite 0, 1, 2 ou 3.`

## Decisões de projeto

| Contexto | Decisão | Consequência |
|---|---|---|
| Cobranças variam conforme o tipo | Interface `PoliticaDeCobranca`; o ticket recebe a política no construtor | O ticket executa a cobrança sem selecionar o tipo novamente |
| Ocupação precisa permanecer sob controle | Composição de `Vaga` em `Estacionamento`, acesso de pacote e lista não modificável | A consulta não permite remover ou substituir vagas |
| Persistência não é regra de domínio | O serviço usa `RegistroRepository`; o record fica no domínio | Memória e CSV usam o mesmo contrato; domínio não importa repositório |
| Erros de negócio e técnicos precisam de mensagens diferentes | Exceções não verificadas, base comum de negócio e exceção técnica separada | O menu trata os erros previstos e continua recebendo operações |
| Falhas não devem deixar uma saída pela metade | Validar e salvar antes de liberar a vaga e remover o ticket | Uma falha de gravação permite nova tentativa com a vaga ainda ocupada |
| Entrada e saída precisam da mesma chave | Métodos explícitos de normalização de placa e tipo | Preserva os formatos, os resultados para `null` e as mensagens |
| Tipos repetidos podem divergir | Constantes `Carro.TIPO` e `Moto.TIPO`, sem enum | Os literais têm uma definição; `getTipo()` e CSV continuam com `String` |

Os mesmos registros estão detalhados em [docs/decisoes.md](docs/decisoes.md), no formato Contexto → Decisão → Consequência.

## Como o código evolui

| Tag | Encontro | Evolução |
|---|---|---|
| `v11` | 11 — Reciclagem | Pacote único, herança, interface, polimorfismo, composição e 10 testes. O exemplo de carro e moto por 60 minutos soma R$ 9,60. |
| `v12` | 12 — Pacotes e repositório | Pacotes por responsabilidade, record no repositório e persistência por memória ou CSV. O estacionamento ainda usa o repositório. São 15 testes. |
| `v13` | 13 — Exceções e camadas | Exceções próprias, serviço, menu e raiz de composição. O record vai para o domínio e o serviço usa o repositório. São 29 testes: 27 da referência e dois de memória preservados. |
| `v14` | 14 — Testes, refatoração e documentação | Cobrança parametrizada e casos-limite, normalização centralizada, constantes de tipo e saída consistente após falha de gravação. Documentação e diagramas atualizados. São 44 testes. |

Com o trabalho local salvo, navegue pelas aulas:

```sh
git checkout v12
mvn clean test
java -cp target/classes br.ulbra.estacionamento.app.Main
git checkout main
```

As tags abrem uma versão de estudo em estado destacado (*detached HEAD*). No v11, o nome executável é `br.ulbra.estacionamento.Main`. Ao trocar de versão, use `mvn clean test` para remover classes compiladas de pacotes antigos. As tags v11, v12 e v13 foram preservadas; a tag v14 identifica o estado do Encontro 14. A publicação está registrada no relatório do encontro.

## Saídas reais da verificação

O passo a passo foi executado em um clone novo do repositório local, com JDK 25.0.4 e Maven 3.9.16. O cache Maven foi redirecionado apenas no ambiente de execução para uma pasta temporária; isso não altera o POM nem acrescenta arquivos ao projeto.

Trecho final copiado da execução de `mvn test`:

```text
[INFO] Results:
[INFO]
[INFO] Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  13.865 s
[INFO] Finished at: 2026-11-16T14:02:36-03:00
[INFO] ------------------------------------------------------------------------
```

Saída copiada da execução do menu com o pipe mostrado acima:

```text
1 - Registrar entrada | 2 - Registrar saída | 3 - Listar registros | 0 - Sair
Opção: Placa: Tipo (CARRO ou MOTO): Entrada registrada. Vagas livres: 2

1 - Registrar entrada | 2 - Registrar saída | 3 - Listar registros | 0 - Sair
Opção: Placa: Tipo (CARRO ou MOTO): Erro: Placa inválida: "123". Use o formato ABC1D23.

1 - Registrar entrada | 2 - Registrar saída | 3 - Listar registros | 0 - Sair
Opção: Placa: Erro: Não há ticket aberto para a placa ZZZ9Z99.

1 - Registrar entrada | 2 - Registrar saída | 3 - Listar registros | 0 - Sair
Opção: Encerrado.
```

Os testes usam horários fixos e diretórios `@TempDir`. A sequência da saída com falha de gravação é verificada com um repositório falso, incluindo a nova tentativa. O [registro de melhorias](docs/melhorias.md) apresenta os casos adicionados e a falha observada antes da correção.

## Documentação e uso de IA

- [Decisões de projeto](docs/decisoes.md).
- [Melhorias do Encontro 14](docs/melhorias.md).
- [Relatório do Encontro 14](docs/relatorio-encontro14.md), com commits, testes e publicação.
- [Notas da release v14](docs/notas-v14.md).
- [Relatório dos encontros 11 a 13](docs/relatorio-atualizacao.md), preservado como registro histórico.
- [Planejamento](docs/planejamento.md), [backlog](docs/backlog.md) e [registro anterior de IA](docs/IA.md).

Codex foi usado para ampliar os testes, executar as refatorações e a correção solicitadas e atualizar a documentação. A professora definiu o domínio, os materiais de referência e os requisitos dos encontros. O POM continua com Java 25, JUnit Jupiter 6.0.0 e Surefire 3.5.4, sem novas dependências.