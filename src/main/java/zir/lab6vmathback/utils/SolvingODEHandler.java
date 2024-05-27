package zir.lab6vmathback.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static zir.lab6vmathback.utils.Config.*;

public class SolvingODEHandler {
    private final MethodsManager methodsManager = new MethodsManager();


    @Getter
    @Setter
    private int equation;

    public SolvingODEHandler() {
    }


    public String getSolvingByMethod(BigDecimal leftBorderX, BigDecimal yInLeftBorder, BigDecimal rightBorderX, BigDecimal step, BigDecimal inaccuracy, int method) {
        String res = "";
        HashMap<String, BigDecimal> argsMap = new HashMap<>();
        argsMap.put(diffEquation.name(), BigDecimal.valueOf(equation));
        argsMap.put(Config.leftBorderX.name(), leftBorderX);
        argsMap.put(Config.yInLeftBorder.name(), yInLeftBorder);
        argsMap.put(Config.rightBorderX.name(), rightBorderX);
        argsMap.put(Config.step.name(), step);
        argsMap.put(Config.inaccuracy.name(), inaccuracy);

        methodsManager.setDiffEquation(equation);
        methodsManager.setEquation(equation);
        methodsManager.setArgs(argsMap);
        res += methodsManager.methodsMap.get(method).apply(null) + "\n";

        return res;
    }


}
