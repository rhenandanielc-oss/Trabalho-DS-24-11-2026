package petshop;

import javafx.application.Application;
import javafx.stage.Stage;
import petshop.view.TelaPrincipal;

// Classe que inicia o programa JavaFX
public class App extends Application {

    @Override
    public void start(Stage stage) {
        new TelaPrincipal().mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
