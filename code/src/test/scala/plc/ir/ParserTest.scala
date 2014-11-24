package plc.ir

import org.scalatest._
import edu.hmc.langtools._
import plc._
import plc.PLCParser._

/**
 * TODO: More could be generated programatically
 */

trait TestValues {
  val placeHolderName1 = "foo bar"
  val placeHolderName2 = "zip zoom"
    
  val placeHolderInfo1 = "baz"
  
  val baseCharacter1 = Character(name=placeHolderName1)
  val extChar1 = baseCharacter1.copy(info=List(placeHolderInfo1))
  
  val baseCharacter2 = Character(name=placeHolderName2)
  val extChar2 = baseCharacter2.copy(info=List(placeHolderInfo1))
  
  val baseLoc1 = Location(name=placeHolderName1)
  val extLoc1 = baseLoc1.copy(info=List(placeHolderInfo1))
  
  val placeHolderText = "lorem ipsum" 

  def character1 = "@@" + placeHolderName1 + "@@"
  def character2 = "@@" + placeHolderName2 + "@@"
  def info(contained: String = "") = "{ " + contained + " " + placeHolderInfo1 + " }"
  def location = "%%" + placeHolderName1 + "%%"
  def scene(contained: String = "") = "{{ " + contained + " }}"
}
	

/**
 * The goal here is to eliminate the problem common to Markdowns of poor specification:
 * We should, eventually, cover things thoroughly enough that the parser is fully specified.
 * For now, however, we're settling for "well specified enough to reasonably proceed"
 */
class FullParserTest extends FunSpec with LangParseMatchers[MD] with TestValues {
  override val parser = PLCParser apply _
  describe("A text") {
    it("can be just one scene") {
      // No extra content
      // Currently unsupported; see PLCParser docs
      //program(placeHolderText) should parseAs(Scene())
      program(scene(placeHolderText)).parseResult match {// should parseAs(Scene())
        case NoSuccess(msg, next) => println(msg)
        case Success(_,_) => println("weee")
      }
      
      // Just characters
      program(scene(character1)) should parseAs(Scene(characters=Set(baseCharacter1)))
      program(scene(character1 + " " + character2)) should 
      	parseAs(Scene(characters=Set(baseCharacter1, baseCharacter2)))
      
      // Mix character and text
      program(scene(character1 + " " + placeHolderText)) should 
      	parseAs(Scene(characters=Set(baseCharacter1)))
      
      program(scene(character1 + " " + placeHolderText + character2)) should
      	parseAs(Scene(characters=Set(baseCharacter1, baseCharacter2)))
      
      /*
      // Just info
      program(scene(info())) should parseAs(Scene(info=List(placeHolderInfo1)))
      program(scene(info() + " " + info())) should parseAs(Scene(info=List(placeHolderInfo1, placeHolderInfo1)))
      
      // Character embedded in info    
      program(scene(info(character1))) should parseAs(Scene(characters=Set(extChar1)))      
      
      // Mixed characters and info
      program(scene(info(character1) + " " + info())) should 
      	parseAs(Scene(characters=Set(extChar1), info=List(placeHolderInfo1)))
      
      program(scene(info(character1) + " " + info() + " " + info(character2))) should 
      	parseAs(Scene(characters=Set(extChar1, extChar2), info=List(placeHolderInfo1)))
      */
      // For now just basic location testing, because it's not a finished 
      // product so we're white-box testing and happen to know that the Location 
      // implementation is essentially identical to the character impl.      
      program(scene(location)) should parseAs(Scene(place=baseLoc1))
    }
  }
}

/**
 *   Super basic. Not much to see here.
 */
class ElementParserTest extends FunSpec with LangParseMatchers[Element] with TestValues {
  override val parser = ElementParserTesting apply _
  describe("An element") {
    it("can be a character") {
      program(character1) should parseAs(baseCharacter1)
    }
    
    it("can be a location") {
      program(location) should parseAs(baseLoc1)
    }
  }
} 