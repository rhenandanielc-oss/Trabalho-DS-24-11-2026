package petshop.model;

// HERANCA: Gato "e um" Animal (extends)
public class Gato extends Animal {

    private boolean pelagemLonga;

    public Gato(int id, String nome, int idade, String dono, boolean pelagemLonga) {
        super(id, nome, idade, dono);
        this.pelagemLonga = pelagemLonga;
    }

    public boolean isPelagemLonga() { return pelagemLonga; }
    public void setPelagemLonga(boolean pelagemLonga) { this.pelagemLonga = pelagemLonga; }

    // SOBRESCRITA: gato de pelo longo paga mais pelo banho
    @Override
    public double calcularPrecoBanho() {
        return pelagemLonga ? 70.0 : 50.0;
    }

    @Override
    public String getTipo() { return "Gato"; }
}
