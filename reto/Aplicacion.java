
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Aplicacion{
    static int cols = 80;
    static int rows = 25;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        String fechaActual= Io.fechaActual();
        Connection conn= null;
        String url = "jdbc:mysql://127.0.0.1:3306/BibliotecaMuskiz";
	    String usuario = "alumno1";
	    String password = "alumno1";
        conn = Io.getConexion(url, usuario, password);
        Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
        Io.clearScreen();
        do {
            Io.sop("***************************************************************************************");
            Io.sop("*                                Aplicacion Grupo/Talde 1            "+fechaActual +"         *");
            Io.sop("***************************************************************************************");
            Io.sop("\n                               === OPCIONES BASICAS ===                              ");
            Io.sop("1. Comprobar/Establecer conexion                                2. Generar 1 usuario   ");                                                                                            
            Io.sop("3. Mostrar usuarios                                             4. Eliminar 1 usuario  ");
            Io.sop("5. Realizar la desconexion                                      6. Buscar Dni          ");
            Io.sop("7. Crear Tabla de Usuarios                                      8. Crear 100 Usuarios  ");
            Io.sop("9. Borrar Tabla Usuarios                                        10. Mostrar Usuarios Paginando");
            Io.sop("11. Aumentar el tamaño del CMD                                  12. Disminuir el tamaño del CMD");
            Io.sop("13. Mostrar campos de la Tabla");
            Io.sop("0. Salir");
            Io.sop("\nElige una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    conn=Io.getConexion(url, usuario, password);
                    Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 2:
                    try {
                        Io.generarUsuario(conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 3:
                    try {
                        Io.getUsuarios(conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 4:
                    try {
                        Io.eliminarUsuario(conn, scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 5:
                    Io.cerrarConexion(conn);
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 6:
                    Io.buscarPorCodUsuario(conn, scanner);
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break; 
                case 7:
                    Io.crearTablaUsuarios(conn);
                    scanner.nextInt();
                    break;
                case 8:
                    Io.crearCienUsuarios(conn);
                    scanner.nextInt();
                    break;   
                case 9:
                    Io.borrarTablaUsuarios(conn);
                    scanner.nextInt();
                    break;          
                case 10:
        
                    
                case 11:
    // Aumentar tamaño del CMD
    Aplicacion.cols += 10;
    Aplicacion.rows += 5;
    Io.setConsoleSize(Aplicacion.cols, Aplicacion.rows);
    Io.sop("Presiona Enter para continuar...");
    scanner.nextLine();
    break;
case 12:
    // Disminuir tamaño del CMD
    Aplicacion.cols -= 10;
    Aplicacion.rows -= 5;
    Io.setConsoleSize(Aplicacion.cols, Aplicacion.rows);
    Io.sop("Presiona Enter para continuar...");
    scanner.nextLine();
    break;
                    
                case 0:
                Io.sop("Saliendo del programa... ¡Hasta luego!");
                    break;
                default:
                Io.sop("Opción no válida. Intenta de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }
  
}

