grammar JoinLang;

options {
   output=AST;
   ASTLabelType=CommonTree;
}

@header {
/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.join.lang;

import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.rubric.lang.*;
import com.googlecode.sarasvati.rubric.lang.*;
import java.util.List;
import java.util.ArrayList;

}

@lexer::header {
/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.join.lang;

import com.googlecode.sarasvati.join.lang.*;
}

program returns [JoinLangExpr value]
         :  joinExpr EOF { $value = $joinExpr.value; }
         ;

joinExpr returns [JoinLangExpr value]
        : left=requireSet { $value = $left.value; }
          ( ('or'|'OR')^ right=requireSet { $value = new OrJoinExpr( $value, $right.value ); } )*
        ;

requireSet returns [AndJoinExpr value]
        : firstRequire=require { $value = new AndJoinExpr( $firstRequire.value ); }
          ( moreRequire=require { $value.add( $moreRequire.value ); } )*
        ;

require returns [JoinRequirement value]
        : 'require' 'node' STRING { $value = new NodeRequired( SvUtil.normalizeQuotedString( $STRING.text ) ); }
          ( when { $value.setWhenExpr( $when.value ); } )?

        | 'require' 'tokenset' STRING { $value = new TokenSetRequired( SvUtil.normalizeQuotedString( $STRING.text ) ); }
          ( when { $value.setWhenExpr( $when.value ); } )?

        | 'require' 'all' 'arcs' { $value = new AllArcsRequired(); }
          ( 'labelled' ( ( label=STRING { $value = new LabelArcsRequired( SvUtil.normalizeQuotedString( $label.text ) ); } ) |
                         ( 'default' { $value = new LabelArcsRequired( null ); } ) ) )?
          ( when { $value.setWhenExpr( $when.value ); } )?

        | 'require' 'at' 'least' NUMBER 'arcs' { $value = new AtLeastArcsRequired( SvUtil.parseInt( $NUMBER.text ) ); }
          ( 'labelled' ( ( label=STRING { $value = new AtLeastLabelArcsRequired( SvUtil.normalizeQuotedString( $label.text ), SvUtil.parseInt( $NUMBER.text ) ); } ) |
                         ( 'default' { $value = new AtLeastLabelArcsRequired( null, SvUtil.parseInt( $NUMBER.text ) ); } ) ) )?
          ( when { $value.setWhenExpr( $when.value ); } )?

        ;

when returns [RubricExpr value]
        : 'when' orExpr { $value=$orExpr.value; }
        ;

orExpr returns [RubricExpr value]
        :  left=andExpr { $value = $left.value; } ( ('or'|'OR')^ right=andExpr { $value = new RubricExprOr( $value, $right.value ); } )*
        ;

andExpr returns [RubricExpr value]
        :  left=notExpr { $value = $left.value; } ( ('and'|'AND')^ right=notExpr { $value = new RubricExprAnd( $value, $right.value ); } )*
        ;

notExpr returns [RubricExpr value]
        :  ('not'|'NOT')^ expr { $value = new RubricExprNot( $expr.value ); }
        |  expr { $value = $expr.value; }
        ;

expr returns [RubricExpr value]
        :  ID { $value = new RubricExprSymbol( $ID.text ); }
        |  '('! orExpr ')'! { $value = $orExpr.value; }
        ;

// ===========================================================================
// ================   LEXER   ================================================
// ===========================================================================

STRING   :  '"' ( '\\\"' | ~( '"' ) )* '"'
         ;

ID       :  LETTER ( LETTER | DIGIT | '.' | '_' )*
         ;

NUMBER   : '1'..'9' ( DIGIT )*
         ;

fragment LETTER
         : LOWER | UPPER
         ;

fragment LOWER
         : 'a'..'z'
         ;

fragment UPPER
         : 'A'..'Z'
         ;

fragment DIGIT
         : '0'..'9'
         ;

NEWLINE  :   ('\r'? '\n')+ { $channel=HIDDEN; }
         ;

WHITESPACE
         : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel=HIDDEN; }
         ;

SINGLE_COMMENT
         : '//' ~('\r' | '\n')* NEWLINE { $channel=HIDDEN; }
         ;

MULTI_COMMENT options { greedy = false; }
         : '/*' .* '*/' NEWLINE? { $channel=HIDDEN; }
         ;
