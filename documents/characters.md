## Scene

This is the only not-entirely-in-the-spirit-of-Markdown aspect of the
language, because a scene can be so long. Scenes are opened with a *{*
and closed with a *}*. There is no support for nested scenes.

## Character

A character should be enclosed by a pair of @ symbols on either side
of their name.

## Location

A location name should be tagged by surrounding the name with a pair
of % symbols on either side. Because of the current design of the
language, only the location a scene actually takes place in should be
tagged. There is currently no support for referencing other locations.

## Key information

Key information should be enclosed by {{ on the left and }} on the
right. If the {{ }} encloses a character, the information is assumed
to refer to the character. Otherwise, if it contains a location it is
assumed to refer to the location. Otherwise, it is assumed to be
general information about the scene.
