# Sistema de Estacionamento — Encontro 11

Projeto de demonstração de Fundamentos de Programação Orientada a Objetos — ADS, ULBRA, 2026/2. O projeto Maven completo está na raiz: basta clonar este repositório.

## Regras de negócio

- RN1: o valor cobrado depende do tipo do veículo.
- RN2: carro paga R$ 0,10 por minuto.
- RN3: moto paga R$ 0,06 por minuto.
- RN4: a entrada ocupa uma vaga livre; sem vaga, a entrada é recusada.
- RN5: a saída libera a vaga e usa a política escolhida na entrada.

## Compilar, testar e executar

Pré-requisitos: JDK 25 e Maven 3.9.x. Confira com `java -version` e `mvn -version`.

```sh
mvn compile
mvn test
java -cp target/classes br.ulbra.estacionamento.Main
```

O exemplo registra um carro e uma moto, simula 60 minutos por parâmetros e imprime R$ 6,00 + R$ 3,60 = R$ 9,60. Não depende de espera nem do relógio real.

## Estrutura e conceitos

Todas as classes estão em `src/main/java/br/ulbra/estacionamento`; os testes estão no caminho equivalente em `src/test/java`.

`Veiculo` é abstrata; `Carro` e `Moto` especializam o tipo. `PoliticaDeCobranca` possui duas implementações. `Estacionamento.escolherPolitica` decide qual usar, `Ticket` guarda a referência e `Ticket.fechar` executa a cobrança polimórfica.

O estacionamento cria suas vagas (composição). O construtor e os métodos de alteração de `Vaga` têm acesso de pacote; `getVagas()` expõe uma lista não modificável.

Nesta etapa, os erros são provisórios: `IllegalStateException` para lotação e `IllegalArgumentException` para tipo sem política. As próximas aulas introduzem persistência e exceções próprias.

## Como o código evolui

- `v11`: pacote único, herança, interface, polimorfismo, composição e testes.
- `v12` (próxima etapa): pacotes por responsabilidade e repositórios em memória e CSV.
- `v13` (próxima etapa): exceções próprias, serviço e menu de console.

Após a publicação das tags, use `git checkout v11` para estudar esta aula e `git checkout main` para voltar à principal. Enquanto a atualização estiver em revisão, `git checkout atualizacao-aulas-11-a-13` retorna à branch de trabalho.

## Uso de IA

Codex foi usado para organizar a evolução dos encontros 11 a 13, adaptar os materiais fornecidos, atualizar a documentação e executar as verificações. A definição do domínio e dos materiais de referência é da professora.
