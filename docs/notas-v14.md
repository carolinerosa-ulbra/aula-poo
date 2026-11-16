# v14 — Testes, refatoração e documentação

- Testes parametrizados de cobrança para carro e moto, casos-limite de placa, período e lotação, e testes dos contratos de normalização.
- Normalização da placa e do tipo centralizada e extração das constantes `Carro.TIPO` e `Moto.TIPO`, preservando o comportamento, as mensagens e o CSV.
- Saída corrigida para salvar a cobrança antes de liberar a vaga e remover o ticket. A regressão cobre falha de gravação e uma nova tentativa bem-sucedida.
- README, Javadocs, decisões de projeto, registro de melhorias e diagramas Mermaid atualizados.

Validação: **44 testes executados, zero falhas, zero erros e zero ignorados**, incluindo seis execuções parametrizadas. `mvn test` concluiu com `BUILD SUCCESS`; o clone limpo também passou e o menu tratou os erros previstos com entrada simulada.

Java 25, Maven 3.9.x e JUnit Jupiter 6.0.0. Nenhuma dependência ou funcionalidade nova.

[Leia o README do v14](https://github.com/carolinerosa-ulbra/aula-poo/blob/v14/README.md).