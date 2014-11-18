package plc

import scala.util.parsing.combinator._
import plc.ir._

/**
 * Currently does nothing. Just used to get the tests to compile while the spec
 * is written.
 */
object PLCParser extends JavaTokenParsers with PackratParsers {
	def apply(s: String): ParseResult[AST] = parseAll(prog, s)
	
	lazy val prog: PackratParser[MD] = (
	    temp
	)
	
	
	def temp: Parser[MD] = """*""".r^^(_ => Scene())
}

/**
 * Separated out to make testing more straightforward
 */
object ElementParser extends JavaTokenParsers with PackratParsers {
  def apply(s: String): ParseResult[Element] = parseAll(el, s)
  
  lazy val charBoundary: Parser[String] = literal("@@")
  lazy val locBoundary: Parser[String] = literal("%%")
  
  /**
   * TODO: right now we only allow alphabetic characters, space, ', and - in
   *       names. This is smaller than we want.
   */
  lazy val name: Parser[String] = """[a-zA-Z\-\s']""".r 
  
  lazy val el: PackratParser[Element] = (
	charBoundary~name~charBoundary ^^ {case _~aName~_ => Character(name=aName)}
	| locBoundary~name~locBoundary ^^ {case _~aName~_ => Location(name=aName)}
  )
}