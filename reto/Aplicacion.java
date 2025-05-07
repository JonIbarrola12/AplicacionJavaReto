import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Aplicacion{
    static int cols = 100;
    static int rows = 30;
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
            Io.sop("3. Mostrar Registros                                            4. Eliminar 1 usuario  ");
            Io.sop("5. Realizar la desconexion                                      6. Buscar Dni          ");
            Io.sop("7. Gestionar Tablas (Crear/Borrar)                              8. Devolver Prestamo    ");
            Io.sop("9. Hacer Prestamo                                               10. Mostrar Usuarios Paginando");
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
                    Io.continuar(scanner);;
                    break;
                case 2:
                    try {
                        Io.generarUsuario(conn, scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.continuar(scanner);
                    break;
                case 3:
                    try {
                        Io.mostrarRegistros(conn, scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.continuar(scanner);
                    break;
                case 4:
                    try {
                        Io.eliminarUsuario(conn, scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.continuar(scanner);
                    break;
                case 5:
                    Io.cerrarConexion(conn);
                    Io.continuar(scanner);
                    break;
                case 6:
                    Io.buscarPorCodUsuario(conn, scanner);
                    Io.continuar(scanner);
                    break; 
                case 7:
                    Io.gestionarTablas(conn, scanner);
                    Io.continuar(scanner);
                    break;
                case 8:
                    try {
                        Io.devolverPrestamo(conn, scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.continuar(scanner);
                    break;
                case 9:
                    try{
                        Io.hacerPrestamo(conn, scanner);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Io.continuar(scanner);
                    break;
                case 10:
        
                    
                case 11:
                // Aumentar tamaño
                     Aplicacion.cols += 20;
                     Aplicacion.rows += 10;
                    Io.setConsoleSize(Aplicacion.cols, Aplicacion.rows);
                    Io.sop("Tamaño aumentado a " + Aplicacion.cols + "x" + Aplicacion.rows);
                    Io.continuar(scanner);
                 break;
                case 12:
                    // Disminuir tamaño
                    Aplicacion.cols -= 20;
                    Aplicacion.rows -= 10;
                    Io.setConsoleSize(Aplicacion.cols, Aplicacion.rows);
                    Io.sop("Tamaño reducido a " + Aplicacion.cols + "x" + Aplicacion.rows);
                    Io.continuar(scanner);
                    break;
                case 13:
                    Io.mostrarCamposTablaUsuarios(conn);
                    Io.continuar(scanner);
                    break; 
                case 0:
                Io.sop("Saliendo del programa... ¡Hasta luego!");
                    break;
                default:
                Io.sop("Opción no válida. Intenta de nuevo.");
            }
            Io.clearScreen();
        } while (opcion != 0);

        scanner.close();
    }
  
}

