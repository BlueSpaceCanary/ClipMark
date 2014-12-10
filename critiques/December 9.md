I'm not sure how to use the info tags and info content, or if I'm supposed to. Do I just work with the 
character, location and scene location markers? You should also update your README, and add how to run everything :)

We talked a little about having to use mutable data structures instead of immutable structures. I personally see
no issue. While mutable structures are less functional, there really isn't another way to do it, I think.

For the second question, are you refering to the line
```
scene.info --= scene.info.filter(_.target.name != "")
```
Doesn't seem stylistically or functionally incorrect. Albeit, I'm not exactly sure what the target is, 
but with a little explanation in documentation, I think it would make sense!

As for your Scala code for the semantics, it's quite clean! Overall, good stuff, and I look forward to reading your 
final documentation!
