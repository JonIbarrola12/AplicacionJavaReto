import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
    public static void cerrarConexion(Connection conn){
        try {
                conn.close();
                sop("exitosa");
        } catch (SQLException e) {
            sop("error al cerrar la conexion");
        }
    }
    public static void setConsoleSize(int cols, int rows) {
        try {
            // Limitar valores mínimos
            cols = Math.max(cols, 20);
            rows = Math.max(rows, 10);
    
            String command = "cmd /c mode con: cols=" + cols + " lines=" + rows;
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor(); // Espera a que termine
            
            if (exitCode != 0) {
                System.out.println("Error: No se pudo cambiar el tamaño (Código " + exitCode + ")");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al cambiar el tamaño: " + e.getMessage());
        }
    }
    public static void buscarPorCodUsuario(Connection conn, Scanner scanner) {
        System.out.print("Introduce el codigo del usuario:");
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

        int[] columnWidths = {15, 20, 15, 15, 25, 30, 15};
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i=1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnName(i);
            System.out.printf("%-" + columnWidths[i-1] + "s", columnName);
        }
        System.out.println();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = rs.getString(i);
                System.out.printf("%-" + columnWidths[i-1] + "s", columnValue == null ? "NULL" : columnValue);
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
        String telefono = Azar.getTelefono();
        String numSS = Azar.getNumSS();
        //Comprobamos el codigo para que no se repita ya que es la clave primaria de la tabla usuarios
        while (comprobarCodigo(conn, codUsuario)) {
            codUsuario = (int) (Math.random()*100)+1;
        }
        //Comprobamos el nombre para que no se repita ya que es unn campo unique de la tabla usuarios
        while(comprobarNombre(conn, nombre)){
            nombre = Azar.getNombre();
        }
        //Comprobamos el telefono para que no se repita ya que es unn campo unique de la tabla usuarios
        while(comprobarTelefono(conn, Azar.getTelefono())){
            telefono = Azar.getTelefono();
        }
        //Comprobamos el numero de la seguridad social para que no se repita ya que es unn campo unique de la tabla usuarios
        while(comprobarNumSS(conn, Azar.getNumSS())){
            numSS = Azar.getNumSS();
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codUsuario);
            stmt.setString(2, nombre);
            stmt.setString(3, "1234");
            stmt.setString(4, telefono);
            if (Azar.getRandom()) {
                stmt.setString(5, Azar.getDireccion());
            }else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.setString(6, nombre + "@gmail.com");
            if(Azar.getRandom()){
                stmt.setString(7, numSS);
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
    public static boolean comprobarNombre(Connection conn, String nombre) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean comprobarTelefono(Connection conn, String telefono) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE telefono = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telefono);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean comprobarNumSS(Connection conn, String numSS) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE num_ss = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numSS);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static void crearTablaUsuarios(Connection conn) {
        try {
            String sql = "CREATE TABLE usuarios (" +
                         "cod_usuario INT PRIMARY KEY, " +
                         "nombre_usuario VARCHAR(100), " +
                         "contrasena VARCHAR(100), " +
                         "telefono VARCHAR(20), " +
                         "direccion VARCHAR(255), " +
                         "correo_elec VARCHAR(100), " +
                         "num_ss VARCHAR(20))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            sop("Tabla 'usuarios' creada correctamente.");
        } catch (SQLException e) {
            sop("Error al crear la tabla: " + e.getMessage());
        }
    }

    public static void crearCienUsuarios(Connection conn) {
        try {
            String sql = "INSERT INTO usuarios (cod_usuario, nombre_usuario, contrasena, telefono, direccion, correo_elec, num_ss) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < 100; i++) {
                int codigo = (int) (Math.random()*1000);
                while (comprobarCodigo(conn, codigo)) {
                    codigo = (int) (Math.random()*1000)+1;
                }

                stmt.setInt(1, codigo);
                stmt.setString(2, "Usuario" + codigo);
                stmt.setString(3, "BibliotecaMuskiz" + codigo);
                stmt.setNull(4, java.sql.Types.VARCHAR);
                stmt.setString(5, "");
                stmt.setString(6, "usuario" + codigo + "@bibliotecamuskiz.eus");
                stmt.setNull(7, java.sql.Types.VARCHAR);
                stmt.executeUpdate();


            }
           
            stmt.close();
            sop("100 usuarios creados.");
        } catch (SQLException e) {
            sop("Error al crear los usuarios: " + e.getMessage());
        }
    }
    public static void borrarTablaUsuarios(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("DROP TABLE IF EXISTS usuarios");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            stmt.close();
            sop("Tabla eliminada correctamente");
        } catch (SQLException e) {
            sop("Error al eliminar las tabla: " + e.getMessage());
        }
    }
    public static void mostrarCamposTablaUsuarios(Connection conn) {
        try {
            String sql = "SELECT * FROM usuarios LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
    
            for (int i = 1; i <= columnCount; i++) {
                sop(rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")");
            }
    
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            sop("Error al obtener los campos de la tabla: " + e.getMessage());
        }
    }
    public static void continuar(Scanner scanner) {
        Io.sop("Presiona Enter para continuar...");
        scanner.nextLine();
        Io.clearScreen();
    }
    
}


