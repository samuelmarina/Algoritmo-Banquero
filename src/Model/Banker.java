package Model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Banker {

    public int p = 6;  //Numero de procesos (sucursales)
    public int r = 6;  //Numero de recursos (empleados)
    
    //Matrices
    public int[][] requerimiento;
    public ArrayList<ArrayList<Integer>> max = new ArrayList<>();
//    max.get(numDeLaSuc).set(numDelEmp, numAPasar)
    public ArrayList<ArrayList<Integer>> inicial = new ArrayList<>();
    
    public int[] disp; //Recursos disponibles
    public int[] secuencia;
    
    public ArrayList<String> sec = new ArrayList<>();
    
    public ArrayList<String> sucursales = new ArrayList<>();
    public ArrayList<String> empleados = new ArrayList<>();
 
    
    public Banker() {
        this.inicializarValores(p, r);
    }

     /**
     * Organiza la matriz inicial
     * @param area TextArea que se mostrara
     */
    
    public void PrintIniciales(JTextArea area){
        String text = "";
        for (int i = 0; i < r; i++) {
            text += "\t" + this.empleados.get(i).charAt(0);
        }

        text += "\n";
        for (int i = 0; i < p; i++) {
            text += "\n" + this.sucursales.get(i).charAt(0);
            for (int j = 0; j < r; j++) {
                text += "\t" + inicial.get(i).get(j);
            }

            text += "\t" + "\n";
        }
        
        area.setText(text);
    }
    
     /**
     * Organiza la matriz de requeridos
     * @param area TextArea que se mostrara
     */
    
    public void PrintRequerido(JTextArea area){
        String text = "";
        for (int i = 0; i < r; i++) {
            text += "\t" + this.empleados.get(i).charAt(0);
        }
        text += "\n";
        for (int i = 0; i < p; i++) {
            text += "\n" + this.sucursales.get(i).charAt(0);
            for (int j = 0; j < r; j++) {
                text += "\t" + max.get(i).get(j);
            }
            text += "\t" + "\n";
        }
        
        area.setText(text);
    }
    
     /**
     * Modifica la sucursal segun input de usuario
     * @param area TextArea que se modificara
     * @param box ComboBox con eleccion del usuario
     * @param matrz matriz a editar
     */
    
    public void editarMatrizPanel(JTextArea area, JComboBox box, ArrayList matriz){
        int sucursal = box.getSelectedIndex()-1;
        this.empleados.forEach((e) -> {
            String respuesta = JOptionPane.showInputDialog("Ingrese cantidad de empleados de " + e +": ");
            if((respuesta) != ""){
                this.editarMatriz(matriz, sucursal, this.empleados.indexOf(e), Integer.parseInt(respuesta));}
        });
        this.PrintRequerido(area);
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
     * @return la secuencia, en caso de existir. De lo contrario,
     * interbloqueo
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
    
    /**
     * Agregar una nueva sucursal a las matrices
     * @param name el nombre de la sucursal
     */
    
    public void agregarSucursal(String name){
        this.p += 1;
        this.sucursales.add(name);
        this.inicial.add(new ArrayList<>());
        this.max.add(new ArrayList<>());
        for (int i = 0; i < r; i++) {
            inicial.get(inicial.size()-1).add(0);
            max.get(max.size()-1).add(0);
        }
    }
    
    /**
     * Editar una posición específica en la matriz
     * @param matriz matriz a modificar
     * @param x pos. x en la matriz sucursales
     * @param y pos. y en la matriz empleados
     * @param value valor nuevo a sustituir
     */
    public void editarMatriz(ArrayList<ArrayList<Integer>> matriz, int x, int y, int value){
        matriz.get(x).set(y, value);
    }
    
    /**
     * Ejecuta el algoritmo del banquero
     * @return el resultado del algoritmo (secuencia o interbloqueo)
     */
    public String mostrarSecuencia(){
        calcularRequerimiento();
        return safety();
    }

}