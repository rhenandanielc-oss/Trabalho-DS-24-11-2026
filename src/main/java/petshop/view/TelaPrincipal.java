package petshop.view;

import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import petshop.controller.AnimalController;
import petshop.model.Animal;
import petshop.model.Cachorro;
import petshop.model.Gato;

// VIEW: so monta a tela e repassa as acoes para o Controller.
public class TelaPrincipal {

    private final AnimalController controller = new AnimalController();

    // Componentes do formulario
    private final ComboBox<String> cbTipo = new ComboBox<>();
    private final TextField txtNome = new TextField();
    private final TextField txtIdade = new TextField();
    private final TextField txtDono = new TextField();
    private final ComboBox<String> cbExtra = new ComboBox<>();
    private final Label lblExtra = new Label("Porte:");
    private final TextField txtBusca = new TextField();
    private final TableView<Animal> tabela = new TableView<>();
    private final Label lblTotal = new Label();

    public void mostrar(Stage stage) {
        // --- Formulario ---
        cbTipo.getItems().addAll("Cachorro", "Gato");
        cbTipo.setValue("Cachorro");
        cbTipo.setOnAction(e -> trocarCampoExtra());
        trocarCampoExtra();

        GridPane form = new GridPane();
        form.getStyleClass().add("formulario");
        form.addRow(0, new Label("Tipo:"), cbTipo);
        form.addRow(1, new Label("Nome:"), txtNome);
        form.addRow(2, new Label("Idade:"), txtIdade);
        form.addRow(3, new Label("Dono:"), txtDono);
        form.addRow(4, lblExtra, cbExtra);

        // --- Botoes ---
        Button btnCadastrar = new Button("Cadastrar");
        Button btnAtualizar = new Button("Atualizar");
        Button btnDeletar = new Button("Deletar");
        Button btnLimpar = new Button("Limpar");
        btnDeletar.getStyleClass().add("perigo");

        btnCadastrar.setOnAction(e -> cadastrar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnDeletar.setOnAction(e -> deletar());
        btnLimpar.setOnAction(e -> limpar());

        HBox botoes = new HBox(10, btnCadastrar, btnAtualizar, btnDeletar, btnLimpar);

        // --- Busca ---
        txtBusca.setPromptText("Buscar por nome...");
        txtBusca.textProperty().addListener((obs, antigo, novo) -> atualizarTabela());

        // --- Tabela ---
        criarColunas();
        tabela.getSelectionModel().selectedItemProperty()
              .addListener((obs, antigo, animal) -> preencherFormulario(animal));

        Label titulo = new Label("Pet Shop - Cadastro de Animais");
        titulo.getStyleClass().add("titulo");

        VBox raiz = new VBox(12, titulo, form, botoes, txtBusca, tabela, lblTotal);
        raiz.getStyleClass().add("raiz");

        Scene cena = new Scene(raiz, 620, 640);
        // Liga o arquivo CSS a tela
        cena.getStylesheets().add(getClass().getResource("/petshop/estilo.css").toExternalForm());

        stage.setTitle("Pet Shop");
        stage.setScene(cena);
        stage.show();
        atualizarTabela();
    }

    private void criarColunas() {
        TableColumn<Animal, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Animal, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Animal, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Animal, Integer> colIdade = new TableColumn<>("Idade");
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        TableColumn<Animal, String> colDono = new TableColumn<>("Dono");
        colDono.setCellValueFactory(new PropertyValueFactory<>("dono"));
        // Usa calcularPrecoBanho() -> POLIMORFISMO direto na tabela
        TableColumn<Animal, Double> colPreco = new TableColumn<>("Banho (R$)");
        colPreco.setCellValueFactory(c ->
            new SimpleObjectProperty<>(c.getValue().calcularPrecoBanho()));

        // List.of evita o aviso de "generic array" do compilador
        tabela.getColumns().addAll(List.of(colId, colTipo, colNome, colIdade, colDono, colPreco));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    // Muda o ultimo campo conforme o tipo escolhido
    private void trocarCampoExtra() {
        cbExtra.getItems().clear();
        if ("Cachorro".equals(cbTipo.getValue())) {
            lblExtra.setText("Porte:");
            cbExtra.getItems().addAll("P", "M", "G");
            cbExtra.setValue("P");
        } else {
            lblExtra.setText("Pelo longo:");
            cbExtra.getItems().addAll("Nao", "Sim");
            cbExtra.setValue("Nao");
        }
    }

    // ===== Acoes dos botoes (todas passam pelo Controller) =====

    private void cadastrar() {
        try {
            controller.cadastrar(cbTipo.getValue(), txtNome.getText(),
                    txtIdade.getText(), txtDono.getText(), cbExtra.getValue());
            limpar();
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage()); // mensagem vinda da validacao do Model
        }
    }

    private void atualizar() {
        Animal selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) { mostrarErro("Selecione um animal na tabela."); return; }
        try {
            controller.atualizar(selecionado.getId(), txtNome.getText(),
                    txtIdade.getText(), txtDono.getText(), cbExtra.getValue());
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void deletar() {
        Animal selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) { mostrarErro("Selecione um animal na tabela."); return; }
        controller.deletar(selecionado.getId());
        limpar();
        atualizarTabela();
    }

    private void limpar() {
        tabela.getSelectionModel().clearSelection();
        txtNome.clear();
        txtIdade.clear();
        txtDono.clear();
        cbTipo.setDisable(false);
        trocarCampoExtra();
    }

    // Quando clica numa linha, joga os dados no formulario
    private void preencherFormulario(Animal a) {
        if (a == null) return;
        cbTipo.setValue(a.getTipo());
        cbTipo.setDisable(true); // tipo nao muda na atualizacao
        trocarCampoExtra();
        txtNome.setText(a.getNome());
        txtIdade.setText(String.valueOf(a.getIdade()));
        txtDono.setText(a.getDono());
        if (a instanceof Cachorro) cbExtra.setValue(((Cachorro) a).getPorte());
        if (a instanceof Gato) cbExtra.setValue(((Gato) a).isPelagemLonga() ? "Sim" : "Nao");
    }

    private void atualizarTabela() {
        tabela.setItems(FXCollections.observableArrayList(controller.consultar(txtBusca.getText())));
        tabela.refresh();
        lblTotal.setText(String.format("Total se todos tomarem banho: R$ %.2f",
                controller.calcularTotalBanhos()));
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensagem);
        alerta.setHeaderText("Dados invalidos");
        alerta.showAndWait();
    }
}
