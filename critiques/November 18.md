Overall, I think the design that you have described for your language is good. 
I don't think there is a problem with having a simple IR unless you 
think that changing the IR will make a huge difference in the semantics. One 
thing that I noticed was a confusing element in your specification of 
Key Information. You said: 

  "If the {{ }} encloses a character, the information is assumed to refer to the character. 
  Otherwise, if it contains a location it is assumed to refer to the location. 
  Otherwise, it is assumed to be general information about the scene."

It seems like you are saying either the information is enclosed in {{}} or not, but you have 
three cases. I think I understand what cases you are talking about, but the way that you
specify it here, it seems like the two cases following "Otherwise" are the same. 

-Christine Schubert

---

The decision to not allow for nested scenes seems to be a little weird to me. Do you have a way in mind for handling flashbacks? Unlike timetravel, flashbacks actually seem like they are something that should be supported as they happen quite often in literature. 

Regarding the "{{ }}" syntax that Christine mentioned, I am also a little unclear about how this syntax works. Are you still doing this from your description document?:

`{{ blah blah blah &Rivendale& blah blah blah $"And my ax!" said @Gimli@$ }}`

If so, I am unclear about how you would differentiate about information that should be attached to the scene, location, or character. It looks, however, that you may have simplified the syntax since you wrote this example, but I couldn't quite tell from your descriptions or your code. In particular, I am not sure if you are still looking at making nested definitions and scenes or info tags with multiple labels inside. It may help us if you would give us a couple of example programs in your new syntax (if these already exist, I applogize but I could not find them).

-- Nick Carter

Phil and I had a discussion offline to clarify some of this. It sounds like he is planning to support `{{ blah @@character@@ blah }}`, `{{ blah %%location%% blah }}`, and `{{blah}}` in that order (i.e. a character will get the info if found, if not then a location will be searched for, and if that is not found then it will be attached to the scene. I have a few comments now.

1. Why not support multiple locations and or characters within an "info"? Or, relatedly, shouldn't there be a way to tag an "info" to a location or character when the location or character does not appear in the `{{}}`? It seems like this could be easily accomplished with limited additional syntax and may provide significant additional flexibility.
2. Why is a location not surrounded by `@` signs? They, to me, mean "at" which is much more like a location than a percent. 
3. Why require two symbols? It seems like it would be easier on the writer to use one symbol and, since you are concerned with interfering with already used symbols, simply allow users to escape the special characters. The symbols you are using are rare enough in english that this would seem preferable. Similarly, how about switching scene and info declarations? Scenes I would assume happen less frequently than infos.


Further, it is unclear to me how often you are thinking that someone will make use of the features of this language in writing. Are infos a common occurrence or are they relatively rare? Does a digest look like a synopses?

-- Nick Carter

---

When you say that you will "drop the problem character" from a parse when an error is encountered, do you mean character of text (the particular character at which the parse failed) or do you mean character in the story? I would recommend against ignoring a particular character of text, as it could produce some strange and unwanted results that would substantially mess up the generated appendix. In the former case, though, I think it would be unnecessary to throw out an entire character. If a character is tagged in 100 places, for example, and there's a parse error in only one of those locations, it doesn't seem necessary to throw out the entire character; rather, it makes sense to just throw out that one tag (or perhaps put in a placeholder "unable to parse" message wherever that tag would have appeared).

I also find your discussion of Scala vs. Haskell interesting. I certainly agree that Haksell's parser combinators are way more awesome-feeling than Scala's. I would think that there are probably ways that you could build this language that would not be horribly inefficient with Haskell's laziness (not quite sure exactly how you would do it), but I agree that the naive port of your current approach wouldn't be very efficient. Given that you are probably only growing data structures (I can't see right away why you would want to remove an item from a set), there are probably data structures (like lists, if I recall correctly) which could grow without needing to copy everything. At the same time, though, you would have to be very conscious of Haskell's laziness when developing, and I don't think it provides any major benefits over Scala (except some nicer parser combinators and simpler algebraic data types/pattern matching).

I agree with the confusion about how exactly the {{ }} syntax would be used and what exactly it would be used for. One way to handle it would be to require that absolutely everything in your language is contained between the double curly braces. If you do this, then you could even have a user-configurable option (at some point down the road) to replace {{ }} with something else, especially if the document being marked up uses those characters elsewhere. If you enclose everything in one particular set of delimiting characters, then you can know that everything within those characters is part of your language and not part of the source text, which would allow you to go with slightly characters (for example, perhaps a single @ and a single %) for the rest of the language. In general, though, I think it would be really beneficial to pick a small paragraph or a short poem and mark it up using the syntax, as it's tough to envision what exactly all of this would look like when embedded in a document. (Plus, building up an example or too would be useful during testing anyways, so it wouldn't be wasted work.)

Is your { } syntax for a scene too commonplace/general? In other words, are those characters likely to appear in the main text that's being marked up? I suppose I can't think of a case where curly braces are commonly used in literature, but it certainly seems possible that those characters could show up somewhere. Also, if a scene can be particularly long, matching up curly braces might not be the easiest for the reader to follow. Perhaps some variation of a `begin:SceneName` and `end:SceneName` would be easier to follow. It would also make searching through a document (using CTRL-F or its equivalent) a little easier. I think the @@ and %% syntax works, but is there a reason that you chose @@ for characters and %% for locations? I've always naturally associated @ with locations ("I'm @ home right now"), so I would find it clearer to use @@ for locations.

With respect to the IR, I don't see any particular issues or flaws. It appears to clearly support the information you've discussed in a way that wouldn't be too difficult to construct, and it would provide a solid base from which to start the semantics. I agree that the semantics of the language could end up being reasonably complicated at some point, but I don't immediately see how you could change the IR to make the semantics significantly easier. (Did you have any particular ideas about what you would change with the IR?)

-Michael Culhane

(Note: This is about an hour late past the Tuesday 11:59pm deadline. Howeveer, Philip explained that a 24 hour extension on this week's critique would be okay, as he had a 24 hour extension on this week's work. In retrospect, I've realized that I planned to critique my own team this week, instead of having all three of us critique this project. I think that's okay, though---we did the vast majority of our work/discussion/thinking together last week, so my critiquing effort is probably best spent here anyways.)
