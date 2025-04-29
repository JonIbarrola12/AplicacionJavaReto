import java.sql.Connection;
import java.util.Scanner;

public class Aplicacion{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        Connection conn= null;
        String url = "jdbc:mysql://127.0.0.1:3306/BibliotecaMuskiz";
	    String usuario = "alumno1";
	    String password = "alumno1";
        conn = Io.getConexion(url, usuario, password);
        Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));

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

            switch (opcion) {
                case 1:
                    Io.getConexion(url, usuario, password);
                    Io.sop("Estado de la conexion: " + Io.estadoConexion(conn));
                case 2:
        
                    
                case 3:
        
                    
                case 4:
        
                    
                case 5:
        
                    
                case 6:
        
                    
                case 7:
        
                    
                case 8:
        
                    
                case 9:
        
                    
                case 10:
        
                    
                case 11:
        
                    
                case 12:
        
                    
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
