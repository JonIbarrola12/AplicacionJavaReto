import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Aplicacion{ 
    
    public static void main(String[] args) {
        char opc;
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
            Io.sop("1. Comprobar/Establecer conexion                                2. Generar 1 Registro   ");                                                                                            
            Io.sop("3. Mostrar Registros                                            4. Eliminar 1 Registro  ");
            Io.sop("5. Realizar la desconexion                                      6. Buscar por Clave Primaria");
            Io.sop("7. Gestionar Tablas (Crear/Borrar)                              8. Devolver Prestamo    ");
            Io.sop("9. Hacer Prestamo                                               10. Mostrar Usuarios Paginando");
            Io.sop("11. Aumentar el tamaño del CMD                                  12. Disminuir el tamaño del CMD");
            Io.sop("13. Mostrar campos de la Tablas");
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
                    Io.sop("¿Qué tabla deseas paginar? (u/ l/ a/ pe/ pr/ r)");
                    String tabla = scanner.nextLine();

                    switch (tabla) {
                        case "u":
                            Io.mostrarUsuariosPaginados(conn, scanner);
                            break;
                        case "l":
                            Io.mostrarLibrosPaginados(conn, scanner);
                            break;
                        case "a":
                            Io.mostrarAutoresPaginados(conn, scanner);
                            break;
                        case "pe":
                            Io.mostrarPenalizacionesPaginadas(conn, scanner);
                            break;
                        case "pr":
                            Io.mostrarReservasPaginadas(conn, scanner);
                            break;
                        case "r":
                            Io.mostrarReservasPaginadas(conn, scanner);
                        default:
                            Io.sop("Opcion Incorrecta.");
                            break;
                    }
                    Io.sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                    break;  
                case 11:
                // Aumentar tamaño
                    Io.aumentarFuenteCmd(16);
                    Io.Sop("Se va a cerrar el programa para que los cambios");
                    Io.Sop("tengan efecto");
                    opc='0';
                    break;
                case 12:
                    // Disminuir tamaño
                    Io.aumentarFuenteCmd(28);
                    Io.Sop("Se va a cerrar el programa para que los cambios");
                    Io.Sop("tengan efecto");
                    opc='0';
                    break;
                case 13:
                    Io.mostrarCampos(conn, scanner);
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

