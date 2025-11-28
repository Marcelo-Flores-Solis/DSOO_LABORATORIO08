package gestor;

import modelo.cuentas.*;
import modelo.transacciones.*;  // Esta importa todas las clases de transacciones
import modelo.personas.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class GestorBanco {
    private GestorUsuarios gestorUsuarios;
    private List<Cuenta> cuentas;
    private List<Transaccion> transacciones;
    private List<Titularidad> titularidades;
    
    // Constructor
    public GestorBanco() {
        this.gestorUsuarios = new GestorUsuarios();
        this.cuentas = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.titularidades = new ArrayList<>();
        inicializarDatosDemo();
    }
    
    // Getters
    public GestorUsuarios getGestorUsuarios() { return gestorUsuarios; }
    public List<Cuenta> getCuentas() { return new ArrayList<>(cuentas); }
    public List<Transaccion> getTransacciones() { return new ArrayList<>(transacciones); }
    public List<Titularidad> getTitularidades() { return new ArrayList<>(titularidades); }
    
    // Inicialización de datos de demostración
    private void inicializarDatosDemo() {
        // Obtener clientes demo del gestor de usuarios
        List<Cliente> clientes = gestorUsuarios.getClientes();
        
        if (clientes.size() >= 2) {
            // Crear cuentas demo
            CuentaAhorros cuenta1 = new CuentaAhorros("AHO-001", 1500.0, clientes.get(0));
            CuentaCorriente cuenta2 = new CuentaCorriente("CTE-001", 3000.0, clientes.get(1));
            CuentaAhorros cuenta3 = new CuentaAhorros("AHO-002", 500.0, clientes.get(0));
            
            cuentas.add(cuenta1);
            cuentas.add(cuenta2);
            cuentas.add(cuenta3);
            
            // Crear titularidades demo
            Titularidad titularidad1 = new Titularidad(cuenta1, clientes.get(0));
            Titularidad titularidad2 = new Titularidad(cuenta2, clientes.get(1));
            Titularidad titularidad3 = new Titularidad(cuenta3, clientes.get(0));
            
            titularidades.add(titularidad1);
            titularidades.add(titularidad2);
            titularidades.add(titularidad3);
            
            // Agregar cuentas a los clientes
            clientes.get(0).agregarCuenta("AHO-001");
            clientes.get(0).agregarCuenta("AHO-002");
            clientes.get(1).agregarCuenta("CTE-001");
            
            System.out.println("Datos bancarios de demostración inicializados correctamente");
        }
    }
    
    // Métodos de gestión de cuentas
    public boolean crearCuentaAhorros(String numeroCuenta, double saldoInicial, Cliente cliente) {
        // Verificar si la cuenta ya existe
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                System.out.println("Error: Ya existe una cuenta con ese número");
                return false;
            }
        }
        
        CuentaAhorros nuevaCuenta = new CuentaAhorros(numeroCuenta, saldoInicial, cliente);
        cuentas.add(nuevaCuenta);
        
        // Crear titularidad
        Titularidad titularidad = new Titularidad(nuevaCuenta, cliente);
        titularidades.add(titularidad);
        
        // Agregar cuenta al cliente
        cliente.agregarCuenta(numeroCuenta);
        
        System.out.println("Cuenta de ahorros " + numeroCuenta + " creada exitosamente para " + 
                         cliente.getNombreCompleto());
        return true;
    }
    
    public boolean crearCuentaCorriente(String numeroCuenta, double saldoInicial, Cliente cliente) {
        // Verificar si la cuenta ya existe
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                System.out.println("Error: Ya existe una cuenta con ese número");
                return false;
            }
        }
        
        CuentaCorriente nuevaCuenta = new CuentaCorriente(numeroCuenta, saldoInicial, cliente);
        cuentas.add(nuevaCuenta);
        
        // Crear titularidad
        Titularidad titularidad = new Titularidad(nuevaCuenta, cliente);
        titularidades.add(titularidad);
        
        // Agregar cuenta al cliente
        cliente.agregarCuenta(numeroCuenta);
        
        System.out.println("Cuenta corriente " + numeroCuenta + " creada exitosamente para " + 
                         cliente.getNombreCompleto());
        return true;
    }
    
    public Cuenta buscarCuenta(String numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }
    
    public List<Cuenta> buscarCuentasPorCliente(Cliente cliente) {
        List<Cuenta> cuentasCliente = new ArrayList<>();
        for (Titularidad titularidad : titularidades) {
            if (titularidad.getTitulares().contains(cliente)) {
                cuentasCliente.add(titularidad.getCuenta());
            }
        }
        return cuentasCliente;
    }
    
    public boolean cerrarCuenta(String numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta != null) {
            if (cuenta.getSaldo() == 0) {
                cuenta.desactivarCuenta();
                System.out.println("Cuenta " + numeroCuenta + " cerrada exitosamente");
                return true;
            } else {
                System.out.println("Error: La cuenta debe tener saldo cero para ser cerrada");
                return false;
            }
        } else {
            System.out.println("Error: Cuenta no encontrada");
            return false;
        }
    }
    
    // Métodos de transacciones
    public Transaccion realizarDeposito(String numeroCuenta, double monto, String descripcion) {
        if (!gestorUsuarios.tienePermiso("realizar_depositos")) {
            System.out.println("Error: No tiene permisos para realizar depósitos");
            return null;
        }
        
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: Cuenta no encontrada");
            return null;
        }
        
        Usuario usuarioEjecutor = gestorUsuarios.getUsuarioActual();
        Deposito deposito = new Deposito(monto, cuenta, descripcion, usuarioEjecutor);
        
        if (deposito.ejecutar()) {
            transacciones.add(deposito);
            System.out.println("Depósito realizado exitosamente");
            return deposito;
        } else {
            System.out.println("Error al realizar el depósito");
            return null;
        }
    }
    
    public Transaccion realizarRetiro(String numeroCuenta, double monto, String metodoRetiro, String ubicacion) {
        if (!gestorUsuarios.tienePermiso("realizar_retiros")) {
            System.out.println("Error: No tiene permisos para realizar retiros");
            return null;
        }
        
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Error: Cuenta no encontrada");
            return null;
        }
        
        Usuario usuarioEjecutor = gestorUsuarios.getUsuarioActual();
        String descripcion = "Retiro por " + metodoRetiro + " en " + ubicacion;
        Retiro retiro = new Retiro(monto, cuenta, metodoRetiro, ubicacion, descripcion, usuarioEjecutor);
        
        if (retiro.ejecutar()) {
            transacciones.add(retiro);
            System.out.println("Retiro realizado exitosamente");
            return retiro;
        } else {
            System.out.println("Error al realizar el retiro");
            return null;
        }
    }
    
    public Transaccion realizarTransferencia(String cuentaOrigen, String cuentaDestino, 
                                           double monto, String concepto, String referencia) {
        if (!gestorUsuarios.tienePermiso("realizar_transferencias")) {
            System.out.println("Error: No tiene permisos para realizar transferencias");
            return null;
        }
        
        Cuenta origen = buscarCuenta(cuentaOrigen);
        Cuenta destino = buscarCuenta(cuentaDestino);
        
        if (origen == null || destino == null) {
            System.out.println("Error: Una de las cuentas no existe");
            return null;
        }
        
        if (origen.equals(destino)) {
            System.out.println("Error: No puede transferir a la misma cuenta");
            return null;
        }
        
        Usuario usuarioEjecutor = gestorUsuarios.getUsuarioActual();
        String descripcion = "Transferencia - " + concepto;
        Transferencia transferencia = new Transferencia(monto, origen, destino, concepto, referencia, descripcion, usuarioEjecutor);
        
        if (transferencia.ejecutar()) {
            transacciones.add(transferencia);
            System.out.println("Transferencia realizada exitosamente");
            return transferencia;
        } else {
            System.out.println("Error al realizar la transferencia");
            return null;
        }
    }
    
    // Métodos de consulta
    public void consultarSaldo(String numeroCuenta) {
        if (!gestorUsuarios.tienePermiso("consultar_saldo")) {
            System.out.println("Error: No tiene permisos para consultar saldos");
            return;
        }
        
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta != null) {
            cuenta.consultarSaldo();
        } else {
            System.out.println("Error: Cuenta no encontrada");
        }
    }
    
    public void consultarMovimientos(String numeroCuenta) {
        if (!gestorUsuarios.tienePermiso("consultar_movimientos")) {
            System.out.println("Error: No tiene permisos para consultar movimientos");
            return;
        }
        
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta != null) {
            cuenta.consultarMovimientos();
        } else {
            System.out.println("Error: Cuenta no encontrada");
        }
    }
    
    // Métodos de información
    public void mostrarTodasLasCuentas() {
        System.out.println("=== LISTA DE TODAS LAS CUENTAS ===");
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas registradas");
            return;
        }
        
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuenta = cuentas.get(i);
            String tipo = cuenta.getClass().getSimpleName();
            String estado = cuenta.estaActiva() ? "Activa" : "Inactiva";
            System.out.printf("%d. %s - %s - Saldo: $%.2f - %s%n", 
                i + 1, cuenta.getNumeroCuenta(), tipo, cuenta.getSaldo(), estado);
        }
    }
    
    public void mostrarTodasLasTransacciones() {
        System.out.println("=== LISTA DE TODAS LAS TRANSACCIONES ===");
        if (transacciones.isEmpty()) {
            System.out.println("No hay transacciones registradas");
            return;
        }
        
        for (int i = 0; i < transacciones.size(); i++) {
            Transaccion transaccion = transacciones.get(i);
            System.out.printf("%d. %s%n", i + 1, transaccion.toStringCorto());
        }
    }
    
    public void mostrarEstadisticasBancarias() {
        System.out.println("=== ESTADÍSTICAS BANCARIAS ===");
        System.out.println("Total de cuentas: " + cuentas.size());
        System.out.println("Total de transacciones: " + transacciones.size());
        System.out.println("Total de titularidades: " + titularidades.size());
        
        // Contar por tipo de cuenta
        long ahorrosCount = cuentas.stream().filter(c -> c instanceof CuentaAhorros).count();
        long corrienteCount = cuentas.stream().filter(c -> c instanceof CuentaCorriente).count();
        
        System.out.println("Cuentas de ahorro: " + ahorrosCount);
        System.out.println("Cuentas corrientes: " + corrienteCount);
        
        // Contar por tipo de transacción
        long depositosCount = transacciones.stream().filter(t -> t instanceof Deposito).count();
        long retirosCount = transacciones.stream().filter(t -> t instanceof Retiro).count();
        long transferenciasCount = transacciones.stream().filter(t -> t instanceof Transferencia).count();
        
        System.out.println("Depósitos realizados: " + depositosCount);
        System.out.println("Retiros realizados: " + retirosCount);
        System.out.println("Transferencias realizadas: " + transferenciasCount);
        
        // Calcular total de dinero en el sistema
        double totalDinero = cuentas.stream().mapToDouble(Cuenta::getSaldo).sum();
        System.out.printf("Total de dinero en el sistema: $%.2f%n", totalDinero);
    }
    
    // Métodos de utilidad
    public boolean existeCuenta(String numeroCuenta) {
        return buscarCuenta(numeroCuenta) != null;
    }
    
    public boolean esTitularCuenta(Cliente cliente, String numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) return false;
        
        for (Titularidad titularidad : titularidades) {
            if (titularidad.getCuenta().equals(cuenta) && titularidad.esTitular(cliente)) {
                return true;
            }
        }
        return false;
    }
    
    // Método para obtener transacciones recientes
    public List<Transaccion> getTransaccionesRecientes(int cantidad) {
        int startIndex = Math.max(0, transacciones.size() - cantidad);
        return new ArrayList<>(transacciones.subList(startIndex, transacciones.size()));
    }
}