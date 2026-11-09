# Diagramas — estado v13

Os diagramas abaixo representam o código atual. A imagem `diagrama.png` foi preservada como material anterior; sua alteração local não faz parte da atualização dos encontros 11 a 13.

## Classes

```mermaid
classDiagram
    class Veiculo {
        <<abstract>>
        -String placa
        +getPlaca() String
        +getTipo() String*
    }
    class Carro
    class Moto
    Veiculo <|-- Carro
    Veiculo <|-- Moto

    class PoliticaDeCobranca {
        <<interface>>
        +calcularValor(long minutosEstacionado) double
    }
    class CobrancaCarro
    class CobrancaMoto
    PoliticaDeCobranca <|.. CobrancaCarro
    PoliticaDeCobranca <|.. CobrancaMoto

    class Ticket {
        -LocalDateTime entrada
        +Ticket(Veiculo, PoliticaDeCobranca, LocalDateTime)
        +fechar(LocalDateTime saida) double
    }
    Ticket --> "1" Veiculo
    Ticket --> "1" PoliticaDeCobranca : armazena e executa

    class Vaga {
        -int numero
        ~Vaga(int numero)
        ~ocupar(Veiculo)
        ~liberar()
        +isOcupada() boolean
    }
    class Estacionamento {
        +Estacionamento(int quantidadeDeVagas)
        +getVagas() List~Vaga~
        +vagasLivres() int
        +registrarEntrada(Veiculo, LocalDateTime) Ticket
        +registrarSaida(Ticket, LocalDateTime) double
        -escolherPolitica(Veiculo) PoliticaDeCobranca
    }
    Estacionamento "1" *-- "0..*" Vaga : cria e controla
    Vaga --> "0..1" Veiculo : ocupa
    Estacionamento ..> Ticket : cria
    Estacionamento ..> PoliticaDeCobranca : decide

    class RegistroCobranca {
        <<record>>
        +String placa
        +String tipo
        +LocalDateTime entrada
        +LocalDateTime saida
        +double valor
    }
    class RegistroRepository {
        <<interface>>
        +salvar(RegistroCobranca)
        +listarTodos() List~RegistroCobranca~
    }
    class RegistroRepositoryEmMemoria
    class RegistroRepositoryArquivo {
        -Path arquivo
    }
    RegistroRepository <|.. RegistroRepositoryEmMemoria
    RegistroRepository <|.. RegistroRepositoryArquivo
    RegistroRepository ..> RegistroCobranca
    class ServicoDeEstacionamento {
        -Map~String, Ticket~ ticketsAbertos
        +registrarEntrada(String placa, String tipo, LocalDateTime) Ticket
        +registrarSaida(String placa, LocalDateTime) RegistroCobranca
        +listarRegistros() List~RegistroCobranca~
        +totalArrecadado() double
        +vagasLivres() int
    }
    ServicoDeEstacionamento --> Estacionamento
    ServicoDeEstacionamento --> RegistroRepository
    ServicoDeEstacionamento --> Ticket : mantém abertos por placa
    ServicoDeEstacionamento ..> RegistroCobranca : cria
    class MenuConsole {
        -Scanner entrada
        -PrintStream saida
        -Supplier~LocalDateTime~ relogio
        +executar()
    }
    MenuConsole --> ServicoDeEstacionamento
    class Main {
        +main(String[] args)$
    }
    Main ..> MenuConsole : monta
    Main ..> ServicoDeEstacionamento : monta
    Main ..> Estacionamento : monta
    Main ..> RegistroRepositoryArquivo : escolhe

    class EstacionamentoException
    class PlacaInvalidaException
    class TipoDeVeiculoInvalidoException
    class EstacionamentoLotadoException
    class TicketNaoEncontradoException
    class VeiculoJaEstacionadoException
    class PeriodoInvalidoException
    class RepositorioException
    RuntimeException <|-- EstacionamentoException
    EstacionamentoException <|-- PlacaInvalidaException
    EstacionamentoException <|-- TipoDeVeiculoInvalidoException
    EstacionamentoException <|-- EstacionamentoLotadoException
    EstacionamentoException <|-- TicketNaoEncontradoException
    EstacionamentoException <|-- VeiculoJaEstacionadoException
    EstacionamentoException <|-- PeriodoInvalidoException
    RuntimeException <|-- RepositorioException
    MenuConsole ..> EstacionamentoException : trata
    MenuConsole ..> RepositorioException : trata
```

## Dependências entre pacotes

```mermaid
flowchart LR
    app --> servico
    servico --> dominio
    servico --> repositorio
    repositorio --> dominio
    dominio --> cobranca
    app -.->|composição, registros e exceções| dominio
    app -.->|composição e erro técnico| repositorio
```

As setas indicam dependências de código. O domínio não conhece o repositório; o serviço coordena os dois. A apresentação também conhece os tipos necessários para compor a aplicação e tratar erros.