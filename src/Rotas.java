import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.Exception;
import java.util.ArrayList;

public class Rotas {
    public static void main(String[] args) throws Exception{
        CityPathGenerator path_generator = new CityPathGenerator();
        path_generator.loadFromFile("rotas.txt");

        ArrayList<String> path1 = path_generator.shortest_jump_number("Porto Alegre", "Gramado");
        ArrayList<String> path2 = path_generator.shortest_distance_km("Porto Alegre", "Gramado");
        ArrayList<Double> distance_cust = path_generator.calculate_distance_cust("Porto Alegre", "Gramado");
        ArrayList<ArrayList<String>> paths = path_generator.generate_all_paths("Porto Alegre", "Gramado");

        BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"));
        bw.write(String.format("A caminho com menor saltos é %s \n", String.join(" , ", path1)));
        bw.write(String.format("A caminho com menor distança é %s \n", String.join(" , ", path2)));
        bw.write(String.format("Custo melhor ruta: %f, Custo segunda melhor ruta %f, Distança melhor ruta %f, Distança segunda ruta %f \n",
                distance_cust.get(0), distance_cust.get(2), distance_cust.get(1), distance_cust.get(3)));

        for(ArrayList<String> path: paths){
            bw.write(String.format("Caminho: %s \n", String.join(" , ", path)));
        }
        
        bw.close();
    }
}
