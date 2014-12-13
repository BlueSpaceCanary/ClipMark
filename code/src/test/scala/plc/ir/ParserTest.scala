package plc.ir

import org.scalatest._
import edu.hmc.langtools._
import plc._
import plc.PLCParser._

/**
 * TODO: More could be generated programatically
 */

trait TestValues {
  val placeHolderText = "lorem ipsum" 
  val placeHolderName1 = "foo bar"
  val placeHolderName2 = "zip zoom"
    
  def character1 = "%%" + placeHolderName1 + "%%"
  def character2 = "%%" + placeHolderName2 + "%%"
    
  val placeHolderInfo1 = Info(PlaceholderElement(placeHolderName1), placeHolderText)
  val placeHolderInfo2 = Info(PlaceholderElement(placeHolderName2), placeHolderText)
  val infoWithChar1     = Info(PlaceholderElement(placeHolderName2), placeHolderText + " " + placeHolderName1)
  
  val baseCharacter1 = Character(name=placeHolderName1)
  
  val baseCharacter2 = Character(name=placeHolderName2)
  
  val baseLoc1 = Location(name=placeHolderName1)
  

  def info(contained: String = "", tagged: String = "") = "[[ " + tagged + "]]((" + contained + " " + " ))"
  def location = "@@" + placeHolderName1 + "@@"
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
      program(scene(placeHolderText)) should parseAs(Scene())
      
      
      // Just characters
      program(scene(character1)) should parseAs(Scene(characters=Set(baseCharacter1)))
      program(scene(character1 + " " + character2)) should 
      	parseAs(Scene(characters=Set(baseCharacter1, baseCharacter2)))
      
      // Mix character and text
      program(scene(character1 + " " + placeHolderText)) should 
      	parseAs(Scene(characters=Set(baseCharacter1)))
      
      program(scene(character1 + " " + placeHolderText + character2)) should
      	parseAs(Scene(characters=Set(baseCharacter1, baseCharacter2)))
      
      
      
      // Just info
      program(scene(info(tagged=placeHolderName1))) should parseAs(
          Scene( info = collection.mutable.Set( Info( PlaceholderElement( placeHolderName1) ) ) )
      )
      
      
     program(scene(
         info(placeHolderInfo1.content,placeHolderInfo1.target.name) + " " + 
         info(placeHolderInfo2.content, placeHolderInfo2.target.name)
      )) should parseAs(
         Scene(info=collection.mutable.Set(placeHolderInfo1, placeHolderInfo2))
      )
      
      // Character embedded in info    
      program(scene(
          info(placeHolderInfo1.content + " " + character1, infoWithChar1.target.name)
      )) should parseAs(
          Scene(
              characters=Set(baseCharacter1),
              info=collection.mutable.Set(infoWithChar1)
          )
      )  
      
      
      // Mixed characters and info  
      program(scene(
          character2 + info(placeHolderInfo1.content + " " + character1, infoWithChar1.target.name)
      )) should parseAs(
          Scene(
              characters=Set(baseCharacter1, baseCharacter2),
              info=collection.mutable.Set(infoWithChar1)
          )
      )  
      
      // For now just basic location testing, because it's not a finished 
      // product so we're white-box testing and happen to know that the Location 
      // implementation is essentially identical to the character impl.      
      program(scene(location)) should parseAs(Scene(place=baseLoc1))
    }
    it("can be multiple scenes") {
       program("   " + scene(placeHolderText) + " " + scene(placeHolderText)) should parseAs(Story(Scene(), Scene()))
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