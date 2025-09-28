package com.example.calc;

// AST moderno con sealed + records: inmutable y conciso.
public sealed interface Expr permits Expr.NumberLit, Expr.Unary, Expr.Binary, Expr.Call {
    public record NumberLit(double value) implements Expr {}
    public record Unary(char op, Expr expr) implements Expr {}
    public record Binary(Expr left, char op, Expr right) implements Expr {}
    public record Call(String name, Expr arg) implements Expr {}
}
