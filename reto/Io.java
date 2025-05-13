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
        
        sop("En que tabla buscas el registro por clave primaria? (u/ l/ a/ e/ pr/ pe)");
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
            case "pr":
                buscarPorCodPrestamo(conn, scanner);
                break;
            case "pe":
                buscarPorCodPenalizacion(conn, scanner);
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
    public static void buscarPorCodPrestamo(Connection conn, Scanner scanner) {
        System.out.print("Introduce el código del préstamo: ");
        String codPrestamo = scanner.nextLine();
    
        try {
            String query = "SELECT * FROM prestamos WHERE cod_prest = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codPrestamo);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                sop("Préstamo encontrado:");
                sop("Código préstamo: " + rs.getString("cod_prest"));
                sop("Fecha de préstamo: " + rs.getDate("fecha_prestamo"));
                sop("Fecha de entrega: " + rs.getDate("fecha_entrega"));
                sop("Fecha de devolución: " + rs.getDate("fecha_devolucion"));
                sop("Código de usuario: " + rs.getString("cod_usuario"));
            } else {
                sop("No se encontró ningún préstamo con ese código.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar el préstamo: " + e.getMessage());
        }
    }
    public static void buscarPorCodPenalizacion(Connection conn, Scanner scanner) {
        System.out.print("Introduce el ID de la penalización: ");
        String idPenalizacion = scanner.nextLine();
    
        try {
            String query = "SELECT * FROM penalizaciones WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idPenalizacion);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                sop("Penalización encontrada:");
                sop("ID: " + rs.getString("id"));
                sop("Código de usuario: " + rs.getString("cod_usuario"));
                sop("Motivo: " + rs.getString("motivo"));
                sop("Fecha de inicio: " + rs.getDate("fecha_inicio"));
                sop("Fecha de fin: " + rs.getDate("fecha_fin"));
            } else {
                sop("No se encontró ninguna penalización con ese ID.");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            sop("Error al buscar la penalización: " + e.getMessage());
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
        String sql = "SELECT * FROM prestamos WHERE cod_usuario = ? AND fecha_devolucion IS NULL";
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
    public static void anadirRegistro (Connection conn, Scanner scanner) throws SQLException {
        sop("Que tabla quieres añadir? (u/ l/ a/ e)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                try {
                    anadirUsuario(conn, scanner);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "l":
                try {
                    anadirLibro(conn, scanner);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "a":
                try {
                    anadirAutor(conn, scanner);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "e":
                try {
                    anadirEjemplar(conn, scanner);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                sop("opcion incorrecta");
                break;
        }

    }
    public static void anadirUsuario(Connection conn, Scanner scanner) throws SQLException {
        sop("Quieres generar un usuario automaticamente o manualmente? (a/m)");
        String opcion = scanner.nextLine();
        if (opcion.equalsIgnoreCase("a")) {
            generarUsuarioAutomaticamente(conn);
        } else if (opcion.equalsIgnoreCase("m")) {
            anadirUsuarioManual(conn, scanner);
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
        while (comprobarCodigoUsuario(conn, codUsuario)) {
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
    public static void anadirUsuarioManual(Connection conn, Scanner scanner) throws SQLException {
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
            }else{
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.setString(6, correo);
            if (opcion.equalsIgnoreCase("t")) {
                stmt.setString(7, numSS);
            }else{
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
            sop("Usuario " + nombre + " generado correctamente.");
        }
    }
    public static void anadirLibro(Connection conn, Scanner scanner) throws SQLException {
        String isbn = generarISBN(conn, scanner);
        
        String titulo = generarTitulo(scanner);
        
        int paginas = generarPaginas(scanner);

        String urlImg = null;
        sop("Quieres introducir una URL de la imagen? (s/n)");
        String respuesta = scanner.nextLine();
        if(respuesta.equalsIgnoreCase("s")) { 
            urlImg = generarUrlImg(scanner);
        }
        String sql = "INSERT INTO libros (isbn, titulo,nº_copias_existentes, paginas, urlimg) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, titulo);
            stmt.setNull(3, java.sql.Types.VARCHAR);    
            stmt.setInt(4, paginas);
            if (respuesta.equalsIgnoreCase("s")) {
                stmt.setString(5, urlImg);
            }else{
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
            sop("Libro " + titulo + " generado correctamente.");
        }
        String autor = generarNombreAutor(scanner);
        String apellido = generarApellidoAutor(scanner);
        String dni = getDNIByNombreAndApellido(conn, scanner, autor, apellido);
        while (dni == null){
            sop("No se ha encontrado el autor, ¿Qieres crear uno nuevo o volver a intentarlo? (crear/repetir):");
            respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("repetir")) {
                autor = generarNombreAutor(scanner);
                apellido = generarApellidoAutor(scanner);
                dni = getDNIByNombreAndApellido(conn, scanner, autor, apellido);
            }else if (respuesta.equalsIgnoreCase("crear")) {
                dni = generarDNIAutor(conn, scanner);
                autor = generarNombreAutor(scanner);
                apellido = generarApellidoAutor(scanner);
                String apellido2 = null;
                sop("Quieres introducir el segundo apellido del autor? (s/n)");
                respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("s")) {
                    apellido2 = generarApellidoAutor(scanner);
                }
                insertarAutor(conn, scanner, dni, autor, apellido, apellido2);
            }else{
                sop("Opcion no valida.");
            }
        }
        int numEjemplares = generarNumEjemplares(scanner);
        actualizarLibro(conn, isbn, numEjemplares);

        for (int i = 0; i < numEjemplares; i++) {
            insertarEjemplar(conn, scanner, isbn);
        }
    }
    public static void anadirAutor(Connection conn, Scanner scanner) throws SQLException {
        String dni = generarDNIAutor(conn, scanner);
        String nombre = generarNombreAutor(scanner);
        String apellido = generarApellidoAutor(scanner);
        sop("Quieres introducir el segundo apellido del autor? (s/n)");
        String respuesta = scanner.nextLine();
        String apellido2 = null;
        if (respuesta.equalsIgnoreCase("s")) {
            apellido2 = generarApellidoAutor(scanner);
        }
        insertarAutor(conn, scanner, dni, nombre, apellido, apellido2);
    }
    public static void anadirEjemplar(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el isbn:");
        String isbn = scanner.nextLine();
        while (!comprobarISBN(conn, isbn)) {
            sop("Introduce el isbn:");
            isbn = scanner.nextLine();
        }
        insertarEjemplar(conn, scanner, isbn);
    }
    public static void insertarAutor(Connection conn, Scanner scanner, String dni, String nombre , String apellido , String apellido2) throws SQLException {
        String sql = "INSERT INTO autores (dni, nombre, ape1, ape2) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            if (apellido2==null) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }else{
                stmt.setString(4, apellido2);
            }
            stmt.executeUpdate();
            sop("Autor " + nombre + " generado correctamente.");
        }
    }
    public static void insertarEjemplar(Connection conn, Scanner scanner, String isbn) throws SQLException {
        String sql = "INSERT INTO ejemplares (cod_ejem, idioma, estado, isbn, cod_prest) VALUES (?, ?, ?, ?, ?)";
        int codEjem = (int) (Math.random()*1000)+1;
        if (comprobarCodigoEjemplar(conn, codEjem)) {
            codEjem = (int) (Math.random()*1000)+1;
        }
        String idioma = Azar.getIdioma();
        String estado = "disponible";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codEjem);
            stmt.setString(2, idioma);
            stmt.setString(3, estado);
            stmt.setString(4, isbn);
            stmt.setNull(5, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            sop("Ejemplar generado correctamente.");
        }
    }
    public static int generarCodigoManual(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el codigo del usuario:");
        int codUsuario = scanner.nextInt();
        scanner.nextLine(); 
        while (comprobarCodigoUsuario(conn, codUsuario)) {
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
    public static String generarISBN(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el ISBN del libro:");
        String isbn = scanner.nextLine();
        while (comprobarISBN(conn, isbn)) {
            sop("El ISBN ya existe. Introduce un ISBN valido:");
            isbn = scanner.nextLine();
        }
        return isbn;
    }
    public static String generarTitulo(Scanner scanner) throws SQLException {
        sop("Introduce el titulo del libro:");
        String titulo = scanner.nextLine();
        return titulo;
    }
    public static int generarPaginas(Scanner scanner) {
        sop("Introduce el numero de paginas del libro:");
        int paginas = scanner.nextInt();
        scanner.nextLine(); 
        return paginas;
    }
    public static String generarUrlImg(Scanner scanner) {
        sop("Introduce la URL de la imagen del libro:");
        String urlImg = scanner.nextLine();
        return urlImg;
    }
    public static String generarDNIAutor(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el DNI del autor:");
        String dni = scanner.nextLine();
        while (comprobarDNI(conn, dni)) {
            sop("El DNI ya existe. Introduce otro DNI:");
            dni = scanner.nextLine();
        }
        return dni;
    }
    public static String generarNombreAutor(Scanner scanner) {
        sop("Introduce el nombre del autor:");
        String nombre = scanner.nextLine();
        return nombre;
    }
    public static String generarApellidoAutor(Scanner scanner) {
        sop("Introduce el apellido del autor:");
        String apellido = scanner.nextLine();
        return apellido;
    }
    public static int generarNumEjemplares(Scanner scanner) {
        sop("Cuantos ejemplares quieres añadir?");
        int numEjemplares = scanner.nextInt();
        scanner.nextLine(); 
        while (numEjemplares <= 0) {
            sop("El numero de ejemplares debe ser mayor que 0. Introduce otro numero:");
            numEjemplares = scanner.nextInt();
            scanner.nextLine();
        }
        return numEjemplares;
    }
    public static void eliminarRegistro(Connection conn, Scanner scanner) throws SQLException {
        sop("De que tabla quieres eliminar un registro? (u/ l/ a/ e)");
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "u":
                eliminarUsuario(conn, scanner);
                break;
            case "l":
                eliminarLibro(conn, scanner);
                break;
            case "a":
                eliminarAutor(conn, scanner);
                break;
            case "e":
                eliminarEjemplares(conn, scanner);
                break;
            default:
                sop("opcion incorrecta");
                break;
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
    public static void eliminarLibro(Connection conn, Scanner scanner) throws SQLException {
        mostrarLibros(conn);
        sop("Introduce el ISBN del libro a eliminar:");
        String isbn = scanner.nextLine();
        String sql = "DELETE FROM libros WHERE isbn = ?";
        if (!comprobarEjemplaresEnPrestamo(conn, isbn)){
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, isbn);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    sop("Libro eliminado correctamente.");
                } else {
                    sop("No se encontro ningun libro con ese ISBN.");
                }
                eliminarEjemplaresByIsbn(conn, isbn);
            }
        }else{
            sop("No se pudo eliminar ya que hay un ejemplar en prestamo");
        }
    }
    public static void eliminarEjemplaresByIsbn(Connection conn, String isbn) throws SQLException {
        String sql = "DELETE FROM ejemplares WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sop("Ejemplares eliminados correctamente.");
            } else {
                sop("No se encontro ningun ejemplar con ese ISBN.");
            }
        }
    }
    public static void eliminarEjemplares(Connection conn, Scanner scanner) throws SQLException {
        mostrarEjemplares(conn);
        String sql = "DELETE FROM ejemplares WHERE cod_ejem = ?";
        sop("Introduce el codigo del ejemplar a eliminar:");
        String codEjem = scanner.nextLine();
        if (!comprobarEjemplarEnPrestamo(conn, codEjem)){
            if (!comprobarCantidadEjemplares(conn, codEjem)){
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, codEjem);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        sop("Ejemplar eliminado correctamente.");
                    } else {
                        sop("No se encontro ningun ejemplar con ese codigo.");
                    }
                }
            }else{
                sop("No se puede eliminar el ejemplar ya que es el ultimo que queda de ese libro");
            }
        }else{
            sop("No se pudo eliminar el ejemplar ya que esta en prestamo o reparacion");
        }
    }
    public static void eliminarAutor(Connection conn, Scanner scanner) throws SQLException {
        mostrarAutores(conn);
        sop("Introduce el DNI del autor a eliminar:");
        String dni = scanner.nextLine();
        String sql = "DELETE FROM autores WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sop("Autor eliminado correctamente.");
            } else {
                sop("No se encontro ningun autor con ese DNI.");
            }
        }
    }
    public static boolean comprobarCantidadEjemplares(Connection conn, String codEjem){
        String sql = "SELECT * FROM ejemplares WHERE cod_ejem = ?";
        int cont=0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, codEjem);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                cont++;
            }
        }catch (SQLException e){
            sop("Error al eliminar el ejemplar: "+ e.getMessage());
        }
        return cont<2;
    }
    public static boolean comprobarEjemplarEnPrestamo(Connection conn, String codEjem){
        String sql ="SELECT * FROM ejemplares WHERE cod_ejem = ? AND estado != 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, codEjem);
            ResultSet rs =stmt.executeQuery();
            return rs.next();
        }catch(SQLException e){
            sop("Error al comprobar el estado de los ejemplares: "+ e.getMessage());
            return false;
        }
    }
    public static boolean comprobarEjemplaresEnPrestamo(Connection conn, String isbn){
        String sql ="SELECT * FROM ejemplares WHERE isbn = ? AND estado != 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, isbn);
            ResultSet rs =stmt.executeQuery();
            return rs.next();
        }catch(SQLException e){
            sop("Error al comprobar el estado de los ejemplares: "+ e.getMessage());
            return true;
        }
    }
    public static boolean comprobarCodigoUsuario(Connection conn, int codigo) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE cod_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean comprobarCodigoPrestamo(Connection conn, int codigo) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE cod_prest = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean comprobarCodigoEjemplar(Connection conn, int codigo) throws SQLException {
        String sql = "SELECT * FROM ejemplares WHERE cod_ejem = ?";
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
    public static boolean comprobarISBN(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean comprobarDNI(Connection conn, String dni) throws SQLException {
        String sql = "SELECT * FROM autores WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static void crearTablas(Connection conn, Scanner scanner) {
        
        sop("Que tabla quieres crear? (u/ l/ a/ e/ pr/ pe)");
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
            case "pr":
                crearTablaPrestamos(conn);
                break;
            case "pe":
                crearTablaPenalizaciones(conn);
                break;
            default:
                sop("opcion incorrecta");
                break;
        }

    }
    public static void borrarTablas(Connection conn, Scanner scanner) {
        
        sop("Que tabla quieres crear? (u/ l/ a/ e/ pr/ pe)");
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
            case "pr":
                borrarTablaPrestamos(conn);
                break;
            case "pe":
                borrarTablaPenalizaciones(conn);
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
        if (!comprobarExistenciaTablaUsuarios(conn)) {
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
        }else{
            sop("La tabla usuarios ya esta creada.");
        }
    }
    public static void crearTablaAutores(Connection conn) {
        if (!comprobarExistenciaTablaAutores(conn)) {   
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
        }else{
            sop("La tabla autores ya esta creada.");
        }
    }
    public static void crearTablaEjemplares(Connection conn) {
        if (!comprobarExistenciaTablaEjemplares(conn)) {    
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
        }else{
            sop("La tabla ejemplares ya esta creada.");
        }
    }
    public static void crearTablaLibros(Connection conn) {
        if (!comprobarExistenciaTablaLibros(conn)) {    
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
        }else{
            sop("La tabla libros ya esta creada.");
        }
    }
    public static void crearTablaPrestamos(Connection conn) {
        if (!comprobarExistenciaTablaPrestamos(conn)) {    
            try {
                String sql = "CREATE TABLE prestamos (" +
                            "cod_prest INT PRIMARY KEY, " +
                            "fecha_prestamo DATE, " +
                            "fecha_entrega DATE, " +
                            "fecha_devolucion DATE, " +
                            "cod_usuario INT)";
                
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                stmt.close();
                sop("Tabla 'prestamos' creada correctamente.");
            } catch (SQLException e) {
                sop("Error al crear la tabla: " + e.getMessage());
            }
        }else{
            sop("La tabla prestamos ya esta creada.");
        }   
    }
    public static void crearTablaPenalizaciones(Connection conn) {
        if (!comprobarExistenciaTablaPenalizaciones(conn)) {    
            try {
                String sql = "CREATE TABLE penalizaciones (" +
                            "id INT PRIMARY KEY AUTO_INCREMENT, " +
                            "cod_usuario INT, " +
                            "motivo VARCHAR(255), " +
                            "fecha_inicio DATE, " +
                            "fecha_fin DATE)";
                
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                stmt.close();
                sop("Tabla 'penalizaciones' creada correctamente.");
            } catch (SQLException e) {
                sop("Error al crear la tabla: " + e.getMessage());
            }
        }else{
            sop("La tabla penalizaciones ya esta creada.");
        }
    }

    public static void borrarTablaUsuarios(Connection conn) {
        if (comprobarExistenciaTablaUsuarios(conn)) {
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
        }else{
            sop("La tabla usuarios ya estaba eliminada.");
        }
    }
    public static void borrarTablaLibros(Connection conn) {
        if (comprobarExistenciaTablaLibros(conn)) {
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
        }else{
            sop("La tabla libros ya estaba eliminada.");
        }
    }
    public static void borrarTablaEjemplares(Connection conn) {
        if (comprobarExistenciaTablaEjemplares(conn)) {
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
        }else{
            sop("La tabla ejemplares ya estaba eliminada.");
        }
    }
    public static void borrarTablaAutores(Connection conn) {
        if (comprobarExistenciaTablaAutores(conn)){
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
        }else{
            sop("La tabla autores ya estaba eliminada.");
        }
    }
    public static void borrarTablaPrestamos(Connection conn) {
        if (comprobarExistenciaTablaPrestamos(conn)) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
                stmt.executeUpdate("DROP TABLE IF EXISTS prestamos");
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
                stmt.close();
                sop("Tabla 'prestamos' eliminada correctamente.");
            } catch (SQLException e) {
                sop("Error al eliminar la tabla 'prestamos': " + e.getMessage());
            }
        }else{
            sop("La tabla prestamos ya estaba eliminada.");
        }
    }
    
    public static void borrarTablaPenalizaciones(Connection conn) {
        if (comprobarExistenciaTablaPenalizaciones(conn)) {  
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
                stmt.executeUpdate("DROP TABLE IF EXISTS penalizaciones");
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
                stmt.close();
                sop("Tabla 'penalizaciones' eliminada correctamente.");
            } catch (SQLException e) {
                sop("Error al eliminar la tabla 'penalizaciones': " + e.getMessage());
            }
        }else{
            sop("La tabla penalizaciones ya estaba eliminada.");
        }
    }
    public static boolean comprobarExistenciaTablaUsuarios(Connection conn) {
        String sql = "SHOW TABLES LIKE 'usuarios'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'usuarios': " + e.getMessage());
            return false;
        }
    }
    public static boolean comprobarExistenciaTablaLibros(Connection conn) {
        String sql = "SHOW TABLES LIKE 'libros'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'libros': " + e.getMessage());
            return false;
        }
    }
    public static boolean comprobarExistenciaTablaEjemplares(Connection conn) {
        String sql = "SHOW TABLES LIKE 'ejemplares'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'ejemplares': " + e.getMessage());
            return false;
        }
    }
    public static boolean comprobarExistenciaTablaAutores(Connection conn) {
        String sql = "SHOW TABLES LIKE 'autores'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'autores': " + e.getMessage());
            return false;
        }
    }
    public static boolean comprobarExistenciaTablaPrestamos(Connection conn) {
        String sql = "SHOW TABLES LIKE 'prestamos'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'prestamos': " + e.getMessage());
            return false;
        }
    }
    public static boolean comprobarExistenciaTablaPenalizaciones(Connection conn) {
        String sql = "SHOW TABLES LIKE 'penalizaciones'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            sop("Error al comprobar la existencia de la tabla 'penalizaciones': " + e.getMessage());
            return false;
        }
    }
    public static void mostrarCampos(Connection conn, Scanner scanner) {
        
        sop("De que tabla quieres ver los Campos? (u/ l/ a/ e/ pr/ pe)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                mostrarCamposTablaUsuarios(conn);
                break;
            case "l":
                mostrarCamposTablaLibros(conn);
                break;
            case "a":
                mostrarCamposTablaAutores(conn);
                break;
            case "e":
                mostrarCamposTablaEjemplares(conn);
                break;
            case "pr":
                mostrarCamposTablaPrestamos(conn);
                break;
            case "pe":
                mostrarCamposTablaPenalizaciones(conn);
                break;
            default:
                sop("opcion incorrecta");
                break;
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
    public static void mostrarCamposTablaLibros(Connection conn) {
        try {
            String sql = "SELECT * FROM libros LIMIT 1";
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
            sop("Error al obtener los campos de la tabla 'libros': " + e.getMessage());
        }
    }
    
    public static void mostrarCamposTablaAutores(Connection conn) {
        try {
            String sql = "SELECT * FROM autores LIMIT 1";
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
            sop("Error al obtener los campos de la tabla 'autores': " + e.getMessage());
        }
    }
    public static void mostrarCamposTablaEjemplares(Connection conn) {
        try {
            String sql = "SELECT * FROM ejemplares LIMIT 1";
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
            sop("Error al obtener los campos de la tabla 'ejemplares': " + e.getMessage());
        }
    }
    public static void mostrarCamposTablaPrestamos(Connection conn) {
        try {
            String sql = "SELECT * FROM prestamos LIMIT 1";
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
            sop("Error al obtener los campos de la tabla 'prestamos': " + e.getMessage());
        }
    }
    public static void mostrarCamposTablaPenalizaciones(Connection conn) {
        try {
            String sql = "SELECT * FROM penalizaciones LIMIT 1";
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
            sop("Error al obtener los campos de la tabla 'penalizaciones': " + e.getMessage());
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
        eliminarPenalizacion(conn, codUsuario);
        if (comprobarCantidadPrestamos(conn, codUsuario)){
            if(!algunaPenalizacion(conn, codUsuario)){
                mostrarLibros(conn);
                sop("Introduce el isbn del libro que quiere:");
                String isbn = scanner.nextLine();
                if (!comprobarISBN(conn, isbn)) {
                    sop("El ISBN no existe. Introduce otro ISBN:");
                    isbn = scanner.nextLine();
                }
                if (!algunaDisponibilidad(conn,isbn)) {
                    sop("No hay ejemplares disponibles.");
                    continuar(scanner);
                    mostrarLibros(conn);
                    sop("Introduce el isbn del libro que quiere:");
                    isbn = scanner.nextLine();
                    if (!comprobarISBN(conn, isbn)) {
                        sop("El ISBN no existe. Introduce otro ISBN:");
                        isbn = scanner.nextLine();
                    }
                }
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
                while (comprobarCodigoPrestamo(conn, codPrest)) {
                    codPrest = (int) (Math.random()*1000)+1;
                }
                insertarPrestamo(conn, codPrest, fechaPrestamo, fechaEntrega, codUsuario);
                actualizarEjemplar(conn, codPrest, codEjemplar);
            }else{
                sop("El usuario tiene una penalizacion activa.");
            }
        }else{
            sop("El usuario ya tiene 3 prestamos activos.");
        }
    }
    public static boolean comprobarCantidadPrestamos(Connection conn, String codUsuario) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE cod_usuario = ? AND fecha_devolucion IS NULL";
        int cont=0;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cont++;
            }
        return cont < 3;
        }
    }
    
    public static boolean algunaPenalizacion(Connection conn, String codUsuario) throws SQLException {
        String sql = "SELECT * FROM penalizaciones WHERE cod_usuarios = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static void eliminarPenalizacion(Connection conn, String codUsuario) throws SQLException {
        String sql = "DELETE FROM penalizaciones WHERE cod_usuarios = ? AND DATE_ADD(fecha_pen, INTERVAL 30 DAY) < CURRENT_DATE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUsuario);
            stmt.executeUpdate();
        }
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
    public static String getDNIByNombreAndApellido(Connection conn, Scanner scanner, String nombre, String apellido) throws SQLException {
        String sql = "SELECT dni FROM autores WHERE nombre = ? AND ape1 = ?";
        String dni = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dni = rs.getString("dni");
                return dni;
            } else {
                sop("No se encontro ningun autor con ese nombre y apellido.");
            }
        } catch (SQLException e) {
            sop("Error al buscar el autor: " + e.getMessage());
        }
        return dni;
    }
    public static boolean comprobarDisponibilidad(Connection conn, int codEjemplar) throws SQLException {
        String sql = "SELECT * FROM ejemplares WHERE cod_ejem = ? AND estado = 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codEjemplar);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        }
    }
    public static boolean algunaDisponibilidad(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT * FROM ejemplares WHERE isbn= ? AND estado = 'disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
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
    public static void actualizarLibro(Connection conn, String isbn, int numEjemplares){
        String sql = "UPDATE libros SET nº_copias_existentes = ? WHERE isbn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numEjemplares);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
            stmt.close();
            sop("Estado del libro actualizado");
        } catch (SQLException e) {
            sop("Error al actualizar el estado del libro: " + e.getMessage());
        }
    }
    public static void devolverPrestamo(Connection conn, Scanner scanner) throws SQLException {
        sop("Introduce el nombre de usuario:");
        String nombre = scanner.nextLine();
        String codUsuario = getCodUsuarioByNombre(conn, scanner, nombre);
        while (codUsuario == null) {
            sop("Introduce el nombre de usuario:");
            nombre = scanner.nextLine();
            codUsuario = getCodUsuarioByNombre(conn, scanner, nombre);
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
            actualizarEjemplarDevolucion(conn, codPrestamo);
            sop("El libro ha sido devuelto, pero has recibido una penalizacion de " + diasDePenalizacion + " dias.");
        } else {
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
    public static void mostrarTablas(Connection conn, Scanner scanner) {
        
        sop("De que tabla quieres ver los Campos? (u/ l/ a/ e/ pr/ pe)");
        String opcion=scanner.nextLine();
        switch (opcion) {
            case "u":
                mostrarUsuariosPaginados(conn, scanner);
                break;
            case "l":
                mostrarLibrosPaginados(conn, scanner);
                break;
            case "a":
                mostrarAutoresPaginados(conn, scanner);
                break;
            case "e":
                mostrarPenalizacionesPaginadas(conn, scanner);
                break;
            case "pr":
                mostrarReservasPaginadas(conn, scanner);
                break;
            default:
                sop("opcion incorrecta");
                break;
        }

    }
    public static void mostrarUsuariosPaginados(Connection conn, Scanner scanner){
        int pagina = 0;
        int tamanioPagina = 10;
        int totalUsuarios = 0;

        try{
            String conteo = "SELECT COUNT(*) FROM usuarios";
            PreparedStatement stmtConteo = conn.prepareStatement(conteo);
            ResultSet rsConteo = stmtConteo.executeQuery();

            if (rsConteo.next()){
                totalUsuarios = rsConteo.getInt(1);
            }

            rsConteo.close();
            stmtConteo.close();

            if (totalUsuarios == 0){
                sop("No hay usuarios en la base de datos");
                return;
            }

            char opcion = ' ';
            int totalPaginas = (totalUsuarios + tamanioPagina - 1) / tamanioPagina;

            do {
                Io.clearScreen();
                sop("Mostrando usuarios - Pagina " + (pagina + 1) + " de " + totalPaginas + "\n");

                String sql = "SELECT * FROM usuarios LIMIT ? OFFSET ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tamanioPagina);
                stmt.setInt(2, pagina * tamanioPagina);
                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                for (int i = 1; i <= columnCount; i++){
                    System.out.printf("%-20s", rsmd.getColumnName(i));
                }
                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++){
                        System.out.printf("%-20s", rs.getString(i));
                    }
                    System.out.println();
                }
                rs.close();
                stmt.close();

                sop("\nNavegar: '+' siguiente, '-' anterior, 'q' salir.");
                String input = scanner.nextLine();
                if (!input.isEmpty()){
                    opcion = input.charAt(0);
                

                    switch (opcion) {
                        case '+':
                            pagina = (pagina + 1) % totalPaginas; 
                            break;
                        case '-':
                            pagina = (pagina - 1 + totalPaginas) % totalPaginas;
                            break;
                        case 'q':
                            break;
                        default:
                            sop("Opcion invalida. Usa '+' para avanzar, '-' para retroceder o 'q' para salir.");

                            sop("Presiona Enter para continuar...");
                            scanner.nextLine();
                            break;
                    }
                } else if (!input.isEmpty()){
                    sop("Usa solo '+', '-' o 'q'.");
                    sop("Presiona Enter para continuar...");
                    scanner.nextLine();
                }

            } while (opcion != 'q');

        } catch (SQLException e){
            sop("Error al mostrar usuarios paginados: " + e.getMessage());
        }
    }
    public static void mostrarLibrosPaginados(Connection conn, Scanner scanner) {
        int pagina = 0;
        int tamanioPagina = 10;
        int totalLibros = 0;
    
        try {
            String conteo = "SELECT COUNT(*) FROM libros";
            PreparedStatement stmtConteo = conn.prepareStatement(conteo);
            ResultSet rsConteo = stmtConteo.executeQuery();
    
            if (rsConteo.next()) {
                totalLibros = rsConteo.getInt(1);
            }
    
            rsConteo.close();
            stmtConteo.close();
    
            if (totalLibros == 0) {
                sop("No hay libros en la base de datos");
                return;
            }
    
            char opcion = ' ';
            int totalPaginas = (totalLibros + tamanioPagina - 1) / tamanioPagina;
    
            do {
                Io.clearScreen();
                sop("Mostrando libros - Página " + (pagina + 1) + " de " + totalPaginas + "\n");
    
                String sql = "SELECT ISBN, TITULO, PAGINAS, URLIMG FROM libros LIMIT ? OFFSET ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tamanioPagina);
                stmt.setInt(2, pagina * tamanioPagina);
                ResultSet rs = stmt.executeQuery();
    
                System.out.printf("%-20s%-40s%-10s%-50s\n", "ISBN", "TITULO", "PAGINAS", "URLIMG");
                System.out.println("----------------------------------------------------------------------------------------------------------");
    
                while (rs.next()) {
                    System.out.printf("%-20s%-40s%-10d%-50s\n",
                        rs.getString("ISBN"),
                        rs.getString("TITULO"),
                        rs.getInt("PAGINAS"),
                        rs.getString("URLIMG")
                    );
                }
    
                rs.close();
                stmt.close();
    
                sop("\nNavegar: '+' siguiente, '-' anterior, 'q' salir.");
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    opcion = input.charAt(0);
    
                    switch (opcion) {
                        case '+':
                            pagina = (pagina + 1) % totalPaginas;
                            break;
                        case '-':
                            pagina = (pagina - 1 + totalPaginas) % totalPaginas;
                            break;
                        case 'q':
                            break;
                        default:
                            sop("Opción inválida. Usa '+' para avanzar, '-' para retroceder o 'q' para salir.");
                            sop("Presiona Enter para continuar...");
                            scanner.nextLine();
                            break;
                    }
                }
    
            } while (opcion != 'q');
    
        } catch (SQLException e) {
            sop("Error al mostrar libros paginados: " + e.getMessage());
        }
    }
    public static void mostrarAutoresPaginados(Connection conn, Scanner scanner) {
        int pagina = 0;
        int tamanioPagina = 10;
        int totalAutores = 0;
    
        try {
            String conteo = "SELECT COUNT(*) FROM autores";
            PreparedStatement stmtConteo = conn.prepareStatement(conteo);
            ResultSet rsConteo = stmtConteo.executeQuery();
    
            if (rsConteo.next()) {
                totalAutores = rsConteo.getInt(1);
            }
    
            rsConteo.close();
            stmtConteo.close();
    
            if (totalAutores == 0) {
                sop("No hay autores en la base de datos");
                return;
            }
    
            char opcion = ' ';
            int totalPaginas = (totalAutores + tamanioPagina - 1) / tamanioPagina;
    
            do {
                Io.clearScreen();
                sop("Mostrando autores - Página " + (pagina + 1) + " de " + totalPaginas + "\n");
    
                String sql = "SELECT DNI, NOMBRE, APE1, APE2 FROM autores LIMIT ? OFFSET ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tamanioPagina);
                stmt.setInt(2, pagina * tamanioPagina);
                ResultSet rs = stmt.executeQuery();
    
                System.out.printf("%-15s%-20s%-20s%-20s\n", "DNI", "NOMBRE", "APE1", "APE2");
                System.out.println("----------------------------------------------------------------------------------------------------------");
    
                while (rs.next()) {
                System.out.printf("%-15s%-20s%-20s%-20s\n",
                    rs.getString("DNI"),
                    rs.getString("NOMBRE"),
                    rs.getString("APE1"),
                    rs.getString("APE2")
                    );
                }
    
                rs.close();
                stmt.close();
    
                sop("\nNavegar: '+' siguiente, '-' anterior, 'q' salir.");
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    opcion = input.charAt(0);
    
                    switch (opcion) {
                        case '+':
                            pagina = (pagina + 1) % totalPaginas;
                            break;
                        case '-':
                            pagina = (pagina - 1 + totalPaginas) % totalPaginas;
                            break;
                        case 'q':
                            break;
                        default:
                            sop("Opción inválida. Usa '+' para avanzar, '-' para retroceder o 'q' para salir.");
                            sop("Presiona Enter para continuar...");
                            scanner.nextLine();
                            break;
                    }
                }
    
            } while (opcion != 'q');
    
        } catch (SQLException e) {
            sop("Error al mostrar autores paginados: " + e.getMessage());
        }
    }
    public static void mostrarPenalizacionesPaginadas(Connection conn, Scanner scanner) {
        int pagina = 0;
        int tamanioPagina = 10;
        int totalPenalizaciones = 0;
    
        try {
            String conteo = "SELECT COUNT(*) FROM penalizaciones";
            PreparedStatement stmtConteo = conn.prepareStatement(conteo);
            ResultSet rsConteo = stmtConteo.executeQuery();
    
            if (rsConteo.next()) {
                totalPenalizaciones = rsConteo.getInt(1);
            }
    
            rsConteo.close();
            stmtConteo.close();
    
            if (totalPenalizaciones == 0) {
                sop("No hay penalizaciones en la base de datos");
                return;
            }
    
            char opcion = ' ';
            int totalPaginas = (totalPenalizaciones + tamanioPagina - 1) / tamanioPagina;
    
            do {
                Io.clearScreen();
                sop("Mostrando penalizaciones - Página " + (pagina + 1) + " de " + totalPaginas + "\n");
    
                String sql = "SELECT ID, COD_USUARIO, MOTIVO, FECHA_INICIO, FECHA_FIN FROM penalizaciones LIMIT ? OFFSET ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tamanioPagina);
                stmt.setInt(2, pagina * tamanioPagina);
                ResultSet rs = stmt.executeQuery();
    
                System.out.printf("%-5s%-15s%-30s%-15s%-15s\n", "ID", "COD_USUARIO", "MOTIVO", "FECHA_INICIO", "FECHA_FIN");
                System.out.println("----------------------------------------------------------------------------------------------------------");
    
                while (rs.next()) {
                System.out.printf("%-5d%-15s%-30s%-15s%-15s\n",
                    rs.getInt("ID"),
                    rs.getString("COD_USUARIO"),
                    rs.getString("MOTIVO"),
                    rs.getDate("FECHA_INICIO"),
                    rs.getDate("FECHA_FIN")
                    );
                }
    
                rs.close();
                stmt.close();
    
                sop("\nNavegar: '+' siguiente, '-' anterior, 'q' salir.");
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    opcion = input.charAt(0);
    
                    switch (opcion) {
                        case '+':
                            pagina = (pagina + 1) % totalPaginas;
                            break;
                        case '-':
                            pagina = (pagina - 1 + totalPaginas) % totalPaginas;
                            break;
                        case 'q':
                            break;
                        default:
                            sop("Opción inválida. Usa '+' para avanzar, '-' para retroceder o 'q' para salir.");
                            sop("Presiona Enter para continuar...");
                            scanner.nextLine();
                            break;
                    }
                }
    
            } while (opcion != 'q');
    
        } catch (SQLException e) {
            sop("Error al mostrar penzalizaciones paginadas: " + e.getMessage());
        }
    }
    public static void mostrarReservasPaginadas(Connection conn, Scanner scanner) {
        int pagina = 0;
        int tamanioPagina = 10;
        int totalReservas = 0;
    
        try {
            String conteo = "SELECT COUNT(*) FROM reservas";
            PreparedStatement stmtConteo = conn.prepareStatement(conteo);
            ResultSet rsConteo = stmtConteo.executeQuery();
    
            if (rsConteo.next()) {
                totalReservas = rsConteo.getInt(1);
            }
    
            rsConteo.close();
            stmtConteo.close();
    
            if (totalReservas == 0) {
                sop("No hay reservas en la base de datos");
                return;
            }
    
            char opcion = ' ';
            int totalPaginas = (totalReservas + tamanioPagina - 1) / tamanioPagina;
    
            do {
                Io.clearScreen();
                sop("Mostrando reservas - Página " + (pagina + 1) + " de " + totalPaginas + "\n");
    
                String sql = "SELECT ID, COD_USUARIO, ISBN, FECHA_RESERVA FROM reservas LIMIT ? OFFSET ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tamanioPagina);
                stmt.setInt(2, pagina * tamanioPagina);
                ResultSet rs = stmt.executeQuery();
    
                System.out.printf("%-5s%-15s%-20s%-15s\n", "ID", "COD_USUARIO", "ISBN", "FECHA_RESERVA");
                System.out.println("----------------------------------------------------------------------------------------------------------");
    
                while (rs.next()) {
                System.out.printf("%-5d%-15s%-20s%-15s\n",
                    rs.getInt("ID"),
                    rs.getString("COD_USUARIO"),
                    rs.getString("ISBN"),
                    rs.getDate("FECHA_RESERVA")
                    );
                }
    
                rs.close();
                stmt.close();
    
                sop("\nNavegar: '+' siguiente, '-' anterior, 'q' salir.");
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    opcion = input.charAt(0);
    
                    switch (opcion) {
                        case '+':
                            pagina = (pagina + 1) % totalPaginas;
                            break;
                        case '-':
                            pagina = (pagina - 1 + totalPaginas) % totalPaginas;
                            break;
                        case 'q':
                            break;
                        default:
                            sop("Opción inválida. Usa '+' para avanzar, '-' para retroceder o 'q' para salir.");
                            sop("Presiona Enter para continuar...");
                            scanner.nextLine();
                            break;
                    }
                }
    
            } while (opcion != 'q');
    
        } catch (SQLException e) {
            sop("Error al mostrar reservas paginadas: " + e.getMessage());
        }
    }
}


