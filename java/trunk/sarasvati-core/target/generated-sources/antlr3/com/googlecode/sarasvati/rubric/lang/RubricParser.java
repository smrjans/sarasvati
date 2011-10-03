// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 com/googlecode/sarasvati/rubric/lang/Rubric.g 2011-10-02 20:05:36

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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.impl.AcceptTokenGuardResult;
import com.googlecode.sarasvati.impl.DiscardTokenGuardResult;
import com.googlecode.sarasvati.impl.SkipNodeGuardResult;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.rubric.lang.*;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class RubricParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACCEPT", "DISCARD", "SKIP", "AFTER", "BEFORE", "DAY", "DAYS", "HOUR", "HOURS", "WEEK", "WEEKS", "BUSINESS", "ID", "NUMBER", "STRING", "LETTER", "DIGIT", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "SINGLE_COMMENT", "MULTI_COMMENT", "'if'", "'IF'", "'then'", "'THEN'", "'else'", "'ELSE'", "'or'", "'OR'", "'and'", "'AND'", "'not'", "'NOT'", "'('", "')'", "'@'"
    };
    public static final int WEEK=13;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int LETTER=19;
    public static final int MULTI_COMMENT=26;
    public static final int NUMBER=17;
    public static final int WHITESPACE=24;
    public static final int BEFORE=8;
    public static final int DAY=9;
    public static final int AFTER=7;
    public static final int ID=16;
    public static final int HOUR=11;
    public static final int BUSINESS=15;
    public static final int EOF=-1;
    public static final int ACCEPT=4;
    public static final int SKIP=6;
    public static final int DAYS=10;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int WEEKS=14;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int NEWLINE=23;
    public static final int DISCARD=5;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int SINGLE_COMMENT=25;
    public static final int LOWER=21;
    public static final int DIGIT=20;
    public static final int UPPER=22;
    public static final int HOURS=12;
    public static final int STRING=18;

    // delegates
    // delegators


        public RubricParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RubricParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return RubricParser.tokenNames; }
    public String getGrammarFileName() { return "com/googlecode/sarasvati/rubric/lang/Rubric.g"; }


    public static class program_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:79:1: program returns [RubricStmt value] : stmt EOF ;
    public final RubricParser.program_return program() throws RecognitionException {
        RubricParser.program_return retval = new RubricParser.program_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2=null;
        RubricParser.stmt_return stmt1 = null;


        CommonTree EOF2_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:80:10: ( stmt EOF )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:80:13: stmt EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_stmt_in_program220);
            stmt1=stmt();

            state._fsp--;

            adaptor.addChild(root_0, stmt1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_program222); 
            EOF2_tree = (CommonTree)adaptor.create(EOF2);
            adaptor.addChild(root_0, EOF2_tree);

             retval.value = (stmt1!=null?stmt1.value:null); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class stmt_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stmt"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:83:1: stmt returns [RubricStmt value] : ( ( 'if' | 'IF' ) e= orExpr ( 'then' | 'THEN' ) ifStmt= stmt ( 'else' | 'ELSE' ) elseStmt= stmt | result );
    public final RubricParser.stmt_return stmt() throws RecognitionException {
        RubricParser.stmt_return retval = new RubricParser.stmt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set3=null;
        Token set4=null;
        Token set5=null;
        RubricParser.orExpr_return e = null;

        RubricParser.stmt_return ifStmt = null;

        RubricParser.stmt_return elseStmt = null;

        RubricParser.result_return result6 = null;


        CommonTree set3_tree=null;
        CommonTree set4_tree=null;
        CommonTree set5_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:84:10: ( ( 'if' | 'IF' ) e= orExpr ( 'then' | 'THEN' ) ifStmt= stmt ( 'else' | 'ELSE' ) elseStmt= stmt | result )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=27 && LA1_0<=28)) ) {
                alt1=1;
            }
            else if ( ((LA1_0>=ACCEPT && LA1_0<=SKIP)||(LA1_0>=NUMBER && LA1_0<=STRING)||LA1_0==39||LA1_0==41) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:84:13: ( 'if' | 'IF' ) e= orExpr ( 'then' | 'THEN' ) ifStmt= stmt ( 'else' | 'ELSE' ) elseStmt= stmt
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    set3=(Token)input.LT(1);
                    set3=(Token)input.LT(1);
                    if ( (input.LA(1)>=27 && input.LA(1)<=28) ) {
                        input.consume();
                        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set3), root_0);
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_orExpr_in_stmt265);
                    e=orExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, e.getTree());
                    set4=(Token)input.LT(1);
                    if ( (input.LA(1)>=29 && input.LA(1)<=30) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_stmt_in_stmt276);
                    ifStmt=stmt();

                    state._fsp--;

                    adaptor.addChild(root_0, ifStmt.getTree());
                    set5=(Token)input.LT(1);
                    if ( (input.LA(1)>=31 && input.LA(1)<=32) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_stmt_in_stmt287);
                    elseStmt=stmt();

                    state._fsp--;

                    adaptor.addChild(root_0, elseStmt.getTree());
                    retval.value = new RubricStmtIf( (e!=null?e.value:null), (ifStmt!=null?ifStmt.value:null), (elseStmt!=null?elseStmt.value:null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:86:13: result
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_result_in_stmt315);
                    result6=result();

                    state._fsp--;

                    adaptor.addChild(root_0, result6.getTree());
                     retval.value = (result6!=null?result6.value:null); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "stmt"

    public static class orExpr_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orExpr"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:89:1: orExpr returns [RubricExpr value] : left= andExpr ( ( 'or' | 'OR' ) right= andExpr )* ;
    public final RubricParser.orExpr_return orExpr() throws RecognitionException {
        RubricParser.orExpr_return retval = new RubricParser.orExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set7=null;
        RubricParser.andExpr_return left = null;

        RubricParser.andExpr_return right = null;


        CommonTree set7_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:90:10: (left= andExpr ( ( 'or' | 'OR' ) right= andExpr )* )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:90:13: left= andExpr ( ( 'or' | 'OR' ) right= andExpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_andExpr_in_orExpr351);
            left=andExpr();

            state._fsp--;

            adaptor.addChild(root_0, left.getTree());
             retval.value = (left!=null?left.value:null); 
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:90:52: ( ( 'or' | 'OR' ) right= andExpr )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=33 && LA2_0<=34)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:90:54: ( 'or' | 'OR' ) right= andExpr
            	    {
            	    set7=(Token)input.LT(1);
            	    set7=(Token)input.LT(1);
            	    if ( (input.LA(1)>=33 && input.LA(1)<=34) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set7), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_andExpr_in_orExpr366);
            	    right=andExpr();

            	    state._fsp--;

            	    adaptor.addChild(root_0, right.getTree());
            	     retval.value = new RubricExprOr( retval.value, (right!=null?right.value:null) ); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "orExpr"

    public static class andExpr_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "andExpr"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:93:1: andExpr returns [RubricExpr value] : left= notExpr ( ( 'and' | 'AND' ) right= notExpr )* ;
    public final RubricParser.andExpr_return andExpr() throws RecognitionException {
        RubricParser.andExpr_return retval = new RubricParser.andExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set8=null;
        RubricParser.notExpr_return left = null;

        RubricParser.notExpr_return right = null;


        CommonTree set8_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:94:10: (left= notExpr ( ( 'and' | 'AND' ) right= notExpr )* )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:94:13: left= notExpr ( ( 'and' | 'AND' ) right= notExpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_notExpr_in_andExpr405);
            left=notExpr();

            state._fsp--;

            adaptor.addChild(root_0, left.getTree());
             retval.value = (left!=null?left.value:null); 
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:94:52: ( ( 'and' | 'AND' ) right= notExpr )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=35 && LA3_0<=36)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:94:54: ( 'and' | 'AND' ) right= notExpr
            	    {
            	    set8=(Token)input.LT(1);
            	    set8=(Token)input.LT(1);
            	    if ( (input.LA(1)>=35 && input.LA(1)<=36) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set8), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_notExpr_in_andExpr420);
            	    right=notExpr();

            	    state._fsp--;

            	    adaptor.addChild(root_0, right.getTree());
            	     retval.value = new RubricExprAnd( retval.value, (right!=null?right.value:null) ); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "andExpr"

    public static class notExpr_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "notExpr"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:97:1: notExpr returns [RubricExpr value] : ( ( 'not' | 'NOT' ) expr | expr );
    public final RubricParser.notExpr_return notExpr() throws RecognitionException {
        RubricParser.notExpr_return retval = new RubricParser.notExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set9=null;
        RubricParser.expr_return expr10 = null;

        RubricParser.expr_return expr11 = null;


        CommonTree set9_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:98:10: ( ( 'not' | 'NOT' ) expr | expr )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=37 && LA4_0<=38)) ) {
                alt4=1;
            }
            else if ( (LA4_0==ID||LA4_0==39) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:98:13: ( 'not' | 'NOT' ) expr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    set9=(Token)input.LT(1);
                    set9=(Token)input.LT(1);
                    if ( (input.LA(1)>=37 && input.LA(1)<=38) ) {
                        input.consume();
                        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set9), root_0);
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_expr_in_notExpr464);
                    expr10=expr();

                    state._fsp--;

                    adaptor.addChild(root_0, expr10.getTree());
                     retval.value = new RubricExprNot( (expr10!=null?expr10.value:null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:99:13: expr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expr_in_notExpr480);
                    expr11=expr();

                    state._fsp--;

                    adaptor.addChild(root_0, expr11.getTree());
                     retval.value = (expr11!=null?expr11.value:null); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "notExpr"

    public static class expr_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expr"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:102:1: expr returns [RubricExpr value] : ( ID | '(' orExpr ')' );
    public final RubricParser.expr_return expr() throws RecognitionException {
        RubricParser.expr_return retval = new RubricParser.expr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID12=null;
        Token char_literal13=null;
        Token char_literal15=null;
        RubricParser.orExpr_return orExpr14 = null;


        CommonTree ID12_tree=null;
        CommonTree char_literal13_tree=null;
        CommonTree char_literal15_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:103:10: ( ID | '(' orExpr ')' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ID) ) {
                alt5=1;
            }
            else if ( (LA5_0==39) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:103:13: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID12=(Token)match(input,ID,FOLLOW_ID_in_expr514); 
                    ID12_tree = (CommonTree)adaptor.create(ID12);
                    adaptor.addChild(root_0, ID12_tree);

                     retval.value = new RubricExprSymbol( (ID12!=null?ID12.getText():null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:104:13: '(' orExpr ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal13=(Token)match(input,39,FOLLOW_39_in_expr530); 
                    pushFollow(FOLLOW_orExpr_in_expr533);
                    orExpr14=orExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, orExpr14.getTree());
                    char_literal15=(Token)match(input,40,FOLLOW_40_in_expr535); 
                     retval.value = (orExpr14!=null?orExpr14.value:null); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expr"

    public static class result_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "result"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:107:1: result returns [RubricStmt value] : ( guardResult | NUMBER | STRING | dateResult | stringResult );
    public final RubricParser.result_return result() throws RecognitionException {
        RubricParser.result_return retval = new RubricParser.result_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NUMBER17=null;
        Token STRING18=null;
        RubricParser.guardResult_return guardResult16 = null;

        RubricParser.dateResult_return dateResult19 = null;

        RubricParser.stringResult_return stringResult20 = null;


        CommonTree NUMBER17_tree=null;
        CommonTree STRING18_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:108:10: ( guardResult | NUMBER | STRING | dateResult | stringResult )
            int alt6=5;
            switch ( input.LA(1) ) {
            case ACCEPT:
            case DISCARD:
            case SKIP:
                {
                alt6=1;
                }
                break;
            case NUMBER:
                {
                alt6=2;
                }
                break;
            case STRING:
                {
                alt6=3;
                }
                break;
            case 39:
                {
                alt6=4;
                }
                break;
            case 41:
                {
                alt6=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:108:13: guardResult
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_guardResult_in_result570);
                    guardResult16=guardResult();

                    state._fsp--;

                    adaptor.addChild(root_0, guardResult16.getTree());
                     retval.value = new RubricStmtResult( (guardResult16!=null?guardResult16.value:null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:109:13: NUMBER
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NUMBER17=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_result587); 
                    NUMBER17_tree = (CommonTree)adaptor.create(NUMBER17);
                    adaptor.addChild(root_0, NUMBER17_tree);

                     retval.value = new RubricStmtResult( Integer.parseInt( (NUMBER17!=null?NUMBER17.getText():null) ) ); 

                    }
                    break;
                case 3 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:110:13: STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STRING18=(Token)match(input,STRING,FOLLOW_STRING_in_result609); 
                    STRING18_tree = (CommonTree)adaptor.create(STRING18);
                    adaptor.addChild(root_0, STRING18_tree);

                     retval.value = new RubricStmtResult( SvUtil.normalizeQuotedString( (STRING18!=null?STRING18.getText():null) ) ); 

                    }
                    break;
                case 4 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:111:13: dateResult
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_dateResult_in_result631);
                    dateResult19=dateResult();

                    state._fsp--;

                    adaptor.addChild(root_0, dateResult19.getTree());
                     retval.value = (dateResult19!=null?dateResult19.value:null); 

                    }
                    break;
                case 5 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:112:13: stringResult
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_stringResult_in_result649);
                    stringResult20=stringResult();

                    state._fsp--;

                    adaptor.addChild(root_0, stringResult20.getTree());
                     retval.value = (stringResult20!=null?stringResult20.value:null); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "result"

    public static class dateResult_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "dateResult"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:115:1: dateResult returns [RubricStmt value] : '(' dateSpec ')' ;
    public final RubricParser.dateResult_return dateResult() throws RecognitionException {
        RubricParser.dateResult_return retval = new RubricParser.dateResult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal21=null;
        Token char_literal23=null;
        RubricParser.dateSpec_return dateSpec22 = null;


        CommonTree char_literal21_tree=null;
        CommonTree char_literal23_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:116:10: ( '(' dateSpec ')' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:116:13: '(' dateSpec ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal21=(Token)match(input,39,FOLLOW_39_in_dateResult683); 
            pushFollow(FOLLOW_dateSpec_in_dateResult686);
            dateSpec22=dateSpec();

            state._fsp--;

            adaptor.addChild(root_0, dateSpec22.getTree());
            char_literal23=(Token)match(input,40,FOLLOW_40_in_dateResult688); 
             retval.value = (dateSpec22!=null?dateSpec22.value:null); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "dateResult"

    public static class stringResult_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stringResult"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:119:1: stringResult returns [RubricStmt value] : '@' ID ;
    public final RubricParser.stringResult_return stringResult() throws RecognitionException {
        RubricParser.stringResult_return retval = new RubricParser.stringResult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal24=null;
        Token ID25=null;

        CommonTree char_literal24_tree=null;
        CommonTree ID25_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:120:10: ( '@' ID )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:120:12: '@' ID
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal24=(Token)match(input,41,FOLLOW_41_in_stringResult722); 
            char_literal24_tree = (CommonTree)adaptor.create(char_literal24);
            adaptor.addChild(root_0, char_literal24_tree);

            ID25=(Token)match(input,ID,FOLLOW_ID_in_stringResult724); 
            ID25_tree = (CommonTree)adaptor.create(ID25);
            adaptor.addChild(root_0, ID25_tree);

             retval.value = new RubricStmtStringSymbol( (ID25!=null?ID25.getText():null) ); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "stringResult"

    public static class dateSpec_return extends ParserRuleReturnScope {
        public RubricStmt value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "dateSpec"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:123:1: dateSpec returns [RubricStmt value] : ( ID | NUMBER ( BUSINESS )? unit= ( HOUR | HOURS | DAY | DAYS | WEEK | WEEKS ) type= ( BEFORE | AFTER ) ID );
    public final RubricParser.dateSpec_return dateSpec() throws RecognitionException {
        RubricParser.dateSpec_return retval = new RubricParser.dateSpec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token unit=null;
        Token type=null;
        Token ID26=null;
        Token NUMBER27=null;
        Token BUSINESS28=null;
        Token ID29=null;

        CommonTree unit_tree=null;
        CommonTree type_tree=null;
        CommonTree ID26_tree=null;
        CommonTree NUMBER27_tree=null;
        CommonTree BUSINESS28_tree=null;
        CommonTree ID29_tree=null;


          boolean business = false;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:127:10: ( ID | NUMBER ( BUSINESS )? unit= ( HOUR | HOURS | DAY | DAYS | WEEK | WEEKS ) type= ( BEFORE | AFTER ) ID )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                alt8=1;
            }
            else if ( (LA8_0==NUMBER) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:127:13: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID26=(Token)match(input,ID,FOLLOW_ID_in_dateSpec763); 
                    ID26_tree = (CommonTree)adaptor.create(ID26);
                    adaptor.addChild(root_0, ID26_tree);

                     retval.value = new RubricStmtDateSymbol( (ID26!=null?ID26.getText():null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:128:13: NUMBER ( BUSINESS )? unit= ( HOUR | HOURS | DAY | DAYS | WEEK | WEEKS ) type= ( BEFORE | AFTER ) ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NUMBER27=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_dateSpec779); 
                    NUMBER27_tree = (CommonTree)adaptor.create(NUMBER27);
                    adaptor.addChild(root_0, NUMBER27_tree);

                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:128:20: ( BUSINESS )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==BUSINESS) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // com/googlecode/sarasvati/rubric/lang/Rubric.g:128:22: BUSINESS
                            {
                            BUSINESS28=(Token)match(input,BUSINESS,FOLLOW_BUSINESS_in_dateSpec783); 
                            BUSINESS28_tree = (CommonTree)adaptor.create(BUSINESS28);
                            adaptor.addChild(root_0, BUSINESS28_tree);

                             business = true; 

                            }
                            break;

                    }

                    unit=(Token)input.LT(1);
                    if ( (input.LA(1)>=DAY && input.LA(1)<=WEEKS) ) {
                        input.consume();
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(unit));
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    type=(Token)input.LT(1);
                    if ( (input.LA(1)>=AFTER && input.LA(1)<=BEFORE) ) {
                        input.consume();
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(type));
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    ID29=(Token)match(input,ID,FOLLOW_ID_in_dateSpec814); 
                    ID29_tree = (CommonTree)adaptor.create(ID29);
                    adaptor.addChild(root_0, ID29_tree);

                     retval.value = new RubricStmtRelativeDate( Integer.parseInt( (NUMBER27!=null?NUMBER27.getText():null) ), business, (unit!=null?unit.getText():null), (type!=null?type.getText():null), (ID29!=null?ID29.getText():null) ); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "dateSpec"

    public static class guardResult_return extends ParserRuleReturnScope {
        public GuardResult value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "guardResult"
    // com/googlecode/sarasvati/rubric/lang/Rubric.g:132:1: guardResult returns [GuardResult value] : ( ACCEPT | DISCARD | SKIP ID | SKIP STRING | SKIP );
    public final RubricParser.guardResult_return guardResult() throws RecognitionException {
        RubricParser.guardResult_return retval = new RubricParser.guardResult_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ACCEPT30=null;
        Token DISCARD31=null;
        Token SKIP32=null;
        Token ID33=null;
        Token SKIP34=null;
        Token STRING35=null;
        Token SKIP36=null;

        CommonTree ACCEPT30_tree=null;
        CommonTree DISCARD31_tree=null;
        CommonTree SKIP32_tree=null;
        CommonTree ID33_tree=null;
        CommonTree SKIP34_tree=null;
        CommonTree STRING35_tree=null;
        CommonTree SKIP36_tree=null;

        try {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:133:10: ( ACCEPT | DISCARD | SKIP ID | SKIP STRING | SKIP )
            int alt9=5;
            switch ( input.LA(1) ) {
            case ACCEPT:
                {
                alt9=1;
                }
                break;
            case DISCARD:
                {
                alt9=2;
                }
                break;
            case SKIP:
                {
                switch ( input.LA(2) ) {
                case ID:
                    {
                    alt9=3;
                    }
                    break;
                case STRING:
                    {
                    alt9=4;
                    }
                    break;
                case EOF:
                case 31:
                case 32:
                    {
                    alt9=5;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 3, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:133:13: ACCEPT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ACCEPT30=(Token)match(input,ACCEPT,FOLLOW_ACCEPT_in_guardResult860); 
                    ACCEPT30_tree = (CommonTree)adaptor.create(ACCEPT30);
                    adaptor.addChild(root_0, ACCEPT30_tree);

                     retval.value = AcceptTokenGuardResult.INSTANCE; 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:134:13: DISCARD
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    DISCARD31=(Token)match(input,DISCARD,FOLLOW_DISCARD_in_guardResult882); 
                    DISCARD31_tree = (CommonTree)adaptor.create(DISCARD31);
                    adaptor.addChild(root_0, DISCARD31_tree);

                     retval.value = DiscardTokenGuardResult.INSTANCE; 

                    }
                    break;
                case 3 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:135:13: SKIP ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    SKIP32=(Token)match(input,SKIP,FOLLOW_SKIP_in_guardResult903); 
                    SKIP32_tree = (CommonTree)adaptor.create(SKIP32);
                    root_0 = (CommonTree)adaptor.becomeRoot(SKIP32_tree, root_0);

                    ID33=(Token)match(input,ID,FOLLOW_ID_in_guardResult906); 
                    ID33_tree = (CommonTree)adaptor.create(ID33);
                    adaptor.addChild(root_0, ID33_tree);

                     retval.value = new SkipNodeGuardResult( (ID33!=null?ID33.getText():null) ); 

                    }
                    break;
                case 4 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:136:13: SKIP STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    SKIP34=(Token)match(input,SKIP,FOLLOW_SKIP_in_guardResult926); 
                    SKIP34_tree = (CommonTree)adaptor.create(SKIP34);
                    root_0 = (CommonTree)adaptor.becomeRoot(SKIP34_tree, root_0);

                    STRING35=(Token)match(input,STRING,FOLLOW_STRING_in_guardResult929); 
                    STRING35_tree = (CommonTree)adaptor.create(STRING35);
                    adaptor.addChild(root_0, STRING35_tree);

                     retval.value = new SkipNodeGuardResult( SvUtil.normalizeQuotedString( (STRING35!=null?STRING35.getText():null) ) ); 

                    }
                    break;
                case 5 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:137:13: SKIP
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    SKIP36=(Token)match(input,SKIP,FOLLOW_SKIP_in_guardResult945); 
                    SKIP36_tree = (CommonTree)adaptor.create(SKIP36);
                    adaptor.addChild(root_0, SKIP36_tree);

                     retval.value = SkipNodeGuardResult.DEFAULT_ARC_SKIP_NODE_RESULT; 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "guardResult"

    // Delegated rules


 

    public static final BitSet FOLLOW_stmt_in_program220 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_program222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_stmt256 = new BitSet(new long[]{0x000000E000010000L});
    public static final BitSet FOLLOW_orExpr_in_stmt265 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_set_in_stmt267 = new BitSet(new long[]{0x0000028018060070L});
    public static final BitSet FOLLOW_stmt_in_stmt276 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_set_in_stmt278 = new BitSet(new long[]{0x0000028018060070L});
    public static final BitSet FOLLOW_stmt_in_stmt287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_result_in_stmt315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpr_in_orExpr351 = new BitSet(new long[]{0x0000000600000002L});
    public static final BitSet FOLLOW_set_in_orExpr357 = new BitSet(new long[]{0x000000E000010000L});
    public static final BitSet FOLLOW_andExpr_in_orExpr366 = new BitSet(new long[]{0x0000000600000002L});
    public static final BitSet FOLLOW_notExpr_in_andExpr405 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_set_in_andExpr411 = new BitSet(new long[]{0x000000E000010000L});
    public static final BitSet FOLLOW_notExpr_in_andExpr420 = new BitSet(new long[]{0x0000001800000002L});
    public static final BitSet FOLLOW_set_in_notExpr457 = new BitSet(new long[]{0x000000E000010000L});
    public static final BitSet FOLLOW_expr_in_notExpr464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_in_notExpr480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_expr514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_expr530 = new BitSet(new long[]{0x000000E000010000L});
    public static final BitSet FOLLOW_orExpr_in_expr533 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_expr535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_guardResult_in_result570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_result587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_result609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dateResult_in_result631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringResult_in_result649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_dateResult683 = new BitSet(new long[]{0x0000000000030000L});
    public static final BitSet FOLLOW_dateSpec_in_dateResult686 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_dateResult688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_stringResult722 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ID_in_stringResult724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_dateSpec763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_dateSpec779 = new BitSet(new long[]{0x000000000000FE00L});
    public static final BitSet FOLLOW_BUSINESS_in_dateSpec783 = new BitSet(new long[]{0x0000000000007E00L});
    public static final BitSet FOLLOW_set_in_dateSpec792 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_set_in_dateSpec808 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ID_in_dateSpec814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACCEPT_in_guardResult860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISCARD_in_guardResult882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_in_guardResult903 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ID_in_guardResult906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_in_guardResult926 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_STRING_in_guardResult929 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SKIP_in_guardResult945 = new BitSet(new long[]{0x0000000000000002L});

}