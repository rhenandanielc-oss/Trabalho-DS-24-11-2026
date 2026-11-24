package petshop;

import javafx.application.Application;
import javafx.stage.Stage;
import petshop.view.TelaPrincipal;

// Classe que inicia o programa JavaFX.
// Todo programa JavaFX "extends Application" e comeca pelo metodo start().
public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Stage = a janela. Pedimos para a View montar a tela dentro dela.
        new TelaPrincipal().mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
