import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

public class CityPathGenerator {
    HashMap<SimpleEntry<String, String>, Double> graph = new HashMap<>();
    ArrayList<String> cities = new ArrayList<>();

    public static SimpleEntry<String, String> order_two_cities(String[] cities){
        String city1 = cities[0].compareTo(cities[1]) < 0? cities[0]: cities[1];
        String city2 = cities[0].compareTo(cities[1]) < 0? cities[1]: cities[0];

        return new SimpleEntry(city1, city2);
    }

    private Double dfs_path(String destination, String current,
                            HashMap<String, Boolean> non_visited_cities,
                            HashMap<String, Double> distance_destination,
                            HashMap<String, String> using_destination,
                            Runnable runnable) throws Exception{
        if(destination.equals(current))
            return 0.0;

        Double result = null;
        for(String non_visited_city: non_visited_cities.keySet()){
            SimpleEntry<String, String> pair = order_two_cities(new String[]{current, non_visited_city});
            if(this.graph.containsKey(pair)){
                Double d = runnable.distance(non_visited_city, current);
                if(!non_visited_cities.get(non_visited_city)){
                    if(!non_visited_city.equals(destination))
                        non_visited_cities.put(non_visited_city, true);
                    Double _result = this.dfs_path(destination, non_visited_city, non_visited_cities,
                            distance_destination, using_destination, runnable);

                    if(_result != null && result == null){
                        result = _result + d;
                        using_destination.put(current, non_visited_city);
                    }
                    else if(_result != null){
                        if(_result + d < result){
                            using_destination.put(current, non_visited_city);
                            result = _result + d;
                        }
                    }
                }
                else if(distance_destination.get(non_visited_city) != null){
                    Double _result = distance_destination.get(non_visited_city);
                    if(_result + d < result){
                        using_destination.put(current, non_visited_city);
                        result = _result + d;
                    }
                }
            }
        }
        distance_destination.put(current, result);
        return result;
    }

    public ArrayList<String> shortest_jump_number(String source, String destination) throws Exception{
        Runnable runnable = new DistanceCountMethod(this.graph);
        return this.shortest_path(source, destination, runnable);
    }

    public ArrayList<String> shortest_distance_km(String source, String destination) throws Exception{
        Runnable runnable = new DisntanceEdgeMethod(this.graph);
        return this.shortest_path(source, destination, runnable);
    }

    private ArrayList<String> shortest_path(String source, String destination, Runnable runnable) throws Exception{
        HashMap<String, Boolean> non_visited_cities = new HashMap<>();
        HashMap<String, Double> distance_destination = new HashMap<>();
        HashMap<String, String> using_destination = new HashMap<>();
        for(String city: this.cities){
            if(!city.equals(source)){
                non_visited_cities.put(city, false);
                distance_destination.put(city, null);
                using_destination.put(city, null);
            }
        }
        distance_destination.put(destination, 0.0);
        Double result = this.dfs_path(destination, source, non_visited_cities, distance_destination, using_destination,
                runnable);
        if(result == null)
            throw new Exception("Não existe caminho");
        return this.shorted_path(source, using_destination, destination);
    }

    public ArrayList<ArrayList<String>> generate_all_paths(String source, String destination) throws Exception{
        Runnable runnable_cust = new CustMethod(this.graph);
        Runnable runnable_distance = new DisntanceEdgeMethod(this.graph);
        ArrayList<ArrayList<String>> paths = this.two_shortest_path(source, destination, runnable_distance,
                this.cities.size() * this.cities.size());
        return paths;
    }

    public ArrayList<Double> calculate_distance_cust(String source, String destination) throws Exception{
        Runnable runnable_cust = new CustMethod(this.graph);
        Runnable runnable_distance = new DisntanceEdgeMethod(this.graph);
        ArrayList<ArrayList<String>> paths = this.two_shortest_path(source, destination, runnable_distance, 2);
        ArrayList<Double> result = new ArrayList<>();

        for(ArrayList<String> path: paths) {
            Double best_distance = 0.0;
            Double best_cust = 0.0;
            for (int i = 0; i < path.size() - 1; i++) {
                best_distance += runnable_distance.distance(path.get(i), path.get(i + 1));
                best_cust += runnable_cust.distance(path.get(i), path.get(i + 1));
            }
            result.add(best_distance);
            result.add(best_cust);
        }

        return result;
    }

