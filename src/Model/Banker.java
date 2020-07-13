package Model;

import java.util.Arrays;

public class Banker {

    int p;  //Numero de procesos (sucursales)
    int r;  //Numero de recursos (empleados)

    //Matrices
    int[][] requerimiento;      //Matriz de requerimiento
    int[][] max;
    int[][] inicial;            //Matriz inicial (indicada por el usuario)

    int[] disp; //Recursos disponibles
    int[] secuencia;
    
    public Banker(int p, int r) {
        this.p = 5;
        this.r = 3;
        this.requerimiento = new int[p][r];
        this.secuencia = new int[p];
        this.inicializarValores();
        this.calcularRequerimiento();
        this.safety();
    }

    //Esta hay que cambiarla para colocar los valores segun
    //indique el usuario
    
    public void inicializarValores() {
        inicial = new int[][]{{0, 1, 0}, //P0    
        {2, 0, 0}, //P1 
        {3, 0, 2}, //P2 
        {2, 1, 1}, //P3 
        {0, 0, 2}};

        max = new int[][]{{7, 5, 3}, //P0 
        {3, 2, 2}, //P1 
        {9, 0, 2}, //P2 
        {2, 2, 2}, //P3  
        {4, 3, 3}};

        disp = new int[]{3, 3, 2};
    }
    /**
     * Algoritmo de seguridad que indica si existe
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
                        if (requerimiento[i][j] > work[j]) break; 
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
     * Calcula la matriz de requerimientos
     */
    public void calcularRequerimiento() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < r; j++) {
                requerimiento[i][j] = max[i][j] - inicial[i][j];
            }
        }
    }

}