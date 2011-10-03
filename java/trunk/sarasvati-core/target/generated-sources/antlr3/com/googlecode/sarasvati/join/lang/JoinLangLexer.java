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

import com.googlecode.sarasvati.join.lang.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class JoinLangLexer extends Lexer {
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
    public static final int T__30=30;
    public static final int T__19=19;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int NEWLINE=11;
    public static final int T__17=17;
    public static final int SINGLE_COMMENT=13;
    public static final int DIGIT=8;
    public static final int LOWER=9;
    public static final int UPPER=10;
    public static final int STRING=4;

    // delegates
    // delegators

    public JoinLangLexer() {;} 
    public JoinLangLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public JoinLangLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "com/googlecode/sarasvati/join/lang/JoinLang.g"; }

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:28:7: ( 'or' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:28:9: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:29:7: ( 'OR' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:29:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:30:7: ( 'require' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:30:9: 'require'
            {
            match("require"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:31:7: ( 'node' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:31:9: 'node'
            {
            match("node"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:32:7: ( 'tokenset' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:32:9: 'tokenset'
            {
            match("tokenset"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:33:7: ( 'all' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:33:9: 'all'
            {
            match("all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:34:7: ( 'arcs' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:34:9: 'arcs'
            {
            match("arcs"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:35:7: ( 'labelled' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:35:9: 'labelled'
            {
            match("labelled"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:36:7: ( 'default' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:36:9: 'default'
            {
            match("default"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:37:7: ( 'at' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:37:9: 'at'
            {
            match("at"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:38:7: ( 'least' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:38:9: 'least'
            {
            match("least"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:39:7: ( 'when' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:39:9: 'when'
            {
            match("when"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:40:7: ( 'and' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:40:9: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:41:7: ( 'AND' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:41:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:42:7: ( 'not' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:42:9: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:43:7: ( 'NOT' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:43:9: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:44:7: ( '(' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:44:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:45:7: ( ')' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:45:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:122:10: ( '\"' ( '\\\\\\\"' | ~ ( '\"' ) )* '\"' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:122:13: '\"' ( '\\\\\\\"' | ~ ( '\"' ) )* '\"'
            {
            match('\"'); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:122:17: ( '\\\\\\\"' | ~ ( '\"' ) )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\\') ) {
                    int LA1_2 = input.LA(2);

                    if ( (LA1_2=='\"') ) {
                        int LA1_4 = input.LA(3);

                        if ( ((LA1_4>='\u0000' && LA1_4<='\uFFFF')) ) {
                            alt1=1;
                        }

                        else {
                            alt1=2;
                        }

                    }
                    else if ( ((LA1_2>='\u0000' && LA1_2<='!')||(LA1_2>='#' && LA1_2<='\uFFFF')) ) {
                        alt1=2;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='[')||(LA1_0>=']' && LA1_0<='\uFFFF')) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:122:19: '\\\\\\\"'
            	    {
            	    match("\\\""); 


            	    }
            	    break;
            	case 2 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:122:28: ~ ( '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:125:10: ( LETTER ( LETTER | DIGIT | '.' | '_' )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:125:13: LETTER ( LETTER | DIGIT | '.' | '_' )*
            {
            mLETTER(); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:125:20: ( LETTER | DIGIT | '.' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:128:10: ( '1' .. '9' ( DIGIT )* )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:128:12: '1' .. '9' ( DIGIT )*
            {
            matchRange('1','9'); 
            // com/googlecode/sarasvati/join/lang/JoinLang.g:128:21: ( DIGIT )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:128:23: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:132:10: ( LOWER | UPPER )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "LOWER"
    public final void mLOWER() throws RecognitionException {
        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:136:10: ( 'a' .. 'z' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:136:12: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "LOWER"

    // $ANTLR start "UPPER"
    public final void mUPPER() throws RecognitionException {
        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:140:10: ( 'A' .. 'Z' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:140:12: 'A' .. 'Z'
            {
            matchRange('A','Z'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UPPER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:144:10: ( '0' .. '9' )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:144:12: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:147:10: ( ( ( '\\r' )? '\\n' )+ )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:147:14: ( ( '\\r' )? '\\n' )+
            {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:147:14: ( ( '\\r' )? '\\n' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:147:15: ( '\\r' )? '\\n'
            	    {
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:147:15: ( '\\r' )?
            	    int alt4=2;
            	    int LA4_0 = input.LA(1);

            	    if ( (LA4_0=='\r') ) {
            	        alt4=1;
            	    }
            	    switch (alt4) {
            	        case 1 :
            	            // com/googlecode/sarasvati/join/lang/JoinLang.g:147:15: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:151:10: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:151:12: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            {
            // com/googlecode/sarasvati/join/lang/JoinLang.g:151:12: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='\t' && LA6_0<='\n')||(LA6_0>='\f' && LA6_0<='\r')||LA6_0==' ') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "SINGLE_COMMENT"
    public final void mSINGLE_COMMENT() throws RecognitionException {
        try {
            int _type = SINGLE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:155:10: ( '//' (~ ( '\\r' | '\\n' ) )* NEWLINE )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:155:12: '//' (~ ( '\\r' | '\\n' ) )* NEWLINE
            {
            match("//"); 

            // com/googlecode/sarasvati/join/lang/JoinLang.g:155:17: (~ ( '\\r' | '\\n' ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:155:17: ~ ( '\\r' | '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            mNEWLINE(); 
             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINGLE_COMMENT"

    // $ANTLR start "MULTI_COMMENT"
    public final void mMULTI_COMMENT() throws RecognitionException {
        try {
            int _type = MULTI_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/join/lang/JoinLang.g:159:10: ( '/*' ( . )* '*/' ( NEWLINE )? )
            // com/googlecode/sarasvati/join/lang/JoinLang.g:159:12: '/*' ( . )* '*/' ( NEWLINE )?
            {
            match("/*"); 

            // com/googlecode/sarasvati/join/lang/JoinLang.g:159:17: ( . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // com/googlecode/sarasvati/join/lang/JoinLang.g:159:17: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 

            // com/googlecode/sarasvati/join/lang/JoinLang.g:159:25: ( NEWLINE )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\n'||LA9_0=='\r') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // com/googlecode/sarasvati/join/lang/JoinLang.g:159:25: NEWLINE
                    {
                    mNEWLINE(); 

                    }
                    break;

            }

             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTI_COMMENT"

    public void mTokens() throws RecognitionException {
        // com/googlecode/sarasvati/join/lang/JoinLang.g:1:8: ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | STRING | ID | NUMBER | NEWLINE | WHITESPACE | SINGLE_COMMENT | MULTI_COMMENT )
        int alt10=25;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:10: T__15
                {
                mT__15(); 

                }
                break;
            case 2 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:16: T__16
                {
                mT__16(); 

                }
                break;
            case 3 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:22: T__17
                {
                mT__17(); 

                }
                break;
            case 4 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:28: T__18
                {
                mT__18(); 

                }
                break;
            case 5 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:34: T__19
                {
                mT__19(); 

                }
                break;
            case 6 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:40: T__20
                {
                mT__20(); 

                }
                break;
            case 7 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:46: T__21
                {
                mT__21(); 

                }
                break;
            case 8 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:52: T__22
                {
                mT__22(); 

                }
                break;
            case 9 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:58: T__23
                {
                mT__23(); 

                }
                break;
            case 10 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:64: T__24
                {
                mT__24(); 

                }
                break;
            case 11 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:70: T__25
                {
                mT__25(); 

                }
                break;
            case 12 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:76: T__26
                {
                mT__26(); 

                }
                break;
            case 13 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:82: T__27
                {
                mT__27(); 

                }
                break;
            case 14 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:88: T__28
                {
                mT__28(); 

                }
                break;
            case 15 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:94: T__29
                {
                mT__29(); 

                }
                break;
            case 16 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:100: T__30
                {
                mT__30(); 

                }
                break;
            case 17 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:106: T__31
                {
                mT__31(); 

                }
                break;
            case 18 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:112: T__32
                {
                mT__32(); 

                }
                break;
            case 19 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:118: STRING
                {
                mSTRING(); 

                }
                break;
            case 20 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:125: ID
                {
                mID(); 

                }
                break;
            case 21 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:128: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 22 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:135: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 23 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:143: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 24 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:154: SINGLE_COMMENT
                {
                mSINGLE_COMMENT(); 

                }
                break;
            case 25 :
                // com/googlecode/sarasvati/join/lang/JoinLang.g:1:169: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\13\17\5\uffff\1\23\1\44\2\uffff\1\47\1\50\5\17\1\57\7\17"+
        "\5\uffff\2\17\1\71\1\17\1\73\1\17\1\uffff\1\75\4\17\1\102\1\103"+
        "\1\17\1\105\1\uffff\1\17\1\uffff\1\107\1\uffff\3\17\1\113\2\uffff"+
        "\1\17\1\uffff\1\17\1\uffff\1\17\1\117\1\17\1\uffff\3\17\1\uffff"+
        "\1\17\1\125\2\17\1\130\1\uffff\1\131\1\132\3\uffff";
    static final String DFA10_eofS =
        "\133\uffff";
    static final String DFA10_minS =
        "\1\11\1\162\1\122\1\145\2\157\1\154\1\141\1\145\1\150\1\116\1\117"+
        "\5\uffff\1\12\1\11\1\uffff\1\52\2\56\1\161\1\144\1\153\1\154\1\143"+
        "\1\56\1\144\1\142\1\141\1\146\1\145\1\104\1\124\5\uffff\1\165\1"+
        "\145\1\56\1\145\1\56\1\163\1\uffff\1\56\1\145\1\163\1\141\1\156"+
        "\2\56\1\151\1\56\1\uffff\1\156\1\uffff\1\56\1\uffff\1\154\1\164"+
        "\1\165\1\56\2\uffff\1\162\1\uffff\1\163\1\uffff\1\154\1\56\1\154"+
        "\1\uffff\3\145\1\uffff\1\164\1\56\1\164\1\144\1\56\1\uffff\2\56"+
        "\3\uffff";
    static final String DFA10_maxS =
        "\1\172\1\162\1\122\1\145\2\157\1\164\2\145\1\150\1\116\1\117\5\uffff"+
        "\1\12\1\40\1\uffff\1\57\2\172\1\161\1\164\1\153\1\154\1\143\1\172"+
        "\1\144\1\142\1\141\1\146\1\145\1\104\1\124\5\uffff\1\165\1\145\1"+
        "\172\1\145\1\172\1\163\1\uffff\1\172\1\145\1\163\1\141\1\156\2\172"+
        "\1\151\1\172\1\uffff\1\156\1\uffff\1\172\1\uffff\1\154\1\164\1\165"+
        "\1\172\2\uffff\1\162\1\uffff\1\163\1\uffff\1\154\1\172\1\154\1\uffff"+
        "\3\145\1\uffff\1\164\1\172\1\164\1\144\1\172\1\uffff\2\172\3\uffff";
    static final String DFA10_acceptS =
        "\14\uffff\1\21\1\22\1\23\1\24\1\25\2\uffff\1\27\20\uffff\1\26\1"+
        "\30\1\31\1\1\1\2\6\uffff\1\12\11\uffff\1\17\1\uffff\1\6\1\uffff"+
        "\1\15\4\uffff\1\16\1\20\1\uffff\1\4\1\uffff\1\7\3\uffff\1\14\3\uffff"+
        "\1\13\5\uffff\1\3\2\uffff\1\11\1\5\1\10";
    static final String DFA10_specialS =
        "\133\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\23\1\22\1\uffff\1\23\1\21\22\uffff\1\23\1\uffff\1\16\5\uffff"+
            "\1\14\1\15\5\uffff\1\24\1\uffff\11\20\7\uffff\1\12\14\17\1\13"+
            "\1\2\13\17\6\uffff\1\6\2\17\1\10\7\17\1\7\1\17\1\4\1\1\2\17"+
            "\1\3\1\17\1\5\2\17\1\11\3\17",
            "\1\25",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32\1\uffff\1\35\3\uffff\1\33\1\uffff\1\34",
            "\1\36\3\uffff\1\37",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "",
            "",
            "",
            "",
            "",
            "\1\22",
            "\1\23\1\22\1\uffff\1\23\1\21\22\uffff\1\23",
            "",
            "\1\46\4\uffff\1\45",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\51",
            "\1\52\17\uffff\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "",
            "",
            "",
            "",
            "",
            "\1\67",
            "\1\70",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\72",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\74",
            "",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\104",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "",
            "\1\106",
            "",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "",
            "",
            "\1\114",
            "",
            "\1\115",
            "",
            "\1\116",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\120",
            "",
            "\1\121",
            "\1\122",
            "\1\123",
            "",
            "\1\124",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\126",
            "\1\127",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "\1\17\1\uffff\12\17\7\uffff\32\17\4\uffff\1\17\1\uffff\32\17",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | STRING | ID | NUMBER | NEWLINE | WHITESPACE | SINGLE_COMMENT | MULTI_COMMENT );";
        }
    }
 

}