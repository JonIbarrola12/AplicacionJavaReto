import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public static void aumentarFuenteCmd(int tamanioFuente) {
        try {
            // Cambiar tamaño de la fuente (requiere acceso a configuración del registro o herramientas externas)
            String fontCommand = "reg add HKCU\\Console /v FontSize /t REG_DWORD /d "+ (tamanioFuente * 65536) + " /f";
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", fontCommand});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   public static int getTamanioFuenteCmd() {
	   //import java.io.InputStreamReader;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "reg query HKCU\\Console /v FontSize"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("FontSize")) {
                   // String[] parts = line.trim().split("\s+");
                   int fontSize=10;//  = Integer.parseInt(parts[parts.length - 1], 16) / 65536;
                    return fontSize;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return -1; // Valor por defecto si falla
    }
    public static void buscarPorClavePrimaria(Connection conn, Scanner scanner) {
        
        sop("En que tabla buscas el registro por clave primaria? (u/ l/ a/ e)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                buscarPorCodUsuario(conn, scanner);
                break;
            case "l":
                buscarPorCodLibro(conn, scanner);
                break;
            case "a":
                buscarPorCodAutor(conn, scanner);
                break;
            case "e":
                buscarPorCodEjemplares(conn, scanner);
                break;
            default:
                sop("opcion incorrecta");
                break;
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
    public static void buscarPorCodLibro(Connection conn, Scanner scanner) {
        System.out.print("Introduce el (ISBN) del libro: ");
        String codLibro = scanner.nextLine();
    
        try {
            String query = "SELECT * FROM libros WHERE cod_libro = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codLibro);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                sop("Libro encontrado:");
                sop("ISBN: " + rs.getString("cod_libro"));
                sop("Título: " + rs.getString("titulo"));
                sop("Páginas: " + rs.getInt("paginas"));
                sop("URL de la imagen: " + rs.getString("url_img"));
            } else {
                sop("No se encontró ningún libro.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar el libro: " + e.getMessage());
        }
    }
    public static void buscarPorCodAutor(Connection conn, Scanner scanner) {
        System.out.print("Introduce el DNI del autor: ");
        String dniAutor = scanner.nextLine();

        try {
            String query = "SELECT * FROM autores WHERE dni = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dniAutor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sop("Autor encontrado:");
                sop("DNI: " + rs.getString("dni"));
                sop("Nombre: " + rs.getString("nombre"));
                sop("Primer apellido: " + rs.getString("ape1"));
                sop("Segundo apellido: " + rs.getString("ape2"));
            } else {
                sop("No se encontró ningún autor con ese DNI.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar el autor: " + e.getMessage());
        }
    }
    public static void buscarPorCodEjemplares(Connection conn, Scanner scanner) {
        System.out.print("Introduce el código del ejemplar: ");
        String codEjem = scanner.nextLine();

        try {
            String query = "SELECT * FROM ejemplares WHERE cod_ejem = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codEjem);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sop("Ejemplar encontrado:");
                sop("Código ejemplar: " + rs.getString("cod_ejem"));
                sop("Idioma: " + rs.getString("idioma"));
                sop("Estado: " + rs.getString("estado"));
                sop("ISBN: " + rs.getString("isbn"));
                sop("Código de préstamo: " + rs.getString("cod_prest"));
            } else {
                sop("No se encontró ningún ejemplar.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar el ejemplar: " + e.getMessage());
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
    public static void mostrarRegistros(Connection conn, Scanner scanner) throws SQLException {
        sop("Que tabla quieres mostrar? (u/ l/ a/ e /pr /pe )");
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "u":
                mostrarUsuarios(conn);
                break;
            case "l":
                mostrarLibros(conn);
                break;
            case "a":
                mostrarAutores(conn);
                break;
            case "e":
                mostrarEjemplares(conn);
                break;
            case "pr":
                mostrarPrestamos(conn);
                break;
            case "pe":
                mostrarPenalizaciones(conn);
                break;
            default:
                sop("opcion incorrecta");
                break;
        }
    }
    public static void mostrarUsuarios(Connection conn) throws SQLException {
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
    public static void mostrarLibros(Connection conn) throws SQLException {
        String sql = "SELECT isbn,titulo,paginas FROM libros";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {20, 60, 20};
        
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
    public static void mostrarAutores(Connection conn) throws SQLException {
        String sql = "SELECT * FROM autores";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {20, 20, 20, 20};
        
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
    public static void mostrarEjemplares(Connection conn) throws SQLException {
        String sql = "SELECT * FROM ejemplares";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {15, 20, 20, 20, 20};
        
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
    public static void mostrarPrestamos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM prestamos";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {15, 20, 20, 20, 15};
        
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
    public static void mostrarPenalizaciones (Connection conn) throws SQLException {
        String sql = "SELECT * FROM penalizaciones";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {15, 20, 20, 20};
        
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
    public static void mostrarEjemplaresByIsbn(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT * FROM Ejemplares WHERE isbn = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {15, 20, 20, 20, 20};
        
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
    public static void mostrarPrestamosByCodUsuario(Connection conn, String codUsuario) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE cod_usuario = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, codUsuario);
        ResultSet rs = stmt.executeQuery();

        int[] columnWidths = {15, 20, 20, 20, 15};
        
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
    public static void generarUsuario(Connection conn, Scanner scanner) throws SQLException {
        sop("Quieres generar un usuario automaticamente o manualmente? (a/m)");
        String opcion = scanner.nextLine();
        if (opcion.equalsIgnoreCase("a")) {
            generarUsuarioAutomaticamente(conn);
        } else if (opcion.equalsIgnoreCase("m")) {
            generarUsuarioManual(conn, scanner);
        } else {
            sop("Opcion no valida.");
        }
    }
    public static void generarUsuarioAutomaticamente(Connection conn) throws SQLException {
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
            Azar.comprobarCont();
        }
        //Comprobamos el telefono para que no se repita ya que es unn campo unique de la tabla usuarios
        while(comprobarTelefono(conn, Azar.getTelefono())){
            telefono = Azar.getTelefono();
            Azar.comprobarCont();
        }
        //Comprobamos el numero de la seguridad social para que no se repita ya que es unn campo unique de la tabla usuarios
        while(comprobarNumSS(conn, Azar.getNumSS())){
            numSS = Azar.getNumSS();
            Azar.comprobarCont();
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
    public static void generarUsuarioManual(Connection conn, Scanner scanner) throws SQLException {
        String direccion="", numSS=""; 
        sop("Quieres generar un usario o un trabajador? (u/t)");
        String opcion = scanner.nextLine();

        int codUsuario = Io.generarCodigoManual(conn, scanner);

        String nombre = Io.generarNombreManual(conn, scanner);

        String contrasena = Io.generarContrasenaManual(scanner);

        String telefono = Io.generarTelefonoManual(conn, scanner);

        sop("Quieres introducir una direccion? (s/n)");
        String respuesta = scanner.nextLine();
        if(respuesta.equalsIgnoreCase("s")) { 
            direccion = Io.generarDireccionManual(scanner);
        }

        String correo = Io.generarCorreoManual(conn, scanner);

        if (opcion.equalsIgnoreCase("t")) {
            numSS= Io.generarNumSSManual(conn, scanner);
        }
        String sql = "INSERT INTO usuarios (cod_usuario, nombre_usuario, contrasena, telefono, direccion, correo_elec, num_ss) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codUsuario);
            stmt.setString(2, nombre);
            stmt.setString(3, contrasena);
            stmt.setString(4, telefono);
            if (respuesta.equalsIgnoreCase("s")) {
                stmt.setString(5, direccion);
            }
            stmt.setNull(5, java.sql.Types.VARCHAR);
            stmt.setString(6, correo);
            if (opcion.equalsIgnoreCase("t")) {
                stmt.setString(7, numSS);
            }
            stmt.setNull(7, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            sop("Usuario " + nombre + " generado correctamente.");
        }
    }
    public static int generarCodigoManual(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el codigo del usuario:");
        int codUsuario = scanner.nextInt();
        scanner.nextLine(); 
        while (comprobarCodigo(conn, codUsuario)) {
            sop("El codigo ya existe. Introduce otro codigo:");
            codUsuario = scanner.nextInt();
            scanner.nextLine();
        } 
        return codUsuario;
    }
    public static String generarNombreManual(Connection conn,Scanner scanner) throws SQLException {
        sop("Introduce el nombre del usuario:");
        String nombre = scanner.nextLine();
        while (comprobarNombre(conn, nombre)) {
            sop("El nombre ya existe. Introduce otro nombre:");
            nombre = scanner.nextLine();
        }
        return nombre;
    }
    public static String generarContrasenaManual(Scanner scanner) {
        sop("Introduce la contraseña del usuario:");
        String contrasena = scanner.nextLine();
        return contrasena;
    }
    public static String generarTelefonoManual(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el telefono del usuario:");
        String telefono = scanner.nextLine();
        while (comprobarTelefono(conn, telefono)) {
            sop("El telefono ya existe. Introduce otro telefono:");
            telefono = scanner.nextLine();
        }
        return telefono;
    }
    public static String generarDireccionManual(Scanner scanner) {
        sop("Introduce la direccion del usuario:");
        String direccion = scanner.nextLine();
        return direccion;
    }
    public static String generarCorreoManual(Connection conn,Scanner scanner)throws SQLException {
        sop("Introduce el correo electronico del usuario:");
        String correo = scanner.nextLine();
        while (comprobarCorreo(conn, correo)) {
            sop("El correo electronico ya existe. Introduce otro correo:");
            correo = scanner.nextLine();
        }
        return correo;
    }
    public static String generarNumSSManual(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el numero de la seguridad social del usuario:");
        String numSS = scanner.nextLine();
        while (comprobarNumSS(conn, numSS)) {
            sop("El numero de la seguridad social ya existe. Introduce otro numero:");
            numSS = scanner.nextLine();
        }
        return numSS;
    }
    public static void generarPenalizaciones(Connection conn, Scanner scanner, String codUsuario, int diasDePenalizacion, Date fechaPenalizacion ) throws SQLException {
        String sql = "INSERT INTO penalizaciones (cod_usuario, fecha_pen, dias_pen, cod_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUsuario);
            stmt.setDate(2, fechaPenalizacion);
            stmt.setInt(3, diasDePenalizacion);
            stmt.setString(4, codUsuario);
            stmt.executeUpdate();
            sop("Penalizacion registrada correctamente.");
        }
    }
    public static void eliminarUsuario(Connection conn, Scanner scanner) throws SQLException {
        mostrarUsuarios(conn);
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
    public static void eliminarPrestamo(Connection conn, String codUsuario, int codPrestamo) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE cod_prest = ? AND cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrestamo);
            stmt.setString(2, codUsuario);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sop("Prestamo eliminado correctamente.");
            } else {
                sop("No se encontro ningun prestamo con ese codigo.");
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
    public static boolean comprobarCorreo(Connection conn, String correo) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo_elec = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    public static void crearTablas(Connection conn, Scanner scanner) {
        
        sop("Que tabla quieres crear? (u/ l/ a/ e)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                crearTablaUsuarios(conn);
                break;
            case "l":
                crearTablaLibros(conn);
                break;
            case "a":
                crearTablaAutores(conn);
                break;
            case "e":
                crearTablaEjemplares(conn);
                break;
            default:
                sop("opcion incorrecta");
                break;
        }

    }
    public static void borrarTablas(Connection conn, Scanner scanner) {
        
        sop("Que tabla quieres crear? (u/ l/ a/ e)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                borrarTablaUsuarios(conn);
                break;
            case "l":
                borrarTablaLibros(conn);
                break;
            case "a":
                borrarTablaAutores(conn);
                break;
            case "e":
                borrarTablaEjemplares(conn);
                break;
            default:
                sop("opcion incorrecta");
                break;
        }

    }
    public static void gestionarTablas(Connection conn, Scanner scanner) {
        sop("Quieres Crear o Eliminar una Tabla  (c/b)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "c":
                crearTablas(conn, scanner);
                break;
            case "b":
                borrarTablas(conn, scanner);
                break;
        }

    }
    public static void crearTablaUsuarios(Connection conn) {
        try {
            String sql = "CREATE TABLE usuarios (" +
                         "cod_usuario INT PRIMARY KEY, " +
                         "nombre_usuario VARCHAR(40), " +
                         "contrasena VARCHAR(20), " +
                         "telefono VARCHAR(20), " +
                         "direccion VARCHAR(255), " +
                         "correo_elec VARCHAR(50), " +
                         "num_ss VARCHAR(20))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            sop("Tabla 'usuarios' creada correctamente.");
        } catch (SQLException e) {
            sop("Error al crear la tabla: " + e.getMessage());
        }
    }
    public static void crearTablaAutores(Connection conn) {
        try {
            String sql = "CREATE TABLE autores (" +
                         "dni varchar2(20) PRIMARY KEY, " +
                         "nombre VARCHAR(20), " +
                         "ape1 VARCHAR(20), " +
                         "ape2 VARCHAR(20))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            sop("Tabla 'autores' creada correctamente.");
        } catch (SQLException e) {
            sop("Error al crear la tabla: " + e.getMessage());
        }
    }
    public static void crearTablaEjemplares(Connection conn) {
        try {
            String sql = "CREATE TABLE ejemplares (" +
                         "cod_ejem int(5) PRIMARY KEY, " +
                         "idioma VARCHAR(20), " +
                         "estado VARCHAR(30), " +
                         "isbn VARCHAR(13), " +
                         "cod_prest int(5))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            sop("Tabla 'ejemplares' creada correctamente.");
        } catch (SQLException e) {
            sop("Error al crear la tabla: " + e.getMessage());
        }
    }
    public static void crearTablaLibros(Connection conn) {
        try {
            String sql = "CREATE TABLE libros (" +
                         "isbn int(13) PRIMARY KEY, " +
                         "titulo VARCHAR(20), " +
                         "paginas VARCHAR(30), " +
                         "urlimg VARCHAR(13))";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            sop("Tabla 'libros' creada correctamente.");
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
    public static void borrarTablaLibros(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("DROP TABLE IF EXISTS libros");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            stmt.close();
            sop("Tabla eliminada correctamente");
        } catch (SQLException e) {
            sop("Error al eliminar las tabla: " + e.getMessage());
        }
    }
    public static void borrarTablaEjemplares(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("DROP TABLE IF EXISTS ejemplares");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            stmt.close();
            sop("Tabla eliminada correctamente");
        } catch (SQLException e) {
            sop("Error al eliminar las tabla: " + e.getMessage());
        }
    }
    public static void borrarTablaAutores(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("DROP TABLE IF EXISTS autores");
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
    public static void hacerPrestamo(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el nombre del usuario:");
        String nombre = scanner.nextLine();
        while (!comprobarNombre(conn, nombre)) {
            sop("El nombre no existe. Introduce otro nombre:");
            nombre = scanner.nextLine();
        }
        String codUsuario = getCodUsuarioByNombre(conn, scanner, nombre);
        mostrarLibros(conn);
        sop("Introduce el isbn del libro que quiere:");
        String isbn = scanner.nextLine();
        mostrarEjemplaresByIsbn(conn, isbn);
        sop("Introduce el codigo del ejemplar:");
        int codEjemplar = scanner.nextInt();
        scanner.nextLine();
        while  (!comprobarDisponibilidad(conn,codEjemplar)){
            sop("El ejemplar no esta disponible. Introduce otro codigo:");
            codEjemplar = scanner.nextInt();
            scanner.nextLine();
        }
        LocalDate hoy = LocalDate.now();
        LocalDate entrega = hoy.plusDays(30);

        Date fechaPrestamo = Date.valueOf(hoy);
        Date fechaEntrega = Date.valueOf(entrega);
        int codPrest = (int) (Math.random()*1000)+1;
        while (comprobarCodigo(conn, codPrest)) {
            codPrest = (int) (Math.random()*1000)+1;
        }
        insertarPrestamo(conn, codPrest, fechaPrestamo, fechaEntrega, codUsuario);
        actualizarEjemplar(conn, codPrest, codEjemplar);
    }
    public static String  getCodUsuarioByNombre(Connection conn, Scanner scanner, String nombre) throws SQLException {
        String sql = "SELECT cod_usuario FROM usuarios WHERE nombre_usuario = ?";
        String codUsuario = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                codUsuario = rs.getString("cod_usuario");
                return codUsuario;
            } else {
                sop("No se encontro ningun usuario con ese nombre.");
            }
        } catch (SQLException e) {
            sop("Error al buscar el usuario: " + e.getMessage());
        }
        return codUsuario;
    }
    public static boolean comprobarDisponibilidad(Connection conn, int codEjemplar) throws SQLException {
        String sql = "SELECT * FROM ejemplares WHERE cod_ejem = ? AND estado = 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codEjemplar);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean algunaDisponibilidad(Connection conn) throws SQLException {
        String sql = "SELECT * FROM ejemplares WHERE estado = 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static void insertarPrestamo(Connection conn, int codPrest, Date fecha_prestamo, Date fecha_entrega, String codUsuario ) throws SQLException {
        String sql = "INSERT INTO prestamos (cod_prest, fecha_prestamo, fecha_entrega, fecha_devolucion, cod_usuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrest);
            stmt.setDate(2, fecha_prestamo);
            stmt.setDate(3, fecha_entrega);
            stmt.setNull(4, java.sql.Types.DATE);
            stmt.setString(5, codUsuario);
            stmt.executeUpdate();
            stmt.close();
            sop("Prestamo realizado correctamente.");
        } catch (SQLException e) {
            sop("Error al realizar el prestamo: " + e.getMessage());
        }
    }
    public static void actualizarEjemplar(Connection conn, int codPrest, int codEjemplar) throws SQLException {
        String sql = "UPDATE ejemplares SET estado = 'prestado', cod_prest = ? WHERE cod_ejem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrest);
            stmt.setInt(2, codEjemplar);
            stmt.executeUpdate();
            stmt.close();
            sop("Estado del ejemplar actualizado");
        } catch (SQLException e) {
            sop("Error al actualizar el estado del ejemplar: " + e.getMessage());
        }
    }
    public static void devolverPrestamo(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el nombre de usuario:");
        String nombre = scanner.nextLine();
        String codUsuario = getCodUsuarioByNombre(conn, scanner, nombre);
        if (codUsuario.isEmpty()) {
            sop("No se encontro ningun usuario con ese nombre.");
            return;
        }
        mostrarPrestamosByCodUsuario(conn, codUsuario);
        sop("Introduce el codigo del prestamo:");
        int codPrestamo = scanner.nextInt();
        scanner.nextLine();
        while (!comprobarPrestamo(conn, codPrestamo, codUsuario)){
            sop("El prestamo no existe. Introduce otro codigo:");
            codPrestamo = scanner.nextInt();
            scanner.nextLine();
        }
        LocalDate hoy = LocalDate.now();
        Date fechaDevolucion = Date.valueOf(hoy);
        actualizarFechaPrestamo(conn, codPrestamo, codUsuario, fechaDevolucion);
        Date fechaEntrega = getFechaEntrega(conn, codPrestamo);
        int diasPrestado = diferenciaFechas(fechaEntrega, fechaDevolucion);
        if (diasPrestado > 30) {
            int diasDePenalizacion = (diasPrestado - 30) * 2;;
            generarPenalizaciones(conn, scanner, codUsuario, diasDePenalizacion, fechaEntrega);
            eliminarPrestamo(conn, codUsuario, codPrestamo);
            actualizarEjemplarDevolucion(conn, codPrestamo);
            sop("El libro ha sido devuelto, pero has recibido una penalizacion de " + diasDePenalizacion + " dias.");
        } else {
            eliminarPrestamo(conn, codUsuario, codPrestamo);
            actualizarEjemplarDevolucion(conn, codPrestamo);
            sop("El libro ha sido devuelto a tiempo.");
        }
    }
    public static boolean comprobarPrestamo(Connection conn, int codPrestamo, String codUsuario) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE cod_prest = ? AND cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrestamo);
            stmt.setString(2, codUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static void actualizarFechaPrestamo(Connection conn, int codPrestamo, String codUsuario, Date fechaDevolucion) throws SQLException {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE cod_prest = ? AND cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, fechaDevolucion);
            stmt.setInt(2, codPrestamo);
            stmt.setString(3, codUsuario);
            stmt.executeUpdate();
            stmt.close();
            sop("Prestamo actualizado correctamente.");
        } catch (SQLException e) {
            sop("Error al devolver el prestamo: " + e.getMessage());
        }
    }
    public static void actualizarEjemplarDevolucion(Connection conn, int codPrestamo) throws SQLException {
        String sql = "UPDATE ejemplares SET estado = 'disponible', cod_prest = NULL WHERE cod_prest = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrestamo);
            stmt.executeUpdate();
            stmt.close();
            sop("Ejemplar actualizado correctamente.");
        } catch (SQLException e) {
            sop("Error al actualizar el ejemplar: " + e.getMessage());
        }
    }
    public static Date getFechaEntrega(Connection conn, int codPrestamo) throws SQLException {
        String sql = "SELECT fecha_entrega FROM prestamos WHERE cod_prest = ?";
        Date fechaEntrega = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrestamo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fechaEntrega = rs.getDate("fecha_entrega");
            }
        } catch (SQLException e) {
            sop("Error al obtener la fecha de entrega: " + e.getMessage());
        }
        return fechaEntrega;
    }
    public static int diferenciaFechas(Date fecha1, Date fecha2) {
        LocalDate localDate1 = fecha1.toLocalDate();
        LocalDate localDate2 = fecha2.toLocalDate();
        return (int) ChronoUnit.DAYS.between(localDate1, localDate2);
    }

}


