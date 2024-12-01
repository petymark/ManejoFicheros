import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionCoches {

    private static final String FICHERO_DAT = "coches.dat";
    private static final String FICHERO_CSV = "coches.csv";
    private static ArrayList<Coche> listaCoches;

    public static void main(String[] args) {
        listaCoches = cargarDatos();
        Scanner scanner = new Scanner(System.in);

        int opcion;
        do {
            System.out.println("-- Menú --");
            System.out.println("1. Añadir nuevo coche");
            System.out.println("2. Borrar coche por ID");
            System.out.println("3. Consultar coche por ID");
            System.out.println("4. Listar todos los coches");
            System.out.println("5. Terminar el programa");
            System.out.println("6. Exportar coches a archivo CSV");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> anadirCoche(scanner);
                case 2 -> borrarCoche(scanner);
                case 3 -> consultarCoche(scanner);
                case 4 -> listarCoches();
                case 5 -> terminarPrograma();
                case 6 -> exportarACSV();
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        scanner.close();
    }

    private static ArrayList<Coche> cargarDatos() {
        File fichero = new File(FICHERO_DAT);
        if (fichero.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
                return (ArrayList<Coche>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar los datos: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private static void anadirCoche(Scanner scanner) {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (listaCoches.stream().anyMatch(c -> c.getId() == id)) {
            System.out.println("Error: Ya existe un coche con este ID.");
            return;
        }

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        if (listaCoches.stream().anyMatch(c -> c.getMatricula().equals(matricula))) {
            System.out.println("Error: Ya existe un coche con esta matrícula.");
            return;
        }

        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();

        listaCoches.add(new Coche(id, matricula, marca, modelo, color));
        System.out.println("Coche añadido correctamente.");
    }

    private static void borrarCoche(Scanner scanner) {
        System.out.print("ID del coche a borrar: ");
        int id = scanner.nextInt();

        if (listaCoches.removeIf(c -> c.getId() == id)) {
            System.out.println("Coche eliminado correctamente.");
        } else {
            System.out.println("No se encontró un coche con ese ID.");
        }
    }

    private static void consultarCoche(Scanner scanner) {
        System.out.print("ID del coche a consultar: ");
        int id = scanner.nextInt();

        listaCoches.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        coche -> System.out.println(coche),
                        () -> System.out.println("No se encontró un coche con ese ID.")
                );
    }

    private static void listarCoches() {
        if (listaCoches.isEmpty()) {
            System.out.println("No hay coches en el almacén.");
        } else {
            listaCoches.forEach(System.out::println);
        }
    }

    private static void terminarPrograma() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHERO_DAT))) {
            oos.writeObject(listaCoches);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
        System.out.println("Programa terminado.");
    }

    private static void exportarACSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHERO_CSV))) {
            writer.println("ID;Matrícula;Marca;Modelo;Color");
            for (Coche coche : listaCoches) {
                writer.printf("%d;%s;%s;%s;%s%n", coche.getId(), coche.getMatricula(), coche.getMarca(),
                        coche.getModelo(), coche.getColor());
            }
            System.out.println("Datos exportados a CSV correctamente.");
        } catch (IOException e) {
            System.out.println("Error al exportar a CSV: " + e.getMessage());
        }
    }
}

