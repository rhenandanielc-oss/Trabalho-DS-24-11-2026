# Pet Shop - Sistema de Cadastro de Animais

Trabalho de Desenvolvimento de Sistemas (ETEC) - Java + POO + MVC + JavaFX.

## Como executar

Requisitos: Java 17 ou superior e Maven.

```bash
mvn javafx:run
```

Pela IDE: rode a classe `petshop.Main`.

## Como rodar os testes

```bash
mvn test
```

## Estrutura

```
src/main/java/petshop/
├── model/        -> Animal (base), Cachorro, Gato, AnimalRepositorio
├── controller/   -> AnimalController
├── view/         -> TelaPrincipal
├── App.java      -> inicia o JavaFX
└── Main.java     -> ponto de partida
src/main/resources/petshop/estilo.css  -> visual da tela
src/test/java/petshop/AnimalControllerTest.java -> 6 testes
evidencias/       -> print da tela e resultado dos testes
RELATORIO.md      -> relatorio tecnico (resumo)
documentacao/     -> relatorio no padrao ABNT (.docx e .pdf)
```
