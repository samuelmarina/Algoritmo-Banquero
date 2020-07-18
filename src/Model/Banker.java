package Model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Banker {

    int p = 6;  //Numero de procesos (sucursales)
    int r = 6;  //Numero de recursos (empleados)

    //Matrices
          //Matriz de requerimiento
//    int[][] max;                //Matriz de cantidad de empleados a necesitar
//    int[][] inicial;            //Matriz inicial (indicada por el usuario)
    
    //Matrices
    int[][] requerimiento;
    ArrayList<ArrayList<Integer>> max = new ArrayList<>();
    ArrayList<ArrayList<Integer>> inicial = new ArrayList<>();
    
    int[] disp; //Recursos disponibles
    int[] secuencia;
    
    ArrayList<String> sec = new ArrayList<>();
    
    ArrayList<String> sucursales = new ArrayList<>();
    ArrayList<String> empleados = new ArrayList<>();
 
    
    public Banker() {
        this.inicializarValores(p, r);
    }
    
    public void Test(){
        this.calcularRequerimiento();
        System.out.println(this.safety());
    }
    
    /**
     * Lista para obtener los nombres de las sucursales
     */
    public void inicializarSucursales(){
        String[] temp = {
            "Macaracuay",
            "Vizcaya",
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
        for (int i = 0; i < p; i++) {
            inicial.add(new ArrayList<>());
            max.add(new ArrayList<>());
            for (int j = 0; j < r; j++) {
                inicial.get(i).add(0);
                max.get(i).add(0);
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
    public String safety() {
        this.secuencia = new int[p];
        
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
                        if (requerimiento[i][j] > work[j]) break; 
//                        if(requerimiento.get(i).get(j) > work[j]) break;
                    }
                    
                    //Si esto se cumple, entonces se pudo cubrir la cantidad de empleados
                    //necesaria para que la sucursal entrara en funcionamiento
                    if (j == r) {
                        secuencia[count++] = i;
                        sec.add(sucursales.get(i));
                        visited[i] = true;
                        flag = false;
                        
                        //Actualizamos ahora la matriz de empleados disponibles
                        for (j = 0; j < r; j++) {
                            work[j] = work[j] + inicial.get(i).get(j);
                        }
                    }
                }
            }
            if (flag) break;
        }

        if (count < p) {
            return "Ocurrió un interbloqueo. No existe frecuencia que ofrezca solución";
        } else {
            String res = "Secuencia: ";
            for (int i = 0; i < p; i++) {
                res += this.sec.get(i);
                if (i != p - 1) {
                    res += " -> ";
                }
            }
            
            return res;
        }
    }
    
    /**
     * Calcula la matriz de requerimientos (max-inicial)
     */
    public void calcularRequerimiento() {
        this.requerimiento = new int[p][r];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < r; j++) {
                requerimiento[i][j] = max.get(i).get(j) - inicial.get(i).get(j);
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