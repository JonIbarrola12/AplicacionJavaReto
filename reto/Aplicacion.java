
import java.sql.Connection;
import java.util.Scanner;

public class Aplicacion{
    static int cols = 80;
    static int rows = 25;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        int continuar;
        Connection conn= null;
        String url = "jdbc:mysql://127.0.0.1:3306/BibliotecaMuskiz";
	    String usuario = "alumno1";
	    String password = "alumno1";
        conn = Io.getConexion(url, usuario, password);
        Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
        Io.clearScreen();
        do {
            System.out.println("***************************************************************************************");
            System.out.println("*                                Aplicacion Grupo/Talde 1                             *");
            System.out.println("***************************************************************************************");
            System.out.println("\n                               === OPCIONES BASICAS ===                              ");
            System.out.println("1. Comprobar/Establecer conexion                                2. Generar 1 usuario   ");                                                                                            
            System.out.println("3. Mostrar usuarios                                             4. Eliminar 1 usuario  ");
            System.out.println("5. Realizar la desconexion                                      6. Buscar Dni          ");
            System.out.println("7. Crear Tabla de Usuarios                                      8. Crear 100 Usuarios  ");
            System.out.println("9. Borrar Tabla Usuarios                                        10. Mostrar Usuarios Paginando");
            System.out.println("11. Aumentar el tamaño del CMD                                  12. Disminuir el tamaño del CMD");
            System.out.println("13. Mostrar campos de la Tabla");
            System.out.println("0. Salir");
            System.out.print("\nElige una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    Io.getConexion(url, usuario, password);
                    Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 2://
                    
                    
                case 3:
        
                    
                case 4:
        
                    
                case 5:
                    Io.cerrarConexion(conn);
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 6:
                    Io.buscarPorCodUsuario(conn, scanner);
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break; 
                    
                case 7:
        
                    
                case 8:
        
                    
                case 9:
        
                    
                case 10:
        
                    
                case 11:
                    cols += 5;
                    rows += 2;
                    Io.setConsoleSize(cols, rows);
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;

                case 12:
                    cols = Math.max(20, cols - 5);
                    rows = Math.max(10, rows - 2);
                    Io.setConsoleSize(cols, rows);
                    System.out.println("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;
        
                    
                case 0:
                    System.out.println("Saliendo del programa... ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }
  
}

