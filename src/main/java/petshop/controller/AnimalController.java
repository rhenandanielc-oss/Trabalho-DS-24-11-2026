package petshop.controller;

import java.util.List;
import petshop.model.Animal;
import petshop.model.AnimalRepositorio;
import petshop.model.Cachorro;
import petshop.model.Gato;

// CONTROLLER: recebe os dados da tela, converte, e manda para o Model.
// A tela (View) nunca mexe direto na lista de animais.
public class AnimalController {

    private final AnimalRepositorio repositorio = new AnimalRepositorio();

    // CADASTRAR: "extra" e o porte (cachorro) ou "Sim"/"Nao" para pelo longo (gato)
    public Animal cadastrar(String tipo, String nome, String idadeTexto, String dono, String extra) {
        int idade = converterIdade(idadeTexto);
        int id = repositorio.gerarId();

        Animal animal; // variavel do tipo da classe-base guarda qualquer subclasse
        if ("Cachorro".equals(tipo)) {
            animal = new Cachorro(id, nome, idade, dono, extra);
        } else {
            animal = new Gato(id, nome, idade, dono, "Sim".equals(extra));
        }
        repositorio.adicionar(animal);
        return animal;
    }

    // CONSULTAR: texto vazio devolve todos
    public List<Animal> consultar(String nome) {
        if (nome == null || nome.isBlank()) return repositorio.listarTodos();
        return repositorio.buscarPorNome(nome);
    }

    // ATUALIZAR: muda os dados de um animal ja existente
    public void atualizar(int id, String nome, String idadeTexto, String dono, String extra) {
        Animal animal = repositorio.buscarPorId(id);
        if (animal == null) throw new IllegalArgumentException("Animal nao encontrado.");

        animal.setNome(nome);
        animal.setIdade(converterIdade(idadeTexto));
        animal.setDono(dono);

        // Cada tipo tem seu atributo proprio
        if (animal instanceof Cachorro) {
            ((Cachorro) animal).setPorte(extra);
        } else if (animal instanceof Gato) {
            ((Gato) animal).setPelagemLonga("Sim".equals(extra));
        }
    }

    // DELETAR
    public boolean deletar(int id) {
        return repositorio.remover(id);
    }

    // POLIMORFISMO: soma o preco sem saber se e Cachorro ou Gato
    public double calcularTotalBanhos() {
        double total = 0;
        for (Animal a : repositorio.listarTodos()) {
            total += a.calcularPrecoBanho(); // cada objeto usa a SUA versao do metodo
        }
        return total;
    }

    // Transforma o texto digitado em numero
    private int converterIdade(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Idade deve ser um numero inteiro.");
        }
    }
}
