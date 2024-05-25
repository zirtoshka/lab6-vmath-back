package zir.lab6vmathback.utils;

import javax.swing.text.View;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DifferentialEquationsManger {
    private int method;
    private int equation;

    public Map<Integer, Function<BigDecimal[], BigDecimal>> diffEquationsMap = new HashMap<>();

    private Map<Integer, Function<BigDecimal[], BigDecimal>> equationsMap = new HashMap<>();

    private Map<Integer, Function<BigDecimal[], BigDecimal>> constsMap = new HashMap<>();



    public DifferentialEquationsManger() {
        diffEquationsMap.put(1, this::firstDiffEquation);
        equationsMap.put(1, this::firstEquation);
        constsMap.put(1, this::getFirstConst);

        diffEquationsMap.put(2, this::secondDiffEquation);
        equationsMap.put(2, this::secondEquation);
        constsMap.put(2, this::getSecondConst);

        diffEquationsMap.put(3, this::thirdDiffEquation);
        equationsMap.put(3, this::thirdEquation);
        constsMap.put(3, this::getThirdConst);
    }





    //y'=x^3+x^2
    private BigDecimal firstDiffEquation(BigDecimal[] xy) {
        return xy[0].pow(3).add(xy[0].pow(2));
    }


    //y=(x^4)/4+(x^3)/3+c
    private BigDecimal firstEquation(BigDecimal[] xc) {
        return xc[0].pow(4).divide(BigDecimal.valueOf(4), MathContext.DECIMAL32)
                .add(xc[0].pow(3).divide(BigDecimal.valueOf(3), MathContext.DECIMAL32))
                .add(xc[1]);
    }

    private BigDecimal getFirstConst(BigDecimal[] xy) {
        return xy[1].subtract(
                xy[0].pow(4).divide(BigDecimal.valueOf(4), MathContext.DECIMAL32)
                        .add(xy[0].pow(3).divide(BigDecimal.valueOf(3), MathContext.DECIMAL32))
        );
    }

    //y=ce^x-x^2-3x-3
    private BigDecimal secondEquation(BigDecimal[] xc) {
        return BigDecimal.valueOf(Math.pow(Math.E, xc[0].doubleValue()))
                .multiply(xc[1])
                .subtract(xc[0].pow(2))
                .subtract(xc[0].multiply(BigDecimal.valueOf(3)))
                .subtract(BigDecimal.valueOf(3));
    }

    private BigDecimal getSecondConst(BigDecimal[] xy) {
        return xy[1].add((
                        xy[0].pow(2))
                        .add(xy[0].multiply(BigDecimal.valueOf(3)))
                        .add(BigDecimal.valueOf(3)))
                .divide(BigDecimal.valueOf(Math.pow(Math.E, xy[0].doubleValue())), MathContext.DECIMAL32);

    }

    // y'=x+y+x^2;
    private BigDecimal secondDiffEquation(BigDecimal[] xy) {
        return xy[0].add(xy[1]).add(xy[0].pow(2));
    }

    //y=-cosx+(x^2)/2+c
    private BigDecimal thirdEquation(BigDecimal[] xc) {
        return BigDecimal.valueOf(-Math.cos(xc[0].doubleValue()))
                .add(xc[0].pow(2).divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
                .add(xc[1]);
    }

    private BigDecimal getThirdConst(BigDecimal[] xy) {
        return xy[1].subtract(
                BigDecimal.valueOf(-Math.cos(xy[0].doubleValue()))
                        .add(xy[0].pow(2).divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
        );
    }

    // y'=x+sinx;
    private BigDecimal thirdDiffEquation(BigDecimal[] xy) {
        return xy[0].add(BigDecimal.valueOf(Math.sin(xy[0].doubleValue())));
    }


}
