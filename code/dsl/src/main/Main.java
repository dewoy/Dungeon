package main;

import antlr.main.*;
import org.antlr.v4.runtime.*;
import parser.DungeonDSLListener;

public class Main {

    /**
     * minimal ANTLR setup to parse a progam
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String program = "graph g {\n" + "A -- B \n" + "B -- C -- D -> E \n" + "}";
        var stream = CharStreams.fromString(program);
        var lexer = new DungeonDSLLexer(stream);

        var tokenStream = new CommonTokenStream(lexer);
        var parser = new DungeonDSLParser(tokenStream);
        var programParseTree = parser.program();

        DungeonDSLListener astConverter = new DungeonDSLListener();
        var programAST = astConverter.walk(programParseTree);
    }
}
