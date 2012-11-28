#!/usr/bin/python

import os
import re
import sys
import urllib2

hr_pat = re.compile( r"-{3,}" )
bullet_pat = re.compile( r"^(\s+)\*\s+(.*)" )
number_pat = re.compile( r"^(\s+)\#\s+(.*)" )
intro_pat = re.compile( r"=+\s*Introduction\s*=+" )
section_pat = re.compile( r"^\s*==\s*([^=]+)\s*==" )
subsection_pat = re.compile( r"^\s*====?\s*([^=]+)\s*====?" )
contents_link_pat = re.compile( r"^\[\#" )
image_pat = re.compile( "^\[(http.*/([^/]*)\.jpg)\]" )
link_pat = re.compile( r".*here\]" )
verbatim_start_pat = re.compile( r"^\{\{\{.*" )
verbatim_end_pat   = re.compile( r"^\}\}\}.*" )

table_pat = re.compile( r"^\|\|.*\|\|.*")

bullet_stack = []
number_stack = []

image_prefix = "images/"

section_index = 1
current_section = 1;
current_subsection = 1;
in_section = False
in_subsection = False

def replaceBold(match):
    return "<emphasis>" + match.group(1) + "</emphasis>"

def replaceItalic(match):
    return "<emphasis>" + match.group(1) + "</emphasis>"

def replaceInlineCode(match):
    return "<literal>" + match.group(1) + "</literal>"

def processImage( url, name ):
    image_path = image_prefix + name + ".jpg"
    fout.write( "<informalfigure>\n" +
                "  <graphic fileref=\"" + image_path + "\"/>\n" +
                "</informalfigure>\n" )

def convertStyle(orig_line):
    line = re.sub( r"`([^`]+)`", replaceInlineCode, orig_line)

    if line == orig_line:
        line = re.sub( r"\*([^*]+)\*", replaceBold, line)
        line = re.sub( r"_([^_]+)_", replaceItalic, line)

    return line

def printIndent( bullets, numbers ):
    if ( bullets ):
        printItemizeIndent()
    if ( numbers ):
        printEnumerateIndent()

def processLine(line):
    image = image_pat.search( line )
    if ( image ):
        processImage( image.group( 1 ), image.group( 2 ) )
    else:
        fout.write( convertStyle( line ) )

def printItemizeIndent ():
    if ( len( bullet_stack ) == 0 ):
        return
    for _ in range(0, bullet_stack[0]):
        fout.write( '  ' )

def popItemizeIndent ():
    bullet_stack.pop( 0 )
    printItemizeIndent()
    fout.write( "</itemizedlist>\n" )
    if ( len( bullet_stack ) > 0 ):
        printItemizeIndent()
        fout.write( "</listitem>\n" )


def printEnumerateIndent ():
    if ( len( number_stack ) == 0 ):
        return
    for _ in range(0, number_stack[0]):
        fout.write( '  ' )

def popEnumerateIndent ():
    number_stack.pop( 0 )
    printEnumerateIndent()
    fout.write( "</orderedlist>\n" )
    if ( len( number_stack ) != 0 ):
        printEnumerateIndent()
        fout.write( "</listitem>\n" )

def closeOpenSubSection ():
    if ( in_subsection ):
        fout.write( "</section>\n" )

def closeOpenSection ():
    closeOpenSubSection()
    if ( in_section ):
        fout.write( "</section>\n" )


