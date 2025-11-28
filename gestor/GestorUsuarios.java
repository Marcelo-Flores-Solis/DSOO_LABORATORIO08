package gestor;

import modelo.personas.*;
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
            // Definir qué operaciones puede realizar un empleado
            String[] permisosEmpleado = {"gestionar_clientes", "crear_cuentas", "ver_reportes"};
            for (String permiso : permisosEmpleado) {
                if (permiso.equals(operacion)) {
                    return true;
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