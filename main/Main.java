package main;
import gestor.GestorBanco;
import modelo.personas.*;
import modelo.cuentas.*;
import modelo.transacciones.*;
import java.util.Scanner;

public class Main {
    private static GestorBanco gestorBanco;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        gestorBanco = new GestorBanco();
        scanner = new Scanner(System.in);
        
        System.out.println("==========================================");
        System.out.println("    SISTEMA BANCARIO - LABORATORIO 07");
        System.out.println("==========================================");
        
        mostrarMenuPrincipal();
        
        scanner.close();
        System.out.println("¡Gracias por usar el Sistema Bancario!");
    }
    
    // Menú principal del sistema
    private static void mostrarMenuPrincipal() {
        int opcion;
        
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ingresar al Sistema");
            System.out.println("2. Ver Información del Sistema");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    procesoAutenticacion();
                    break;
                case 2:
                    mostrarInformacionSistema();
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 3);
    }
    
    // Proceso de autenticación
    private static void procesoAutenticacion() {
        System.out.println("\n=== INICIO DE SESIÓN ===");
        
        System.out.print("Nombre de usuario: ");
        String usuario = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();
        
        boolean autenticado = gestorBanco.getGestorUsuarios().autenticarUsuario(usuario, contraseña);
        
        if (autenticado) {
            String tipoUsuario = gestorBanco.getGestorUsuarios().getTipoUsuarioActual();
            System.out.println("\n¡Autenticación exitosa! Tipo de usuario: " + tipoUsuario);
            mostrarMenuPorTipoUsuario(tipoUsuario);
        } else {
            System.out.println("Error en la autenticación. Intente nuevamente.");
        }
    }
    
    // Menús específicos por tipo de usuario
    private static void mostrarMenuPorTipoUsuario(String tipoUsuario) {
        switch (tipoUsuario) {
            case "CLIENTE":
                mostrarMenuCliente();
                break;
            case "EMPLEADO":
                mostrarMenuEmpleado();
                break;
            case "ADMINISTRADOR":
                mostrarMenuAdministrador();
                break;
            default:
                System.out.println("Tipo de usuario no reconocido.");
        }
    }
    
    // Menú para Clientes
    private static void mostrarMenuCliente() {
        int opcion;
        
        do {
            System.out.println("\n=== MENÚ CLIENTE ===");
            System.out.println("1. Consultar Saldo");
            System.out.println("2. Realizar Depósito");
            System.out.println("3. Realizar Retiro");
            System.out.println("4. Realizar Transferencia");
            System.out.println("5. Ver Movimientos");
            System.out.println("6. Ver Mis Cuentas");
            System.out.println("7. Ver Mis Permisos");
            System.out.println("8. Ver Mi Información");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    consultarSaldoCliente();
                    break;
                case 2:
                    realizarDepositoCliente();
                    break;
                case 3:
                    realizarRetiroCliente();
                    break;
                case 4:
                    realizarTransferenciaCliente();
                    break;
                case 5:
                    verMovimientosCliente();
                    break;
                case 6:
                    verCuentasCliente();
                    break;
                case 7:
                    mostrarPermisosCliente();
                    break;
                case 8:
                    mostrarInformacionCliente();
                    break;
                case 0:
                    gestorBanco.getGestorUsuarios().cerrarSesion();
                    System.out.println("Sesión cerrada exitosamente.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    
    // Menú para Empleados
    private static void mostrarMenuEmpleado() {
        int opcion;
        
        do {
            System.out.println("\n=== MENÚ EMPLEADO ===");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Cuentas");
            System.out.println("3. Procesar Transacciones");
            System.out.println("4. Consultar Información");
            System.out.println("5. Ver Reportes");
            System.out.println("6. Ver Mis Permisos");
            System.out.println("7. Ver Mi Información");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    mostrarSubMenuGestionClientes();
                    break;
                case 2:
                    mostrarSubMenuGestionCuentas();
                    break;
                case 3:
                    mostrarSubMenuTransacciones();
                    break;
                case 4:
                    mostrarSubMenuConsultas();
                    break;
                case 5:
                    mostrarReportesEmpleado();
                    break;
                case 6:
                    mostrarPermisosEmpleado();
                    break;
                case 7:
                    mostrarInformacionEmpleado();
                    break;
                case 0:
                    gestorBanco.getGestorUsuarios().cerrarSesion();
                    System.out.println("Sesión cerrada exitosamente.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    
    // Menú para Administradores
    private static void mostrarMenuAdministrador() {
        int opcion;
        
        do {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Gestión de Usuarios");
            System.out.println("2. Gestión de Empleados");
            System.out.println("3. Gestión Completa del Sistema");
            System.out.println("4. Reportes y Estadísticas");
            System.out.println("5. Auditoría del Sistema");
            System.out.println("6. Ver Todos los Permisos");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            
            opcion = leerEntero();
            
            switch (opcion) {
                case 1:
                    mostrarSubMenuGestionUsuarios();
                    break;
                case 2:
                    mostrarSubMenuGestionEmpleados();
                    break;
                case 3:
                    mostrarSubMenuGestionCompleta();
                    break;
                case 4:
                    mostrarReportesAdministrador();
                    break;
                case 5:
                    mostrarAuditoria();
                    break;
                case 6:
                    mostrarTodosLosPermisos();
                    break;
                case 0:
                    gestorBanco.getGestorUsuarios().cerrarSesion();
                    System.out.println("Sesión cerrada exitosamente.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    
    // ========== MÉTODOS PARA CLIENTES ==========
    
    private static void consultarSaldoCliente() {
        System.out.print("Ingrese número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        gestorBanco.consultarSaldo(numeroCuenta);
    }
    
    private static void realizarDepositoCliente() {
        System.out.print("Ingrese número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        
        System.out.print("Ingrese monto a depositar: ");
        double monto = leerDouble();
        
        System.out.print("Ingrese descripción: ");
        String descripcion = scanner.nextLine();
        
        gestorBanco.realizarDeposito(numeroCuenta, monto, descripcion);
    }
    
    private static void realizarRetiroCliente() {
        System.out.print("Ingrese número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        
        System.out.print("Ingrese monto a retirar: ");
        double monto = leerDouble();
        
        System.out.print("Método de retiro (CAJERO/VENTANILLA): ");
        String metodo = scanner.nextLine();
        
        System.out.print("Ubicación: ");
        String ubicacion = scanner.nextLine();
        
        gestorBanco.realizarRetiro(numeroCuenta, monto, metodo, ubicacion);
    }
    
    private static void realizarTransferenciaCliente() {
        System.out.print("Ingrese cuenta origen: ");
        String cuentaOrigen = scanner.nextLine();
        
        System.out.print("Ingrese cuenta destino: ");
        String cuentaDestino = scanner.nextLine();
        
        System.out.print("Ingrese monto: ");
        double monto = leerDouble();
        
        System.out.print("Concepto: ");
        String concepto = scanner.nextLine();
        
        System.out.print("Referencia: ");
        String referencia = scanner.nextLine();
        
        gestorBanco.realizarTransferencia(cuentaOrigen, cuentaDestino, monto, concepto, referencia);
    }
    
    private static void verMovimientosCliente() {
        System.out.print("Ingrese número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        gestorBanco.consultarMovimientos(numeroCuenta);
    }
    
    private static void verCuentasCliente() {
        // Obtener el cliente actual
        Usuario usuarioActual = gestorBanco.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual instanceof UsuarioCliente) {
            UsuarioCliente usuarioCliente = (UsuarioCliente) usuarioActual;
            usuarioCliente.consultarInformacionCliente();
        }
    }
    
    private static void mostrarPermisosCliente() {
        Usuario usuarioActual = gestorBanco.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual != null) {
            usuarioActual.mostrarPermisos();
        }
    }
    
    private static void mostrarInformacionCliente() {
        Usuario usuarioActual = gestorBanco.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual != null) {
            usuarioActual.mostrarInformacionUsuario();
            if (usuarioActual instanceof UsuarioCliente) {
                ((UsuarioCliente) usuarioActual).consultarInformacionCliente();
            }
        }
    }
    
    // ========== MÉTODOS PARA EMPLEADOS ==========
    
    private static void mostrarSubMenuGestionClientes() {
        System.out.println("\n--- Gestión de Clientes ---");
        System.out.println("1. Ver todos los clientes");
        System.out.println("2. Buscar cliente por DNI");
        System.out.println("3. Agregar nuevo cliente");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                gestorBanco.getGestorUsuarios().mostrarTodosLosClientes();
                break;
            case 2:
                System.out.print("Ingrese DNI: ");
                String dni = scanner.nextLine();
                Cliente cliente = gestorBanco.getGestorUsuarios().buscarCliente(dni);
                if (cliente != null) {
                    cliente.mostrarInformacion();
                } else {
                    System.out.println("Cliente no encontrado.");
                }
                break;
            case 3:
                // Implementar creación de cliente
                System.out.println("Función en desarrollo...");
                break;
        }
    }
    
    private static void mostrarSubMenuGestionCuentas() {
        System.out.println("\n--- Gestión de Cuentas ---");
        System.out.println("1. Ver todas las cuentas");
        System.out.println("2. Crear cuenta de ahorros");
        System.out.println("3. Crear cuenta corriente");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                gestorBanco.mostrarTodasLasCuentas();
                break;
            case 2:
                System.out.print("Número de cuenta: ");
                String numero = scanner.nextLine();
                System.out.print("DNI del cliente: ");
                String dni = scanner.nextLine();
                System.out.print("Saldo inicial: ");
                double saldo = leerDouble();
                
                Cliente cliente = gestorBanco.getGestorUsuarios().buscarCliente(dni);
                if (cliente != null) {
                    gestorBanco.crearCuentaAhorros(numero, saldo, cliente);
                } else {
                    System.out.println("Cliente no encontrado.");
                }
                break;
            case 3:
                // Similar a cuenta de ahorros
                System.out.println("Función en desarrollo...");
                break;
        }
    }
    
    private static void mostrarSubMenuTransacciones() {
        System.out.println("\n--- Procesar Transacciones ---");
        System.out.println("1. Procesar depósito para cliente");
        System.out.println("2. Procesar retiro para cliente");
        System.out.println("3. Procesar transferencia para cliente");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        // Implementación similar a los métodos de cliente pero con validaciones de empleado
        System.out.println("Función en desarrollo...");
    }
    
    private static void mostrarSubMenuConsultas() {
        System.out.println("\n--- Consultas ---");
        System.out.println("1. Consultar saldo de cuenta");
        System.out.println("2. Consultar movimientos de cuenta");
        System.out.println("3. Ver transacciones recientes");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                consultarSaldoCliente(); // Reutilizar método
                break;
            case 2:
                verMovimientosCliente(); // Reutilizar método
                break;
            case 3:
                System.out.print("Número de transacciones a mostrar: ");
                int cantidad = leerEntero();
                gestorBanco.getTransaccionesRecientes(cantidad).forEach(t -> 
                    System.out.println(t.toStringCorto()));
                break;
        }
    }
    
    private static void mostrarReportesEmpleado() {
        System.out.println("\n--- Reportes para Empleados ---");
        gestorBanco.mostrarEstadisticasBancarias();
    }
    
    private static void mostrarPermisosEmpleado() {
        Usuario usuarioActual = gestorBanco.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual != null) {
            usuarioActual.mostrarPermisos();
        }
    }
    
    private static void mostrarInformacionEmpleado() {
        Usuario usuarioActual = gestorBanco.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual != null) {
            usuarioActual.mostrarInformacionUsuario();
            if (usuarioActual instanceof UsuarioEmpleado) {
                ((UsuarioEmpleado) usuarioActual).consultarInformacionEmpleado();
            }
        }
    }
    
    // ========== MÉTODOS PARA ADMINISTRADORES ==========
    
    private static void mostrarSubMenuGestionUsuarios() {
        System.out.println("\n--- Gestión de Usuarios ---");
        System.out.println("1. Ver todos los usuarios");
        System.out.println("2. Activar/Desactivar usuario");
        System.out.println("3. Eliminar usuario");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                gestorBanco.getGestorUsuarios().mostrarTodosLosUsuarios();
                break;
            case 2:
                System.out.print("Nombre de usuario: ");
                String username = scanner.nextLine();
                System.out.print("¿Activar (A) o Desactivar (D)? ");
                String accion = scanner.nextLine();
                if (accion.equalsIgnoreCase("A")) {
                    gestorBanco.getGestorUsuarios().activarUsuario(username);
                } else if (accion.equalsIgnoreCase("D")) {
                    gestorBanco.getGestorUsuarios().desactivarUsuario(username);
                }
                break;
            case 3:
                System.out.print("Nombre de usuario a eliminar: ");
                String userToDelete = scanner.nextLine();
                gestorBanco.getGestorUsuarios().eliminarUsuario(userToDelete);
                break;
        }
    }
    
    private static void mostrarSubMenuGestionEmpleados() {
        System.out.println("\n--- Gestión de Empleados ---");
        System.out.println("1. Ver todos los empleados");
        System.out.println("2. Agregar nuevo empleado");
        System.out.println("3. Buscar empleado por DNI");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                gestorBanco.getGestorUsuarios().mostrarTodosLosEmpleados();
                break;
            case 2:
                // Implementar creación de empleado
                System.out.println("Función en desarrollo...");
                break;
            case 3:
                System.out.print("Ingrese DNI: ");
                String dni = scanner.nextLine();
                Empleado empleado = gestorBanco.getGestorUsuarios().buscarEmpleado(dni);
                if (empleado != null) {
                    empleado.mostrarInformacion();
                } else {
                    System.out.println("Empleado no encontrado.");
                }
                break;
        }
    }
    
    private static void mostrarSubMenuGestionCompleta() {
        System.out.println("\n--- Gestión Completa del Sistema ---");
        System.out.println("1. Ver estadísticas completas");
        System.out.println("2. Gestionar todas las cuentas");
        System.out.println("3. Ver todas las transacciones");
        System.out.print("Seleccione opción: ");
        
        int opcion = leerEntero();
        switch (opcion) {
            case 1:
                gestorBanco.getGestorUsuarios().mostrarEstadisticas();
                gestorBanco.mostrarEstadisticasBancarias();
                break;
            case 2:
                gestorBanco.mostrarTodasLasCuentas();
                break;
            case 3:
                gestorBanco.mostrarTodasLasTransacciones();
                break;
        }
    }
    
    private static void mostrarReportesAdministrador() {
        System.out.println("\n--- Reportes para Administradores ---");
        gestorBanco.getGestorUsuarios().mostrarEstadisticas();
        gestorBanco.mostrarEstadisticasBancarias();
    }
    
    private static void mostrarAuditoria() {
        System.out.println("\n--- Auditoría del Sistema ---");
        System.out.println("=== USUARIOS DEL SISTEMA ===");
        gestorBanco.getGestorUsuarios().mostrarTodosLosUsuarios();
        
        System.out.println("\n=== TRANSACCIONES RECIENTES ===");
        gestorBanco.getTransaccionesRecientes(10).forEach(t -> {
            System.out.println(t.toStringCorto());
        });
        
        System.out.println("\n=== ESTADO DEL SISTEMA ===");
        System.out.println("Sistema funcionando correctamente");
        System.out.println("Total de registros: " + 
            (gestorBanco.getGestorUsuarios().getUsuarios().size() + 
             gestorBanco.getCuentas().size() + 
             gestorBanco.getTransacciones().size()));
    }
    
    private static void mostrarTodosLosPermisos() {
        System.out.println("\n=== PERMISOS POR TIPO DE USUARIO ===");
        
        // Crear usuarios demo para mostrar permisos
        UsuarioCliente clienteDemo = new UsuarioCliente("demo", "demo", null);
        UsuarioEmpleado empleadoDemo = new UsuarioEmpleado("demo", "demo", null);
        UsuarioAdministrador adminDemo = new UsuarioAdministrador("demo", "demo");
        
        System.out.println("\n--- PERMISOS DE CLIENTE ---");
        clienteDemo.mostrarPermisos();
        
        System.out.println("\n--- PERMISOS DE EMPLEADO ---");
        empleadoDemo.mostrarPermisos();
        
        System.out.println("\n--- PERMISOS DE ADMINISTRADOR ---");
        adminDemo.mostrarPermisos();
    }
    
    // ========== MÉTODOS GENERALES ==========
    
    private static void mostrarInformacionSistema() {
        System.out.println("\n=== INFORMACIÓN DEL SISTEMA ===");
        System.out.println("Sistema Bancario - Laboratorio 07");
        System.out.println("Desarrollo de Software Orientado a Objetos");
        System.out.println("Usuarios de demostración:");
        System.out.println("  Cliente: usuario='cliente1', contraseña='123'");
        System.out.println("  Empleado: usuario='empleado1', contraseña='789'");
        System.out.println("  Administrador: usuario='admin', contraseña='admin'");
        System.out.println("\nCaracterísticas implementadas:");
        System.out.println("  ✓ Sistema de autenticación por roles");
        System.out.println("  ✓ Gestión de usuarios y permisos");
        System.out.println("  ✓ Cuentas de ahorro y corriente");
        System.out.println("  ✓ Transacciones (depósitos, retiros, transferencias)");
        System.out.println("  ✓ Control de acceso y validaciones");
    }
    
    // ========== MÉTODOS DE UTILIDAD ==========
    
    private static int leerEntero() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Por favor, ingrese un número válido: ");
            }
        }
    }
    
    private static double leerDouble() {
        while (true) {
            try {
                double valor = Double.parseDouble(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Por favor, ingrese un número válido: ");
            }
        }
    }
}