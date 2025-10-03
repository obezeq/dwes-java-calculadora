package com.example.calc;

import java.util.List;

// AST moderno con sealed + records: inmutable y conciso.
public sealed interface Expr permits Expr.NumberLit, Expr.Unary, Expr.Binary, Expr.Call {
    record NumberLit(double value) implements Expr {}
    record Unary(char op, Expr expr) implements Expr {}
    record Binary(Expr left, char op, Expr right) implements Expr {}
    record Call(String name, List<Expr> args) implements Expr {}
}
