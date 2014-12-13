# ClipMark

Syntax guide:

* Character: ```%%name%%```
* Location: ```@@name@@```
* Scene: ```{{ contents }}```
* Information: ```[[targetName]](( contents ))```. Blank target name tags scene

The sample folder contains a very basic example of how to use this, with text courtesy of Project Gutenberg. 

To generate an appendix, type ```sbt "run filename"``` where ```filename``` is the path to your source text file. The appendix will be printed to stdout as with any good Unix utility.
