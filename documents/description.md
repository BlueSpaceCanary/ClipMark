# Project description and plan

PLAC.md (Plot, Location, And Character markdown) is a markdown
extension for annotating stories. It allows an author (or a reader) to
demarcate each scene, and within scenes note characters, setting, key
information (respectively, some samples from LotR might be "Bilbo
Baggins," "Helm's Deep," "One does not simply walk into Mordor"), and
any other useful information that is recommended to me and not too
difficult to support.

This is used to generate appendices with
information on each character such as key related characters (i.e. who
they show up in the same scenes as most often), information on major
locations such as tagged important information (Bag End might have had
"hobbit hole" marked in-text, for example), and information on scenes
(list of who was there, where it took place, possibly tagged
information on who was there).

We can then provide in-text links to these appendices if desired,
because using the parent markdown language we still support the
typical markdown formatted-text generation (most likely in something
simple like HTML, but possibly TeX if I'm feeling ambitious and have
too much free time).

Ideally, I would also like to implement a method for autogenerating
charts in the spirit of [this xkcd](https://xkcd.com/657/), but that
is **much** more complicated and probably won't happen.

## Motivation

Keeping track of everything in a story can be difficult even for an
author when it gets too complicated. Having a tool for tracking
interactions, relationships, setting use, etc would be really nice. It
can even be used to mark up public domain works to make oneself study
guides or just follow the story better (goodness knows I couldn't keep
track of who had offended whom in *Emma*). 

## Language domain

[See Motivation section](#motivation)

## Language design

[See Project section](#project-description-and-plan)

## Example computations
{{
   blah blah blah &Rivendale& blah blah blah
   $"And my ax!" said @Gimli@$
}}

would give us a scene marked as taking place in Rivendale, with Gimli
present, and the key information that he said "And my axle!"
