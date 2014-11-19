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


The decision to not allow for nested scenes seems to be a little weird to me. Do you have a way in mind for handling flashbacks? Unlike timetravel, flashbacks actually seem like they are something that should be supported as they happen quite often in literature. 

Regarding the "{{ }}" syntax that Christine mentioned, I am also a little unclear about how this syntax works. Are you still doing this from your description document?:

`{{ blah blah blah &Rivendale& blah blah blah $"And my ax!" said @Gimli@$ }}`

If so, I am unclear about how you would differentiate about information that should be attached to the scene, location, or character. It looks, however, that you may have simplified the syntax since you wrote this example, but I couldn't quite tell from your descriptions or your code. In particular, I am not sure if you are still looking at making nested definitions and scenes or info tags with multiple labels inside. It may help us if you would give us a couple of example programs in your new syntax (if these already exist, I applogize but I could not find them).

-- Nick Carter
