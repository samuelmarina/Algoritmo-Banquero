package Model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Banker {

    int p = 6;  //Numero de procesos (sucursales)
    int r = 6;  //Numero de recursos (empleados)

    //Matrices
//    int[][] requerimiento;      //Matriz de requerimiento
//    int[][] max;                //Matriz de cantidad de empleados a necesitar
//    int[][] inicial;            //Matriz inicial (indicada por el usuario)
    
    //Matrices
    ArrayList<ArrayList<Integer>> requerimiento = new ArrayList<>();
    ArrayList<ArrayList<Integer>> max = new ArrayList<>();
    ArrayList<ArrayList<Integer>> inicial = new ArrayList<>();
    
    int[] disp; //Recursos disponibles
    int[] secuencia;    //Cambiar a lista
    
    ArrayList<String> sucursales = new ArrayList<>();
    ArrayList<String> empleados = new ArrayList<>();
 
    
    public Banker() {
//        this.requerimiento = new int[p][r];
//        this.secuencia = new int[p];
        this.inicializarValores(p, r);
    }
    
    /**
     * Lista para obtener los nombres de las sucursales
     */
    public void inicializarSucursales(){
        String[] temp = {
            "Macaracuay",
            "Vizcara",
            "Las Mercedes",
            "Santa Paula",
            "Chuao",
            "Caurimare"
        };
        
        sucursales.addAll(Arrays.asList(temp));
    }
    
    /**
     * Lista para obtener los tipos de empleados
     */
    public void inicializarEmpleados(){
        String[] temp = {
            "Cajeros",
            "Seguridad",
            "Empaquetamiento",
            "Limpieza",
            "Gerencia",
            "Abastecimiento"
        };
        
        empleados.addAll(Arrays.asList(temp));
    }

    /**
     * Inicializa las dos matrices con ceros
     * @param p cantidad de procesos (sucursales)
     * @param r cantidad de recursos (empleados)
     */
    public void inicializarValores(int p, int r) {
//        inicial = new int[p][r];
//        max = new int[p][r];
//        
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < r; j++) {
                inicial.get(i).set(j, 0);
                max.get(i).set(j, 0);
            }
        }
        
        disp = new int[r];
        Arrays.fill(disp, 0);
        
        this.inicializarSucursales();
        this.inicializarEmpleados();
    }
    /**
     * Algoritmo del banquero que indica si existe
     * una secuencia segura para evitar un interbloqueo
     */
    public void safety() {
        int count = 0;

        //Inicializamos el arreglo indicando que no se ha
        //visitado ninguna sucursal
        boolean[] visited = new boolean[p];
        Arrays.fill(visited, false);

        //Arreglo que guarda una copia de los empleados
        //disponibles
        int[] work = new int[r];
        for (int i = 0; i < r; i++) {
            work[i] = disp[i];
        }

        //Bucle para recorrer todas las sucursales hasta que se haya
        //conseguido recorrerlas todas o se haya generado un interbloqueo
        while (count < p) {
            boolean flag = true;   //Indica si hay interbloqueo

            for (int i = 0; i < p; i++) {
                //Si la sucursal no ha sido visitada, entonces entramos
                //a ver si hay suficientes trabajadores
                if (!visited[i]) {
                    int j;
                    for (j = 0; j < r; j++) {
                        //En caso de que la cantidad de empleados necesaria para que
                        //la sucursal funcione es menor que los que se encuentran,
                        //entonces ocurrió un interbloqueo
//                        if (requerimiento[i][j] > work[j]) break; 
                        if(requerimiento.get(i).get(j) > work[j]) break;
                    }
                    
                    //Si esto se cumple, entonces se pudo cubrir la cantidad de empleados
                    //necesaria para que la sucursal entrara en funcionamiento
                    if (j == r) {
                        secuencia[count++] = i;
                        visited[i] = true;
                        flag = false;
                        
                        //Actualizamos ahora la matriz de empleados disponibles
                        for (j = 0; j < r; j++) {
                            work[j] = work[j] + inicial[i][j];
                        }
                    }
                }
            }
            if (flag) break;
        }

        if (count < p) {
            System.out.println("Unsafe");
        } else {
            System.out.println("Safe");
            for (int i = 0; i < p; i++) {
                System.out.print("P" + secuencia[i]);
                if (i != p - 1) {
                    System.out.println(" -> ");
                }
            }
        }
    }
    
    /**
     * Calcula la matriz de requerimientos (max-inicial)
     */
    public void calcularRequerimiento() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < r; j++) {
                requerimiento[i][j] = max[i][j] - inicial[i][j];
            }
        }
    }
    
    /**
     * Agregar un empleado en específico
     * @param pos la posición del empleado a aumentar
     */
    public void agregarEmpleado(int pos){
        disp[pos] += 1;
    }
    
    /**
     * Eliminar un empleado en específico
     * @param pos la posición del empleado a restar
     */
    public void eliminarEmpleado(int pos){
        disp[pos] -= 1;
    }

}