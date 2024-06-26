package zir.lab6vmathback.utils;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class MethodsManager {

    public Map<Integer, Function<Void, String>> methodsMap = new HashMap<>();
    private final DifferentialEquationsManger differentialEquationsManger = new DifferentialEquationsManger();
    private Function<BigDecimal[], BigDecimal> diffEquation;
    private Function<BigDecimal[], BigDecimal> equation;
    private Function<BigDecimal[], BigDecimal> getConstant;

    private BigDecimal yInLeftBorder;
    private BigDecimal leftBorderX;
    private BigDecimal rightBorderX;
    private BigDecimal inaccuracy;
    private BigDecimal step;


    public MethodsManager() {
        methodsMap.put(0, this::getExactSolution);
        methodsMap.put(1, this::improvedEulerMethod);
        methodsMap.put(2, this::rungeKuttaOfThe4thOrderMethod);
        methodsMap.put(3, this::milnesMethod);

    }


    public void setDiffEquation(int n) {
        this.diffEquation = differentialEquationsManger.getDiffEquationsMap().get(n);
        this.getConstant = differentialEquationsManger.getConstsMap().get(n);
    }

    public void setEquation(int n) {
        this.equation = differentialEquationsManger.getEquationsMap().get(n);
    }

    public void setArgs(HashMap<String, BigDecimal> args) {
        yInLeftBorder = args.get(Config.yInLeftBorder.name());
        leftBorderX = args.get(Config.leftBorderX.name());
        rightBorderX = args.get(Config.rightBorderX.name());
        inaccuracy = args.get(Config.inaccuracy.name());
        step = args.get(Config.step.name());
    }

    private String improvedEulerMethod(Void unused) {
        System.out.println("euler starts");

        int p = 1;
        int k = 2;
        BigDecimal checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);

        BigDecimal[][] datah = getEulerData(leftBorderX, rightBorderX, yInLeftBorder, step);

        BigDecimal[][] datah2 = getEulerData(leftBorderX, rightBorderX, yInLeftBorder, checkStep);
        while (!checkRungeCriterion(datah, datah2, p, inaccuracy)) {
            datah = datah2;
            k *= 2;
            checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);
            datah2 = getEulerData(leftBorderX, rightBorderX, yInLeftBorder, checkStep);
        }


        System.out.println(k + " it's step euler");

        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        k /= 2;
//        for (int i = 0; i < n && i * k < n; i++) {
//            System.out.println(datah[0][i * k] + " " + datah[1][i * k]);
//        }

        StringBuilder resp = new StringBuilder("\"euler\": [");

        for (int i = 0; i < n && i * k < n; i++) {
            if (i + 1 == n) {
                resp.append(datah[1][i * k].toString()).append("],\n");
            } else {
                resp.append(datah[1][i * k].toString()).append(",");
            }
        }
        return resp.toString();
    }

    private BigDecimal[][] getEulerData(BigDecimal leftBorderX, BigDecimal rightBorderX, BigDecimal yInLeftBorder, BigDecimal step) {
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);

        BigDecimal[][] res = new BigDecimal[2][n];
        BigDecimal yCurr = yInLeftBorder;
        BigDecimal xCurr = leftBorderX;
        BigDecimal f;
        for (int i = 0; i < n; i++) {
            res[0][i] = xCurr;
            res[1][i] = yCurr;
            f = diffEquation.apply(new BigDecimal[]{xCurr, yCurr});
            xCurr = xCurr.add(step);
            yCurr = yCurr.add(
                    (f.add(diffEquation.apply(new BigDecimal[]{xCurr, yCurr.add(step.multiply(f))})))
                            .multiply(step.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
            );

        }

        return res;
    }

    private String rungeKuttaOfThe4thOrderMethod(Void unused) {
        int p = 4;


        int k = 2;
        BigDecimal checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);

        BigDecimal[][] datah = getRungeKuttaData(leftBorderX, rightBorderX, yInLeftBorder, step);

        BigDecimal[][] datah2 = getRungeKuttaData(leftBorderX, rightBorderX, yInLeftBorder, checkStep);
        while (!checkRungeCriterion(datah, datah2, p, inaccuracy)) {
            datah = datah2;
            k *= 2;
            checkStep = step.divide(BigDecimal.valueOf(k), MathContext.DECIMAL32);
            datah2 = getRungeKuttaData(leftBorderX, rightBorderX, yInLeftBorder, checkStep);
        }


        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        k /= 2;
