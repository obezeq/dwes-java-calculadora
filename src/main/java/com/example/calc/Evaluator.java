package com.example.calc;

import static com.example.calc.Expr.*;

public final class Evaluator {
    private Evaluator() { throw new AssertionError("No instanciable"); }

    public static double eval(Expr e) {
        return switch (e) {
            case NumberLit n -> n.value();
            case Unary u -> {
                double v = eval(u.expr());
                yield (u.op() == '-') ? -v : +v;
            }
            case Binary b -> {
                double l = eval(b.left());
                double r = eval(b.right());
                yield switch (b.op()) {
                    case '+' -> l + r;
                    case '-' -> l - r;
                    case '*' -> l * r;
                    case '/' -> l / r;
                    case '^' -> Math.pow(l, r);
                    default -> throw new IllegalStateException("Operador no soportado: " + b.op());
                };
            }
            case Call c -> {

                yield switch (c.name()) {

                    case "sin", "cos", "tan", "ln" -> {
                        if (c.args().size() != 1) {
                            throw new IllegalArgumentException(c.name() + " requiere exactamente 1 argumento");
                        }

                        double x = eval(c.args().getFirst());
                        yield switch (c.name()) {
                            case "sin" -> Math.sin(x);
                            case "cos" -> Math.cos(x);
                            case "tan" -> Math.tan(x);
                            case "ln" -> Math.log(x);
                            default -> throw new AssertionError();
                        };

                    }

                    case "log" -> {

                        if (c.args().size() == 1) {

                            double x = eval(c.args().getFirst());
                            yield Math.log10(x);

                        } else if(c.args().size() == 2) {

                            double base = eval(c.args().getFirst());
                            double x = eval(c.args().get(1));

                            yield Math.log(x) / Math.log(base);

                        } else {

                            throw new IllegalArgumentException(c.name() + " requiere 1 o 2 argumentos solamente");

                        }

                    }

                    default -> throw new IllegalStateException("Función no soportada: " + c.name());
                };

            }
        };
    }
}
