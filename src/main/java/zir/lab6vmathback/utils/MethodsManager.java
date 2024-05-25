package zir.lab6vmathback.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MethodsManager {

    public Map<Integer, Function<HashMap<String, Function<BigDecimal[], BigDecimal>>, String>> methodsMap = new HashMap<>();



    public MethodsManager() {
        methodsMap.put(1, this::improvedEulerMethod);
        methodsMap.put(2, this::rungeKuttaOfThe4thOrderMethod);
        methodsMap.put(3, this::milnesMethod);

    }


    private String improvedEulerMethod(HashMap<String, Function<BigDecimal[], BigDecimal>> args) {
        return "dsfsdfdsfsddfsdf";
    }

    private String rungeKuttaOfThe4thOrderMethod(HashMap<String, Function<BigDecimal[], BigDecimal>> args) {
        return args.toString();
    }

    private String milnesMethod(HashMap<String, Function<BigDecimal[], BigDecimal>> args) {
        return args.toString();
    }
}
