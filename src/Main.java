import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Memoria memoria = null;
        Scanner in = new Scanner(System.in);

        System.out.print("Inicialice el tamaño de la memoria: ");
        int size = in.nextInt();
        memoria = new Memoria(size);
        memoria.showZones();
        while (true) {
            System.out.print("Listado de opcciones: \n");
            System.out.println(" 1. Solicitar espacio\n 2. Recuperar espacio\n 3. Muestrar el estado de la partición\n");
            System.out.print("Por favor seleccione una: ");

            size = in.nextInt();
            switch (size) {
                case 1:
                    System.out.print("Ingrese la cantidad de espacio que necesita desea solicitar: ");
                    size = in.nextInt();
                    memoria.allocation(size);
                    break;
                case 2:
                    System.out.print("Ingrese el número de la partición a reciclar: ");
                    size = in.nextInt();
                    memoria.collection(size);
                    break;
                case 3:
                    memoria.showZones();
                    break;
                default:
                    System.out.println("Por favor seleccione una opcción!");
            }
        }
    }
}