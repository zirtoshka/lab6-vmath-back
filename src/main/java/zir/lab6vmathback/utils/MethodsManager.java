package zir.lab6vmathback.utils;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static zir.lab6vmathback.utils.Config.*;

public class MethodsManager {

    public Map<Integer, Function<HashMap<String, BigDecimal>, String>> methodsMap = new HashMap<>();
    private final DifferentialEquationsManger differentialEquationsManger = new DifferentialEquationsManger();
    private Function<BigDecimal[], BigDecimal> diffEquation;
    private Function<BigDecimal[], BigDecimal> equation;


    public MethodsManager() {
        methodsMap.put(1, this::improvedEulerMethod);
        methodsMap.put(2, this::rungeKuttaOfThe4thOrderMethod);
        methodsMap.put(3, this::milnesMethod);

    }

    public void setDiffEquation(int n) {
        this.diffEquation = differentialEquationsManger.getEquationsMap().get(n);
    }

    public void setEquation(int n) {
        this.equation = differentialEquationsManger.getEquationsMap().get(n);
    }

    private String improvedEulerMethod(HashMap<String, BigDecimal> args) {
        int p=1;
        BigDecimal yInLeftBorder = args.get(Config.yInLeftBorder.name());
        BigDecimal leftBorderX = args.get(Config.leftBorderX.name());
        BigDecimal rightBorderX = args.get(Config.rightBorderX.name());
        BigDecimal inaccuracy = args.get(Config.inaccuracy.name());
        BigDecimal step = args.get(Config.step.name());

        int ordinalNumberEquation = Integer.parseInt(String.valueOf(args.get(Config.diffEquation.name())));

        BigDecimal constant = differentialEquationsManger.getConstsMap().get(ordinalNumberEquation).apply(new BigDecimal[]{leftBorderX, yInLeftBorder});

        int k = 2;
        BigDecimal checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);

        BigDecimal[][] datah = getEulerData(leftBorderX,rightBorderX,yInLeftBorder,step);
        BigDecimal[][] datah2 = getEulerData(leftBorderX,rightBorderX,yInLeftBorder,checkStep);
        while (!checkRungeCriterion(datah,datah2,p,inaccuracy)){
            datah=datah2;
            k*=2;
            checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);
            datah2=getEulerData(leftBorderX,rightBorderX,yInLeftBorder,checkStep);
        }


        System.out.println(constant + " it's c");
        System.out.println(k + " it's step");

        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);

        for (int i=0;i<n && i*k<n;i++){
            System.out.println(datah[0][i*k]+" "+datah[1][i*k]);
        }
        return "euler";
    }

    private BigDecimal[][] getEulerData(BigDecimal leftBorderX, BigDecimal rightBorderX, BigDecimal yInLeftBorder, BigDecimal step) {
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);

        BigDecimal[][] res = new BigDecimal[2][n];
        BigDecimal yCurr=yInLeftBorder;
        BigDecimal xCurr=leftBorderX;
        BigDecimal f;
        for(int i=0;i<n;i++){
            res[0][i]=xCurr;
            res[1][i]=yCurr;
            f = diffEquation.apply(new BigDecimal[]{xCurr, yCurr});
            xCurr = xCurr.add(step);
            yCurr = yCurr.add(
                    (f.add(diffEquation.apply(new BigDecimal[]{xCurr, yCurr.add(step.multiply(f))})))
                            .multiply(step.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
            );

        }

        return res;
    }

    private String rungeKuttaOfThe4thOrderMethod(HashMap<String, BigDecimal> args) {
        return "runge-kutta";
    }

    private String milnesMethod(HashMap<String, BigDecimal> args) {
        return "milnes";
    }

    private boolean checkRungeCriterion(BigDecimal[][] datah, BigDecimal[][] datah2, int p, BigDecimal inaccuracy) {
        int n = datah.length;
        BigDecimal r;
        for (int i = 0; i < n; i++) {
            r = (datah[1][i].subtract(datah2[1][i * 2]).abs())
                    .divide(BigDecimal.valueOf(2).pow(p).subtract(BigDecimal.ONE), MathContext.DECIMAL32);
            if (r.compareTo(inaccuracy) > 0) {
                return false;
            }
        }
        return true;
    }

    }


