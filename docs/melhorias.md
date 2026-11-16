# Melhorias — Encontro 14

O v14 evolui o v13 em testes, organização do código e consistência da saída. O sistema mantém os mesmos tipos de veículo, tarifas, menu e CSV.

## Testes acrescentados

Os 29 testes iniciais foram preservados. Nenhum método de teste existente foi modificado para acomodar uma refatoração.

| Cobertura nova | Verificação |
|---|---|
| `CobrancaCarroTest` parametrizado | 0, 1 e 60 minutos → R$ 0,00, R$ 0,10 e R$ 6,00, com margem 0.001 |
| `CobrancaMotoTest` parametrizado | 0, 1 e 60 minutos → R$ 0,00, R$ 0,06 e R$ 3,60, com margem 0.001 |
| Saída igual à entrada | O ticket cobra zero e não lança exceção |
| Placa com seis caracteres | Lança `PlacaInvalidaException`, com a mensagem esperada |
| Placa com oito caracteres | Lança `PlacaInvalidaException`, com a mensagem esperada |
| Última vaga | A última entrada funciona; a próxima é recusada; após uma saída, outra entrada funciona |
| Contratos de normalização | Placa nula na entrada, placa nula na saída, tipo nulo e espaços/maiúsculas preservam os resultados e as mensagens |
| Falha ao salvar | A vaga continua ocupada e o ticket aberto; com o repositório funcionando, uma nova tentativa grava uma cobrança e libera a vaga |

Os dois métodos parametrizados produzem seis execuções. Somados aos quatro casos-limite, aos quatro testes de normalização e ao teste de consistência, são **15 execuções novas**, totalizando **44 testes**. Os testes usam horários fixos; arquivos de teste ficam em `@TempDir`.

## Refatoração 1 — Normalização

**Antes →** A normalização da placa aparecia no construtor de `Veiculo` e em `ServicoDeEstacionamento.registrarSaida`. A criação do veículo repetia a expressão para o tipo.

**Depois →** `Veiculo.normalizarPlaca(String)` reúne a regra da placa e `Veiculo.normalizarTipo(String)` reúne a do tipo. Os pontos de uso chamam esses métodos.

**Por quê →** Evitar que entrada e saída produzam chaves diferentes. A refatoração preserva `trim().toUpperCase()`, o tratamento de `null`, o formato aceito e as mensagens. Os mesmos 43 testes passaram antes e depois.

## Refatoração 2 — Constantes de tipo

**Antes →** As subclasses retornavam os textos dos tipos e as seleções repetiam os mesmos literais; menu e mensagem também escreviam os nomes diretamente.

**Depois →** `Carro.TIPO` e `Moto.TIPO` definem os valores. As subclasses, os `switch`, o menu e a mensagem de `TipoDeVeiculoInvalidoException` usam as constantes.

**Por quê →** Manter uma definição por tipo, sem alterar `getTipo()` nem o CSV. Os mesmos 43 testes passaram antes e depois. Cada literal aparece uma vez em `src/main/java`.

## Correção — Saída após falha de gravação

O teste `falhaAoGravarDeveManterVagaETicketAteUmaNovaTentativaBemSucedida` foi escrito e executado antes da alteração de produção. Ele falhou no código anterior com:

```text
org.opentest4j.AssertionFailedError: expected: <0> but was: <1>
```

O teste esperava zero vagas livres depois da falha, mas o código anterior já havia liberado a vaga.

A sequência do serviço mudou de:

```text
fechar e liberar → salvar → remover ticket
```

para:

```text
calcular e validar → salvar → liberar vaga → remover ticket
```

`Estacionamento` separa `calcularValorDaSaida` de `liberarVaga`. A operação `registrarSaida(Ticket, LocalDateTime)` continua validando e liberando para os testes e usos diretos do domínio. Na aplicação, o serviço faz a coordenação com o repositório.

Depois da correção, os 44 testes passaram, incluindo a nova tentativa bem-sucedida e a confirmação de que o ticket só deixa de existir após a saída concluída.

## Documentação e verificação

O README descreve a execução em máquina limpa, as regras, os erros, as decisões e a evolução das tags. `docs/decisoes.md` registra Contexto → Decisão → Consequência. Os diagramas Mermaid representam as classes e as dependências atuais; os métodos públicos de `Estacionamento` e `ServicoDeEstacionamento` têm Javadocs curtos.

O POM continua com Java 25, JUnit Jupiter 6.0.0 e Surefire 3.5.4. Não foram acrescentadas dependências nem funcionalidades. O `.gitignore` já continha as regras pedidas e `registros.csv` não estava versionado, portanto não foi necessário alterá-lo.

As evidências do Maven e do menu ficam no README. O [relatório do encontro](relatorio-encontro14.md) registra os commits, a conferência em clone limpo e a situação da publicação.