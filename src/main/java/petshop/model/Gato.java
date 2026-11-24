package petshop.model;

// MODEL - HERANCA: Gato "e um" Animal (extends).
// Herda id, nome, idade e dono; acrescenta so a pelagem.
public class Gato extends Animal {

    private boolean pelagemLonga; // true = pelo longo, false = pelo curto

    public Gato(int id, String nome, int anos, int meses, String dono, boolean pelagemLonga) {
        super(id, nome, anos, meses, dono);
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

    @Override
    public String getDetalhe() { return pelagemLonga ? "Pelo longo" : "Pelo curto"; }
}
