package main;

import antlr.main.*;
import interpreter.dot.Interpreter;
import org.antlr.v4.runtime.*;
import parser.DungeonASTConverter;

public class Main {

    /**
     * minimal ANTLR setup to parse a progam
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String program = "graph g {\n" + "A -- B -- F \n" + "B -- C -- D -> E \n A -- X \n B -- Y \n A -- Z\n" + "}";
        var stream = CharStreams.fromString(program);
        var lexer = new DungeonDSLLexer(stream);

        var tokenStream = new CommonTokenStream(lexer);
        var parser = new DungeonDSLParser(tokenStream);
        var programParseTree = parser.program();

        DungeonASTConverter astConverter = new DungeonASTConverter();
        var programAST = astConverter.walk(programParseTree);

        Interpreter dotInterpreter = new Interpreter();
        programAST.accept(dotInterpreter);
    }
}
