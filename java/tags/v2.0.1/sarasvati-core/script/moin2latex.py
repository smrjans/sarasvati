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

image_prefix = "/home/paul/workspace/thesis/proposal/images/"

scale_dict = { "legend" : 0.6,
               "concepts5" : 0.6,
               "concepts5-3" : 0.6,
               "concepts5-4" : 0.6,
               "concepts-externals2" : 0.5,
               "concepts-externals3" : 0.5,
               "concepts-externals4" : 0.5,
               "concepts-externals5" : 0.5
             }

def replaceBold(match):
    return "\\textbf{" + match.group(1) + "}"

def replaceItalic(match):
    return "\\textit{" + match.group(1) + "}"

def replaceInlineCode(match):
    return "\\verb!" + match.group(1) + "!"

def processImage( url, name ):
    image_path = image_prefix + name + ".jpg"
    if ( not os.path.exists( image_path ) ):
        image_in = urllib2.urlopen( url )
        image_data = image_in.read()
        image_in.close()
        image_out = open( image_path, "w" )
        image_out.write( image_data )
        image_out.close()
        #os.spawnlp( os.P_NOWAIT, "/usr/bin/sam2p", "sam2p", jpg_name, "/tmp/" + name + ".eps" )

    scale = 0.8
    if ( scale_dict.has_key( name ) ):
        scale = scale_dict[ name ]

    fout.write( "\\begin{center}\n" +
                "\n\\includegraphics[scale=" + str(scale) + "]{" + image_path + "}\n" +
                "\\end{center}\n" )

    #fout.write( #"\n\\begin{figure}\n" +
                #"\\begin{center}\n" +
                #"\\includegraphics[scale=0.5]{/tmp/" + name + ".eps}\n"
                #"\\end{center}\n" +
                #"\\end{figure}\n\n" )

def convertStyle(orig_line):
    line = re.sub( r"`([^`]+)`", replaceInlineCode, orig_line)

    if line == orig_line:
        line = re.sub( r"\*([^*]+)\*", replaceBold, line)
        line = re.sub( r"_([^_]+)_", replaceItalic, line)

    return line


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
    fout.write( "\\end{itemize}\n" )

def printEnumerateIndent ():
    if ( len( number_stack ) == 0 ):
        return
    for _ in range(0, number_stack[0]):
        fout.write( '  ' )

def popEnumerateIndent ():
    number_stack.pop( 0 )
    printEnumerateIndent()
    fout.write( "\\end{enumerate}\n" )

def convert ():
    past_intro  = False
    in_verbatim = False
    in_table    = False
    for line in fin:

        if ( not past_intro ):
            if ( intro_pat.search( line ) ):
                past_intro = True
            else:
                continue

        if (in_verbatim):
            verbatim = verbatim_end_pat.search( line )
            if (verbatim):
                in_verbatim = False
                fout.write( "\end{verbatim}\n" )
            else:
                fout.write( line )
            continue

        if ( hr_pat.search( line ) or
             contents_link_pat.search( line ) or
             link_pat.search( line ) ):
            continue

        section = section_pat.search( line )
        if ( section ):
            name = section.group( 1 )
            fout.write( "\\section{" + name + "}\n" )
            continue

        subsection = subsection_pat.search( line )
        if ( subsection ):
            name = subsection.group( 1 )
            fout.write( "\\subsection{" + name + "}\n" )
            continue

        verbatim = verbatim_start_pat.search( line )

        if ( verbatim  ):
            fout.write( "\\begin{verbatim}\n" )
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
            if ( len( bullet_stack) == 0 or bullet_stack[0] < indent ):
                printItemizeIndent()
                fout.write( "\\begin{itemize}\n" )
                bullet_stack.insert( 0, indent )
            elif ( bullet_stack[0] > indent ):
                popItemizeIndent()

            for _ in range(0,indent):
                fout.write( '  ' )
            fout.write( '\\item ' )

        elif ( numbers ):
            indent = len( numbers.group( 1 ) )
            line   = numbers.group( 2 ) + '\n'
            if ( len( number_stack) == 0 or number_stack[0] < indent ):
                printEnumerateIndent()
                fout.write( "\\begin{enumerate}\n" )
                number_stack.insert( 0, indent )
            elif ( number_stack[0] > indent ):
                popEnumerateIndent()

            for _ in range(0,indent):
                fout.write( '  ' )
            fout.write( '\\item ' )

        else:
            while ( len( bullet_stack ) > 0 ):
                popItemizeIndent()
            while ( len( number_stack ) > 0 ):
                popEnumerateIndent()

        processLine( line )

def printFooter ():
    fout.write( """


\\bibliographystyle{acm}
\\bibliography{bibdata}

\\end{document}
""" )

def printHeader ():
    fout.write( """
\\documentclass[letterpaper,11pt]{article}
\\usepackage{graphicx}
\\setcounter{tocdepth}{2}

\\begin{document}

\\title{Sarasvati: Simple, Extensible and Transparent Workflow}
\\author{Paul Lorenz}
\\date{05/27/2008}
\\maketitle

\\tableofcontents
""" )


fin = open( sys.argv[1], "r")
fout = open( sys.argv[2], "w" )

#printHeader()
convert()
#printFooter()