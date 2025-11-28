package gestor;

import modelo.personas.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorUsuarios {
    private List<Usuario> usuarios;
    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private Usuario usuarioActual;
    
    // Constructor
    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.usuarioActual = null;
        inicializarDatosDemo();
    }

    // Método interactivo para agregar un cliente por consola
    public boolean agregarClienteInteractivo(Scanner sc) {
        if (!tienePermiso("gestionar_clientes")) {
            System.out.println("Error: No tiene permisos para gestionar clientes");
            return false;
        }

        System.out.println("--- Crear nuevo cliente ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine().trim();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = sc.nextLine().trim();

        System.out.print("Fecha de nacimiento (YYYY-MM-DD) [enter para hoy]: ");
        String fechaStr = sc.nextLine().trim();
        LocalDate fechaNacimiento = LocalDate.now();
        if (!fechaStr.isEmpty()) {
            try {
                fechaNacimiento = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                System.out.println("Formato de fecha inválido, usando fecha actual");
                fechaNacimiento = LocalDate.now();
            }
        }

        System.out.print("Dirección: ");
        String direccion = sc.nextLine().trim();

        System.out.print("ID Cliente (enter para autogenerar): ");
        String idCliente = sc.nextLine().trim();
        if (idCliente.isEmpty()) {
            idCliente = String.format("CLI%03d", clientes.size() + 1);
            System.out.println("ID generado: " + idCliente);
        }

        System.out.print("Categoría (VIP/Regular/Nuevo) [Regular]: ");
        String categoria = sc.nextLine().trim();
        if (categoria.isEmpty()) categoria = "Regular";

        System.out.print("Límite de crédito (ej: 1000.0) [1000.0]: ");
        double limiteCredito = 1000.0;
        String limiteStr = sc.nextLine().trim();
        if (!limiteStr.isEmpty()) {
            try {
                limiteCredito = Double.parseDouble(limiteStr);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para límite de crédito, usando 1000.0");
                limiteCredito = 1000.0;
            }
        }

        Cliente cliente = new Cliente(dni, nombre, apellido, email, telefono, fechaNacimiento, direccion,
                                       idCliente, categoria, limiteCredito);

        boolean creado = agregarCliente(cliente);

        if (creado) {
            System.out.print("¿Desea crear usuario de acceso para este cliente? (s/n): ");
            String opcion = sc.nextLine().trim().toLowerCase();
            if (opcion.equals("s") || opcion.equals("si")) {
                System.out.print("Nombre de usuario (login): ");
                String nombreUsuario = sc.nextLine().trim();
                System.out.print("Contraseña: ");
                String contraseña = sc.nextLine().trim();

                UsuarioCliente nuevoUsuario = new UsuarioCliente(nombreUsuario, contraseña, cliente);
                boolean usuarioCreado = agregarUsuario(nuevoUsuario);
                if (usuarioCreado) {
                    System.out.println("Usuario creado y asociado al cliente: " + nombreUsuario);
                } else {
                    System.out.println("No se pudo crear el usuario para el cliente (nombre de usuario posiblemente existente)");
                }
            }
        }

        return creado;
    }
    
    // Getters
    public List<Usuario> getUsuarios() { return new ArrayList<>(usuarios); }
    public List<Cliente> getClientes() { return new ArrayList<>(clientes); }
    public List<Empleado> getEmpleados() { return new ArrayList<>(empleados); }
    public Usuario getUsuarioActual() { return usuarioActual; }
    
    // Inicialización de datos de demostración
    private void inicializarDatosDemo() {
        // Crear clientes demo
        Cliente cliente1 = new Cliente("12345678", "Juan", "Pérez", "juan@email.com", 
                                      "987654321", java.time.LocalDate.of(1990, 5, 15), 
                                      "Av. Siempre Viva 123", "CLI001", "VIP", 5000.0);
        
        Cliente cliente2 = new Cliente("87654321", "María", "Gómez", "maria@email.com", 
                                      "912345678", java.time.LocalDate.of(1985, 8, 22), 
                                      "Calle Falsa 456", "CLI002", "Regular", 2000.0);
        
        clientes.add(cliente1);
        clientes.add(cliente2);
        
        // Crear empleados demo
        Empleado empleado1 = new Empleado("11223344", "Carlos", "López", "carlos@banco.com",
                         "911223344", java.time.LocalDate.of(1980, 3, 10),
                         "Av. Central 789", "EMP001", "Cajero", "Atención al Cliente", 
                         2500.0, "Mañana");
        
        Empleado empleado2 = new Empleado("44332211", "Ana", "Rodríguez", "ana@banco.com",
                                         "944332211", java.time.LocalDate.of(1975, 12, 5),
                                         "Jr. Libertad 321", "EMP002", "Gerente", "Administración", 
                                         5000.0, "Completo");
        
        empleados.add(empleado1);
        empleados.add(empleado2);
        
        // Crear usuarios demo
        usuarios.add(new UsuarioCliente("cliente1", "123", cliente1));
        usuarios.add(new UsuarioCliente("cliente2", "456", cliente2));
        usuarios.add(new UsuarioEmpleado("empleado1", "789", empleado1));
        usuarios.add(new UsuarioEmpleado("empleado2", "012", empleado2));
        usuarios.add(new UsuarioAdministrador("admin", "admin"));
        
        System.out.println("Datos de demostración inicializados correctamente");
    }
    
    // Métodos de autenticación
    public boolean autenticarUsuario(String nombreUsuario, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.autenticar(nombreUsuario, contraseña) && usuario.isEstado()) {
                usuarioActual = usuario;
                System.out.println("¡Bienvenido " + usuario.getNombreUsuario() + "!");
                return true;
            }
        }
        System.out.println("Error: Credenciales incorrectas o usuario inactivo");
        return false;
    }
    
    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Cerrando sesión de " + usuarioActual.getNombreUsuario());
            usuarioActual = null;
        }
    }
    
    // Métodos de gestión de usuarios
    public boolean agregarUsuario(Usuario usuario) {
        // Verificar si el usuario ya existe
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                System.out.println("Error: El nombre de usuario ya existe");
                return false;
            }
        }
        
        usuarios.add(usuario);
        System.out.println("Usuario " + usuario.getNombreUsuario() + " agregado exitosamente");
        return true;
    }
    
    public boolean eliminarUsuario(String nombreUsuario) {
        // No permitir eliminar al usuario actual
        if (usuarioActual != null && usuarioActual.getNombreUsuario().equals(nombreUsuario)) {
            System.out.println("Error: No puede eliminar su propio usuario");
            return false;
        }
        
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario)) {
                usuarios.remove(usuario);
                System.out.println("Usuario " + nombreUsuario + " eliminado exitosamente");
                return true;
            }
        }
        
        System.out.println("Error: Usuario no encontrado");
        return false;
    }
    
    public Usuario buscarUsuario(String nombreUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario)) {
                return usuario;
            }
        }
        return null;
    }
    
    public void activarUsuario(String nombreUsuario) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario != null) {
            usuario.activarUsuario();
        } else {
            System.out.println("Error: Usuario no encontrado");
        }
    }
    
    public void desactivarUsuario(String nombreUsuario) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario != null) {
            // No permitir desactivar al usuario actual
            if (usuarioActual != null && usuarioActual.getNombreUsuario().equals(nombreUsuario)) {
                System.out.println("Error: No puede desactivar su propio usuario");
                return;
            }
            usuario.desactivarUsuario();
        } else {
            System.out.println("Error: Usuario no encontrado");
        }
    }
    
    // Métodos de gestión de clientes
    public boolean agregarCliente(Cliente cliente) {
        // Verificar si el cliente ya existe
        for (Cliente c : clientes) {
            if (c.getDni().equals(cliente.getDni())) {
                System.out.println("Error: Ya existe un cliente con ese DNI");
                return false;
            }
        }
        
        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNombreCompleto() + " agregado exitosamente");
        return true;
    }
    
    public Cliente buscarCliente(String dni) {
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente;
            }
        }
        return null;
    }
    
    public Cliente buscarClientePorId(String idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdCliente().equals(idCliente)) {
                return cliente;
            }
        }
        return null;
    }
    
    // Métodos de gestión de empleados
    public boolean agregarEmpleado(Empleado empleado) {
        // Verificar si el empleado ya existe
        for (Empleado e : empleados) {
            if (e.getDni().equals(empleado.getDni())) {
                System.out.println("Error: Ya existe un empleado con ese DNI");
                return false;
            }
        }
        
        empleados.add(empleado);
        System.out.println("Empleado " + empleado.getNombreCompleto() + " agregado exitosamente");
        return true;
    }
    
    public Empleado buscarEmpleado(String dni) {
        for (Empleado empleado : empleados) {
            if (empleado.getDni().equals(dni)) {
                return empleado;
            }
        }
        return null;
    }
    
    // Métodos de información
    public void mostrarTodosLosUsuarios() {
        System.out.println("=== LISTA DE TODOS LOS USUARIOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }
        
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            String estado = usuario.isEstado() ? "Activo" : "Inactivo";
            String tipo = usuario.getClass().getSimpleName();
            System.out.printf("%d. %s (%s) - %s%n", i + 1, usuario.getNombreUsuario(), tipo, estado);
        }
    }
    
    public void mostrarTodosLosClientes() {
        System.out.println("=== LISTA DE TODOS LOS CLIENTES ===");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }
        
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            System.out.printf("%d. %s - %s - Categoría: %s%n", 
                i + 1, cliente.getNombreCompleto(), cliente.getDni(), cliente.getCategoria());
        }
    }
    
    public void mostrarTodosLosEmpleados() {
        System.out.println("=== LISTA DE TODOS LOS EMPLEADOS ===");
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados");
            return;
        }
        
        for (int i = 0; i < empleados.size(); i++) {
            Empleado empleado = empleados.get(i);
            String estado = empleado.isActivo() ? "Activo" : "Inactivo";
            System.out.printf("%d. %s - %s - %s - %s%n", 
                i + 1, empleado.getNombreCompleto(), empleado.getCargo(), 
                empleado.getDepartamento(), estado);
        }
    }
    
    public void mostrarEstadisticas() {
        System.out.println("=== ESTADÍSTICAS DEL SISTEMA ===");
        System.out.println("Total de usuarios: " + usuarios.size());
        System.out.println("Total de clientes: " + clientes.size());
        System.out.println("Total de empleados: " + empleados.size());
        
        // Contar por tipo de usuario
        long clientesCount = usuarios.stream().filter(u -> u instanceof UsuarioCliente).count();
        long empleadosCount = usuarios.stream().filter(u -> u instanceof UsuarioEmpleado).count();
        long adminsCount = usuarios.stream().filter(u -> u instanceof UsuarioAdministrador).count();
        
        System.out.println("Usuarios cliente: " + clientesCount);
        System.out.println("Usuarios empleado: " + empleadosCount);
        System.out.println("Usuarios administrador: " + adminsCount);
        
        // Usuarios activos/inactivos
        long activosCount = usuarios.stream().filter(Usuario::isEstado).count();
        long inactivosCount = usuarios.size() - activosCount;
        
        System.out.println("Usuarios activos: " + activosCount);
        System.out.println("Usuarios inactivos: " + inactivosCount);
    }
    
    // Método para verificar permisos
    public boolean tienePermiso(String operacion) {
        if (usuarioActual == null) {
            return false;
        }
        
        // Lógica básica de permisos (se puede expandir)
        if (usuarioActual instanceof UsuarioAdministrador) {
            return true; // Los administradores tienen todos los permisos
        } else if (usuarioActual instanceof UsuarioEmpleado) {
            // Definir qué operaciones puede realizar un empleado (genéricos)
            String[] permisosEmpleado = {"gestionar_clientes", "crear_cuentas", "ver_reportes"};
            for (String permiso : permisosEmpleado) {
                if (permiso.equals(operacion)) {
                    return true;
                }
            }

            // Permisos por cargo: permitir realizar depósitos/retiros/transferencias
            // solo a empleados de atención al cliente (cajero/atención) o gerentes
            if ("realizar_depositos".equals(operacion) || "realizar_retiros".equals(operacion) || "realizar_transferencias".equals(operacion)) {
                UsuarioEmpleado ue = (UsuarioEmpleado) usuarioActual;
                Empleado emp = ue.getEmpleado();
                if (emp != null) {
                    String cargo = emp.getCargo();
                    if (cargo != null) {
                        String cargoLower = cargo.toLowerCase();
                        if (cargoLower.contains("cajero") || cargoLower.contains("atencion") || emp.esGerente()) {
                            return true;
                        }
                    }
                }
            }
        } else if (usuarioActual instanceof UsuarioCliente) {
            // Definir qué operaciones puede realizar un cliente
            String[] permisosCliente = {"consultar_saldo", "realizar_depositos", "realizar_retiros"};
            for (String permiso : permisosCliente) {
                if (permiso.equals(operacion)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // Método para obtener el tipo de usuario actual
    public String getTipoUsuarioActual() {
        if (usuarioActual == null) {
            return "NINGUNO";
        }
        
        if (usuarioActual instanceof UsuarioCliente) {
            return "CLIENTE";
        } else if (usuarioActual instanceof UsuarioEmpleado) {
            return "EMPLEADO";
        } else if (usuarioActual instanceof UsuarioAdministrador) {
            return "ADMINISTRADOR";
        } else {
            return "DESCONOCIDO";
        }
    }
}