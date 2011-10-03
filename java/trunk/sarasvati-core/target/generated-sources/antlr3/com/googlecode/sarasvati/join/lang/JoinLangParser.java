// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 com/googlecode/sarasvati/join/lang/JoinLang.g 2011-10-02 20:05:36

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



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class JoinLangParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STRING", "NUMBER", "ID", "LETTER", "DIGIT", "LOWER", "UPPER", "NEWLINE", "WHITESPACE", "SINGLE_COMMENT", "MULTI_COMMENT", "'or'", "'OR'", "'require'", "'node'", "'tokenset'", "'all'", "'arcs'", "'labelled'", "'default'", "'at'", "'least'", "'when'", "'and'", "'AND'", "'not'", "'NOT'", "'('", "')'"
    };
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int LETTER=7;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int MULTI_COMMENT=14;
    public static final int NUMBER=5;
    public static final int WHITESPACE=12;
    public static final int ID=6;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int NEWLINE=11;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int SINGLE_COMMENT=13;
    public static final int LOWER=9;
    public static final int DIGIT=8;
    public static final int UPPER=10;
    public static final int STRING=4;

    // delegates
    // delegators


        public JoinLangParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public JoinLangParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return JoinLangParser.tokenNames; }
    public String getGrammarFileName() { return "com/googlecode/sarasvati/join/lang/JoinLang.g"; }


    public static class program_return extends ParserRuleReturnScope {
        public JoinLangExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:63:1: program returns [JoinLangExpr value] : joinExpr EOF ;
    public final JoinLangParser.program_return program() throws RecognitionException {
        JoinLangParser.program_return retval = new JoinLangParser.program_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token EOF2=null;
        JoinLangParser.joinExpr_return joinExpr1 = null;


        CommonTree EOF2_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:64:10: ( joinExpr EOF )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:64:13: joinExpr EOF
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_joinExpr_in_program60);
            joinExpr1=joinExpr();

            state._fsp--;

            adaptor.addChild(root_0, joinExpr1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_program62); 
            EOF2_tree = (CommonTree)adaptor.create(EOF2);
            adaptor.addChild(root_0, EOF2_tree);

             retval.value = (joinExpr1!=null?joinExpr1.value:null); 

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

    public static class joinExpr_return extends ParserRuleReturnScope {
        public JoinLangExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "joinExpr"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:67:1: joinExpr returns [JoinLangExpr value] : left= requireSet ( ( 'or' | 'OR' ) right= requireSet )* ;
    public final JoinLangParser.joinExpr_return joinExpr() throws RecognitionException {
        JoinLangParser.joinExpr_return retval = new JoinLangParser.joinExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set3=null;
        JoinLangParser.requireSet_return left = null;

        JoinLangParser.requireSet_return right = null;


        CommonTree set3_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:68:9: (left= requireSet ( ( 'or' | 'OR' ) right= requireSet )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:68:11: left= requireSet ( ( 'or' | 'OR' ) right= requireSet )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_requireSet_in_joinExpr96);
            left=requireSet();

            state._fsp--;

            adaptor.addChild(root_0, left.getTree());
             retval.value = (left!=null?left.value:null); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:69:11: ( ( 'or' | 'OR' ) right= requireSet )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=15 && LA1_0<=16)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:69:13: ( 'or' | 'OR' ) right= requireSet
            	    {
            	    set3=(Token)input.LT(1);
            	    set3=(Token)input.LT(1);
            	    if ( (input.LA(1)>=15 && input.LA(1)<=16) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set3), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_requireSet_in_joinExpr121);
            	    right=requireSet();

            	    state._fsp--;

            	    adaptor.addChild(root_0, right.getTree());
            	     retval.value = new OrJoinExpr( retval.value, (right!=null?right.value:null) ); 

            	    }
            	    break;

            	default :
            	    break loop1;
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
    // $ANTLR end "joinExpr"

    public static class requireSet_return extends ParserRuleReturnScope {
        public AndJoinExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "requireSet"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:72:1: requireSet returns [AndJoinExpr value] : firstRequire= require (moreRequire= require )* ;
    public final JoinLangParser.requireSet_return requireSet() throws RecognitionException {
        JoinLangParser.requireSet_return retval = new JoinLangParser.requireSet_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        JoinLangParser.require_return firstRequire = null;

        JoinLangParser.require_return moreRequire = null;



        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:73:9: (firstRequire= require (moreRequire= require )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:73:11: firstRequire= require (moreRequire= require )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_require_in_requireSet157);
            firstRequire=require();

            state._fsp--;

            adaptor.addChild(root_0, firstRequire.getTree());
             retval.value = new AndJoinExpr( (firstRequire!=null?firstRequire.value:null) ); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:74:11: (moreRequire= require )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==17) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:74:13: moreRequire= require
            	    {
            	    pushFollow(FOLLOW_require_in_requireSet175);
            	    moreRequire=require();

            	    state._fsp--;

            	    adaptor.addChild(root_0, moreRequire.getTree());
            	     retval.value.add( (moreRequire!=null?moreRequire.value:null) ); 

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
    // $ANTLR end "requireSet"

    public static class require_return extends ParserRuleReturnScope {
        public JoinRequirement value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "require"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:77:1: require returns [JoinRequirement value] : ( 'require' 'node' STRING ( when )? | 'require' 'tokenset' STRING ( when )? | 'require' 'all' 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )? | 'require' 'at' 'least' NUMBER 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )? );
    public final JoinLangParser.require_return require() throws RecognitionException {
        JoinLangParser.require_return retval = new JoinLangParser.require_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token label=null;
        Token string_literal4=null;
        Token string_literal5=null;
        Token STRING6=null;
        Token string_literal8=null;
        Token string_literal9=null;
        Token STRING10=null;
        Token string_literal12=null;
        Token string_literal13=null;
        Token string_literal14=null;
        Token string_literal15=null;
        Token string_literal16=null;
        Token string_literal18=null;
        Token string_literal19=null;
        Token string_literal20=null;
        Token NUMBER21=null;
        Token string_literal22=null;
        Token string_literal23=null;
        Token string_literal24=null;
        JoinLangParser.when_return when7 = null;

        JoinLangParser.when_return when11 = null;

        JoinLangParser.when_return when17 = null;

        JoinLangParser.when_return when25 = null;


        CommonTree label_tree=null;
        CommonTree string_literal4_tree=null;
        CommonTree string_literal5_tree=null;
        CommonTree STRING6_tree=null;
        CommonTree string_literal8_tree=null;
        CommonTree string_literal9_tree=null;
        CommonTree STRING10_tree=null;
        CommonTree string_literal12_tree=null;
        CommonTree string_literal13_tree=null;
        CommonTree string_literal14_tree=null;
        CommonTree string_literal15_tree=null;
        CommonTree string_literal16_tree=null;
        CommonTree string_literal18_tree=null;
        CommonTree string_literal19_tree=null;
        CommonTree string_literal20_tree=null;
        CommonTree NUMBER21_tree=null;
        CommonTree string_literal22_tree=null;
        CommonTree string_literal23_tree=null;
        CommonTree string_literal24_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:78:9: ( 'require' 'node' STRING ( when )? | 'require' 'tokenset' STRING ( when )? | 'require' 'all' 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )? | 'require' 'at' 'least' NUMBER 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )? )
            int alt11=4;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==17) ) {
                switch ( input.LA(2) ) {
                case 18:
                    {
                    alt11=1;
                    }
                    break;
                case 19:
                    {
                    alt11=2;
                    }
                    break;
                case 20:
                    {
                    alt11=3;
                    }
                    break;
                case 24:
                    {
                    alt11=4;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:78:11: 'require' 'node' STRING ( when )?
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal4=(Token)match(input,17,FOLLOW_17_in_require209); 
                    string_literal4_tree = (CommonTree)adaptor.create(string_literal4);
                    adaptor.addChild(root_0, string_literal4_tree);

                    string_literal5=(Token)match(input,18,FOLLOW_18_in_require211); 
                    string_literal5_tree = (CommonTree)adaptor.create(string_literal5);
                    adaptor.addChild(root_0, string_literal5_tree);

                    STRING6=(Token)match(input,STRING,FOLLOW_STRING_in_require213); 
                    STRING6_tree = (CommonTree)adaptor.create(STRING6);
                    adaptor.addChild(root_0, STRING6_tree);

                     retval.value = new NodeRequired( SvUtil.normalizeQuotedString( (STRING6!=null?STRING6.getText():null) ) ); 
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:79:11: ( when )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==26) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:79:13: when
                            {
                            pushFollow(FOLLOW_when_in_require229);
                            when7=when();

                            state._fsp--;

                            adaptor.addChild(root_0, when7.getTree());
                             retval.value.setWhenExpr( (when7!=null?when7.value:null) ); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:81:11: 'require' 'tokenset' STRING ( when )?
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal8=(Token)match(input,17,FOLLOW_17_in_require247); 
                    string_literal8_tree = (CommonTree)adaptor.create(string_literal8);
                    adaptor.addChild(root_0, string_literal8_tree);

                    string_literal9=(Token)match(input,19,FOLLOW_19_in_require249); 
                    string_literal9_tree = (CommonTree)adaptor.create(string_literal9);
                    adaptor.addChild(root_0, string_literal9_tree);

                    STRING10=(Token)match(input,STRING,FOLLOW_STRING_in_require251); 
                    STRING10_tree = (CommonTree)adaptor.create(STRING10);
                    adaptor.addChild(root_0, STRING10_tree);

                     retval.value = new TokenSetRequired( SvUtil.normalizeQuotedString( (STRING10!=null?STRING10.getText():null) ) ); 
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:82:11: ( when )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==26) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:82:13: when
                            {
                            pushFollow(FOLLOW_when_in_require267);
                            when11=when();

                            state._fsp--;

                            adaptor.addChild(root_0, when11.getTree());
                             retval.value.setWhenExpr( (when11!=null?when11.value:null) ); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:84:11: 'require' 'all' 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )?
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal12=(Token)match(input,17,FOLLOW_17_in_require285); 
                    string_literal12_tree = (CommonTree)adaptor.create(string_literal12);
                    adaptor.addChild(root_0, string_literal12_tree);

                    string_literal13=(Token)match(input,20,FOLLOW_20_in_require287); 
                    string_literal13_tree = (CommonTree)adaptor.create(string_literal13);
                    adaptor.addChild(root_0, string_literal13_tree);

                    string_literal14=(Token)match(input,21,FOLLOW_21_in_require289); 
                    string_literal14_tree = (CommonTree)adaptor.create(string_literal14);
                    adaptor.addChild(root_0, string_literal14_tree);

                     retval.value = new AllArcsRequired(); 
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:85:11: ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==22) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:85:13: 'labelled' ( (label= STRING ) | ( 'default' ) )
                            {
                            string_literal15=(Token)match(input,22,FOLLOW_22_in_require305); 
                            string_literal15_tree = (CommonTree)adaptor.create(string_literal15);
                            adaptor.addChild(root_0, string_literal15_tree);

                            // com/googlecode/sarasvati/join/lang/JoinLang.g:85:24: ( (label= STRING ) | ( 'default' ) )
                            int alt5=2;
                            int LA5_0 = input.LA(1);

                            if ( (LA5_0==STRING) ) {
                                alt5=1;
                            }
                            else if ( (LA5_0==23) ) {
                                alt5=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 5, 0, input);

                                throw nvae;
                            }
                            switch (alt5) {
                                case 1 :
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:85:26: (label= STRING )
                                    {
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:85:26: (label= STRING )
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:85:28: label= STRING
                                    {
                                    label=(Token)match(input,STRING,FOLLOW_STRING_in_require313); 
                                    label_tree = (CommonTree)adaptor.create(label);
                                    adaptor.addChild(root_0, label_tree);

                                     retval.value = new LabelArcsRequired( SvUtil.normalizeQuotedString( (label!=null?label.getText():null) ) ); 

                                    }


                                    }
                                    break;
                                case 2 :
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:86:26: ( 'default' )
                                    {
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:86:26: ( 'default' )
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:86:28: 'default'
                                    {
                                    string_literal16=(Token)match(input,23,FOLLOW_23_in_require348); 
                                    string_literal16_tree = (CommonTree)adaptor.create(string_literal16);
                                    adaptor.addChild(root_0, string_literal16_tree);

                                     retval.value = new LabelArcsRequired( null ); 

                                    }


                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    // com/googlecode/sarasvati/join/lang/JoinLang.g:87:11: ( when )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==26) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:87:13: when
                            {
                            pushFollow(FOLLOW_when_in_require371);
                            when17=when();

                            state._fsp--;

                            adaptor.addChild(root_0, when17.getTree());
                             retval.value.setWhenExpr( (when17!=null?when17.value:null) ); 

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:89:11: 'require' 'at' 'least' NUMBER 'arcs' ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )? ( when )?
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal18=(Token)match(input,17,FOLLOW_17_in_require389); 
                    string_literal18_tree = (CommonTree)adaptor.create(string_literal18);
                    adaptor.addChild(root_0, string_literal18_tree);

                    string_literal19=(Token)match(input,24,FOLLOW_24_in_require391); 
                    string_literal19_tree = (CommonTree)adaptor.create(string_literal19);
                    adaptor.addChild(root_0, string_literal19_tree);

                    string_literal20=(Token)match(input,25,FOLLOW_25_in_require393); 
                    string_literal20_tree = (CommonTree)adaptor.create(string_literal20);
                    adaptor.addChild(root_0, string_literal20_tree);

                    NUMBER21=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_require395); 
                    NUMBER21_tree = (CommonTree)adaptor.create(NUMBER21);
                    adaptor.addChild(root_0, NUMBER21_tree);

                    string_literal22=(Token)match(input,21,FOLLOW_21_in_require397); 
                    string_literal22_tree = (CommonTree)adaptor.create(string_literal22);
                    adaptor.addChild(root_0, string_literal22_tree);

                     retval.value = new AtLeastArcsRequired( SvUtil.parseInt( (NUMBER21!=null?NUMBER21.getText():null) ) ); 
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:90:11: ( 'labelled' ( (label= STRING ) | ( 'default' ) ) )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==22) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:90:13: 'labelled' ( (label= STRING ) | ( 'default' ) )
                            {
                            string_literal23=(Token)match(input,22,FOLLOW_22_in_require413); 
                            string_literal23_tree = (CommonTree)adaptor.create(string_literal23);
                            adaptor.addChild(root_0, string_literal23_tree);

                            // com/googlecode/sarasvati/join/lang/JoinLang.g:90:24: ( (label= STRING ) | ( 'default' ) )
                            int alt8=2;
                            int LA8_0 = input.LA(1);

                            if ( (LA8_0==STRING) ) {
                                alt8=1;
                            }
                            else if ( (LA8_0==23) ) {
                                alt8=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 8, 0, input);

                                throw nvae;
                            }
                            switch (alt8) {
                                case 1 :
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:90:26: (label= STRING )
                                    {
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:90:26: (label= STRING )
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:90:28: label= STRING
                                    {
                                    label=(Token)match(input,STRING,FOLLOW_STRING_in_require421); 
                                    label_tree = (CommonTree)adaptor.create(label);
                                    adaptor.addChild(root_0, label_tree);

                                     retval.value = new AtLeastLabelArcsRequired( SvUtil.normalizeQuotedString( (label!=null?label.getText():null) ), SvUtil.parseInt( (NUMBER21!=null?NUMBER21.getText():null) ) ); 

                                    }


                                    }
                                    break;
                                case 2 :
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:91:26: ( 'default' )
                                    {
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:91:26: ( 'default' )
                                    // com/googlecode/sarasvati/join/lang/JoinLang.g:91:28: 'default'
                                    {
                                    string_literal24=(Token)match(input,23,FOLLOW_23_in_require456); 
                                    string_literal24_tree = (CommonTree)adaptor.create(string_literal24);
                                    adaptor.addChild(root_0, string_literal24_tree);

                                     retval.value = new AtLeastLabelArcsRequired( null, SvUtil.parseInt( (NUMBER21!=null?NUMBER21.getText():null) ) ); 

                                    }


                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    // com/googlecode/sarasvati/join/lang/JoinLang.g:92:11: ( when )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==26) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // com/googlecode/sarasvati/join/lang/JoinLang.g:92:13: when
                            {
                            pushFollow(FOLLOW_when_in_require479);
                            when25=when();

                            state._fsp--;

                            adaptor.addChild(root_0, when25.getTree());
                             retval.value.setWhenExpr( (when25!=null?when25.value:null) ); 

                            }
                            break;

                    }


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
    // $ANTLR end "require"

    public static class when_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "when"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:96:1: when returns [RubricExpr value] : 'when' orExpr ;
    public final JoinLangParser.when_return when() throws RecognitionException {
        JoinLangParser.when_return retval = new JoinLangParser.when_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal26=null;
        JoinLangParser.orExpr_return orExpr27 = null;


        CommonTree string_literal26_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:97:9: ( 'when' orExpr )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:97:11: 'when' orExpr
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal26=(Token)match(input,26,FOLLOW_26_in_when514); 
            string_literal26_tree = (CommonTree)adaptor.create(string_literal26);
            adaptor.addChild(root_0, string_literal26_tree);

            pushFollow(FOLLOW_orExpr_in_when516);
            orExpr27=orExpr();

            state._fsp--;

            adaptor.addChild(root_0, orExpr27.getTree());
             retval.value =(orExpr27!=null?orExpr27.value:null); 

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
    // $ANTLR end "when"

    public static class orExpr_return extends ParserRuleReturnScope {
        public RubricExpr value;
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "orExpr"
    // com/googlecode/sarasvati/join/lang/JoinLang.g:100:1: orExpr returns [RubricExpr value] : left= andExpr ( ( 'or' | 'OR' ) right= andExpr )* ;
    public final JoinLangParser.orExpr_return orExpr() throws RecognitionException {
        JoinLangParser.orExpr_return retval = new JoinLangParser.orExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set28=null;
        JoinLangParser.andExpr_return left = null;

        JoinLangParser.andExpr_return right = null;


        CommonTree set28_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:101:9: (left= andExpr ( ( 'or' | 'OR' ) right= andExpr )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:101:12: left= andExpr ( ( 'or' | 'OR' ) right= andExpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_andExpr_in_orExpr550);
            left=andExpr();

            state._fsp--;

            adaptor.addChild(root_0, left.getTree());
             retval.value = (left!=null?left.value:null); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:101:51: ( ( 'or' | 'OR' ) right= andExpr )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=15 && LA12_0<=16)) ) {
                    int LA12_2 = input.LA(2);

                    if ( (LA12_2==ID||(LA12_2>=29 && LA12_2<=31)) ) {
                        alt12=1;
                    }


                }


                switch (alt12) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:101:53: ( 'or' | 'OR' ) right= andExpr
            	    {
            	    set28=(Token)input.LT(1);
            	    set28=(Token)input.LT(1);
            	    if ( (input.LA(1)>=15 && input.LA(1)<=16) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set28), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_andExpr_in_orExpr565);
            	    right=andExpr();

            	    state._fsp--;

            	    adaptor.addChild(root_0, right.getTree());
            	     retval.value = new RubricExprOr( retval.value, (right!=null?right.value:null) ); 

            	    }
            	    break;

            	default :
            	    break loop12;
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
    // com/googlecode/sarasvati/join/lang/JoinLang.g:104:1: andExpr returns [RubricExpr value] : left= notExpr ( ( 'and' | 'AND' ) right= notExpr )* ;
    public final JoinLangParser.andExpr_return andExpr() throws RecognitionException {
        JoinLangParser.andExpr_return retval = new JoinLangParser.andExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set29=null;
        JoinLangParser.notExpr_return left = null;

        JoinLangParser.notExpr_return right = null;


        CommonTree set29_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:105:9: (left= notExpr ( ( 'and' | 'AND' ) right= notExpr )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:105:12: left= notExpr ( ( 'and' | 'AND' ) right= notExpr )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_notExpr_in_andExpr602);
            left=notExpr();

            state._fsp--;

            adaptor.addChild(root_0, left.getTree());
             retval.value = (left!=null?left.value:null); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:105:51: ( ( 'and' | 'AND' ) right= notExpr )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=27 && LA13_0<=28)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:105:53: ( 'and' | 'AND' ) right= notExpr
            	    {
            	    set29=(Token)input.LT(1);
            	    set29=(Token)input.LT(1);
            	    if ( (input.LA(1)>=27 && input.LA(1)<=28) ) {
            	        input.consume();
            	        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set29), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_notExpr_in_andExpr617);
            	    right=notExpr();

            	    state._fsp--;

            	    adaptor.addChild(root_0, right.getTree());
            	     retval.value = new RubricExprAnd( retval.value, (right!=null?right.value:null) ); 

            	    }
            	    break;

            	default :
            	    break loop13;
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
    // com/googlecode/sarasvati/join/lang/JoinLang.g:108:1: notExpr returns [RubricExpr value] : ( ( 'not' | 'NOT' ) expr | expr );
    public final JoinLangParser.notExpr_return notExpr() throws RecognitionException {
        JoinLangParser.notExpr_return retval = new JoinLangParser.notExpr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set30=null;
        JoinLangParser.expr_return expr31 = null;

        JoinLangParser.expr_return expr32 = null;


        CommonTree set30_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:109:9: ( ( 'not' | 'NOT' ) expr | expr )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( ((LA14_0>=29 && LA14_0<=30)) ) {
                alt14=1;
            }
            else if ( (LA14_0==ID||LA14_0==31) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:109:12: ( 'not' | 'NOT' ) expr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    set30=(Token)input.LT(1);
                    set30=(Token)input.LT(1);
                    if ( (input.LA(1)>=29 && input.LA(1)<=30) ) {
                        input.consume();
                        root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set30), root_0);
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_expr_in_notExpr659);
                    expr31=expr();

                    state._fsp--;

                    adaptor.addChild(root_0, expr31.getTree());
                     retval.value = new RubricExprNot( (expr31!=null?expr31.value:null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:110:12: expr
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_expr_in_notExpr674);
                    expr32=expr();

                    state._fsp--;

                    adaptor.addChild(root_0, expr32.getTree());
                     retval.value = (expr32!=null?expr32.value:null); 

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
    // com/googlecode/sarasvati/join/lang/JoinLang.g:113:1: expr returns [RubricExpr value] : ( ID | '(' orExpr ')' );
    public final JoinLangParser.expr_return expr() throws RecognitionException {
        JoinLangParser.expr_return retval = new JoinLangParser.expr_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID33=null;
        Token char_literal34=null;
        Token char_literal36=null;
        JoinLangParser.orExpr_return orExpr35 = null;


        CommonTree ID33_tree=null;
        CommonTree char_literal34_tree=null;
        CommonTree char_literal36_tree=null;

        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:114:9: ( ID | '(' orExpr ')' )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==ID) ) {
                alt15=1;
            }
            else if ( (LA15_0==31) ) {
                alt15=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:114:12: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    ID33=(Token)match(input,ID,FOLLOW_ID_in_expr706); 
                    ID33_tree = (CommonTree)adaptor.create(ID33);
                    adaptor.addChild(root_0, ID33_tree);

                     retval.value = new RubricExprSymbol( (ID33!=null?ID33.getText():null) ); 

                    }
                    break;
                case 2 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:115:12: '(' orExpr ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal34=(Token)match(input,31,FOLLOW_31_in_expr721); 
                    pushFollow(FOLLOW_orExpr_in_expr724);
                    orExpr35=orExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, orExpr35.getTree());
                    char_literal36=(Token)match(input,32,FOLLOW_32_in_expr726); 
                     retval.value = (orExpr35!=null?orExpr35.value:null); 

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

    // Delegated rules


 

    public static final BitSet FOLLOW_joinExpr_in_program60 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_program62 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_requireSet_in_joinExpr96 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_set_in_joinExpr112 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_requireSet_in_joinExpr121 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_require_in_requireSet157 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_require_in_requireSet175 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_require209 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_require211 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_STRING_in_require213 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_when_in_require229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_require247 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_require249 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_STRING_in_require251 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_when_in_require267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_require285 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_require287 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_require289 = new BitSet(new long[]{0x0000000004400002L});
    public static final BitSet FOLLOW_22_in_require305 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_STRING_in_require313 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_23_in_require348 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_when_in_require371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_require389 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_require391 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_require393 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NUMBER_in_require395 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_require397 = new BitSet(new long[]{0x0000000004400002L});
    public static final BitSet FOLLOW_22_in_require413 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_STRING_in_require421 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_23_in_require456 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_when_in_require479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_when514 = new BitSet(new long[]{0x00000000E0000040L});
    public static final BitSet FOLLOW_orExpr_in_when516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andExpr_in_orExpr550 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_set_in_orExpr556 = new BitSet(new long[]{0x00000000E0000040L});
    public static final BitSet FOLLOW_andExpr_in_orExpr565 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_notExpr_in_andExpr602 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_set_in_andExpr608 = new BitSet(new long[]{0x00000000E0000040L});
    public static final BitSet FOLLOW_notExpr_in_andExpr617 = new BitSet(new long[]{0x0000000018000002L});
    public static final BitSet FOLLOW_set_in_notExpr652 = new BitSet(new long[]{0x00000000E0000040L});
    public static final BitSet FOLLOW_expr_in_notExpr659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expr_in_notExpr674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_expr706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_expr721 = new BitSet(new long[]{0x00000000E0000040L});
    public static final BitSet FOLLOW_orExpr_in_expr724 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_expr726 = new BitSet(new long[]{0x0000000000000002L});

}