def convert ():
    global section_index
    global current_section
    global current_subsection
    global in_section
    global in_subsection

    past_intro    = False
    in_verbatim   = False
    in_table      = False
    in_para       = False

    prevline = None
    line = None
    nextline = fin.readline()
    while ( line != "" ):
        prevline = line
        line = nextline
        nextline = fin.readline()

        if (in_verbatim):
            verbatim = verbatim_end_pat.search( line )
            if (verbatim):
                in_verbatim = False
                fout.write( "]]></programlisting>\n" )
            else:
                fout.write( line )
            continue

        if ( hr_pat.search( line ) or
             contents_link_pat.search( line ) or
             link_pat.search( line ) ):
            continue

        section = section_pat.search( line )
        if ( section ):
            closeOpenSection()
            name = section.group( 1 )
            fout.write( "<section id=\"sect" + str(section_index) + "\">\n" )
            fout.write( "<title>" + section.group( 1 ) + "</title>\n" )
            current_section = section_index
            section_index = section_index + 1
            in_section    = True;
            in_subsection = False
            continue

        subsection = subsection_pat.search( line )
        if ( subsection ):
            closeOpenSubSection()
            name = subsection.group( 1 )
            fout.write( "<section id=\"sect" + str(section_index) + "\">\n" )
            fout.write( "<title>" + subsection.group( 1 ) + "</title>\n" )
            current_subsection = section_index
            section_index = section_index + 1
            in_subsection = True
            continue

        verbatim = verbatim_start_pat.search( line )

        if ( verbatim  ):
            fout.write( "<programlisting><![CDATA[" )
            in_verbatim = True
            continue

        table = table_pat.search( line )
        if ( table ):
            line = convertStyle( line )
            cols = line.split( "||" )
            if ( not in_table ):
                fout.write( "\\begin{tabular}{" )
                for col in cols:
                    if ( col != "" and col != "\n"):
                        fout.write( "| c " )
                fout.write( "| }\n\\hline\n" )
                in_table = True
            first_written = False
            for idx in range( len(cols) ):
                is_col = cols[idx] != "" and cols[idx] != "\n"

                if ( first_written and idx < (len(cols) -1) and is_col):
                   fout.write( "&" )

                if is_col:
                    fout.write( cols[idx] )
                    first_written = True

                if idx == len(cols) -1:
                    fout.write( "\\\\ \\hline\n" )
            continue
        elif ( in_table ):
            fout.write( "\\end{tabular}\n" )
            in_table = False

        bullets = bullet_pat.search( line )
        numbers = number_pat.search( line )

        if ( bullets ):
            indent = len( bullets.group( 1 ) )
            line   = bullets.group( 2 ) + '\n'
            if ( len( bullet_stack ) == 0 or bullet_stack[0] < indent ):
                printItemizeIndent()
                fout.write( "<itemizedlist>\n" )
                bullet_stack.insert( 0, indent )
            elif ( bullet_stack[0] > indent ):
                popItemizeIndent()

        elif ( numbers ):
            indent = len( numbers.group( 1 ) )
            line   = numbers.group( 2 ) + '\n'
            if ( len( number_stack ) == 0 or number_stack[0] < indent ):
                printEnumerateIndent()
                fout.write( "<orderedlist>\n" )
                number_stack.insert( 0, indent )
            elif ( number_stack[0] > indent ):
                popEnumerateIndent()

        else:
            while ( len( bullet_stack ) > 0 ):
                popItemizeIndent()
            while ( len( number_stack ) > 0 ):
                popEnumerateIndent()

        if ( bullets or numbers ):
            printIndent( bullets, numbers )
            fout.write( "<listitem>\n" )
            printIndent( bullets, numbers )
            fout.write( "  <para>\n" )

        if ( not bullets and not numbers and not in_para and line != "\n" ):
            in_para = True
            fout.write( "<para>\n" )

        processLine( line )

        if ( in_para and not bullets and not numbers and line != "\n" and nextline == "\n" ):
            in_para = False
            fout.write( "</para>\n" )

        if ( bullets or numbers ):
            printIndent( bullets, numbers )
            fout.write( "  </para>\n" )
            next_bullets = bullet_pat.search( nextline )
            next_numbers = number_pat.search( line )

            if ( not ( next_bullets and len(bullet_stack) > 0 and len( next_bullets.group( 1 ) ) > bullet_stack[0] ) and
                 not ( next_numbers and len(number_stack) > 0 and len( next_numbers.group( 1 ) ) > number_stack[0] ) ):
                printIndent( bullets, numbers )
                fout.write( "</listitem>\n" )

def printFooter ():
    closeOpenSection()
    fout.write( "</chapter>\n" )

def printHeader (name):
    fout.write( "<?xml version='1.0' encoding=\"UTF-8\"?>\n" )
    fout.write( "<!DOCTYPE chapter PUBLIC \"-//OASIS//DTD DocBook XML V4.5//EN\" \"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd\">\n" )
    fout.write( "\n<chapter id=\"" + name + "\">\n" )


fin = open( sys.argv[1], "r")
fout = open( sys.argv[2], "w" )

printHeader( sys.argv[3] )
convert()
printFooter()