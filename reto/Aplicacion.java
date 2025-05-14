import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Aplicacion{ 
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion =-1 ;
        Connection conn= null;
        String url = "jdbc:mysql://127.0.0.1:3306/BibliotecaMuskiz";
	    String usuario = "alumno1";
	    String password = "alumno1";
        conn = Io.getConexion(url, usuario, password);
        Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
        Io.clearScreen();
        do {
            Io.mostrarMenu();
            while(true){
                if(scanner.hasNextInt()){
                    opcion = scanner.nextInt();
                    scanner.nextLine();
                    break;
                }else{
                    Io.clearScreen();
                    Io.mostrarMenu();
                    Io.sop("No se introdujo un numero. Preube de nuevo");
                    scanner.nextLine();
                }
            }
            switch (opcion) {
                case 1:
                    conn=Io.getConexion(url, usuario, password);
                    Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
                    Io.continuar(scanner);
                    break;
                case 2:
                    try {
                        Io.anadirRegistro(conn, scanner);
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
                        Io.eliminarRegistro(conn, scanner);
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
                    Io.buscarPorClavePrimaria(conn, scanner);
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
                    Io.mostrarPaginados(conn,scanner);
                    Io.continuar(scanner);
                    break;  
                case 11:
                // Aumentar tamaño
                    Io.aumentarFuenteCmd(16);
                    Io.Sop("Se va a cerrar el programa para que los cambios");
                    Io.Sop("tengan efecto");
                    Io.continuar(scanner);
                    return;
                case 12:
                    // Disminuir tamaño
                    Io.aumentarFuenteCmd(28);
                    Io.Sop("Se va a cerrar el programa para que los cambios");
                    Io.Sop("tengan efecto");
                    Io.continuar(scanner);
                    return;
                case 13:
                    Io.mostrarCampos(conn, scanner);
                    Io.continuar(scanner);
                    break; 
                case 0:
                    Io.sop("Saliendo del programa... ¡Hasta luego!");
                    Io.continuar(scanner);
                    break;
                default:
                Io.sop("Opción no válida. Intenta de nuevo.");
                break;
            }
            Io.clearScreen();
        } while (opcion != 0);
        scanner.close();
    }
  
}

