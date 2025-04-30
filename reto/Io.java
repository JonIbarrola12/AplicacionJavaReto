import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    public static boolean cerrarConnection(Connection conn){
        boolean dev=true;
        try {
                conn.close();
        } catch (SQLException e) {
            dev=false;
        }
        return dev;
    }
      static void setConsoleSize(int cols, int rows) {
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
}
