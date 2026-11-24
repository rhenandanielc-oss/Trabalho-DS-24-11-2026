package petshop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import petshop.controller.AnimalController;
import petshop.model.Animal;

// TESTES (JUnit): conferem automaticamente se o Controller e o Model funcionam.
// Cada metodo com @Test e um caso de teste. Rodar com: mvn test
// assertEquals(esperado, obtido) -> o teste falha se forem diferentes.
public class AnimalControllerTest {

    private AnimalController controller;

    @BeforeEach // roda antes de cada teste: comeca sempre com a lista vazia
    void preparar() {
        controller = new AnimalController();
    }

    @Test // 1) cadastrar
    void deveCadastrarAnimal() {
        controller.cadastrar("Cachorro", "Rex", "3", "0", "Ana", "M");
        assertEquals(1, controller.consultar("").size());
    }

    @Test // 2) consultar: "mi" deve achar a Mimi
    void deveConsultarPorNome() {
        controller.cadastrar("Cachorro", "Rex", "3", "0", "Ana", "M");
        controller.cadastrar("Gato", "Mimi", "2", "5", "Bruno", "Sim");
        assertEquals("Mimi", controller.consultar("mi").get(0).getNome());
    }

    @Test // 3) atualizar
    void deveAtualizarAnimal() {
        Animal a = controller.cadastrar("Gato", "Mimi", "0", "6", "Bruno", "Não");
        controller.atualizar(a.getId(), "Mimi", "1", "2", "Carla", "Não");
        assertEquals("1 ano e 2 meses", a.getIdadeFormatada());
        assertEquals("Carla", a.getDono());
    }

    @Test // 4) deletar
    void deveDeletarAnimal() {
        Animal a = controller.cadastrar("Cachorro", "Rex", "3", "0", "Ana", "P");
        assertTrue(controller.deletar(a.getId()));
        assertTrue(controller.consultar("").isEmpty());
    }

    @Test // 5) validacao: assertThrows espera que de erro
    void naoDeveCadastrarComDadosInvalidos() {
        // A validacao do Model deve impedir: nome vazio e 12 meses (o maximo e 11)
        assertThrows(IllegalArgumentException.class,
                () -> controller.cadastrar("Cachorro", "", "3", "0", "Ana", "P"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.cadastrar("Gato", "Mimi", "1", "12", "Bruno", "Sim"));
    }

    @Test // 6) polimorfismo: cada animal calcula o seu preco
    void deveCalcularPrecoComPolimorfismo() {
        controller.cadastrar("Cachorro", "Rex", "3", "0", "Ana", "G");    // 80
        controller.cadastrar("Gato", "Mimi", "2", "0", "Bruno", "Sim");   // 70
        assertEquals(150.0, controller.calcularTotalBanhos());
    }
}
