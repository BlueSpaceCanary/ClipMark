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
