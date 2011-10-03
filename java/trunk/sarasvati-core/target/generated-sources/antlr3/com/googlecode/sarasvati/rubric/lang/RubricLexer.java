// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 com/googlecode/sarasvati/rubric/lang/Rubric.g 2011-10-02 20:05:37

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

    Copyright 2008 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.rubric.lang.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RubricLexer extends Lexer {
    public static final int T__40=40;
    public static final int WEEK=13;
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
    public static final int EOF=-1;
    public static final int BUSINESS=15;
    public static final int ACCEPT=4;
    public static final int SKIP=6;
    public static final int T__30=30;
    public static final int DAYS=10;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int WEEKS=14;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int DISCARD=5;
    public static final int NEWLINE=23;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int SINGLE_COMMENT=25;
    public static final int DIGIT=20;
    public static final int LOWER=21;
    public static final int HOURS=12;
    public static final int UPPER=22;
    public static final int STRING=18;

    // delegates
    // delegators

    public RubricLexer() {;} 
    public RubricLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RubricLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "com/googlecode/sarasvati/rubric/lang/Rubric.g"; }

    // $ANTLR start "ACCEPT"
    public final void mACCEPT() throws RecognitionException {
        try {
            int _type = ACCEPT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:28:8: ( 'Accept' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:28:10: 'Accept'
            {
            match("Accept"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ACCEPT"

    // $ANTLR start "DISCARD"
    public final void mDISCARD() throws RecognitionException {
        try {
            int _type = DISCARD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:29:9: ( 'Discard' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:29:11: 'Discard'
            {
            match("Discard"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DISCARD"

    // $ANTLR start "SKIP"
    public final void mSKIP() throws RecognitionException {
        try {
            int _type = SKIP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:30:6: ( 'Skip' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:30:8: 'Skip'
            {
            match("Skip"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SKIP"

    // $ANTLR start "AFTER"
    public final void mAFTER() throws RecognitionException {
        try {
            int _type = AFTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:31:7: ( 'after' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:31:9: 'after'
            {
            match("after"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AFTER"

    // $ANTLR start "BEFORE"
    public final void mBEFORE() throws RecognitionException {
        try {
            int _type = BEFORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:32:8: ( 'before' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:32:10: 'before'
            {
            match("before"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BEFORE"

    // $ANTLR start "DAY"
    public final void mDAY() throws RecognitionException {
        try {
            int _type = DAY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:33:5: ( 'day' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:33:7: 'day'
            {
            match("day"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DAY"

    // $ANTLR start "DAYS"
    public final void mDAYS() throws RecognitionException {
        try {
            int _type = DAYS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:34:6: ( 'days' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:34:8: 'days'
            {
            match("days"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DAYS"

    // $ANTLR start "HOUR"
    public final void mHOUR() throws RecognitionException {
        try {
            int _type = HOUR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:35:6: ( 'hour' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:35:8: 'hour'
            {
            match("hour"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HOUR"

    // $ANTLR start "HOURS"
    public final void mHOURS() throws RecognitionException {
        try {
            int _type = HOURS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:36:7: ( 'hours' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:36:9: 'hours'
            {
            match("hours"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HOURS"

    // $ANTLR start "WEEK"
    public final void mWEEK() throws RecognitionException {
        try {
            int _type = WEEK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:37:6: ( 'week' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:37:8: 'week'
            {
            match("week"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WEEK"

    // $ANTLR start "WEEKS"
    public final void mWEEKS() throws RecognitionException {
        try {
            int _type = WEEKS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:38:7: ( 'weeks' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:38:9: 'weeks'
            {
            match("weeks"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WEEKS"

    // $ANTLR start "BUSINESS"
    public final void mBUSINESS() throws RecognitionException {
        try {
            int _type = BUSINESS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:39:10: ( 'business' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:39:12: 'business'
            {
            match("business"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BUSINESS"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:40:7: ( 'if' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:40:9: 'if'
            {
            match("if"); 


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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:41:7: ( 'IF' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:41:9: 'IF'
            {
            match("IF"); 


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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:42:7: ( 'then' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:42:9: 'then'
            {
            match("then"); 


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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:43:7: ( 'THEN' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:43:9: 'THEN'
            {
            match("THEN"); 


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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:44:7: ( 'else' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:44:9: 'else'
            {
            match("else"); 


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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:45:7: ( 'ELSE' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:45:9: 'ELSE'
            {
            match("ELSE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:46:7: ( 'or' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:46:9: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:47:7: ( 'OR' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:47:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:48:7: ( 'and' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:48:9: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:49:7: ( 'AND' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:49:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:50:7: ( 'not' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:50:9: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:51:7: ( 'NOT' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:51:9: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:52:7: ( '(' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:52:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:53:7: ( ')' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:53:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:54:7: ( '@' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:54:9: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:144:10: ( '\"' ( '\\\\\\\"' | ~ ( '\"' ) )* '\"' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:144:13: '\"' ( '\\\\\\\"' | ~ ( '\"' ) )* '\"'
            {
            match('\"'); 
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:144:17: ( '\\\\\\\"' | ~ ( '\"' ) )*
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
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:144:19: '\\\\\\\"'
            	    {
            	    match("\\\""); 


            	    }
            	    break;
            	case 2 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:144:28: ~ ( '\"' )
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:147:10: ( LETTER ( LETTER | DIGIT | '.' | '_' )* )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:147:13: LETTER ( LETTER | DIGIT | '.' | '_' )*
            {
            mLETTER(); 
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:147:20: ( LETTER | DIGIT | '.' | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:10: ( ( '-' )? ( DIGIT )+ )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:13: ( '-' )? ( DIGIT )+
            {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:13: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:13: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:18: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:150:18: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:154:10: ( LOWER | UPPER )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:158:10: ( 'a' .. 'z' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:158:12: 'a' .. 'z'
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:162:10: ( 'A' .. 'Z' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:162:12: 'A' .. 'Z'
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:166:10: ( '0' .. '9' )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:166:12: '0' .. '9'
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:10: ( ( ( '\\r' )? '\\n' )+ )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:14: ( ( '\\r' )? '\\n' )+
            {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:14: ( ( '\\r' )? '\\n' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:15: ( '\\r' )? '\\n'
            	    {
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:15: ( '\\r' )?
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( (LA5_0=='\r') ) {
            	        alt5=1;
            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // com/googlecode/sarasvati/rubric/lang/Rubric.g:169:15: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

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
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:173:10: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:173:12: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            {
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:173:12: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\t' && LA7_0<='\n')||(LA7_0>='\f' && LA7_0<='\r')||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:177:10: ( '//' (~ ( '\\r' | '\\n' ) )* NEWLINE )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:177:12: '//' (~ ( '\\r' | '\\n' ) )* NEWLINE
            {
            match("//"); 

            // com/googlecode/sarasvati/rubric/lang/Rubric.g:177:17: (~ ( '\\r' | '\\n' ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='\t')||(LA8_0>='\u000B' && LA8_0<='\f')||(LA8_0>='\u000E' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:177:17: ~ ( '\\r' | '\\n' )
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
            	    break loop8;
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
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:10: ( '/*' ( . )* '*/' ( NEWLINE )? )
            // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:12: '/*' ( . )* '*/' ( NEWLINE )?
            {
            match("/*"); 

            // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:17: ( . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='*') ) {
                    int LA9_1 = input.LA(2);

                    if ( (LA9_1=='/') ) {
                        alt9=2;
                    }
                    else if ( ((LA9_1>='\u0000' && LA9_1<='.')||(LA9_1>='0' && LA9_1<='\uFFFF')) ) {
                        alt9=1;
                    }


                }
                else if ( ((LA9_0>='\u0000' && LA9_0<=')')||(LA9_0>='+' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:17: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match("*/"); 

            // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:25: ( NEWLINE )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // com/googlecode/sarasvati/rubric/lang/Rubric.g:181:25: NEWLINE
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
        // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:8: ( ACCEPT | DISCARD | SKIP | AFTER | BEFORE | DAY | DAYS | HOUR | HOURS | WEEK | WEEKS | BUSINESS | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | STRING | ID | NUMBER | NEWLINE | WHITESPACE | SINGLE_COMMENT | MULTI_COMMENT )
        int alt11=34;
        alt11 = dfa11.predict(input);
        switch (alt11) {
            case 1 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:10: ACCEPT
                {
                mACCEPT(); 

                }
                break;
            case 2 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:17: DISCARD
                {
                mDISCARD(); 

                }
                break;
            case 3 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:25: SKIP
                {
                mSKIP(); 

                }
                break;
            case 4 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:30: AFTER
                {
                mAFTER(); 

                }
                break;
            case 5 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:36: BEFORE
                {
                mBEFORE(); 

                }
                break;
            case 6 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:43: DAY
                {
                mDAY(); 

                }
                break;
            case 7 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:47: DAYS
                {
                mDAYS(); 

                }
                break;
            case 8 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:52: HOUR
                {
                mHOUR(); 

                }
                break;
            case 9 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:57: HOURS
                {
                mHOURS(); 

                }
                break;
            case 10 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:63: WEEK
                {
                mWEEK(); 

                }
                break;
            case 11 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:68: WEEKS
                {
                mWEEKS(); 

                }
                break;
            case 12 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:74: BUSINESS
                {
                mBUSINESS(); 

                }
                break;
            case 13 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:83: T__27
                {
                mT__27(); 

                }
                break;
            case 14 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:89: T__28
                {
                mT__28(); 

                }
                break;
            case 15 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:95: T__29
                {
                mT__29(); 

                }
                break;
            case 16 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:101: T__30
                {
                mT__30(); 

                }
                break;
            case 17 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:107: T__31
                {
                mT__31(); 

                }
                break;
            case 18 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:113: T__32
                {
                mT__32(); 

                }
                break;
            case 19 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:119: T__33
                {
                mT__33(); 

                }
                break;
            case 20 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:125: T__34
                {
                mT__34(); 

                }
                break;
            case 21 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:131: T__35
                {
                mT__35(); 

                }
                break;
            case 22 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:137: T__36
                {
                mT__36(); 

                }
                break;
            case 23 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:143: T__37
                {
                mT__37(); 

                }
                break;
            case 24 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:149: T__38
                {
                mT__38(); 

                }
                break;
            case 25 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:155: T__39
                {
                mT__39(); 

                }
                break;
            case 26 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:161: T__40
                {
                mT__40(); 

                }
                break;
            case 27 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:167: T__41
                {
                mT__41(); 

                }
                break;
            case 28 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:173: STRING
                {
                mSTRING(); 

                }
                break;
            case 29 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:180: ID
                {
                mID(); 

                }
                break;
            case 30 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:183: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 31 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:190: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 32 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:198: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 33 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:209: SINGLE_COMMENT
                {
                mSINGLE_COMMENT(); 

                }
                break;
            case 34 :
                // com/googlecode/sarasvati/rubric/lang/Rubric.g:1:224: MULTI_COMMENT
                {
                mMULTI_COMMENT(); 

                }
                break;

        }

    }


    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA11_eotS =
        "\1\uffff\22\27\6\uffff\1\33\1\62\2\uffff\13\27\1\100\1\101\4\27"+
        "\1\106\1\107\2\27\3\uffff\1\27\1\113\3\27\1\117\2\27\1\123\2\27"+
        "\2\uffff\4\27\2\uffff\1\132\1\133\1\27\1\uffff\1\27\1\136\1\27\1"+
        "\uffff\2\27\1\142\1\uffff\1\144\1\146\1\147\1\150\1\151\1\152\2"+
        "\uffff\2\27\1\uffff\1\155\2\27\1\uffff\1\160\1\uffff\1\161\5\uffff"+
        "\1\162\1\27\1\uffff\1\164\1\27\3\uffff\1\166\1\uffff\1\27\1\uffff"+
        "\1\170\1\uffff";
    static final String DFA11_eofS =
        "\171\uffff";
    static final String DFA11_minS =
        "\1\11\1\116\1\151\1\153\1\146\1\145\1\141\1\157\1\145\1\146\1\106"+
        "\1\150\1\110\1\154\1\114\1\162\1\122\1\157\1\117\6\uffff\1\12\1"+
        "\11\1\uffff\1\52\1\143\1\104\1\163\1\151\1\164\1\144\1\146\1\163"+
        "\1\171\1\165\1\145\2\56\1\145\1\105\1\163\1\123\2\56\1\164\1\124"+
        "\3\uffff\1\145\1\56\1\143\1\160\1\145\1\56\1\157\1\151\1\56\1\162"+
        "\1\153\2\uffff\1\156\1\116\1\145\1\105\2\uffff\2\56\1\160\1\uffff"+
        "\1\141\1\56\1\162\1\uffff\1\162\1\156\1\56\1\uffff\6\56\2\uffff"+
        "\1\164\1\162\1\uffff\1\56\2\145\1\uffff\1\56\1\uffff\1\56\5\uffff"+
        "\1\56\1\144\1\uffff\1\56\1\163\3\uffff\1\56\1\uffff\1\163\1\uffff"+
        "\1\56\1\uffff";
    static final String DFA11_maxS =
        "\1\172\1\143\1\151\1\153\1\156\1\165\1\141\1\157\1\145\1\146\1\106"+
        "\1\150\1\110\1\154\1\114\1\162\1\122\1\157\1\117\6\uffff\1\12\1"+
        "\40\1\uffff\1\57\1\143\1\104\1\163\1\151\1\164\1\144\1\146\1\163"+
        "\1\171\1\165\1\145\2\172\1\145\1\105\1\163\1\123\2\172\1\164\1\124"+
        "\3\uffff\1\145\1\172\1\143\1\160\1\145\1\172\1\157\1\151\1\172\1"+
        "\162\1\153\2\uffff\1\156\1\116\1\145\1\105\2\uffff\2\172\1\160\1"+
        "\uffff\1\141\1\172\1\162\1\uffff\1\162\1\156\1\172\1\uffff\6\172"+
        "\2\uffff\1\164\1\162\1\uffff\1\172\2\145\1\uffff\1\172\1\uffff\1"+
        "\172\5\uffff\1\172\1\144\1\uffff\1\172\1\163\3\uffff\1\172\1\uffff"+
        "\1\163\1\uffff\1\172\1\uffff";
    static final String DFA11_acceptS =
        "\23\uffff\1\31\1\32\1\33\1\34\1\35\1\36\2\uffff\1\40\26\uffff\1"+
        "\37\1\41\1\42\13\uffff\1\15\1\16\4\uffff\1\23\1\24\3\uffff\1\26"+
        "\3\uffff\1\25\3\uffff\1\6\6\uffff\1\27\1\30\2\uffff\1\3\3\uffff"+
        "\1\7\1\uffff\1\10\1\uffff\1\12\1\17\1\20\1\21\1\22\2\uffff\1\4\2"+
        "\uffff\1\11\1\13\1\1\1\uffff\1\5\1\uffff\1\2\1\uffff\1\14";
    static final String DFA11_specialS =
        "\171\uffff}>";
    static final String[] DFA11_transitionS = {
            "\1\33\1\32\1\uffff\1\33\1\31\22\uffff\1\33\1\uffff\1\26\5\uffff"+
            "\1\23\1\24\3\uffff\1\30\1\uffff\1\34\12\30\6\uffff\1\25\1\1"+
            "\2\27\1\2\1\16\3\27\1\12\4\27\1\22\1\20\3\27\1\3\1\14\6\27\6"+
            "\uffff\1\4\1\5\1\27\1\6\1\15\2\27\1\7\1\11\4\27\1\21\1\17\4"+
            "\27\1\13\2\27\1\10\3\27",
            "\1\36\24\uffff\1\35",
            "\1\37",
            "\1\40",
            "\1\41\7\uffff\1\42",
            "\1\43\17\uffff\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\32",
            "\1\33\1\32\1\uffff\1\33\1\31\22\uffff\1\33",
            "",
            "\1\64\4\uffff\1\63",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\110",
            "\1\111",
            "",
            "",
            "",
            "\1\112",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\120",
            "\1\121",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\22\27"+
            "\1\122\7\27",
            "\1\124",
            "\1\125",
            "",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\134",
            "",
            "\1\135",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\137",
            "",
            "\1\140",
            "\1\141",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\22\27"+
            "\1\143\7\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\22\27"+
            "\1\145\7\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "",
            "\1\153",
            "\1\154",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\156",
            "\1\157",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "",
            "",
            "",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\163",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\165",
            "",
            "",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "\1\167",
            "",
            "\1\27\1\uffff\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ACCEPT | DISCARD | SKIP | AFTER | BEFORE | DAY | DAYS | HOUR | HOURS | WEEK | WEEKS | BUSINESS | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | STRING | ID | NUMBER | NEWLINE | WHITESPACE | SINGLE_COMMENT | MULTI_COMMENT );";
        }
    }
 

}