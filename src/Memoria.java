import java.util.LinkedList;
import java.util.Scanner;

public class Memoria{
    /**
     * tamaño de la memoria
     */
    private final int size;

    /**
     * Tamaño mínimo de partición restante
     */
    private static final int MIN_SIZE = 5;

    /**
     * Partición de memoria
     */
    private final LinkedList<Zone> zones;

    /**
     * La ubicación del área libre asignada por última vez
     */
    private int pointer;

    /**
     * Clase de nodo de partición
     */
    static class Zone{
        //Tamaño de la partición
        private int size;

        //Dirección de inicio de la partición
        private final int head;

        //Estado inactivo
        private boolean isFree;

        public Zone(int head, int size) {
            this.head = head;
            this.size = size;
            this.isFree = true;
        }
    }

    public Memoria(int size) {
        this.size = size;
        this.pointer = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(0, size));
    }

    /**
     * Asignación de memoria
     * @param  size especifica el tamaño que se asignará
     */
    public void allocation(int size){
        System.out.println("\n1.Primer ajuste 2.Siguiente ajuste 3.Mejor ajuste 4.Peor ajuste");
        System.out.print("Seleccione un algoritmo de asignación: \n");
        Scanner in = new Scanner(System.in);
        int algorithm = in.nextInt();
        switch (algorithm){
            case 1:
                primerAjuste(size);break;
            case 2:
                siguienteAjuste(size);break;
            case 3:
                mejorAjuste(size);break;
            case 4:
                peorAjuste(size);break;
            default:
                System.out.println("¡Por favor, elija de nuevo!");
        }
    }

    /**
     * Primer algoritmo de adaptación
     * @param  size especifica el tamaño que se asignará
     */
    private void primerAjuste(int size){
        // Recorre la lista de particiones
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            // Encuentra la partición disponible (tamaño libre y suficiente)
            if (tmp.isFree && (tmp.size > size)){
                doAllocation(size, pointer, tmp);
                return;
            }
        }
        // Si no se encuentra la partición libre después del recorrido, la asignación de memoria falla
        System.out.println("¡No hay espacio de memoria disponible!");
    }

    /**
     * Algoritmo de adaptación del primer bucle
     * @param  size especifica el tamaño que se asignará
     */
    private void siguienteAjuste(int size){
        // Recorre la lista de particiones comenzando desde la última ubicación de área libre asignada
        Zone tmp = zones.get(pointer);
        if (tmp.isFree && (tmp.size > size)){
            doAllocation(size, pointer, tmp);
            return;
        }
        int len = zones.size();
        int i = (pointer + 1) % len;
        for (; i != pointer; i = (i+1) % len){
            tmp = zones.get(i);
            // Encuentra la partición disponible (tamaño libre y suficiente)
            if (tmp.isFree && (tmp.size > size)){
                doAllocation(size, i, tmp);
                return;
            }
        }
        // Si no se encuentra la partición libre después del recorrido, la asignación de memoria falla
        System.out.println("¡No hay espacio de memoria disponible!");
    }

    /**
     * Mejor algoritmo de adaptación
     * @param  size especifica el tamaño que se asignará
     */
    private void mejorAjuste(int size){
        int flag = -1;
        int min = this.size;
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            if (tmp.isFree && (tmp.size > size)){
                if (min > tmp.size - size){
                    min = tmp.size - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1){
            System.out.println("¡No hay espacio de memoria disponible!");
        }else {
            doAllocation(size, flag, zones.get(flag));
        }
    }

    /**
     * Peor algoritmo de adaptación
     * @param  size especifica el tamaño que se asignará
     */
    private void peorAjuste(int size){
        int flag = -1;
        int max = 0;
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            if (tmp.isFree && (tmp.size > size)){
                if (max < tmp.size - size){
                    max = tmp.size - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1){
            System.out.println("¡No hay espacio de memoria disponible!");
        }else {
            doAllocation(size, flag, zones.get(flag));
        }
    }

    /**
     * Realizar distribución
     * @param  size tamaño de la aplicación
     * @param  location Ubicación actual de la partición disponible
     * @param  tmp zona libre disponible
     */
    private void doAllocation(int size, int location, Zone tmp) {
        // Si el tamaño restante de la partición es demasiado pequeño (MIN_SIZE) después de la división, se asignarán todas las particiones; de lo contrario, se dividirán en dos particiones
        if(tmp.size - size <= MIN_SIZE){
            tmp.isFree = false;
        } else {
            Zone split = new Zone(tmp.head + size, tmp.size - size);
            zones.add(location + 1, split);
            tmp.size = size;
            tmp.isFree = false;
        }
        System.out.println("Asignado correctamente " + size + " KB");
    }

    /**
     * Reciclaje de memoria
     * @param  id especifica el número de la partición que se reciclará
     */
    public void collection(int id){
        if (id >= zones.size()){
            System.out.println("¡No hay tal número de partición!");
            return;
        }
        Zone tmp = zones.get(id);
        int size = tmp.size;
        if (tmp.isFree) {
            System.out.println("La partición especificada no está asignada y no necesita ser reciclada");
            return;
        }
        // Si la partición recuperada no es la partición final y la última partición está libre, fusiona con la última partición
        if (id < zones.size() - 1 && zones.get(id + 1).isFree){
            Zone next = zones.get(id + 1);
            tmp.size += next.size;
            zones.remove(next);
        }
        // Si la partición de recuperación no es la primera partición y la partición anterior está libre, fusionar con la partición anterior
        if (id > 0 && zones.get(id - 1).isFree){
            Zone previous = zones.get(id - 1);
            previous.size += tmp.size;
            zones.remove(id);
            id--;
        }
        zones.get(id).isFree = true;
        System.out.println("¡Memoria recuperada con éxito! Esta vez recuperados " + size + " KB");
    }

    /**
     * Mostrar el estado de la partición de memoria
     */
    public void showZones(){
        System.out.println("------------------------------------");
        System.out.println("Número de partición - dirección de inicio de la partición - tamaño de la partición - estado de orden \t");
        System.out.println("------------------------------------");
        for (int i = 0; i < zones.size(); i++){
            Zone tmp = zones.get(i);
            System.out.println(i + " \t\t " + tmp.head + " \t\t " + tmp.size + "  \t\t " + tmp.isFree);
        }
        System.out.println("------------------------------------");
    }
}