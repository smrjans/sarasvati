# NOTE: (Slightly) Non-Standard SVN Layout #

Because Sarasvati has multiple implementations in different languages, the source control is organized somewhat differently from the default layout.

Each langauge has it's own top level folder, under which is the usual branches/ tags/ trunk/ layout. There is also a common/ folder for shared files, such as licenses, database schema and some global documenation.

You can also [browse the SVN repository](http://code.google.com/p/sarasvati/source/browse) as well see a [list of commits](http://code.google.com/p/sarasvati/source/list).


# Command-Line Access #

## Haskell codebase ##

---


If you plan to make changes, use this command to check out the code as yourself using HTTPS:

```
#Project members authenticate over HTTPS to allow committing changes>
svn checkout https://sarasvati.googlecode.com/svn/haskell/trunk/ sarasvati-haskell --username <username>
```

When prompted, enter your generated [googlecode.com password](http://code.google.com/hosting/settings).


Use this command to anonymously check out the latest project source code:

```
# Non-members may check out a read-only working copy anonymously over HTTP.
svn checkout http://sarasvati.googlecode.com/svn/haskell/trunk/ sarasvati-haskell-ro
```

## Java codebase ##

---


If you plan to make changes, use this command to check out the code as yourself using HTTPS:

```
# Project members authenticate over HTTPS to allow committing changes.
svn checkout https://sarasvati.googlecode.com/svn/java/trunk/ sarasvati-java --username <username>
```

When prompted, enter your generated [googlecode.com password](http://code.google.com/hosting/settings).


Use this command to anonymously check out the latest project source code:

```
# Non-members may check out a read-only working copy anonymously over HTTP.
svn checkout http://sarasvati.googlecode.com/svn/java/trunk/ sarasvati-java-ro
```

## GUI and IDE Access ##

This project's Subversion repository may be accessed using many different [client programs and plug-ins](http://subversion.tigris.org/links.html#clients). See your client's documentation for more information.