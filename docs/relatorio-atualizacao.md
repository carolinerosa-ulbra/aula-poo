# Relatório da atualização — Encontros 11 a 13

## Diagnóstico inicial

A branch `main`, em `adf2454`, continha um modelo inicial anterior ao Encontro 11: `Main`, `Veiculo`, `Ticket`, `TipoVaga`, uma `Vaga` de domínio e um record `Vaga` vazio. Os pacotes começavam incorretamente por `main.java.br.com.sistemaEstacionamento`. Não havia `pom.xml`, testes ou tags. Não havia `PortariaEstacionamento` nem `CobrancaCarga`.

Havia uma alteração local em `docs/diagrama-classes/diagrama.png`, inicialmente preservada fora dos três commits das aulas. Após autorização da professora, a imagem foi incluída em um commit complementar, sem mover as tags. Os materiais locais foram usados como referência e ignorados pelo Git; os alunos recebem todo o projeto pela raiz do repositório.

## Commits, tags e validação

A branch local `atualizacao-aulas-11-a-13` foi criada a partir de `main`. Cada tag foi criada somente depois da compilação, dos testes e da execução do respectivo estado.

| Commit | Tag | Mensagem | Resultado dos testes reais |
|---|---|---|---|
| `064fec4` | `v11` | Consolide o estacionamento e os testes do encontro 11 | 10 executados, zero falhas, erros ou ignorados |
| `a98b6f8` | `v12` | Organize os pacotes e persista as cobrancas no encontro 12 | 15 executados, zero falhas, erros ou ignorados |
| Identificado por `git rev-parse v13` | `v13` | Separe as camadas e trate as excecoes no encontro 13 | 29 executados, zero falhas, erros ou ignorados |

Ambiente: Temurin JDK 25.0.4 e Maven 3.9.16. Foram executados `mvn -q test` no v11 e `mvn -q clean test` no v12 e v13, com `-Dmaven.repo.local` apontando para um cache temporário fora do repositório. O comando inicial no projeto antigo falhou primeiro pelo acesso ao cache padrão; ao usar cache temporário, confirmou a ausência de POM. Uma cópia temporária do gabarito também passou nos 10 testes antes das alterações.

O ambiente restrito inicialmente bloqueou o acesso ao Maven Central e a leitura do cache. Os testes foram executados com a permissão necessária para o Maven acessar as dependências; nenhum teste foi simulado. O POM conserva Java 25, JUnit Jupiter 6.0.0 e Surefire 3.5.4, sem novas dependências.

No v11 e no v12, `java -cp target/classes ...Main` confirmou o cenário de 60 minutos e o total de R$ 9,60. No v12, a execução também confirmou duas linhas no CSV. No v13, a execução por pipe cobriu placa inválida, opção inválida, entrada, saída, listagem e encerramento com `0`, com código de saída zero. A cobrança imediata foi R$ 0,00, pois não transcorreu um minuto completo. Os CSVs dessas verificações ficaram em diretórios temporários.

A busca por `import br.ulbra.estacionamento.repositorio` em `src/main/java/br/ulbra/estacionamento/dominio` não encontrou resultados. Todos os arquivos Java da referência v13 estão presentes. A comparação desconsiderando comentários e espaços confirmou o mesmo código de produção; os 27 testes fornecidos e o POM estão idênticos à referência.

## Evolução e divergências da referência

- O v11 parte de `estacionamento-live-encontro11/estacionamento-gabarito`, a versão completa do material disponível; o roteiro continha implementações parciais e erros planejados. O material local e seu Git interno não foram incorporados.
- O v12 evolui esse código: reorganiza pacotes, introduz o record e os repositórios e ainda usa `UncheckedIOException`. Seus testes verificam memória, persistência entre duas instâncias e integração da saída com o repositório.
- O v13 segue `estacionamento-v13-referencia` sem mudanças de comportamento. Apenas `Estacionamento` e `Ticket` receberam comentários sobre composição, validação antes da alteração e os três pontos do polimorfismo.
- Os dois testes de `RegistroRepositoryEmMemoriaTest` do v12 foram preservados e ajustados ao novo pacote do record. Por isso há 29 testes, incluindo integralmente os 27 da referência.
- README, diagramas Mermaid e índice de documentação foram atualizados para uso independente pelos alunos. Planejamento, backlog e registro anterior de IA foram preservados.

## Classes preexistentes

| Elemento anterior | Decisão |
|---|---|
| `Veiculo` concreto, com modelo, cor e mensalista | Substituído por `Veiculo` abstrato e subclasses `Carro`/`Moto`, conforme o gabarito e a referência |
| `Ticket` com relógio interno e cobrança por hora/fração | Substituído pelo ticket com política injetada e horários recebidos por parâmetro |
| `model.Vaga` e `TipoVaga` | Substituídos pela vaga controlada pelo estacionamento; tipos de vaga e mensalistas conflitam com o escopo da referência |
| Record `Vaga` vazio no pacote da aplicação | Removido por não ter comportamento nem equivalente no design da referência |
| `Main` com espera e montagem manual das vagas | Substituído pelo cenário do v11/v12 e, no v13, pela raiz de composição com menu |

O modelo antigo permanece no histórico anterior a `v11`. Nenhuma classe antiga foi mantida em paralelo como código morto. A imagem modificada pela professora foi preservada byte a byte e incluída no commit complementar `Inclua o diagrama fornecido pela professora`. Esse commit altera apenas a imagem e a documentação; os testes executados nos estados das aulas continuam sendo os registrados acima.

## Pontos para a professora avaliar

Não há decisão pendente que impeça o uso dos três estados. Foram mantidas escolhas discutíveis da referência, conforme solicitado:

- `ServicoDeEstacionamento.registrarSaida` libera a vaga pelo domínio antes de salvar o registro; se a gravação falhar, a vaga fica livre e o ticket continua aberto no mapa. Não foi introduzida transação nem reorganizado esse fluxo.
- O exemplo usa `double` para valores, tickets em memória e menu que espera uma opção `0` para encerrar. EOF não é tratado como encerramento. A validação de placa aceita também o padrão antigo sem hífen (`ABC1234`).

Esses pontos podem ser discutidos em aula; não foram alterados para manter fidelidade ao material.

## Publicação

Nenhum push nem merge foi realizado; `main` e o histórico anterior foram preservados. Para publicar a branch e somente as três tags criadas:

```sh
git push -u origin atualizacao-aulas-11-a-13
git push origin v11 v12 v13
```

Para conferir os commits completos:

```sh
git log --oneline --decorate main..atualizacao-aulas-11-a-13
```