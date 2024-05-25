package zir.lab6vmathback.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SolvingODEHandler {
    private final DifferentialEquationsManger differentialEquationsManger = new DifferentialEquationsManger();
    private final MethodsManager methodsManager = new MethodsManager();


    @Getter
    @Setter
    private int equation;

    public SolvingODEHandler() {
    }

    public String getSolvingByMethod(BigDecimal leftBorderX, BigDecimal rightBorderX, BigDecimal step, int method) {
        HashMap<String, Function<BigDecimal[], BigDecimal>> argsMap = new HashMap<>();
        argsMap.put("diffEquation", x->differentialEquationsManger.diffEquationsMap.get(equation).apply(x));
        System.out.println(methodsManager.methodsMap.get(method).apply(argsMap));

        return "fofofof";
    }


}
