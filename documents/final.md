# Final Writeup

## Introduction

Essentially, Character Location and Plot Markdown (CLPMark, pronounced
"Clipmark") allows the automatic generation of appendices to collect
key information about characters, locations, and scenes. Since it is
designed in the spirit of a Markdown language, it is lightweight and
can generally stay out of an author's way after a brief adjustment
period.

## Design details

### How does a user write programs in your language?

The language is, in spirit, just a
[markdown language](https://en.wikipedia.org/wiki/Markdown). Obviously,
it is not a Markdown in the truest sense, but it tries to follow the
spirit behind Markdown.

### How does the syntax of your language help users write programs more easily than the syntax of a general purpose language?

As noted above, the language is designed to follow the governing
ideals of Markdown (from now on I'll just say it's a Mark*, in the
spirit of calling Linux a *nix). It's not even in the same realm as a
general purpose language. The only mechanisms accessible to a user are
tags.

### What are the basic data structures?

Characters, scenes, locations, information. They are generated via tags.

### What are the basic control structures?

There aren't any.

### What kinds of input does a program require?

None, the "programs" are embedded within their text.

### Error handling?

There isn't a serious attempt at it. If parsing fails, the program
just bombs out because, with the complexity of nested tags and such,
recovery turns out to be very difficult if tags are misplaced.

### Tool support?

None, right now. If I am bored over break I might write an emacs major
mode for it.

### Are there any other DSLs for this domain?

I haven't been able to find any, but it does seem strange to me that
no one else would have tried this.

## Example programs

A very basic sample program may be found in /samples. The text is
provided courtesy of Project Gutenberg.

## Language implementation

### What host language did you use, and why?

I used Scala as my host language, because it has good functional
programming and parser support (which are, obviously, very useful for
language design). Ideally I would have used Haskell (better parser
support), but unfortunately I ended up needing a fair bit of internal
mutable state for performance reasons (large texts would otherwise
have had potentially very expensive data structure copies happening
quite frequently during pass 2 of the semantic phase).

### Internal or external DSL?

Since it's a Mark*, and therefore meant for marking up plaintext, the
language had to be external.

### Architecture?

Since the language doesn't have any interface or interaction, it
doesn't really have a frontend or a backend.

### Parsing?

The provided text is parsed into a set of Character, Scene, Location,
and Information objects. The 3 semantic phases then create the links
between these objects.

### Intermediate representation?

Uses the same objects as mentioned above, pre-linking.

### Execution?

An execution is a parse, semantic pass set, and then (like any good
Unix utility) a properly formatted print onto stdout. Specifically, a
section for each of all scenes, all characters, and all locations is
printed.

## Evaluation

### How "DSL-y" is your language?

I would argue that a Mark* is inherently very DSL-y, since it can't
even in principle perform general computation.

### What works well in your language? What are you particularly pleased with?

The implementation is slightly embarrassing in places, but I think
that I can be proud of the flexibility of my parser. It captures
probably 90% of what I would like it to capture, and considering that
it has to take essentially arbitrary text as input and do fairly
sophisticated things to understand it, I think that's pretty solid.

### What doesn't/what could be improved?

There are a number of problems noted on the GitHub issue tracker. The
biggest stuff that I don't like are: lack of name flexibility,
slightly annoying info-tagging syntax, lack of distinction between
mentioned locations and location of current scene, inability to change
locations within a scene, lack of appearance/mention counting, and
lack of a query syntax. Because the semantic representation is so
rich, with a few minor tweaks (like adding the aforementioned
counting) it would be possible to ask fairly complex questions about a
text with a halfway decent query language.

If possible I would also like to make the characters used for each
purpose configurable so that users who have them in their text could
change what the language uses rather than change their text. 

### Re-visit original plan

I had to make a few significant tweaks to syntax, almost entirely to
do with info tagging (which turned out to have very painful scoping
issues the way I originally intended to do it). But, I am able to
parse real texts now!

### Where did you run into trouble, and why?

As mentioned above, info syntax really bit me.


