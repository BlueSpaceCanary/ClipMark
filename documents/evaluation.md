# Preliminary evaluation

**What works well? What are you particularly pleased with?**

I think my parser has ended up being pretty sophisticated. It's hard
to say without full semantic support yet, but I think I did a good job
making a full-featured parser. I also think my tests-as-spec has
worked really well so far.

**What could be improved? For example, how could the user's experience
 be better? How might your implementation be simpler or more
 cohesive?**

Since I don't have working semantics yet, this can only be partially
answered. In terms of the syntax, I'd really like to be less
restrictive, have better nesting support, and build related features
into the semantics based on that nesting.

**Re-visit your evaluation plan from the beginning of the
  project. Which tools have you used to evaluate the quality of your
  design? What have you learned from these evaluations? Have you made
  any significant changes as a result of these tools, the critiques,
  or user tests?**

The only tools I've used are my ScalaTest spec tests.

**Where did you run into trouble and why? For example, did you come up
with some syntax that you found difficult to implement, given your
host language choice? Did you want to support multiple features, but
you had trouble getting them to play well together?**

My info syntax caused a lot of problems. Rather than using scoping I
was forced to write it the way a traditional markdown writes links.

**What's left to accomplish before the end of the project?**

I need to implement the majority of the semantics. Fortunately, most
of the implementation complexity was in the parser. The semantics
require a few distinct passes to do everything properly, but those
passes are conceptually quite simple.
