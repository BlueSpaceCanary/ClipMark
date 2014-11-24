package plc

import scala.util.parsing.combinator._
import plc.ir._


trait ElementParser extends JavaTokenParsers with PackratParsers {
  lazy val charBoundary: Parser[String] = literal("@@")
  lazy val locBoundary: Parser[String] = literal("%%")
  
  /**
   * TODO: right now we only allow alphabetic characters, space, ', and - in
   *       names. This is smaller than we want.
   */
  lazy val name: Parser[String] = """[a-zA-Z\- ']+""".r 
  lazy val character: PackratParser[Character] = 
    charBoundary~name~charBoundary ^^ {case _~aName~_ => Character(name=aName)}
  lazy val location: PackratParser[Location] = 
    locBoundary~name~locBoundary ^^ {case _~aName~_ => Location(name=aName)}
  
  lazy val el: PackratParser[Element] = (
	character ^^ {case char => char}
	| location ^^ {case location => location}
  )
}

/**
 * TODO: At the moment, this discards info.
 */
trait InfoParser extends ElementParser {
  lazy val infoOpen: Parser[String] = literal("{")
  lazy val infoClose: Parser[String] = literal("}")
  
  /**
   * Eat up text when there's nothing in our info block 
   */
  lazy val emptyInfoClose: Parser[String] = (
		  infoClose | 
		  """.""".r ~ emptyInfoClose ^^ {case _~emptyInfoClose => "}"}
  )
  /**
   *  TODO: Currently just spits back a blank element on failing to find a loc/char.
   *  This is the wrong thing to do. Really we want to return an Option[Element]
   *  TODO: Also we're throwing out the info right now as noted below. Oops.
   */
  lazy val info: PackratParser[Element] = {(
    infoOpen ~ el ~ infoClose ^^ {case _~element~_ => element} 
    | infoOpen ~ emptyInfoClose ^^ {case _~_ => new Element()}
  )}
}


/**
 * Claims to fully parse my specified language per ParserTest. Actually dumps
 * some stuff right now
 * TODO: dumped stuff is basically all info tags, because those have the weird
 *       issue of actually being inverted: the info parser needs to parse its 
 *       child (location, character, whatever), then jam the info into that.
 *       Except if it doesn't find anything, it needs to get eaten by the scene
 *       somehow. Complicated, so left out for now
 */
object PLCParser extends JavaTokenParsers with PackratParsers with InfoParser {
	def apply(s: String): ParseResult[MD] = parseAll(doc, s)
	
	/*
	 *  Interesting type inference note: these parsers break *badly* if type isn't 
	 *  specified very precisely. So, when I used Set.empty instead of 
	 *  Set.empty[Location], and the type inferred was _<:Set[Location], typechecking failed.
	 */
	lazy val sceneContents: PackratParser[(Set[Character], Set[Location])] = { ( 
	  character ~ sceneContents ^^ {case char ~ rest => (rest._1 + char, rest._2)}
	  | location ~ sceneContents ^^ {case loc ~ rest => (rest._1,  rest._2 + loc)}
	  | character ^^ {case char => (Set(char), Set.empty[Location])}
	  | location ^^ {case loc => (Set.empty[Character], Set(loc))}
	  | info ^^ {case _ => (Set.empty[Character], Set.empty[Location])}
	  | not(sceneClose) ~ """.""".r ~ sceneContents ^^ {case _~_~rest => rest}
	  | success((Set.empty[Character], Set.empty[Location]))
	)}
	
	lazy val sceneOpen : Parser[String] = literal("{{")
	lazy val sceneClose: Parser[String] = literal("}}")
	
	/**
	 * TODO: Currently don't support an empty top level scene because that parser turns out to be
	 *       ungodly-difficult to do as I have things currently specified
	 */
	lazy val scene: Parser[Scene] = {
	  sceneOpen~sceneContents~sceneClose ^^ {case _~scn~_ => Scene(scn._2.headOption.getOrElse(Location()), scn._1)}  
	}
	
	lazy val doc: PackratParser[MD] = { (
	    scene~doc ^^ {case sc~rest => Story(sc, rest)}
	    | scene ^^ {case sc => sc}
	)}
} 

/**
 * Separated out to make testing more straightforward
 */
object ElementParserTesting extends ElementParser {
  def apply(s: String): ParseResult[Element] = parseAll(el, s)
}
object InfoParserTesting extends InfoParser