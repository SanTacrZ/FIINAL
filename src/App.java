
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.text.DecimalFormat;

public class App {
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
        System.out.println("\u001B[35m");
        System.out.println("Información de la transacción encontrada:\n[+]");
        System.out.println("Día: " + resultado.getDia());
        System.out.println("Posición: " + resultado.getPosicion());
        System.out.println("Transacción: " + resultado.getTransaccion());
        System.out.println("\u001B[0m");
        
        // Obtener el tipo de emisor y receptor de la transacción
        String tipoEmisor = resultado.getTransaccion().getTipoEmisor();
        String tipoReceptor = resultado.getTransaccion().getTipoReceptor();

        // Calcular el recargo para el tipo de emisor y receptor
        double recargo = calcularRecargoPorTipoEmisorYReceptor(tipoEmisor, tipoReceptor);

        // Calcular el monto con recargo
        double surchargedAmount = resultado.getTransaccion().getAmount() * recargo;
        
        // Formatear el monto con dos decimales
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedAmount = decimalFormat.format(surchargedAmount);

        // Mostrar el monto con recargo formateado
        System.out.println("Monto con recargo: " + formattedAmount);
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

        public void setAmount(double amount) {
            this.amount = amount;
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

    public static Map<String, List<Transaccion>> calcularRecargos(Map<String, List<Transaccion>> transacciones) {
        Map<String, List<Transaccion>> transaccionesConRecargo = new HashMap<>();

        for (Map.Entry<String, List<Transaccion>> entry : transacciones.entrySet()) {
            String dia = entry.getKey();
            List<Transaccion> transaccionesDia = entry.getValue();

            for (Transaccion transaccion : transaccionesDia) {
                double recargo = 1.0;

                int tipoEmisor = Integer.parseInt(transaccion.getTipoEmisor());
                if (tipoEmisor == 2) {
                    recargo += 0.1; // 10% surcharge for sender type 2
                } else if (tipoEmisor == 1) {
                    recargo += 0.15; // 15% surcharge for sender type 1
                }

                int tipoReceptor = Integer.parseInt(transaccion.getTipoReceptor());
                if (tipoReceptor == 2) {
                    recargo += 0.05; // 5% surcharge for receiver type 2
                } else if (tipoReceptor == 1) {
                    recargo += 0.1; // 10% surcharge for receiver type 1
                }

                double surchargedAmount = Math.round((transaccion.getAmount() * recargo) * 100.0) / 100.0;
                transaccion.setAmount(surchargedAmount);
            }

            transaccionesConRecargo.put(dia, transaccionesDia);
        }

        return transaccionesConRecargo;
    }
public static double calcularRecargoPorTipoEmisorYReceptor(String tipoEmisor, String tipoReceptor) {
    double recargo = 1.0;

    int tipoEmisorInt = Integer.parseInt(tipoEmisor);
    int tipoReceptorInt = Integer.parseInt(tipoReceptor);

    // Aplica recargos en función de los tipos de emisor y receptor
    if (tipoEmisorInt == 2) {
        recargo += 0.1; // 10% de recargo para el tipo de emisor 2
    } else if (tipoEmisorInt == 1) {
        recargo += 0.15; // 15% de recargo para el tipo de emisor 1
    }

    if (tipoReceptorInt == 2) {
        recargo += 0.05; // 5% de recargo para el tipo de receptor 2
    } else if (tipoReceptorInt == 1) {
        recargo += 0.1; // 10% de recargo para el tipo de receptor 1
    }

    return recargo;
}

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
}