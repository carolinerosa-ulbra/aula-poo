# Decisões de projeto — Encontro 14

Este registro explica as escolhas do exemplo de estacionamento. Cada decisão liga um problema do código à consequência para quem o usa ou mantém.

## 1. Política de cobrança por interface

**Contexto →** Carros e motos pagam valores diferentes por minuto. O ticket precisa cobrar sem repetir uma seleção por tipo em cada saída.

**Decisão →** `PoliticaDeCobranca` define `calcularValor`. `Estacionamento.escolherPolitica` é o ponto de decisão; `Ticket` armazena a referência à interface e executa a política ao fechar.

**Consequência →** O mesmo ticket usa o contrato da cobrança sem conhecer suas implementações. Os testes podem comparar políticas com horários e minutos definidos por parâmetro. As tarifas continuam R$ 0,10/min para carro e R$ 0,06/min para moto.

## 2. Composição de vagas no estacionamento

**Contexto →** Se qualquer parte do programa pudesse criar, remover ou liberar vagas, a quantidade disponível poderia divergir das entradas registradas.

**Decisão →** `Estacionamento` cria suas próprias vagas. O construtor e os métodos de alteração de `Vaga` têm acesso de pacote. As duas classes permanecem em `dominio`; `getVagas()` expõe uma lista não modificável.

**Consequência →** Quem consulta não pode substituir ou remover vagas da coleção. O serviço coordena a saída por operações do estacionamento, sem alterar uma `Vaga` diretamente.

## 3. Repositório usado pelo serviço

**Contexto →** Guardar registros no domínio faria uma regra de negócio depender de detalhes de arquivo, CSV ou memória.

**Decisão →** `ServicoDeEstacionamento` recebe `Estacionamento` e `RegistroRepository`. O record `RegistroCobranca` pertence ao domínio; as duas implementações do repositório dependem desse record. `Main` escolhe o armazenamento em arquivo.

**Consequência →** O domínio não importa `repositorio`. Testes podem usar memória, enquanto a aplicação usa `registros.csv`, sem mudar as regras de ocupação ou cobrança.

## 4. Exceções não verificadas e tratamento na apresentação

**Contexto →** Placa inválida e lotação são erros previstos de negócio. Uma falha de disco tem outra causa e exige outra mensagem na tela.

**Decisão →** As seis exceções de negócio herdam de `EstacionamentoException`, uma `RuntimeException`. `RepositorioException` representa falhas técnicas em uma hierarquia separada e preserva a causa quando existe uma exceção original. `MenuConsole` trata ambas dentro do laço.

**Consequência →** O programa pode informar o erro e receber outra operação. A apresentação mostra a mensagem de negócio com `Erro: ` e uma mensagem técnica genérica para o repositório, sem expor detalhes do arquivo ao operador.

## 5. Validar e gravar antes de alterar o estado

**Contexto →** No v13, a saída liberava a vaga antes de salvar a cobrança. Se `salvar` lançasse `RepositorioException`, o ticket continuava aberto e a vaga já estava livre.

**Decisão →** A entrada escolhe uma política válida antes de ocupar. Na saída do serviço, a sequência passa a ser: buscar ticket, calcular e validar o período, criar e salvar o registro, liberar a vaga, remover o ticket. `Estacionamento.calcularValorDaSaida` não altera a ocupação; `liberarVaga` concentra a liberação. `registrarSaida(Ticket, LocalDateTime)` permanece disponível para a saída direta no domínio.

**Consequência →** Se a gravação falhar, a vaga e o ticket continuam como estavam, permitindo uma nova tentativa. O teste usa um repositório falso que falha antes de gravar e depois volta a funcionar. Isso não acrescenta transações nem recuperação de uma escrita parcial em arquivo.

## 6. Normalização com métodos explícitos

**Contexto →** A construção do veículo e a busca do ticket repetiam a normalização da placa. A criação do veículo também normalizava o tipo.

**Decisão →** `Veiculo.normalizarPlaca` é usado na construção e na saída. `Veiculo.normalizarTipo` é usado na criação do veículo. Os métodos mantêm a remoção de espaços nas pontas e a conversão para maiúsculas, sem mudar a regra de validação.

**Consequência →** Entrada e saída usam a mesma chave. Placa nula no construtor continua lançando `PlacaInvalidaException`; saída com placa nula continua lançando `TicketNaoEncontradoException`; tipo nulo continua lançando `TipoDeVeiculoInvalidoException`. As mensagens preservam o valor original informado.

## 7. Constantes para os tipos, preservando String

**Contexto →** Os textos dos tipos eram repetidos nas subclasses, nas seleções de política e veículo, no menu e na mensagem de erro.

**Decisão →** `Carro.TIPO` e `Moto.TIPO` são constantes `public static final String`. As seleções, o menu e a mensagem usam essas constantes. Não foi introduzido enum.

**Consequência →** Cada literal aparece uma única vez no código de produção. `getTipo()` continua retornando `String`, as mensagens continuam iguais e o formato do CSV permanece compatível com os registros anteriores.