grammar DungeonDSL;

@header{
    package antlr.main;
}

/*
 * Lexer rules
 */

TYPE_SPECIFIER
        : 'quest_config'
        ;

DOUBLE_LINE : '--';
ARROW       : '->';

ID  : [_a-zA-Z][a-zA-Z0-9_]*;
WS  : [ \t\r\n]+ -> skip;

LINE_COMMENT
        : '//' ~[\r\n]* -> channel(HIDDEN)
        ;

BLOCK_COMMENT
        : '/*' .*? '*/' -> channel(HIDDEN)
        ;

/*
 * Parser rules
 */

// TODO:
// - expression grammar
// - proper stmt definition

program : obj_def* EOF
        //| fn_def
        //| stmt
        ;

obj_def : TYPE_SPECIFIER ID '{' property_def '}'
        | ('graph'|'digraph') ID '{' dot_def '}'
        ;

// TODO: make list (comma)
property_def
        : ID ':' stmt;

// temporary, for testing
stmt    : ID;
/*
 * -------------------- dot related definitions --------------------
 * dot grammar: https://graphviz.org/doc/info/lang.html
 *
 * simplifications:
 * - don't support subgraphs
 * - don't support ports
 */

dot_def : dot_stmt_list?;

dot_stmt_list
        : dot_stmt ';'? dot_stmt_list?
        ;

dot_stmt
        : dot_node_stmt
        | dot_edge_stmt
        | dot_attr_stmt
        | ID '=' ID
        ;

dot_edge_stmt
        : ID dot_edge_RHS+ dot_attr_list?
        ;

dot_edge_RHS
        //: dot_edge_op ID rhs=dot_edge_RHS?
        : dot_edge_op ID
        ;

// dot specifies the keywords as case insensitive,
// we require them to be lowercase for simplicity
dot_attr_stmt
        : ('graph' | 'node' | 'edge') dot_attr_list
        ;

dot_node_stmt
        : ID dot_attr_list?
        ;

dot_attr_list
        : '[' dot_a_list? ']' dot_a_list?
        ;

dot_a_list
        : ID '=' ID (';'|',')? dot_a_list?
        ;

dot_edge_op
        : ARROW
        | DOUBLE_LINE
        ;

// Reference Program
/*
graph g {
  G--T
  T--Q
  Q--D
  Q--F
  T--X
  X--S
  G--W
  W--E
  W--C
  C--U
  C--N
}

quest_config conf {
  style: tree_traversal,
  type: dfs_pre_order,
  tree: g,
  points: 3,
  task_desc: "Öffne die Schatztruhe mit dem korrekten Passwort! Das Passwort setzt
  sich aus einzelnen Buchstaben zusammen, die in den Räumen des Dungeons zu
  finden sind. Die Räume sind in einer Baumstruktur angeordnet. Der Raum mit der
  Schatztruhe stellt die Wurzel des Baumes dar. Um diese Truhe zu öffnen muss
  das Passwort in **Pre-Order** Reihenfolge angegeben werden.",
}

set_quest(conf)
*/
