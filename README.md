# Sistema de Estacionamento — Encontros 11 a 13

Projeto de demonstração da professora Caroline Pacheco da Rosa para Fundamentos de Programação Orientada a Objetos — ADS, ULBRA, 2026/2.

Aplicação de console para registrar a entrada de carros e motos, controlar vagas, calcular a cobrança por minuto e consultar o histórico em CSV. O projeto completo está na raiz do repositório; os alunos precisam apenas cloná-lo.

## Regras de negócio

| Regra | Comportamento |
|---|---|
| RN1 | O valor cobrado depende do tipo do veículo. |
| RN2 | Carro paga R$ 0,10 por minuto. |
| RN3 | Moto paga R$ 0,06 por minuto. |
| RN4 | A entrada ocupa uma vaga livre; sem vaga, a entrada é recusada. |
| RN5 | A saída libera a vaga e cobra pela política definida na entrada. |
| RN6 | A placa deve ter três letras, um dígito, uma letra ou dígito e dois dígitos, sem hífen: `ABC1D23` ou `ABC1234`. Espaços nas pontas são removidos e letras são convertidas para maiúsculas. |
| RN7 | Uma placa com ticket aberto não pode entrar novamente. |
| RN8 | A saída exige um ticket aberto para a placa informada. |
| RN9 | A saída não pode ser anterior à entrada; um período inválido mantém a vaga ocupada. |

O tempo usa minutos completos (`ChronoUnit.MINUTES`): 60 minutos custam R$ 6,00 para carro e R$ 3,60 para moto; zero minutos custa zero. O exemplo didático mantém `double`, conforme o material da aula.

## Pré-requisitos e execução

- JDK **25** (LTS), conferido com `java -version`.
- Maven **3.9.x**, conferido com `mvn -version`.
- Acesso ao Maven Central na primeira compilação para baixar plugins e JUnit Jupiter **6.0.0**.

Execute na pasta que contém `pom.xml`:

```sh
mvn compile
mvn test
java -cp target/classes br.ulbra.estacionamento.app.Main
```

O menu oferece `1` para entrada, `2` para saída, `3` para histórico e total arrecadado, e `0` para encerrar. Informe o tipo `CARRO` ou `MOTO`; espaços e diferenças de maiúsculas são normalizados pelo serviço. Erros previstos mostram uma mensagem e permitem continuar usando o menu.

Exemplo com entrada simulada no PowerShell:

```powershell
"1`nABC1D23`nCARRO`n2`nABC1D23`n3`n0" | java -cp target/classes br.ulbra.estacionamento.app.Main
```

No Bash:

```sh
printf '1\nABC1D23\nCARRO\n2\nABC1D23\n3\n0\n' | java -cp target/classes br.ulbra.estacionamento.app.Main
```

O `Main` cria três vagas e escolhe `RegistroRepositoryArquivo(Path.of("registros.csv"))`. O CSV é criado no diretório de execução e recebe novas linhas a cada saída. Usa `;`, datas ISO e valores com ponto decimal. O histórico sobrevive a uma nova instância; vagas e tickets abertos ficam apenas em memória durante a execução. Os testes usam repositório em memória e `@TempDir`, sem alterar o histórico da aplicação.

## Estrutura de pacotes

As fontes ficam em `src/main/java/br/ulbra/estacionamento`; os testes seguem os mesmos pacotes em `src/test/java`.

| Pacote | Classes e responsabilidade |
|---|---|
| `app` | `Main` monta as dependências; `MenuConsole` lê entradas, chama o serviço e apresenta resultados e erros. |
| `servico` | `ServicoDeEstacionamento` coordena entradas, tickets por placa, saídas e persistência. |
| `dominio` | `Veiculo`, `Carro`, `Moto`, `Vaga`, `Ticket`, `Estacionamento`, `RegistroCobranca` e exceções das regras de negócio. |
| `cobranca` | `PoliticaDeCobranca`, `CobrancaCarro` e `CobrancaMoto`. |
| `repositorio` | `RegistroRepository`, implementações em memória e arquivo, e `RepositorioException`. |

`Vaga` e `Estacionamento` ficam no mesmo pacote: o construtor, `ocupar` e `liberar` têm acesso de pacote. O estacionamento cria suas vagas (composição) e expõe uma lista não modificável. O domínio não importa o pacote `repositorio`.

O polimorfismo aparece em três pontos comentados: **decisão** em `Estacionamento.escolherPolitica`, **armazenamento** da referência à interface no `Ticket` e **execução** em `Ticket.fechar`. A política é escolhida antes de ocupar a vaga; o ticket é validado antes de liberar a vaga.

