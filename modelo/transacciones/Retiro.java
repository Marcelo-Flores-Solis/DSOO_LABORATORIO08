package modelo.transacciones;
import modelo.cuentas.Cuenta;
import modelo.personas.Usuario;
public class Retiro extends Transaccion {
    private String metodoRetiro;
    private String ubicacion;
    private boolean requiereValidacion;
    public Retiro(double monto, Cuenta cuentaOrigen, String metodoRetiro, String ubicacion, 
                  String descripcion, Usuario usuarioEjecutor) {
        super(monto, cuentaOrigen, "RETIRO", descripcion, usuarioEjecutor);
        this.metodoRetiro = metodoRetiro;
        this.ubicacion = ubicacion;
        this.requiereValidacion = monto > 1000;
    }
    public String getMetodoRetiro() { return metodoRetiro; }
    public void setMetodoRetiro(String metodoRetiro) { this.metodoRetiro = metodoRetiro; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public boolean isRequiereValidacion() { return requiereValidacion; }
    @Override
    public boolean validar() {
        if (monto <= 0) {
            rechazarTransaccion("El monto debe ser positivo");
            return false;
        }
        if (getCuentaOrigen() == null) {
            rechazarTransaccion("Cuenta origen no especificada");
            return false;
        }
        if (!getCuentaOrigen().estaActiva()) {
            rechazarTransaccion("La cuenta origen no está activa");
            return false;
        }
        if (monto > getLimiteRetiroDiario()) {
            rechazarTransaccion("Excede el límite de retiro diario");
            return false;
        }
        if (monto > getCuentaOrigen().getSaldo()) {
            rechazarTransaccion("Fondos insuficientes");
            return false;
        }
        if (requiereValidacion && !validarRetiroGrande()) {
            rechazarTransaccion("Validación de retiro grande falló");
            return false;
        }
        System.out.println("Validación de retiro exitosa");
        return true;
    }
    @Override
    public boolean ejecutar() {
        if (!validar()) {
            return false;
        }
        try {
            boolean exito = getCuentaOrigen().retirar(monto);
            if (exito) {
                completarTransaccion();
                registrarRetiro();
                return true;
            } else {
                rechazarTransaccion("Error al procesar el retiro en la cuenta");
                return false;
            }
        } catch (Exception e) {
            rechazarTransaccion("Error inesperado: " + e.getMessage());
            return false;
        }
    }
    @Override
    public void revertir() {
        if (!estaCompletada()) {
            System.out.println("No se puede revertir una transacción no completada");
            return;
        }
        try {
            boolean exito = getCuentaOrigen().depositar(monto);
            if (exito) {
                setEstado(CANCELADA);
                System.out.println("Retiro revertido exitosamente");
                getCuentaOrigen().registrarMovimiento("REVERSION_RETIRO", monto,
                    "Reversión de retiro " + getIdTransaccion());
            } else { System.out.println("Error al revertir el retiro"); }
        } catch (Exception e) { System.out.println("Error al revertir retiro: " + e.getMessage()); }
    }
    private double getLimiteRetiroDiario() {
        switch (metodoRetiro.toUpperCase()) {
            case "CAJERO":
                return 1000.0;
            case "VENTANILLA":
                return 5000.0;
            case "OFICINA":
                return 10000.0;
            default:
                return 1000.0;
        }
    }
    private boolean validarRetiroGrande() {
        if (monto > 5000) {
            System.out.println("Retiro grande detectado - requiere autorización del gerente");
            return Math.random() > 0.1;
        }
        return true;
    }
    private void registrarRetiro() {
        System.out.println("Retiro registrado - Método: " + metodoRetiro + ", Ubicación: " + ubicacion);
        if (requiereValidacion) {
            System.out.println("Retiro grande - registro de validación completado");
        }
    }
    public boolean esRetiroCajero() {
        return "CAJERO".equalsIgnoreCase(metodoRetiro);
    }
    public boolean esRetiroVentanilla() {
        return "VENTANILLA".equalsIgnoreCase(metodoRetiro);
    }
    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("--- DETALLES ESPECÍFICOS DE RETIRO ---");
        System.out.println("Método de retiro: " + metodoRetiro);
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Requiere validación: " + (requiereValidacion ? "Sí" : "No"));
        System.out.println("Límite diario: $" + getLimiteRetiroDiario());
        if (getCuentaOrigen() != null) {
            System.out.println("Saldo actual cuenta: $" + getCuentaOrigen().getSaldo());
        }
    }
}