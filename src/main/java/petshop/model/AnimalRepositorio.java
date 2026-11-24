package petshop.model;

import java.util.ArrayList;
import java.util.List;

// Guarda os animais em memoria (uma lista). Faz parte do MODEL.
public class AnimalRepositorio {

    private final List<Animal> animais = new ArrayList<>();
    private int proximoId = 1;

    public int gerarId() { return proximoId++; }

    public void adicionar(Animal animal) { animais.add(animal); }

    public List<Animal> listarTodos() { return animais; }

    public Animal buscarPorId(int id) {
        for (Animal a : animais) {
            if (a.getId() == id) return a;
        }
        return null; // nao encontrou
    }

    public List<Animal> buscarPorNome(String texto) {
        List<Animal> resultado = new ArrayList<>();
        for (Animal a : animais) {
            if (a.getNome().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public boolean remover(int id) {
        return animais.remove(buscarPorId(id));
    }
}
