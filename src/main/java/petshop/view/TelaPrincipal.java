package petshop.view;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import petshop.controller.AnimalController;
import petshop.model.Animal;
import petshop.model.Cachorro;
import petshop.model.Gato;

// VIEW: so monta a tela e repassa as acoes para o Controller.
public class TelaPrincipal {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");
    private final AnimalController controller = new AnimalController();

    // Componentes do formulario
    private final ComboBox<String> cbTipo = new ComboBox<>();
    private final TextField txtNome = new TextField();
    private final TextField txtAnos = new TextField();
    private final TextField txtMeses = new TextField();
    private final TextField txtDono = new TextField();
    private final ComboBox<String> cbExtra = new ComboBox<>();
    private final Label lblExtra = new Label("Porte");
    private final Label lblMensagem = new Label();

    // Componentes da lista
    private final TextField txtBusca = new TextField();
    private final TableView<Animal> tabela = new TableView<>();
    private final Label lblQuantidade = new Label();
    private final Label lblTotal = new Label();

    public void mostrar(Stage stage) {
        BorderPane raiz = new BorderPane();
        raiz.setTop(criarCabecalho());
        VBox form = criarFormulario();
        VBox lista = criarLista();
        raiz.setLeft(form);
        raiz.setCenter(lista);
        BorderPane.setMargin(form, new Insets(16, 8, 16, 16)); // espaco em volta das caixas
        BorderPane.setMargin(lista, new Insets(16, 16, 16, 8));
        raiz.getStyleClass().add("raiz");

        Scene cena = new Scene(raiz, 1040, 620);
        // Liga o arquivo CSS a tela
        cena.getStylesheets().add(getClass().getResource("/petshop/estilo.css").toExternalForm());

        stage.setTitle("Pet Shop - Cadastro de Animais");
        stage.setMinWidth(900);
        stage.setMinHeight(560);
        stage.setScene(cena);
        stage.show();
        atualizarTabela();
    }

    // ===== Partes da tela =====

    private VBox criarCabecalho() {
        Label titulo = new Label("Pet Shop");
        titulo.getStyleClass().add("titulo");
        Label subtitulo = new Label("Cadastro, consulta e atualização de animais");
        subtitulo.getStyleClass().add("subtitulo");
        VBox cabecalho = new VBox(2, titulo, subtitulo);
        cabecalho.getStyleClass().add("cabecalho");
        return cabecalho;
    }

