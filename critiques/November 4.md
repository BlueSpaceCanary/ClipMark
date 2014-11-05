* What are one or more things that you like about this project? What's exciting?

  * I think that it's a really compelling project. In particular, it has a
well-defined domain and seems like it could be incredibly useful to authors and
readers alike. Furthermore, given the context of the course, the project seems
extraordinarily well-scoped. It also seems very unique and different given the
other projects in the class.


* What's the balance of language design vs sheer programming / engineering in
  this project?

  * This will depend on how involved the implementation gets, but I would imagine
that the project has an appropriate design/engineering split. There's a lot
of interesting design work to be done around the idea of what the syntax
should look like and what the intermediate representation should look like,
especially given the desire to balance both the utility of the language and
the readability of the original text. This project could easily become a
massive engineering undertaking if it turns into an exercise of creating the
most awesome appendix/compilation of information possible. However, and
you seem to hint at this by referencing the xkcd comic, the appendix can
always be made more awesome as a stretch goal of sorts.


* How can the project maximize the time spent on language design? How to
  focus on interesting, possibly new ideas?

  * This doesn't really seem like a challenge for this project. There's not
much (to my knowledge) in this space of plain-text prose annotation, so
there are tons of interesting design decisions to be made around the
syntax of the language, as well as around the semantics. 


* What are some interesting language design questions that the work will have
  to answer? In other words, what are the design challenges? Which design
  problems' solutions are you looking forward to hearing about at the end of
  the project?

  * How can you allow for a very expressive DSL that allows meaningful
annotation without obscuring the the text for someone interested in
reading the content? This is the beauty of Markdown---that a Markdown document
reads just fine in plain text, and I imagine it's a feature you'd want to
preserve. Also, what will the intermediate representation look like? In other
words, this language is basically all about modeling the details of a story.
How will this language actually represent a story's people, locations, and
events in code?


* What are the primitives in this language?

    * The primitives in this language seem to be people, locations,
and events.


* Do you know of any libraries, languages, or projects that might help
  this project?

    * It might be useful to look into some of the documentation systems for
programming languages (Doxygen, Sphinx, etc.), both to get ideas about
how the syntax might work and to get an idea for how they actually work
from an engineering perspective.

### Additional Questions

* What kinds of configuration options make sense? What do you think seems
  tempting but would potentially be really hairy to implement?

    * I'm not exactly sure what you mean by "configuration options", but
I think that some way to generate the original text without the markup
in it (or any of the fancy generated hyperlinks) would be useful. It
also might be cool to be able to tag comments/annotations with the person
who created them, so that annotations could be filtered by person (or
blamed upon a particular person) in multi-user environments. This last
idea would introduce the idea of keeping around additional metadata about
each annotation, though, which would definitely introduce some overhead.


* If you have any particular opinions on flavors of markdown, which do you
  think I should use as the parent language?

    * I've only ever used GitHub Flavored Markdown, so I don't have much of
an opinion on the various flavors of Markdown. Is one more heavily
utilized within the literature community? In other words, which would be
most useful to authors? Perhaps CommonMark (I think that's the name they
settled on) would be the easiest because it has a clear specification? Or
perhaps it would make msot sense to use the subset of Markdown that is
shared between many/most flavors.


* Are there other taggable features I should consider?

    * It might be useful to track specific important items/objects over time. This
seems functionally identical to tracking characters, which would mean it
wouldn't be a crazy additional engineering effort. It might also be useful
to have some sort of custom tagging (with less structure provided in the
appendix, of course).
