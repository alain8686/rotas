import java.util.AbstractMap;
import java.util.HashMap;

abstract class Runnable{
    HashMap<AbstractMap.SimpleEntry<String, String>, Double> graph;
    public Runnable(HashMap<AbstractMap.SimpleEntry<String, String>, Double> graph){
        this.graph = graph;
    }
    abstract Double distance(String origin, String destination);
}
class DisntanceEdgeMethod extends Runnable{
    public DisntanceEdgeMethod(HashMap<AbstractMap.SimpleEntry<String, String>, Double> graph){
        super(graph);
    }

    @Override
    public Double distance(String origin, String destination){
        AbstractMap.SimpleEntry<String, String> key = CityPathGenerator.order_two_cities(new String[]{origin, destination});
        return this.graph.get(key);
    }
}
class DistanceCountMethod extends Runnable{
    public DistanceCountMethod(HashMap<AbstractMap.SimpleEntry<String, String>, Double> graph){
        super(graph);
    }

    @Override
    public Double distance(String origin, String destination){
        AbstractMap.SimpleEntry<String, String> key = CityPathGenerator.order_two_cities(new String[]{origin, destination});
        return (this.graph.containsKey(key))? 1.0: null;
    }
}
class CustMethod extends Runnable{
    public CustMethod(HashMap<AbstractMap.SimpleEntry<String, String>, Double> graph){
        super(graph);
    }

    @Override
    public Double distance(String origin, String destination){
        AbstractMap.SimpleEntry<String, String> key = CityPathGenerator.order_two_cities(new String[]{origin, destination});
        return this.graph.get(key) != null? this.graph.get(key) * 6.95 / 10: null;
    }
}