    private ArrayList<ArrayList<String>> two_shortest_path(String source, String destination,
                                                                             Runnable runnable, int count) throws Exception{
        HashMap<String, Boolean> non_visited_cities = new HashMap<>();
        HashMap<String, Double> distance_destination = new HashMap<>();
        HashMap<String, String> using_destination = new HashMap<>();
        for(String city: this.cities){
            if(!city.equals(source)){
                non_visited_cities.put(city, false);
                distance_destination.put(city, null);
                using_destination.put(city, null);
            }
        }
        distance_destination.put(destination, 0.0);
        Double result = this.dfs_path(destination, source, non_visited_cities, distance_destination, using_destination,
                runnable);
        if(result == null)
            throw new Exception("Não existe caminho");

        ArrayList<ArrayList<String>> ordered_result = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            ArrayList<String> best_path = this.shorted_path(source, using_destination, destination);
            ordered_result.add(best_path);

            Double new_result = this.two_shorted_path(best_path, 0, destination, distance_destination,
                    using_destination, result, 0.0, runnable);

            result = new_result;
            if(new_result == Double.MAX_VALUE)
                break;
        }

        return ordered_result;
    }
    private Double two_shorted_path(ArrayList<String> result_list, int index, String destination, HashMap<String,
            Double> distance_destination, HashMap<String, String> using_destination, Double result,
                                    Double accum, Runnable runnable){
        if(result_list.size() - 1 == index){
            if(result_list.get(0).equals(destination))
                return accum;
            else
                return Double.MAX_VALUE;
        }

        Double val = Double.MAX_VALUE;
        String pivot = result_list.get(index);
        for (String city : this.cities) {
            Double d = runnable.distance(pivot, city);
            if(distance_destination.get(city) == null || d == null || result_list.contains(city))
                continue;
            else if(result < accum + distance_destination.get(city) + d && accum + distance_destination.get(city) < val){
                val = distance_destination.get(city) + d + accum;
                using_destination.put(pivot, city);
                distance_destination.put(pivot, distance_destination.get(city) + d);
            }
        }

        Double delta = runnable.distance(result_list.get(index), result_list.get(index + 1));
        Double _val = this.two_shorted_path(result_list, index + 1, destination, distance_destination, using_destination,
                result, accum + delta, runnable);
        if(_val < val){
            val = _val;
            using_destination.put(pivot, result_list.get(index + 1));
            distance_destination.put(pivot, distance_destination.get(result_list.get(index + 1)) + delta);
        }
        return val;
    }

    private ArrayList<String> shorted_path(String pivot, HashMap<String, String> using_destination, String destination){
        ArrayList<String> result_list = new ArrayList<>();
        result_list.add(pivot);
        while(!pivot.equals(destination)) {
            String travel_to = using_destination.get(pivot);
            result_list.add(travel_to);
            pivot = travel_to;
        }
        return result_list;
    }

    public void loadFromFile(String path) throws Exception{
        this.graph = new HashMap<>();
        this.cities = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));

        String edge_str;
        while ((edge_str = br.readLine()) != null) {
            int index = edge_str.lastIndexOf(' ');

            String cities_str = edge_str.substring(0, index);
            double distance = new Double(edge_str.substring(index));

            String[] _cities = cities_str.split("-");
            String[] c = new String[_cities.length];
            for(int i = 0; i < _cities.length; i++){
                c[i] = _cities[i].trim();
            }
            _cities = c;

            SimpleEntry<String, String> entry = order_two_cities(_cities);
            this.graph.put(entry, distance);

            if(!this.cities.contains(entry.getKey()))
                this.cities.add(entry.getKey());
            if(!this.cities.contains(entry.getValue()))
                this.cities.add(entry.getValue());
        }
    }
}