    private VBox criarFormulario() {
        cbTipo.getItems().addAll("Cachorro", "Gato");
        cbTipo.setValue("Cachorro");
        cbTipo.setOnAction(e -> trocarCampoExtra());
        trocarCampoExtra();

        txtNome.setPromptText("Ex.: Rex");
        txtDono.setPromptText("Ex.: Ana Souza");
        txtAnos.setPromptText("anos");
        txtMeses.setPromptText("meses");
        cbTipo.setMaxWidth(Double.MAX_VALUE);
        cbExtra.setMaxWidth(Double.MAX_VALUE);

        // Idade em dois campos lado a lado: anos + meses
        HBox idade = new HBox(8, txtAnos, new Label("anos"), txtMeses, new Label("meses"));
        idade.setAlignment(Pos.CENTER_LEFT);
        txtAnos.setPrefWidth(60);
        txtMeses.setPrefWidth(60);

        // Botoes
        Button btnCadastrar = new Button("Cadastrar");
        Button btnAtualizar = new Button("Atualizar");
        Button btnDeletar = new Button("Deletar");
        Button btnLimpar = new Button("Limpar");
        btnCadastrar.getStyleClass().add("btn-sucesso");
        btnAtualizar.getStyleClass().add("btn-primario");
        btnDeletar.getStyleClass().add("btn-perigo");
        btnLimpar.getStyleClass().add("btn-neutro");

        btnCadastrar.setOnAction(e -> cadastrar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnDeletar.setOnAction(e -> deletar());
        btnLimpar.setOnAction(e -> limpar());

        // Atualizar e Deletar so ficam ativos com um animal selecionado
        btnAtualizar.disableProperty().bind(tabela.getSelectionModel().selectedItemProperty().isNull());
        btnDeletar.disableProperty().bind(tabela.getSelectionModel().selectedItemProperty().isNull());

        GridPane botoes = new GridPane();
        botoes.setHgap(8);
        botoes.setVgap(8);
        botoes.addRow(0, btnCadastrar, btnAtualizar);
        botoes.addRow(1, btnDeletar, btnLimpar);
        for (Button b : List.of(btnCadastrar, btnAtualizar, btnDeletar, btnLimpar)) {
            b.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(b, Priority.ALWAYS);
        }

        Label secao = new Label("Dados do animal");
        secao.getStyleClass().add("secao");
        lblMensagem.setWrapText(true);

        VBox form = new VBox(6, secao,
                new Label("Tipo"), cbTipo,
                new Label("Nome"), txtNome,
                new Label("Idade"), idade,
                new Label("Dono"), txtDono,
                lblExtra, cbExtra,
                new Region(), botoes, lblMensagem);
        form.getStyleClass().add("cartao");
        form.setPrefWidth(300);
        return form;
    }

    private VBox criarLista() {
        Label secao = new Label("Animais cadastrados");
        secao.getStyleClass().add("secao");
        txtBusca.setPromptText("Buscar por nome...");
        txtBusca.setPrefWidth(240);
        txtBusca.textProperty().addListener((obs, antigo, novo) -> atualizarTabela());

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        HBox topo = new HBox(10, secao, espaco, txtBusca);
        topo.setAlignment(Pos.CENTER_LEFT);

        criarColunas();
        tabela.setPlaceholder(new Label("Nenhum animal cadastrado."));
        tabela.getSelectionModel().selectedItemProperty()
              .addListener((obs, antigo, animal) -> preencherFormulario(animal));
        VBox.setVgrow(tabela, Priority.ALWAYS);

        lblTotal.getStyleClass().add("total");
        Region espaco2 = new Region();
        HBox.setHgrow(espaco2, Priority.ALWAYS);
        HBox rodape = new HBox(10, lblQuantidade, espaco2, lblTotal);
        rodape.getStyleClass().add("rodape");

        VBox lista = new VBox(12, topo, tabela, rodape);
        lista.getStyleClass().add("cartao");
        return lista;
    }

    private void criarColunas() {
        // Cada coluna mostra um dado do animal (texto vindo dos getters)
        tabela.getColumns().addAll(List.of(
            coluna("ID", 40, a -> String.valueOf(a.getId())),
            coluna("Tipo", 80, Animal::getTipo),
            coluna("Nome", 95, Animal::getNome),
            coluna("Idade", 140, Animal::getIdadeFormatada),
            coluna("Dono", 120, Animal::getDono),
            coluna("Detalhe", 95, Animal::getDetalhe),            // POLIMORFISMO
            coluna("Banho", 85, a -> formatarPreco(a.calcularPrecoBanho())) // POLIMORFISMO
        ));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    // Cria uma coluna de texto. "valor" diz qual informacao do animal aparece.
    private TableColumn<Animal, String> coluna(String titulo, double largura,
                                               Function<Animal, String> valor) {
        TableColumn<Animal, String> col = new TableColumn<>(titulo);
        col.setPrefWidth(largura);
        col.setCellValueFactory(c -> new SimpleStringProperty(valor.apply(c.getValue())));
        return col;
    }

    // Muda o ultimo campo conforme o tipo escolhido
    private void trocarCampoExtra() {
        cbExtra.getItems().clear();
        if ("Cachorro".equals(cbTipo.getValue())) {
            lblExtra.setText("Porte");
            cbExtra.getItems().addAll("P", "M", "G");
            cbExtra.setValue("P");
        } else {
            lblExtra.setText("Pelo longo?");
            cbExtra.getItems().addAll("Não", "Sim");
            cbExtra.setValue("Não");
        }
    }

    // ===== Acoes dos botoes (todas passam pelo Controller) =====

    private void cadastrar() {
        try {
            Animal novo = controller.cadastrar(cbTipo.getValue(), txtNome.getText(), txtAnos.getText(),
                    txtMeses.getText(), txtDono.getText(), cbExtra.getValue());
            limpar();
            atualizarTabela();
            mostrarSucesso(novo.getNome() + " foi cadastrado(a)!");
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage()); // mensagem vinda da validacao do Model
        }
    }

    private void atualizar() {
        Animal selecionado = tabela.getSelectionModel().getSelectedItem();
        try {
            controller.atualizar(selecionado.getId(), txtNome.getText(), txtAnos.getText(),
                    txtMeses.getText(), txtDono.getText(), cbExtra.getValue());
            atualizarTabela();
            mostrarSucesso("Dados de " + selecionado.getNome() + " atualizados!");
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void deletar() {
        Animal selecionado = tabela.getSelectionModel().getSelectedItem();
        // Pede confirmacao antes de apagar
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja deletar " + selecionado.getNome() + "?");
        confirmacao.setHeaderText("Confirmar exclusão");
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            controller.deletar(selecionado.getId());
            limpar();
            atualizarTabela();
            mostrarSucesso(selecionado.getNome() + " foi deletado(a).");
        }
    }

    private void limpar() {
        tabela.getSelectionModel().clearSelection();
        txtNome.clear();
        txtAnos.clear();
        txtMeses.clear();
        txtDono.clear();
        cbTipo.setDisable(false);
        trocarCampoExtra();
        lblMensagem.setText("");
    }

    // Quando clica numa linha, joga os dados no formulario
    private void preencherFormulario(Animal a) {
        if (a == null) return;
        cbTipo.setValue(a.getTipo());
        cbTipo.setDisable(true); // o tipo nao muda na atualizacao
        trocarCampoExtra();
        txtNome.setText(a.getNome());
        txtAnos.setText(String.valueOf(a.getAnos()));
        txtMeses.setText(String.valueOf(a.getMeses()));
        txtDono.setText(a.getDono());
        if (a instanceof Cachorro) cbExtra.setValue(((Cachorro) a).getPorte());
        if (a instanceof Gato) cbExtra.setValue(((Gato) a).isPelagemLonga() ? "Sim" : "Não");
        lblMensagem.setText("");
    }

    private void atualizarTabela() {
        List<Animal> animais = controller.consultar(txtBusca.getText());
        tabela.setItems(FXCollections.observableArrayList(animais));
        tabela.refresh();
        lblQuantidade.setText("Animais na lista: " + animais.size());
        lblTotal.setText("Total de banhos: " + formatarPreco(controller.calcularTotalBanhos()));
    }

    private String formatarPreco(double valor) {
        return String.format(BRASIL, "R$ %.2f", valor);
    }

    private void mostrarSucesso(String texto) {
        lblMensagem.getStyleClass().setAll("label", "msg-sucesso");
        lblMensagem.setText(texto);
    }

    private void mostrarErro(String texto) {
        lblMensagem.getStyleClass().setAll("label", "msg-erro");
        lblMensagem.setText(texto);
        Alert alerta = new Alert(Alert.AlertType.ERROR, texto);
        alerta.setHeaderText("Dados inválidos");
        alerta.showAndWait();
    }
}
