package plc

import scala.util.parsing.combinator._
import plc.ir._


trait ElementParser extends JavaTokenParsers with PackratParsers {
  // Sometimes we want whitespace, so we manually decide about it in each parser.
  override val skipWhitespace = true
  lazy val charBoundary: Parser[String] = literal("%%")
  lazy val locBoundary: Parser[String] = literal("@@")
  lazy val whitespaceMatcher: Parser[String] = (
      """( |\t|\n|\r)*""".r
  )
  
  	/**
	 * Fairly simple regex to match arbitrary characters other than special 
	 * characters, and bail out before trailing whitespace. See the overridden 
	 * version in InfoParser and PLCParser if you would like to go dragon hunting
	 */
	lazy val notSpecialCharacter: Parser[String] =
	  """((?!@@|%%).)*((?!\|@@|%%| |\n|\t|\n\r|\r\n).)+""".r
  
  /**
   * TODO: right now we only allow alphabetic characters, space, ', and - in
   *       names. This is smaller than we want.
   */
  lazy val name: Parser[String] = """[a-zA-Z\- ']+""".r 
  lazy val character: PackratParser[Character] = 
    charBoundary~whitespaceMatcher~>name<~whitespaceMatcher~charBoundary ^^ {case aName => Character(name=aName)}
  lazy val location: PackratParser[Location] = 
    locBoundary~whitespaceMatcher~>name<~whitespaceMatcher~locBoundary ^^ {case aName => Location(name=aName)}
  
  lazy val el: PackratParser[Element] = (
	character | location
  )
}

trait InfoParser extends ElementParser {
  override val skipWhitespace = false	
  lazy val infoTagOpen: Parser[String] = literal("[[")
  lazy val infoTagClose: Parser[String] = literal("]]")
  
  lazy val infoContentOpen: Parser[String] = literal("((")
  lazy val infoContentClose: Parser[String] = literal("))")
  
  	/**
	 * Here, as they say, be dragons. This is a horrible evil regex to match any
	 * string without special chars. I hate parsers.
	 * 
	 * Basically, here's what it does: First, grab as many characters without
	 * special meaning in the markdown language as it can. Then, backtrack off
	 * any trailing whitespace it sucked up. We don't worry about leading 
	 * whitespace because it's generally taken care of for us elsewhere. 
	 */
	override lazy val notSpecialCharacter: Parser[String] =
	  """((?!\(\(|\)\)|@@|%%|\[\[|\]\]).)*((?!\(\(|\)\)|@@|%%|\[\[|\]\]| |\n|\t|\n\r|\r\n).)+""".r
	  //See sceneContents for why set.empty needs type annotation

	/**
	 * Parses the content block of an info. This can contain characters and 
	 * locations, so this gets fairly hairy sometimes.
	 * 
	 * TODO?: allow nested info blocks maybe?
	 */
	lazy val infoContent: Parser[(String, Set[Character], Set[Location])] = {(
      infoContentOpen ~ whitespaceMatcher ~> infoContent
      | character ~ infoContent ^^ {case character ~ rest => {
        (character.name + rest._1, rest._2 + character, rest._3)}}
      | location ~ infoContent ^^ {case location ~ rest => {
        (location.name + rest._1, rest._2, rest._3 + location)}}
      | whitespaceMatcher ~ infoContentClose ^^ {case _ =>{("",Set.empty[Character], Set.empty[Location])}}
      | notSpecialCharacter ~ infoContent ^^ {case str ~ rest => {
        (str + rest._1, rest._2, rest._3 )}}
      | whitespaceMatcher ~ infoContent ^^ {case wspc ~ rest => (wspc + rest._1, rest._2, rest._3)}
    )}
 
  lazy val infoTag: PackratParser[PlaceholderElement] = {(
		  infoTagOpen ~ whitespaceMatcher ~> name <~ whitespaceMatcher ~infoTagClose ^^ {case name => PlaceholderElement(name)}
		  | infoTagOpen ~ whitespaceMatcher ~ infoTagClose ^^ {case _ => PlaceholderElement("")}
  )}