Veja os [diagramas de classes e dependências em Mermaid](docs/diagrama-classes/README.md).

## Erros e tratamento no v13

As seis exceções de negócio abaixo herdam de `EstacionamentoException`, que estende `RuntimeException`. `RepositorioException` também é não verificada, mas representa um erro técnico e preserva a causa quando envolve outra exceção.

| Situação | Exceção | Camada que lança | Camada que trata | Mensagem |
|---|---|---|---|---|
| Placa nula ou fora do formato | `PlacaInvalidaException` | Domínio (`Veiculo`) | Apresentação (`MenuConsole`) | `Placa inválida: "<placa>". Use o formato ABC1D23.` |
| Tipo diferente de carro ou moto | `TipoDeVeiculoInvalidoException` | Serviço (criação do veículo) ou domínio (escolha da política) | Apresentação | `Tipo de veículo não aceito: "<tipo>". Use CARRO ou MOTO.` |
| Nenhuma vaga livre | `EstacionamentoLotadoException` | Domínio (`Estacionamento`) | Apresentação | `Não há vaga livre no momento.` |
| Saída sem ticket aberto | `TicketNaoEncontradoException` | Serviço | Apresentação | `Não há ticket aberto para a placa <placa>.` |
| Nova entrada da mesma placa | `VeiculoJaEstacionadoException` | Serviço | Apresentação | `O veículo de placa <placa> já está estacionado.` |
| Saída anterior à entrada | `PeriodoInvalidoException` | Domínio (`Ticket`) | Apresentação | `A saída não pode ser anterior à entrada.` |
| Falha ao gravar ou ler o arquivo | `RepositorioException` | Repositório | Apresentação | Interna: `Não foi possível gravar o registro em <arquivo>` ou `Não foi possível ler os registros de <arquivo>`. Na tela: `Falha ao acessar os registros. Tente novamente.` |
| Linha corrompida no CSV | `RepositorioException` | Repositório | Apresentação | Interna: `Linha inválida no arquivo: <linha>`. Na tela: `Falha ao acessar os registros. Tente novamente.` |

O menu acrescenta `Erro: ` às mensagens de negócio. O `try/catch` fica dentro do laço, permitindo uma nova operação. Uma opção de menu desconhecida é tratada na apresentação com `Opção inválida: "<opcao>". Digite 0, 1, 2 ou 3.`, sem lançar exceção de negócio.

## Como o código evolui

| Tag | Encontro | Evolução |
|---|---|---|
| `v11` | 11 — Reciclagem | Pacote único `br.ulbra.estacionamento`, herança, interface, polimorfismo, composição e 10 testes. `Main` simula carro e moto por 60 minutos, total R$ 9,60. |
| `v12` | 12 — Pacotes e repositório | Separação em `dominio`, `cobranca`, `repositorio` e `app`. `RegistroCobranca` fica em `repositorio`; `Estacionamento(int, RegistroRepository)` salva as saídas. Falhas de arquivo usam `UncheckedIOException`. São 15 testes. |
| `v13` | 13 — Exceções e camadas | Exceções próprias, validações antes de alterar o estado, `RegistroCobranca` no domínio e persistência coordenada pelo serviço. Menu com entrada, saída e relógio injetados; `Main` é a raiz de composição. São 27 testes da referência e 2 testes de memória preservados do v12. |

Com o trabalho local salvo, navegue pelas aulas:

```sh
git checkout v12
mvn clean test
java -cp target/classes br.ulbra.estacionamento.app.Main
git checkout main
```

As tags abrem uma versão de estudo em estado destacado (*detached HEAD*). No v11, execute `br.ulbra.estacionamento.Main`. Use `mvn clean test` ao trocar de versão para remover classes compiladas de pacotes antigos.

A branch `atualizacao-aulas-11-a-13` contém a atualização para revisão; `main` permanece no estado anterior até a professora integrar o trabalho. Para voltar à atualização, use `git checkout atualizacao-aulas-11-a-13`.

## Documentação e uso de IA

O [relatório da atualização](docs/relatorio-atualizacao.md) registra o diagnóstico, as validações e as decisões sobre o modelo anterior. [Planejamento](docs/planejamento.md) e [backlog](docs/backlog.md) preservam o contexto do semestre.

Codex foi usado para organizar a evolução dos encontros 11 a 13, adaptar os materiais fornecidos, atualizar a documentação e executar as verificações. A definição do domínio e dos materiais de referência é da professora. O histórico anterior de uso de IA permanece em [docs/IA.md](docs/IA.md).