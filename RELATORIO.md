# Relatório Técnico – Sistema Pet Shop

## 1. Problema escolhido
Um pet shop precisa cadastrar os animais dos clientes e saber quanto custa o banho de cada um.
O sistema permite **cadastrar, consultar, atualizar e deletar** cachorros e gatos, e mostra o preço do banho de cada animal.

## 2. Tecnologias
- **Java 17+** – linguagem.
- **JavaFX** – interface gráfica, estilizada com **CSS** (`estilo.css`).
- **JUnit 5** – testes automatizados.
- **Maven** – baixa as bibliotecas e executa o projeto.

## 3. Arquitetura MVC

| Camada | Classes | Responsabilidade |
|---|---|---|
| **Model** | `Animal`, `Cachorro`, `Gato`, `AnimalRepositorio` | Dados, regras e validações. Guarda a lista de animais. |
| **View** | `TelaPrincipal`, `estilo.css` | Monta a tela e mostra mensagens. Não tem regra de negócio. |
| **Controller** | `AnimalController` | Recebe o que foi digitado, converte (ex.: texto → número) e chama o Model. |

Fluxo: o usuário clica em **Cadastrar** → a View chama `controller.cadastrar(...)` → o Controller cria o objeto do Model → a View atualiza a tabela.

**Decisão:** os dados ficam em uma `ArrayList` (memória) para manter o projeto simples. Ao fechar o programa os dados são perdidos; um banco de dados seria a próxima melhoria.

## 4. Os quatro pilares da POO

### Encapsulamento
Todos os atributos são `private`. O acesso é feito por getters/setters, e os setters **validam** os dados:
- nome e dono não podem ser vazios;
- idade em **anos (0 a 40) e meses (0 a 11)** – ex.: 1 ano e 2 meses é aceito, 14 meses não;
- porte do cachorro só pode ser `P`, `M` ou `G`.

Se algo estiver errado é lançada uma `IllegalArgumentException`, e a View mostra a mensagem num alerta.

### Abstração
`Animal` é uma classe **abstrata**: representa a ideia geral de animal, com o que todos têm em comum (id, nome, idade, dono). Não é possível criar um `new Animal(...)`.

### Herança
`Cachorro` e `Gato` usam `extends Animal`, herdando atributos e métodos. Cada um adiciona o seu próprio atributo:
- `Cachorro` → `porte`
- `Gato` → `pelagemLonga`

O construtor das subclasses chama `super(...)` para reaproveitar o da classe-base.

### Polimorfismo
`Animal` declara os métodos abstratos `calcularPrecoBanho()`, `getTipo()` e `getDetalhe()`, que são **sobrescritos** (`@Override`) nas subclasses:

| Animal | Regra do banho |
|---|---|
| Cachorro P / M / G | R$ 40 / R$ 60 / R$ 80 |
| Gato pelo curto / longo | R$ 50 / R$ 70 |

No `AnimalController.calcularTotalBanhos()` percorremos uma `List<Animal>` e chamamos `a.calcularPrecoBanho()` sem saber se é cachorro ou gato – o Java executa a versão certa de cada objeto. O mesmo acontece nas colunas "Detalhe" e "Banho" da tabela. Também sobrescrevemos o `toString()` herdado de `Object`.

## 5. Testes (JUnit)
Arquivo `AnimalControllerTest.java` – 6 casos de teste:

| # | Teste | O que verifica |
|---|---|---|
| 1 | `deveCadastrarAnimal` | Cadastro adiciona o animal na lista |
| 2 | `deveConsultarPorNome` | Busca por parte do nome funciona |
| 3 | `deveAtualizarAnimal` | Idade muda para "1 ano e 2 meses" e o dono é alterado |
| 4 | `deveDeletarAnimal` | Animal é removido |
| 5 | `naoDeveCadastrarComDadosInvalidos` | Validação bloqueia nome vazio e 12 meses |
| 6 | `deveCalcularPrecoComPolimorfismo` | Cachorro G (80) + Gato longo (70) = 150 |

Resultado: **6 testes executados, 0 falhas** (ver `evidencias/resultado-testes.txt`).

## 6. Evidências
- `evidencias/tela-principal.png` – tela com animais cadastrados.
- `evidencias/tela-validacao.png` – mensagem de erro ao digitar 14 meses.
- `documentacao/Relatorio_ABNT_PetShop.pdf` – relatório completo no padrão ABNT.
- `evidencias/resultado-testes.txt` – saída do `mvn test`.

## 7. Integrantes e divisão sugerida para a apresentação
- **Felipe:** Model – classe-base `Animal`, encapsulamento e validação.
- **Guilherme Lisboa:** Herança – `Cachorro` e `Gato`.
- **João:** Polimorfismo e sobrescrita – `calcularPrecoBanho()` e `getDetalhe()`.
- **Murilo:** Controller e View – MVC, JavaFX e CSS.
- **Rhenan:** Testes e demonstração do sistema funcionando.
