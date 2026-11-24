package petshop.model;

// Classe-base (abstrata): nao da para criar um "Animal" generico, so Cachorro ou Gato.
public abstract class Animal {

    // Atributos privados = ENCAPSULAMENTO (so acessados pelos getters/setters)
    private int id;
    private String nome;
    private int idade;
    private String dono;

    public Animal(int id, String nome, int idade, String dono) {
        this.id = id;
        setNome(nome);   // usa o setter para ja validar
        setIdade(idade);
        setDono(dono);
    }

    // Metodos abstratos: cada subclasse e OBRIGADA a sobrescrever (POLIMORFISMO)
    public abstract double calcularPrecoBanho();
    public abstract String getTipo();

    // Getters e setters com VALIDACAO
    public int getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome nao pode ficar vazio.");
        }
        this.nome = nome.trim();
    }

    public int getIdade() { return idade; }
    public void setIdade(int idade) {
        if (idade < 0 || idade > 40) {
            throw new IllegalArgumentException("Idade deve estar entre 0 e 40.");
        }
        this.idade = idade;
    }

    public String getDono() { return dono; }
    public void setDono(String dono) {
        if (dono == null || dono.isBlank()) {
            throw new IllegalArgumentException("O nome do dono nao pode ficar vazio.");
        }
        this.dono = dono.trim();
    }

    // Sobrescrita do toString() herdado de Object
    @Override
    public String toString() {
        return getTipo() + ": " + nome + " (dono: " + dono + ")";
    }
}
