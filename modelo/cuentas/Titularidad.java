package modelo.cuentas;
import modelo.personas.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class Titularidad {
    private Cuenta cuenta;
    private List<Cliente> titulares;
    private Cliente titularPrincipal;
    private String tipoTitularidad;
    private LocalDateTime fechaCreacion;
    public static final String INDIVIDUAL = "INDIVIDUAL";
    public static final String CONJUNTA = "CONJUNTA";
    public static final String CORPORATIVA = "CORPORATIVA";
    public Titularidad(Cuenta cuenta, Cliente titularPrincipal) {
        this.cuenta = cuenta;
        this.titularPrincipal = titularPrincipal;
        this.titulares = new ArrayList<>();
        this.titulares.add(titularPrincipal);
        this.tipoTitularidad = INDIVIDUAL;
        this.fechaCreacion = LocalDateTime.now();
    }
    public Titularidad(Cuenta cuenta, List<Cliente> titulares, Cliente titularPrincipal) {
        this.cuenta = cuenta;
        this.titulares = new ArrayList<>(titulares);
        this.titularPrincipal = titularPrincipal;
        this.tipoTitularidad = titulares.size() > 1 ? CONJUNTA : INDIVIDUAL;
        this.fechaCreacion = LocalDateTime.now();
    }
    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
    public List<Cliente> getTitulares() { return new ArrayList<>(titulares); }
    public Cliente getTitularPrincipal() { return titularPrincipal; }
    public void setTitularPrincipal(Cliente titularPrincipal) {
        this.titularPrincipal = titularPrincipal;
    }
    public String getTipoTitularidad() { return tipoTitularidad; }
    public void setTipoTitularidad(String tipoTitularidad) {
        this.tipoTitularidad = tipoTitularidad;
    }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public boolean agregarTitular(Cliente cliente) {
        if (titulares.contains(cliente)) {
            System.out.println("El cliente ya es titular de esta cuenta");
            return false;
        }
        titulares.add(cliente);
        if (tipoTitularidad.equals(INDIVIDUAL) && titulares.size() > 1) {
            tipoTitularidad = CONJUNTA;
        }
        System.out.println("Titular agregado: " + cliente.getNombreCompleto());
        return true;
    }
    public boolean eliminarTitular(Cliente cliente) {
        if (cliente.equals(titularPrincipal)) {
            System.out.println("Error: No se puede eliminar al titular principal");
            return false;
        }
        if (titulares.remove(cliente)) {
            if (titulares.size() == 1) {
                tipoTitularidad = INDIVIDUAL;
            }
            System.out.println("Titular eliminado: " + cliente.getNombreCompleto());
            return true;
        }
        System.out.println("Error: El cliente no es titular de esta cuenta");
        return false;
    }
    public boolean esTitular(Cliente cliente) {
        return titulares.contains(cliente);
    }
    public boolean esTitularPrincipal(Cliente cliente) {
        return titularPrincipal.equals(cliente);
    }
    public void cambiarTitularPrincipal(Cliente nuevoTitularPrincipal) {
        if (!titulares.contains(nuevoTitularPrincipal)) {
            System.out.println("Error: El cliente debe ser titular de la cuenta");
            return;
        }
        this.titularPrincipal = nuevoTitularPrincipal;
        System.out.println("Nuevo titular principal: " + nuevoTitularPrincipal.getNombreCompleto());
    }
    public void mostrarTitulares() {
        System.out.println("=== TITULARES DE CUENTA " + cuenta.getNumeroCuenta() + " ===");
        System.out.println("Tipo de titularidad: " + tipoTitularidad);
        System.out.println("Titular principal: " + titularPrincipal.getNombreCompleto());
        System.out.println("Total de titulares: " + titulares.size());
        for (int i = 0; i < titulares.size(); i++) {
            Cliente titular = titulares.get(i);
            String indicador = titular.equals(titularPrincipal) ? " (Principal)" : "";
            System.out.println((i + 1) + ". " + titular.getNombreCompleto() + indicador);
        }
    }
    public void generarResumenTitularidad() {
        System.out.println("=== RESUMEN DE TITULARIDAD ===");
        System.out.println("Cuenta: " + cuenta.getNumeroCuenta());
        System.out.println("Tipo de cuenta: " + cuenta.getClass().getSimpleName());
        System.out.println("Tipo titularidad: " + tipoTitularidad);
        System.out.println("Fecha creación: " + fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Titular principal: " + titularPrincipal.getNombreCompleto());
        System.out.println("Total titulares: " + titulares.size());
        System.out.println("Saldo: $" + cuenta.getSaldo());
    }
    public boolean esCuentaConjunta() {
        return tipoTitularidad.equals(CONJUNTA);
    }
    public boolean esCuentaIndividual() {
        return tipoTitularidad.equals(INDIVIDUAL);
    }
    @Override
    public String toString() {
        return "Cuenta: " + cuenta.getNumeroCuenta() + 
               " | Titularidad: " + tipoTitularidad + 
               " | Titulares: " + titulares.size();
    }
}