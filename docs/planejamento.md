# Atividade 1 — Exemplo de Entrega

**Domínio de demonstração:** Sistema de Estacionamento
**Fundamentos de Programação Orientada a Objetos · ADS · ULBRA · 2026/2**

> Este é o domínio usado pela professora nas demonstrações — diferente dos domínios oferecidos aos estudantes (biblioteca, clínica veterinária, eventos, assistência técnica, cursos, academia, reservas de espaços). Use como modelo de estrutura e nível de detalhe, não como solução a copiar.

## 1. Domínio e problema

**Domínio escolhido:** Sistema de Estacionamento.

**Problema:** estacionamentos de shoppings, prédios comerciais ou universidades costumam controlar vagas e cobranças manualmente, com papel ou planilha. Isso gera erros de cobrança, filas na saída e dificuldade de saber, em tempo real, quantas vagas estão livres.

## 2. Público / usuários

- Operador do estacionamento (registra entradas, saídas e cobranças)
- Motorista/cliente (consulta vagas, paga pelo uso)
- Gerente do estabelecimento (acompanha ocupação, faturamento e cadastros)

## 3. Funcionalidades iniciais

- Registrar entrada de veículo (placa, horário)
- Registrar saída e calcular valor a pagar
- Consultar vagas disponíveis
- Cadastrar tipos de vaga (comum, coberta, PCD, idoso)
- Aplicar tabela de preços por período (hora, diária, mensalista)
- Cadastrar mensalistas com desconto
- Emitir comprovante de pagamento
- Consultar histórico de veículos que já estacionaram

## 4. Histórias de usuário (10)

| Nº | História de usuário |
|----|----------------------|
| 1 | Como operador, quero registrar a entrada de um veículo informando a placa e o horário, para controlar quanto tempo ele ficou estacionado. |
| 2 | Como operador, quero registrar a saída de um veículo e calcular o valor a pagar automaticamente, para evitar erros de cobrança manual. |
| 3 | Como motorista, quero saber quantas vagas estão disponíveis antes de entrar, para não perder tempo procurando vaga. |
| 4 | Como gerente, quero cadastrar diferentes tipos de vaga (comum, coberta, PCD, idoso), para organizar melhor o espaço físico. |
| 5 | Como gerente, quero definir uma tabela de preços por período (hora, diária, mensalista), para adequar a cobrança conforme o tipo de cliente. |
| 6 | Como motorista mensalista, quero ter um cadastro fixo com desconto aplicado automaticamente, para não pagar o valor avulso todos os dias. |
| 7 | Como operador, quero emitir um comprovante de pagamento na saída, para o cliente ter registro do valor cobrado. |
| 8 | Como gerente, quero consultar o histórico de veículos que já estacionaram, para acompanhar o movimento do estabelecimento. |
| 9 | Como operador, quero ser avisado quando o estacionamento estiver lotado, para orientar os motoristas a não entrarem. |
| 10 | Como gerente, quero visualizar o faturamento diário do estacionamento, para acompanhar o desempenho do negócio. |

## 5. Backlog inicial (ordenado por prioridade)

| Prioridade | Item |
|------------|------|
| Alta | Registrar entrada de veículo |
| Alta | Registrar saída e calcular valor a pagar |
| Alta | Consultar vagas disponíveis |
| Alta | Cadastrar tipos de vaga |
| Média | Definir tabela de preços por período |
| Média | Cadastrar mensalistas com desconto |
| Média | Emitir comprovante de pagamento |
| Baixa | Consultar histórico de veículos |
| Baixa | Avisar quando o estacionamento estiver lotado |
| Baixa | Relatório de faturamento diário |

**Critério usado para priorizar:** funcionalidades "Alta" são as que fazem o fluxo básico do sistema funcionar de ponta a ponta (entrar, sair, saber se há vaga). "Média" refina a cobrança. "Baixa" são funcionalidades de gestão que podem esperar incrementos futuros.

## 6. Declaração de uso de IA (exemplo de preenchimento honesto)

**Uso de IA nesta entrega:** Sim.
**Ferramenta:** Claude.
**Partes em que foi usada:** revisão do formato das histórias de usuário (garantir que todas seguissem "Como / quero / para") e sugestão de critério de priorização do backlog.
**O que foi feito sem apoio de IA:** escolha do domínio, definição do problema e dos usuários, e a decisão de quais funcionalidades entram em cada nível de prioridade.
