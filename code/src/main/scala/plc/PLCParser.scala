package plc

import scala.util.parsing.combinator._
import plc.ir._


trait ElementParser extends JavaTokenParsers with PackratParsers {
  override val skipWhitespace = true
  lazy val charBoundary: Parser[String] = literal("@@")
  lazy val locBoundary: Parser[String] = literal("%%")
  
  /**
   * TODO: right now we only allow alphabetic characters, space, ', and - in
   *       names. This is smaller than we want.
   */
  lazy val name: Parser[String] = """[a-zA-Z\- ']+""".r 
  lazy val character: PackratParser[Character] = 
    charBoundary~"""\s*""".r~>name<~"""\s*""".r~charBoundary ^^ {case aName => Character(name=aName)}
  lazy val location: PackratParser[Location] = 
    locBoundary~"""\s*""".r~>name<~"""\s*""".r~locBoundary ^^ {case aName => Location(name=aName)}
  
  lazy val el: PackratParser[Element] = (
	character | location
  )
}

/**
 * TODO: At the moment, this discards info.
 */
trait InfoParser extends ElementParser {
  override val skipWhitespace = false	
  lazy val infoTagOpen: Parser[String] = literal("[[")
  lazy val infoTagClose: Parser[String] = literal("]]")
  
  lazy val infoContentOpen: Parser[String] = literal("((")
  lazy val infoContentClose: Parser[String] = literal("))")
  
  /**
   * Eat up text when there's nothing in our info block 
   */
  
  /*
   * See sceneContents for why set.empty needs type annotation
   */
  lazy val infoContent: Parser[(String, Set[Character], Set[Location])] = {
    (
      infoContentOpen ~ """\s*""".r ~> infoContent
      | character ~ infoContent ^^ {case character ~ rest => 
        (character.name + rest._1, rest._2 + character, rest._3)}
      | location ~ infoContent ^^ {case location ~ rest => 
        (location.name + rest._1, rest._2, rest._3 + location)}
      | """\s*""".r ~ infoContentClose ^^ {case _ => ("",Set.empty[Character], Set.empty[Location])}
      | not(infoContentClose) ~ """.""".r ~ infoContent ^^ {case _ ~ char ~ rest => 
        (char + rest._1, rest._2, rest._3 )}
    )}
  /**
   *  TODO: Currently just spits back a blank element on failing to find a loc/char.
   *  This is the wrong thing to do. Really we want to return an Option[Element]
   *  TODO: Also we're throwing out the info right now as noted below. Oops.
   */
  
  lazy val infoTag: PackratParser[PlaceholderElement] = {(
		  infoTagOpen ~ """\s*""".r ~> name <~ """\s*""".r ~infoTagClose ^^ {case name => PlaceholderElement(name)}
  )}

  /**
   * TODO: Info blocks currently don't retain a list of related chars/locations
   */
  lazy val info: PackratParser[(Info, Set[Character], Set[Location])] = {(
    infoTag ~ infoContent ^^ {case tag ~ content => (Info(target=tag, content=content._1), content._2, content._3)}
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
	lazy val sceneContents: PackratParser[(Set[Character], Set[Location], Set[Info])] = { ( 
	  character ~ sceneContents ^^ {case char ~ rest => (rest._1 + char, rest._2, rest._3)}
	  | location ~ sceneContents ^^ {case loc ~ rest => (rest._1,  rest._2 + loc, rest._3)}
	  | info ~ sceneContents ^^ {case info ~ rest => (rest._1 ++ info._2, rest._2 ++ info._3, rest._3 + info._1)}
	  | character ^^ {case char => (Set(char), Set.empty[Location], Set.empty[Info])}
	  | location ^^ {case loc => (Set.empty[Character], Set(loc), Set.empty[Info])}
	  | info ^^ {case info => (info._2, info._3, Set(info._1))}
	  | not(sceneClose) ~ """.""".r ~> sceneContents 
	  | success((Set.empty[Character], Set.empty[Location], Set.empty[Info]))
	)}
	
	lazy val sceneOpen : Parser[String] = literal("{{")
	lazy val sceneClose: Parser[String] = literal("}}")
	
	/**
	 * TODO: Currently don't support an empty top level scene because that parser turns out to be
	 *       ungodly-difficult to do as I have things currently specified. Also, assumes first location seen is
	 *       scene location
	 */
	lazy val scene: Parser[Scene] = {
	  sceneOpen~"""\s*""".r~>sceneContents<~"""\s*""".r~sceneClose ^^ {case scn => Scene(scn._2.headOption.getOrElse(Location()), scn._1, scn._3)}  
	}
	
	lazy val doc: PackratParser[MD] = { (
	    scene~"""\s*""".r ~doc ^^ {case sc~_~rest => Story(sc, rest)}
	    | scene
	)}
} 

/**
 * Separated out to make testing more straightforward
 */
object ElementParserTesting extends ElementParser {
  def apply(s: String): ParseResult[Element] = parseAll(el, s)
}
object InfoParserTesting extends InfoParser