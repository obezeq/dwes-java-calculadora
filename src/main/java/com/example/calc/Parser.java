package com.example.calc;

import java.util.ArrayList;
import java.util.List;

import static com.example.calc.TokenType.*;
import static com.example.calc.Expr.*;

public final class Parser {
    private final List<Token> ts;
    private int i = 0;

    public Parser(List<Token> tokens) { this.ts = tokens; }

    public Expr parse() {
        Expr e = expr();
        expect(EOF, "Fin de expresión esperado");
        return e;
    }

    private Expr expr() {
        Expr left = term();
        while (match(PLUS) || match(MINUS)) {
            char op = prev().lexeme().charAt(0);
            Expr right = term();
            left = new Binary(left, op, right);
        }
        return left;
    }

    private Expr term() {
        Expr left = factor();
        while (match(STAR) || match(SLASH)) {
            char op = prev().lexeme().charAt(0);
            Expr right = factor();
            left = new Binary(left, op, right);
        }
        return left;
    }

    private Expr factor() { return power(); }

    private Expr power() {
        Expr base = unary();
        if (match(CARET)) {
            Expr exponent = power(); // asociatividad a derecha
            return new Binary(base, '^', exponent);
        }
        return base;
    }

    private Expr unary() {
        if (match(PLUS) || match(MINUS)) {
            char op = prev().lexeme().charAt(0);
            Expr right = unary();
            return new Unary(op, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(NUMBER)) return new NumberLit(Double.parseDouble(prev().lexeme()));
        if (match(IDENT)) {
            String name = prev().lexeme();
            expect(LPAREN, "Se esperaba '(' tras función");

            List<Expr> args = new ArrayList<>();
            if (!check(RPAREN)) {
                args.add(expr());
                while (match(COMMA)) {
                    args.add(expr());
                }
            }

            expect(RPAREN, "Se esperaba ')' tras argumentos");
            return new Call(name, args);
        }

        if (match(LPAREN)) {
            Expr inside = expr();
            expect(RPAREN, "Se esperaba ')'");
            return inside;
        }
        throw error("Se esperaba un número, función o '(' pero se encontró: " + peek().type() + " en pos " + peek().position());
    }

    private boolean match(TokenType t) { if (check(t)) { i++; return true; } return false; }
    private boolean check(TokenType t) { return peek().type() == t; }
    private Token peek() { return ts.get(i); }
    private Token prev() { return ts.get(i-1); }
    private void expect(TokenType t, String msg) {
        if (!match(t)) {
            throw error(msg + ". Se encontró: " + peek().type() + " en pos " + peek().position());
        }
    }
    private IllegalArgumentException error(String msg) { return new IllegalArgumentException(msg); }
}
