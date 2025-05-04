import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
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
    public static void clearScreen(){
        try{
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String fechaActual(){
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;  //Enero es el mes 0
        int anio = cal.get(Calendar.YEAR);
        return (dia +"/" + mes + "/" + anio);
    }
    public static void getUsuarios(Connection conn) throws SQLException {
        String sql = "SELECT * FROM usuarios";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i=1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnName(i);
            System.out.print(columnName + " ");
        }
        System.out.println();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = rs.getString(i);
                System.out.print( columnValue + " ");
            }
            System.out.println();
        }
        rs.close();
        stmt.close();
    }
    public static void generarUsuario(Connection conn) throws SQLException {
        String sql = "INSERT INTO usuarios (cod_usuario, nombre_usuario, contrasena, telefono, direccion, correo_elec, num_ss) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String nombre = Azar.getNombre();
        int codUsuario = (int) (Math.random()*100)+1;
        //Comprobamos el codigo poara que no se repita ya que es la clave primaria de la tabla usuarios
        while (comprobarCodigo(conn, codUsuario)) {
            codUsuario = (int) (Math.random()*100)+1;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codUsuario);
            stmt.setString(2, nombre);
            stmt.setString(3, "1234");
            stmt.setString(4, Azar.getTelefono());
            if (Azar.getRandom()) {
                stmt.setString(5, Azar.getDireccion());
            }else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.setString(6, nombre + "@gmail.com");
            if(Azar.getRandom()){
                stmt.setString(7, Azar.getNumSS());
            }else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
        }
        sop(nombre + " se ha generado correctamente.");
    }
    public static void eliminarUsuario(Connection conn, Scanner scanner) throws SQLException {
        getUsuarios(conn);
        sop("Introduce el codigo del usuario a eliminar:");
        String codUsuario = scanner.nextLine();
        String sql = "DELETE FROM usuarios WHERE cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUsuario);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sop("Usuario eliminado correctamente.");
            } else {
                sop("No se encontro ningun usuario con ese codigo.");
            }
        }
    }
    public static boolean comprobarCodigo(Connection conn, int codigo) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
}