//        for (int i = 0; i < n && i * k < n; i++) {
//            System.out.println(datah[0][i * k] + " " + datah[1][i * k]);
//        }
        StringBuilder resp = new StringBuilder("\"runge\": [");

        for (int i = 0; i < n && i * k < n; i++) {
            if (i + 1 == n) {
                resp.append(datah[1][i * k].toString()).append("],\n");
            } else {
                resp.append(datah[1][i * k].toString()).append(",");
            }
        }
        return resp.toString();
    }

    private BigDecimal[][] getRungeKuttaData(BigDecimal leftBorderX, BigDecimal rightBorderX, BigDecimal yInLeftBorder, BigDecimal step) {
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);

        BigDecimal[][] res = new BigDecimal[2][n];
        BigDecimal yCurr = yInLeftBorder;
        BigDecimal xCurr = leftBorderX;
        BigDecimal k1, k2, k3, k4;
        for (int i = 0; i < n; i++) {
            res[0][i] = xCurr;
            res[1][i] = yCurr;

            k1 = step.multiply(diffEquation.apply(new BigDecimal[]{xCurr, yCurr}));
            k2 = step.multiply(diffEquation.apply(new BigDecimal[]{
                    xCurr.add(step.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32)),
                    yCurr.add(k1.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
            }));
            k3 = step.multiply(diffEquation.apply(new BigDecimal[]{
                    xCurr.add(step.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32)),
                    yCurr.add(k2.divide(BigDecimal.valueOf(2), MathContext.DECIMAL32))
            }));
            k4 = step.multiply(diffEquation.apply(new BigDecimal[]{
                    xCurr.add(step),
                    yCurr.add(k3)
            }));

            xCurr = xCurr.add(step);
            yCurr = yCurr.add(
                    (k1.add(k2.multiply(BigDecimal.valueOf(2))).add(k3.multiply(BigDecimal.valueOf(2))).add(k4)).divide(BigDecimal.valueOf(6), MathContext.DECIMAL32)
            );

        }

        return res;
    }

    private String milnesMethod(Void unused) {
        System.out.println("it's time for milne");
        BigDecimal[] exactRes = getExactData();
        boolean checkExact = true;
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        int kLupa = 1;
        BigDecimal[][] res = new BigDecimal[2][n];
        BigDecimal checkstep = step;

            System.out.println("new try "+n);

            BigDecimal[][] tmpRes = getRungeKuttaData(leftBorderX, leftBorderX.add(checkstep.multiply(BigDecimal.valueOf(4))), yInLeftBorder, checkstep);
           res = new BigDecimal[2][n];
            for (int i = 0; i < 4; i++) {
                System.out.println(res[0].length+ " "+res[1].length+ " "+tmpRes[0].length+ " "+tmpRes[1].length);
                res[0][i] = tmpRes[0][i];
                res[1][i] = tmpRes[1][i];
            }
            BigDecimal xCurr = res[0][3];
            BigDecimal yPred, yCorr;
            boolean fl;
            for (int i = 4; i < n; i++) {
                xCurr = xCurr.add(step);
                res[0][i] = xCurr;
                fl = true;
                yPred = res[1][i - 4]
                        .add(
                                BigDecimal.valueOf(1.33).multiply(step).multiply(
                                        diffEquation.apply(new BigDecimal[]{res[0][i - 3], res[1][i - 3]}).multiply(BigDecimal.valueOf(2))
                                                .subtract(diffEquation.apply(new BigDecimal[]{res[0][i - 2], res[1][i - 2]}))
                                                .add(diffEquation.apply(new BigDecimal[]{res[0][i - 1], res[1][i - 1]}).multiply(BigDecimal.valueOf(2))

                                                )));
                while (fl) {
                    yCorr = res[1][i - 2]
                            .add(
                                    BigDecimal.valueOf(0.333).multiply(checkstep).multiply(
                                            diffEquation.apply(new BigDecimal[]{res[0][i - 2], res[1][i - 2]})
                                                    .add(diffEquation.apply(new BigDecimal[]{res[0][i - 1], res[1][i - 1]}).multiply(BigDecimal.valueOf(4)))
                                                    .add(diffEquation.apply(new BigDecimal[]{res[0][i], yPred}))
                                    )

                            );

                    if (yCorr.subtract(yPred).abs().compareTo(inaccuracy) < 0) {
                        fl = false;
                    } else {
                        yPred = yCorr;
                    }
                }
                res[1][i] = yPred;

        }

        StringBuilder resp = new StringBuilder("\"milne\": [");
        System.out.println(kLupa+" eto k"+ res[1].length);
        n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        for (int i = 0; i < n && i*kLupa<n; i++) {
            System.out.println(res[1][i]);
            if (i + 1 == n) {
                resp.append(res[1][i*kLupa].toString()).append("]");
            } else {
                resp.append(res[1][i*kLupa].toString()).append(",");
            }
        }
        System.out.println("end");
        return resp.toString();
    }

    public String getExactSolution(Void unused) {
        StringBuilder resp = new StringBuilder("{\n\"xList\": [");
        System.out.println("exact");
        BigDecimal constant = getConstant.apply(new BigDecimal[]{leftBorderX, yInLeftBorder});
        System.out.println(constant + " eto constanta");
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        BigDecimal[] res = new BigDecimal[n];
        BigDecimal xCurr = leftBorderX;

        for (int i = 0; i < n; i++) {
            if (i + 1 == n) {
                resp.append(xCurr.toString()).append("],\n");
            } else {
                resp.append(xCurr.toString()).append(",");
            }
            res[i] = equation.apply(new BigDecimal[]{xCurr, constant});
            xCurr = xCurr.add(step);

        }

        resp.append("\"exact\":[");
        for (int i = 0; i < n; i++) {
            if (i + 1 == n) {
                resp.append(res[i].toString()).append("],\n");
            } else {
                resp.append(res[i].toString()).append(",");
            }
        }
        return resp.toString();

    }

    private BigDecimal[] getExactData() {
        BigDecimal constant = getConstant.apply(new BigDecimal[]{leftBorderX, yInLeftBorder});
        int n = (int) ((rightBorderX.doubleValue() - leftBorderX.doubleValue()) / step.doubleValue() + 1);
        BigDecimal[] res = new BigDecimal[n];
        BigDecimal xCurr = leftBorderX;

        for (int i = 0; i < n; i++) {
            res[i] = equation.apply(new BigDecimal[]{xCurr, constant});
            xCurr = xCurr.add(step);

        }

        return res;
    }


    private boolean checkRungeCriterion(BigDecimal[][] datah, BigDecimal[][] datah2, int p, BigDecimal inaccuracy) {
        int n = datah.length;
        BigDecimal r;
        for (int i = 0; i < n & i * 2 < n; i++) {
            r = (datah[1][i].subtract(datah2[1][i * 2]).abs())
                    .divide(BigDecimal.valueOf(2).pow(p).subtract(BigDecimal.ONE), MathContext.DECIMAL32);
            if (r.compareTo(inaccuracy) > 0) {
                return false;
            }
        }
        return true;
    }

}


