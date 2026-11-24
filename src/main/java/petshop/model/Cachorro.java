package petshop.model;

// HERANCA: Cachorro "e um" Animal (extends)
public class Cachorro extends Animal {

    private String porte; // P, M ou G

    public Cachorro(int id, String nome, int anos, int meses, String dono, String porte) {
        super(id, nome, anos, meses, dono); // chama o construtor da classe-base
        setPorte(porte);
    }

    public String getPorte() { return porte; }
    public void setPorte(String porte) {
        if (porte == null || !porte.matches("[PMG]")) {
            throw new IllegalArgumentException("Porte deve ser P, M ou G.");
        }
        this.porte = porte;
    }

    // SOBRESCRITA: o preco do banho depende do porte
    @Override
    public double calcularPrecoBanho() {
        switch (porte) {
            case "P": return 40.0;
            case "M": return 60.0;
            default:  return 80.0; // G
        }
    }

    @Override
    public String getTipo() { return "Cachorro"; }

    @Override
    public String getDetalhe() { return "Porte " + porte; }
}
