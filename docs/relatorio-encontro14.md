# Relatório — Encontro 14

## Diagnóstico e branch

O remoto correto é `https://github.com/carolinerosa-ulbra/aula-poo.git`. `origin/main`, em `5d44bb7`, já continha a v13 e a integração dos encontros anteriores. A `main` local estava desatualizada e foi avançada apenas por fast-forward; o trabalho do encontro foi feito diretamente nela. A v14 não existia localmente nem no remoto.

O estado inicial tinha 29 testes, todos aprovados. Java 25, JUnit Jupiter 6.0.0 e Surefire 3.5.4 foram mantidos. Os nomes de classes coincidiam com o prompt; não foi necessário renomeá-los. Uma imagem `.avif` pessoal, não rastreada, foi preservada por exclusão apenas local em `.git/info/exclude`. Os materiais de referência e o ZIP continuam fora dos commits.

## Commits criados

| Commit | Mensagem |
|---|---|
| `49013e6` | `testes: adicione cobranca parametrizada e casos-limite` |
| `a9469f7` | `refatora: concentre a normalizacao da placa e do tipo` |
| `bb7806e` | `refatora: extraia constantes para os tipos de veiculo` |
| `b80ac58` | `corrige: so libere a vaga depois de gravar a cobranca` |
| `239ea7c` | `docs: explique os contratos do dominio e do servico` |
| `0033b29` | `docs: atualize os diagramas de classes e pacotes` |
| Último commit, identificado pela tag `v14` | `docs: atualize o README e registre o encontro 14` |

## Testes antes e depois

| Etapa | Antes | Depois |
|---|---|---|
| Ampliação dos testes | 29 aprovados | 43 aprovados |
| Normalização | 43 aprovados | Os mesmos 43 aprovados, sem modificar testes |
| Constantes de tipo | 43 aprovados | Os mesmos 43 aprovados, sem modificar testes |
| Consistência da saída | 43 aprovados; o novo teste isolado falhou com `expected: <0> but was: <1>` | 44 aprovados, incluindo falha de gravação e nova tentativa |
| Javadocs e conferência em clone novo | 44 aprovados | 44 aprovados; `mvn compile` e menu concluídos |

Os comandos `mvn -q test` foram executados nas etapas de código. A conferência em clone novo também executou `mvn test`, com `BUILD SUCCESS`, zero falhas, zero erros e zero ignorados. As saídas reais do Maven e do menu foram copiadas para o README. O Maven usou um cache temporário externo ao repositório para contornar a restrição de acesso ao cache padrão do ambiente; não houve simulação do JUnit.

Os 29 testes iniciais foram preservados; o diff de testes contém somente acréscimos. São 15 novas execuções, incluindo as seis parametrizadas, os casos-limite, os contratos de normalização e a regressão de consistência. A contagem final é **44**.

## Conferência e escopo

O projeto foi clonado da origem local para uma pasta temporária nova, sem os materiais de referência, ZIP, imagem pessoal ou arquivos de compilação. Os testes e a compilação passaram. O menu recebeu a entrada simulada do prompt, apresentou placa inválida e ticket não encontrado e continuou até a opção `0`.

A conferência final usa outro clone novo, `mvn -q test`, execução do menu por pipe e status limpo antes da criação da tag. O primeiro clone também executou os comandos completos `mvn test` e `mvn compile`; suas saídas foram copiadas para o README. O código do domínio não importa `repositorio`. Cada literal de tipo aparece uma única vez em `src/main/java`. As 24 classes, interfaces, record e exceções de produção estão no diagrama. O POM não mudou. O `.gitignore` já atendia ao pedido e `registros.csv` não era rastreado; não foi criado um commit vazio de ajuste.

A correção limita-se à sequência solicitada. Foram preservados `double`, tickets apenas em memória, comportamento de normalização e encerramento do menu por `0`. O repositório falso falha antes de gravar: o exemplo não introduz recuperação de uma escrita parcial de arquivo. Nenhum `docs/uml.png` foi gerado.

## Tag, release e publicação

A tag anotada local **v14** identifica o último commit, após a conferência final, com a mensagem `v14: testes, refatoracao e documentacao (Encontro 14)`. As tags v11, v12 e v13 não foram movidas nem recriadas. A tag `as` não faz parte deste trabalho.

O GitHub CLI não estava instalado. Foi obtida uma cópia portátil oficial, em pasta temporária, com checksum SHA-256 conferido. `gh auth status` respondeu `You are not logged into any GitHub hosts.` Por isso a publicação e a release ficam pendentes de autenticação da professora. As notas estão prontas em `docs/notas-v14.md`.

Na mesma máquina, o CLI portátil pode ser usado assim para publicar a v14 local:

```powershell
$env:Path = "$env:TEMP\codex-encontro14-20261116\gh-portatil\bin;$env:Path"
gh auth login --hostname github.com
gh auth status
git push origin main
git push origin v14
gh release create v14 --repo carolinerosa-ulbra/aula-poo --verify-tag --title "v14 - Testes, refatoração e documentação" --notes-file docs/notas-v14.md
```

Nenhum push deve ser repetido se for recusado; nesse caso, a professora precisa conferir a conta, as permissões ou as regras da branch no GitHub. A decisão pendente é autenticar e publicar. O estado local pode ser conferido com `git log --oneline --decorate 5d44bb7..main` e `git show v14 --no-patch`.