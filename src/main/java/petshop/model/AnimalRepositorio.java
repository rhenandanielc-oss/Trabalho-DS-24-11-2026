package petshop.model;

import java.util.ArrayList;
import java.util.List;

// MODEL - Repositorio: guarda os animais numa lista na memoria.
// E o "banco de dados" do sistema (os dados somem ao fechar o programa).
public class AnimalRepositorio {

    private final List<Animal> animais = new ArrayList<>();
    private int proximoId = 1; // contador para gerar ids 1, 2, 3...

    public int gerarId() { return proximoId++; } // devolve o id atual e soma 1

    public void adicionar(Animal animal) { animais.add(animal); }

    public List<Animal> listarTodos() { return animais; }

    // Procura um animal pelo id, olhando um por um
    public Animal buscarPorId(int id) {
        for (Animal a : animais) {
            if (a.getId() == id) return a;
        }
        return null; // nao encontrou
    }

    // Devolve os animais cujo nome CONTEM o texto (sem diferenciar maiusculas)
    public List<Animal> buscarPorNome(String texto) {
        List<Animal> resultado = new ArrayList<>();
        for (Animal a : animais) {
            if (a.getNome().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    // Remove pelo id; devolve true se conseguiu remover
    public boolean remover(int id) {
        return animais.remove(buscarPorId(id));
    }
}
