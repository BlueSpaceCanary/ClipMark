# Language design and implementation overview

## Language design

### How does a user write programs in your language?

"Programs" isn't quite the right word, since really what a user does
is mark up a text document. Essentially, they use a series of special
symbols (defined in another file in this repository) to mark scenes,
characters, locations, and key information.

### What is the basic computation that your language performs?

The language autogenerates digests, summaries, and notes. Based on the
list of characters/scenes/locations/info, it creates an appendix to
the text that notes things like places a character has been or ways a
character has been described (in a character section), people who have
visited a location (in a location section), or who was present (in a
scene section).

### What are the basic data structures in your DSL, if any? How does a user create and manipulate data?

Objects representing scenes, characters, and locations, and sets
(probably hashsets) collecting all scenes, characters, and locations
in a given document that are built at runtime. However, these are not
user-facing. The mechanism for creating and manipulating data is as
described in the previous section.

### What are the basic control structures in your DSL, if any?

There are none, since it is a purely data-oriented language.

### What kind of input does a program require? What kind of output does a program produce?

A "program," insomuch as the DSL's units can be said to be programs,
is the input. It produces the appendix described
[here](#what-is-the-basic-computation-that-your-language-performs).

### Error handling

The only type of error possible is a failed parse. My current plan is
to issue a warning (e.g. "unclosed scene") and drop the problem
character from the parse to allow at least some output to be
generated.

### What tool support does your project provide?

None is planned, but if I have extra time I may try to write an emacs
major mode for it, mostly for my own convenience.

### Are there any other DSLs for this domain?

Surprisingly, not really. As far as I have found no one else has
really tried this spin on markdown.

## Language implementation

### Internal vs. External

This one is pretty straightforward. As a markdown language, being
external is pretty much mandatory.

### Host language

Scala is the host language because it has nice parser support and good
OO support. I thought about using Haskell, because Scala can't touch
Haskell's wonderful parser combinators, but ultimately it is really
unpleasant on a large text to keep recreating the sets rather than
modifying an existing set.

### Any significant syntax design decisions you've made

The language doesn't really have its own syntax. It follows the spirit
of markdown languages, which are barely a syntax at all.