  /**
   * TODO: Info blocks currently don't retain a list of related chars/locations
   */
  lazy val info: PackratParser[(Info, Set[Character], Set[Location])] = {(
    infoTag ~ infoContent ^^ {case tag ~ content => (Info(target=tag, content=content._1), content._2, content._3)}
  )}
}


/**
 * Claims to fully parse my specified language per ParserTest.
 */
object PLCParser extends JavaTokenParsers with PackratParsers with InfoParser {
	def apply(s: String): ParseResult[MD] = parseAll(doc, s)
	
	/**
	 * Here, as they say, be dragons. This is a horrible evil regex to match any
	 * string without special chars. I hate parsers.
	 * 
	 * Basically, here's what it does: First, grab as many characters without
	 * special meaning in the markdown language as it can. Then, backtrack off
	 * any trailing whitespace it sucked up. We don't worry about leading 
	 * whitespace because it's generally taken care of for us elsewhere. 
	 */
	override lazy val notSpecialCharacter: Parser[String] = 
	  """((?!\(\(|\)\)|\{\{|\}\}|@@|%%|\[\[|\]\]).)*((?!\(\(|\)\)|\{\{|\}\}|@@|%%|\[\[|\]\]| |\n|\t|\n\r|\r\n).)+""".r
	
	/*
	 *  Interesting type inference note: these parsers break *badly* if type isn't 
	 *  specified very precisely. So, when I used Set.empty instead of 
	 *  Set.empty[Location], and the type inferred was _<:Set[Location], typechecking failed.
	 */
	 /**
	  * Grab anything that can have special meaning within our scene.
	  */
	lazy val sceneContents: PackratParser[(Set[Character], Set[Location], Set[Info])] = {(
	  character ~ sceneContents ^^ {case char ~ rest => {(rest._1 + char, rest._2, rest._3)}}
	  | location ~ sceneContents ^^ {case loc ~ rest => {(rest._1,  rest._2 + loc, rest._3)}}
	  | info ~ sceneContents ^^ {case info ~ rest =>{(rest._1 ++ info._2, rest._2 ++ info._3, rest._3 + info._1)}}
	  | character ^^ {case char =>{(Set(char), Set.empty[Location], Set.empty[Info])}}
	  | location ^^ {case loc => {(Set.empty[Character], Set(loc), Set.empty[Info])}}
	  | info ^^ {case info => {(info._2, info._3, Set(info._1))}}
	  | notSpecialCharacter ~> sceneContents ^^ {case scn => {scn}}
	  | whitespaceMatcher ~> sceneContents ^^ {case scn => {scn}}
	  | success((Set.empty[Character], Set.empty[Location], Set.empty[Info]))
	)}
	
	lazy val sceneOpen : Parser[String] = literal("{{")
	lazy val sceneClose: Parser[String] = literal("}}")
	
	/**
	 * TODO: Currently don't support an empty top level scene because that parser turns out to be
	 *       ungodly-difficult to do as I have things currently specified. Also, assumes first location seen is
	 *       scene location
	 */
	lazy val scene: Parser[Scene] = (
	  sceneOpen ~ whitespaceMatcher ~> sceneContents <~ whitespaceMatcher ~ sceneClose ^^ {case scn => 
	    Scene(scn._2.headOption.getOrElse(Location()), scn._1, collection.mutable.Set.empty ++ scn._3)}
	  | not(sceneOpen) ~ """.""".r ~> scene 
	)
	
	lazy val doc: PackratParser[MD] = { (
	    whitespaceMatcher ~> scene ~ whitespaceMatcher ~ doc ^^ {case sc~_~rest => Story(sc, rest)}
	    | whitespaceMatcher ~> scene <~ whitespaceMatcher
	)}
} 

/**
 * Separated out to make testing more straightforward
 */
object ElementParserTesting extends ElementParser {
  def apply(s: String): ParseResult[Element] = parseAll(el, s)
}
object InfoParserTesting extends InfoParser