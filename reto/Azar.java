public class Azar {
    private static String[] nombres= { "JuanPerez", "AnaLopez", "AitorGarcia", "IkerFernandez", "MartaSanchez", "SaraGomez", "DavidHernandez", "AlbaCastillo", "JavierAlvarez", "LuciaGonzalez" };
    private static String[] telefonos= {" 944123456", "944654321", "944987654", "944321987", "944654789", "689023289" , "648309823", "612990238", "679029012", "645725460" };
    private static String[] direcciones= {"Calle Mayor 1", "Avenida de la Libertad 2", "Calle del Sol 3", "Paseo de la Paz 4", "Calle de la Esperanza 5", "Avenida de la Libertad 6" , "Calle del Mar 7", "Paseo de la Montaña 8", "Calle de la Luna 9", "Avenida de la Estrella 10" };
    private static String[] num_ss= {"280012345678","080098765432","410056789012","150087654321","030045612378","470034567890","110076543210","260023456789","120065432189","390078901234"};
    private static boolean[] random = {true, false};
    public static String getNombre() {
        int randomIndex = (int) (Math.random() * nombres.length);
        return nombres[randomIndex];
    }
    public static String getTelefono() {
        int randomIndex = (int) (Math.random() * telefonos.length);
        return telefonos[randomIndex];
    }
    public static String getDireccion() {
        int randomIndex = (int) (Math.random() * direcciones.length);
        return direcciones[randomIndex];
    }
    public static boolean getRandom() {
        int randomIndex = (int) (Math.random() * random.length);
        return random[randomIndex];
    }
    public static String getNumSS() {
        int randomIndex = (int) (Math.random() * num_ss.length);
        return num_ss[randomIndex];
    }
}
