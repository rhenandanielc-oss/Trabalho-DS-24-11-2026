package petshop.model;

// Classe-base (abstrata): nao da para criar um "Animal" generico, so Cachorro ou Gato.
public abstract class Animal {

    // Atributos privados = ENCAPSULAMENTO (so acessados pelos getters/setters)
    private int id;
    private String nome;
    private int anos;
    private int meses;
    private String dono;

    public Animal(int id, String nome, int anos, int meses, String dono) {
        this.id = id;
        setNome(nome);          // usa os setters para ja validar
        setIdade(anos, meses);
        setDono(dono);
    }

    // Metodos abstratos: cada subclasse e OBRIGADA a sobrescrever (POLIMORFISMO)
    public abstract double calcularPrecoBanho();
    public abstract String getTipo();
    public abstract String getDetalhe(); // caracteristica propria (porte ou pelagem)

    // Getters e setters com VALIDACAO
    public int getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }
        this.nome = nome.trim();
    }

    public int getAnos() { return anos; }
    public int getMeses() { return meses; }

    // Idade em anos + meses. Ex.: 1 ano e 2 meses
    public void setIdade(int anos, int meses) {
        if (anos < 0 || anos > 40) {
            throw new IllegalArgumentException("Anos deve estar entre 0 e 40.");
        }
        if (meses < 0 || meses > 11) {
            throw new IllegalArgumentException("Meses deve estar entre 0 e 11.");
        }
        this.anos = anos;
        this.meses = meses;
    }

    // Monta o texto da idade para mostrar na tela
    public String getIdadeFormatada() {
        String textoAnos = anos + (anos == 1 ? " ano" : " anos");
        String textoMeses = meses + (meses == 1 ? " mês" : " meses");
        if (anos == 0) return textoMeses;
        if (meses == 0) return textoAnos;
        return textoAnos + " e " + textoMeses;
    }

    public String getDono() { return dono; }
    public void setDono(String dono) {
        if (dono == null || dono.isBlank()) {
            throw new IllegalArgumentException("O nome do dono não pode ficar vazio.");
        }
        this.dono = dono.trim();
    }

    // Sobrescrita do toString() herdado de Object
    @Override
    public String toString() {
        return getTipo() + ": " + nome + " (dono: " + dono + ")";
    }
}
