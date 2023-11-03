import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

public class App {
    public static String formatMessage(String text, String textColor) {
    // Ejemplo de uso: formatMessage("Texto de color verde", "\u001B[32m");
    return textColor + text + "\u001B[0m"; // Restablece el color
}
    String Verde = formatMessage("Este mensaje es verde", "\u001B[32m");
    String Rosa = formatMessage("Este mensaje es rosa", "\u001B[35m");
    String Azul = formatMessage("Este mensaje es azul", "\u001B[34m");


    public static void main(String[] args) throws Exception {
        Map<String, List<Transaccion>> transaccionesPorDia = cargarDatos();
        
        // Carga los datos desde los archivos de texto

        boolean continuar = true;
        Scanner scanner = new Scanner(System.in);

        while (continuar) {
            System.out.println("\u001B[35m" );
            System.out.println("=============================================================================");
            System.out.println("Menú de interacción:");
            System.out.println("1. Día de la semana con mayor cantidad de dinero movido.");
            System.out.println("2. Hora del día en que más cantidad de dinero se mueve en promedio.");
            System.out.println("3. Encontrar información de una transacción por su ID.");
            System.out.println("4. Visualizar datos de un día específico.");
            System.out.println("5. Salir\n");
            System.out.println("\u001B[0m");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    String diaMayorDinero = obtenerDiaMayorCantidadDinero(transaccionesPorDia);
                    System.out.println(diaMayorDinero);
                    break;
                case 2:
                    int horaPromedioMayorDinero = obtenerHoraPromedioMayorDinero(transaccionesPorDia);
                    System.out
                            .println("La hora promedio con la mayor cantidad de dinero es: \n[+] "
                                    +"\u001B[32m"+ horaPromedioMayorDinero);
                    System.out.println("");
                            System.out.println("\u001B[0m");
                    break;
                case 3:
                      System.out.print("Ingrese el ID de la transacción a buscar: ");
                    String idBuscado = scanner.next();
                    ResultadoBusqueda resultado = buscarTransaccionPorID(transaccionesPorDia, idBuscado);
                    if (resultado != null) {
                        System.out.println("\u001B[35m" );
                        System.out.println("Información de la transacción encontrada:\n[+]");
                        System.out.println("Día: " + resultado.getDia());
                        System.out.println("Posición: " + resultado.getPosicion());
                        System.out.println("Transacción: " + resultado.getTransaccion());
                        System.out.println("\u001B[0m");
                    } else {
                        System.out.println("No se encontró ninguna transacción con el ID proporcionado.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el día de la semana a visualizar: ");
                    String diaVisualizar = scanner.next();
                    visualizarDatosDeDia(transaccionesPorDia, diaVisualizar);
                    break;
                case 5:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción válida.");
            }
        }
    }
    
    public static Map<String, List<Transaccion>> cargarDatos() {
        Map<String, List<Transaccion>> transaccionesPorDia = new HashMap<>();
        String[] diasSemana = { "lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo" };

        for (String dia : diasSemana) {
            List<Transaccion> transaccionesDia = new ArrayList<>();

            // Cargar el archivo desde la carpeta "src" del proyecto
            ClassLoader classLoader = App.class.getClassLoader();
            File file = new File(classLoader.getResource(dia + ".txt").getFile());

            try {
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] partes = line.split(";");
                        if (partes.length == 5) {
                            String ID = partes[0];
                            double amount = Double.parseDouble(partes[1]);
                            String tipoE = partes[2];
                            String tipoR = partes[3];
                            int hora = Integer.parseInt(partes[4]);

                            Transaccion transaccion = new App().new Transaccion(ID, amount, tipoE, tipoR, hora);
                            transaccionesDia.add(transaccion);

                        }
                    }
                    reader.close();
                } else {
                    System.out.println("No se encontraron datos para el día \n" + "[*] " + dia);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            transaccionesPorDia.put(dia, transaccionesDia);
        }
        System.out.println("\u001B[35m" );
        System.out.println("[*] archivos cargados");
        System.out.println("\u001B[0m");

