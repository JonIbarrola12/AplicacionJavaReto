import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Io{
	public static void sop(String mensaje) {
        System.out.println(mensaje);
    }
    public static void Sop(String mensaje) {
        System.out.println(mensaje);
    }
    public static String estadoConexion(Connection conn){
        if (conn==null){
            return ("off");
        }
        return ("on");
        }
        public static Connection getConexion(String url, String user, String pass){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (conn);
    }
    public static boolean cerrarConexion(Connection conn){
        boolean dev=true;
        try {
                conn.close();
        } catch (SQLException e) {
            dev=false;
        }
        return dev;
    }
    public static void setConsoleSize(int cols, int rows) {
        String[] cmd = { "cmd", "/c", "mode", "con:", "cols=" + cols, "lines=" + rows };
        try {
            Process p = new ProcessBuilder(cmd)
                            .redirectErrorStream(true)
                            .inheritIO()
                            .start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al cambiar tamaño del CMD: " + e.getMessage());
        }
    }
    public static void buscarPorCodUsuario(Connection conn, Scanner scanner) {
        System.out.print("Introduce el codigo del usuario:");
        scanner.nextLine();
        String codUsuario = scanner.nextLine();

        try {
            String query = "SELECT * FROM usuarios WHERE cod_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sop("Usuario encontrado:");
                sop("Codigo: " + rs.getString("cod_usuario"));
                sop("Nombre: " + rs.getString("nombre_usuario"));
                sop("Contraseña: " + rs.getString("contrasena"));
                sop("Telefono: " + rs.getString("telefono"));
                sop("Direccion: " + rs.getString("direccion"));
                sop("Correo electronico: " + rs.getString("correo_elec"));
                sop("Número de la Seguridad Social: " + rs.getString("num_ss"));
            } else {
                sop("No se encontro ningun usuario.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar el usuario: " + e.getMessage());
        }
    }
}