        return transaccionesPorDia;
    }

    class Transaccion {
        String ID;
        double amount;
        String tipoE;
        String tipoR;
        int hora;

        public Transaccion(String ID, double amount, String tipoE, String tipoR, int hora) {
            this.ID = ID;
            this.amount = amount;
            this.tipoE = tipoE;
            this.tipoR = tipoR;
            this.hora = hora;
        }

        public Transaccion(String linea) {
            String[] partes = linea.split(";");
            if (partes.length >= 5) {
                this.ID = partes[0];
                this.amount = Double.parseDouble(partes[1]);
                this.tipoE = partes[2];
                this.tipoR = partes[3];
                this.hora = Integer.parseInt(partes[4]);
            }
        }
       public String formatTransaccion() {
    String formattedTransaccion = "\u001B[34m" // Color azul
            + "+---------------------+\n"
            + "\u001B[0m" // Restablece el color
            + "\u001B[35m" // Color rosa
            + "ID: " + "\u001B[32m" // Color verde
            + this.ID + "\u001B[35m" // Color rosa
            + "\n"
            + "Amount: " + "\u001B[32m" // Color verde
            + this.amount + "\u001B[0m" // Restablece el color
            + "\n"
            +
            "\u001B[0m" // Restablece el color
            + "\u001B[35m" // Color rosa
            + "Tipo Emisor: " + "\u001B[32m" // Color verde
            + this.tipoE + "\u001B[0m" // Restablece el color
            + "\n"
            +"\u001B[0m" // Restablece el color
            + "\u001B[35m" // Color rosa
            + "Tipo Receptor: " + "\u001B[32m" // Color verde
            + this.tipoR + "\u001B[0m" // Restablece el color
            + "\n"
            +"\u001B[0m" // Restablece el color
            + "\u001B[35m" // Color rosa
            + "Hora: " + "\u001B[32m" // Color verde
            + this.hora + "\n"
            +"\u001B[0m" // Restablece el color
            + "\u001B[35m" // Color rosa
            + "\u001B[34m" // Color azul
            + "+---------------------+\n"
            + "\u001B[0m"; // Restablece el color
    return formattedTransaccion;
}

    
        

        public String getId() {
            return ID;
        }

        public Double getAmount() {
            return amount;
        }

        public String getTipoEmisor() {
            return tipoE;
        }

        public String getTipoReceptor() {
            return tipoR;
        }

        public int getHora() {
            return hora;
        }
    }
     class ResultadoBusqueda {
        private String dia;
        private int posicion;
        private Transaccion transaccion;

        public ResultadoBusqueda(String dia, int posicion, Transaccion transaccion) {
            this.dia = dia;
            this.posicion = posicion;
            this.transaccion = transaccion;
        }

        public String getDia() {
            return dia;
        }

        public int getPosicion() {
            return posicion;
        }

        public Transaccion getTransaccion() {
            return transaccion;
        }
    }

     



    // Otras funciones para implementar los requisitos mencionados
    // ...

    // Función para encontrar el día de la semana con mayor cantidad de dinero movido
    // ...
    
    public static String obtenerDiaMayorCantidadDinero(Map<String, List<Transaccion>> datos) {
        String diaMayorCantidadDinero = null;
        double mayorCantidadDinero = 0.0;

        for (Map.Entry<String, List<Transaccion>> entry : datos.entrySet()) {
            String dia = entry.getKey();
            List<Transaccion> transacciones = entry.getValue();
            double cantidadDineroDia = 0.0;

            for (Transaccion transaccion : transacciones) {
                cantidadDineroDia += transaccion.getAmount();
            }

            if (cantidadDineroDia > mayorCantidadDinero) {
                mayorCantidadDinero = cantidadDineroDia;
                diaMayorCantidadDinero = dia;
            }
        }

        if (diaMayorCantidadDinero != null) {
        String formattedMessage = "\u001B[35m" // Color rosa
                + "El día de la semana con la mayor cantidad de dinero movido es: \n"
                + "[*] ----------------> \u001b[0m"+ "\u001B[32m"+ diaMayorCantidadDinero + "\u001b[0m"+"\u001B[35m"+" <------------------------[*]\n"
                + "\u001B[0m"; // Restablece el color
        return formattedMessage;
    } else {
        return "No se encontraron datos de transacciones.";
    }
    }

    public static int obtenerHoraPromedioMayorDinero(Map<String, List<Transaccion>> datos) {
    Map<Integer, Double> sumaDineroPorHora = new HashMap<>();
    Map<Integer, Integer> contadorPorHora = new HashMap<>();
    Map<Integer, String> diaPorHora = new HashMap<>();

    for (Map.Entry<String, List<Transaccion>> entry : datos.entrySet()) {
        String dia = entry.getKey();
        List<Transaccion> transacciones = entry.getValue();

        for (Transaccion transaccion : transacciones) {
            int hora = transaccion.getHora();
            double amount = transaccion.getAmount();

            sumaDineroPorHora.put(hora, sumaDineroPorHora.getOrDefault(hora, 0.0) + amount);
            contadorPorHora.put(hora, contadorPorHora.getOrDefault(hora, 0) + 1);
            diaPorHora.put(hora, dia);
        }
    }

    int horaPromedioMayorDinero = -1;
    double mayorPromedioDinero = 0.0;

    for (Map.Entry<Integer, Double> entry : sumaDineroPorHora.entrySet()) {
        int hora = entry.getKey();
        double sumaDinero = entry.getValue();
        int contador = contadorPorHora.get(hora);

        double promedioDinero = sumaDinero / contador;

        if (promedioDinero > mayorPromedioDinero) {
            mayorPromedioDinero = promedioDinero;
            horaPromedioMayorDinero = hora;
        }
    }
    System.out.println("\u001B[35m" );
    System.out.println("La hora promedio con la mayor cantidad de dinero es: \n" +"[*] " + horaPromedioMayorDinero);
    System.out.println("Promedio de cantidad de dinero: \n" + "[*] " +mayorPromedioDinero);
    
    String diaMayorDinero = diaPorHora.get(horaPromedioMayorDinero);
    System.out.println("El día correspondiente es: \n" + "[*] " +diaMayorDinero);
    System.out.println("\u001B[0m");
    return horaPromedioMayorDinero;
}

    
   public static ResultadoBusqueda buscarTransaccionPorID(Map<String, List<Transaccion>> datos, String id) {
    for (Map.Entry<String, List<Transaccion>> entry : datos.entrySet()) {
        String dia = entry.getKey();
        List<Transaccion> transacciones = entry.getValue();

        for (int i = 0; i < transacciones.size(); i++) {
            Transaccion transaccion = transacciones.get(i);
            if (transaccion.getId().equals(id)) {
                // Crear una instancia de ResultadoBusqueda directamente
                
                App appInstance = new App();
                ResultadoBusqueda resultado = appInstance.new ResultadoBusqueda(dia, i, transaccion);
                return resultado;
            }
        }
    }

    return null;
}


    public static void visualizarDatosDeDia(Map<String, List<Transaccion>> datos, String dia) {
    List<Transaccion> transacciones = datos.get(dia);

    if (transacciones != null) {
        System.out.println("\u001B[35m" );
        System.out.println("Transacciones del día " + dia + ":");
        for (Transaccion transaccion : transacciones) {
            String formattedTransaction = transaccion.formatTransaccion();
            System.out.println(formattedTransaction);
        }
        System.out.println("\u001B[0m");
    } else {
        System.out.println("No se encontraron datos para el día " + dia);
    }
}

    // Función para encontrar la hora del día con el promedio de dinero movido más alto
    // ...

    // Función para buscar una transacción por su ID
    // ...

    // Función para visualizar datos de un día específico
    // ...

    // Otras funciones para implementar los requisitos mencionados
    // ...}
}