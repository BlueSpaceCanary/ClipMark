package plc.semantics

import plc.ir._
import scala.io.Source

class Semantics(input: AST) {
  val output = pass3(pass2(pass1(input)))
  
	def pass1(ast: AST): Appendix = ast match {
	  case scene: Scene => {
	    Appendix(collection.mutable.ListBuffer(scene), collection.mutable.Set() ++ scene.characters, collection.mutable.Set(scene.place))
	  }
	  case Story(scene, rest) => {
	    val restEval = pass1(rest)
	    restEval.copy(scene +: restEval.scenes, 
	    			  restEval.chars ++ scene.characters,
	    			  restEval.locs + scene.place)
	  }
	}
	
	/**
	 * Currently pass2 is O(c*s + l*s) where c is the number of characters,
	 * s is the number of scenes, and l is the number of locations, because
	 * for each character we iterate through all scenes and find the ones she
	 * occurs in, then do the same for each location, in order to build their 
	 * secondary lists.
	 * 
	 * Also links scenes and characters
	 */
	def pass2(app: Appendix): Appendix = {
	  val updatedApp = Appendix(app)
	  app.chars.foreach(char => {
		  app.scenes.foreach(scene =>{
		    if(scene.characters.contains(char)) {
		      // lol fake-assignment, I could actually chain these. Oh, Scala...
		      updatedApp -= char 
		      updatedApp += char.copy(scenes = char.scenes + scene, 
		          					  locations = char.locations + scene.place,
		          					  info = char.info ++ scene.info.filter(_.target.name == char.name))
		    }
		  })
	  })

	  app.locs.foreach{case loc: Location => {
	    println("******" + loc)
	    println("xxxxxxx" + app.locs)
	    app.scenes.foreach(scene => {
	    	if(scene.place == loc && scene.place.name != "") {
	    	  updatedApp -= loc
	    	  updatedApp += loc.copy(scenes = loc.scenes + scene, 
	    	        				 visitors = loc.visitors ++ scene.characters,
	    	        				 info = loc.info ++ (scene.info.filter(_.target.name == loc.name)))
	    	}
	    })
	  }}
	    
	  return updatedApp 
	}
	
	/**
	 * Links info not tied to a char or loc to the scenes instead
	 */
	def pass3(app: Appendix): Appendix = {
	  val updatedApp = app.copy()
	  app.scenes.foreach(scene => {
	    scene.info --= scene.info.filter(_.target.name != "")
	  })
	  return updatedApp
	}
}

object Semantics {
  def apply(input: AST) = new Semantics(input)
}
object Runner extends App {
  val inputFile = Source.fromFile(args(0)).mkString
  val parsed = plc.PLCParser(inputFile)
  
  try {
    val parsedValue = parsed.get
      println(Semantics(parsedValue).output.toString)
  } catch {
    case e: NoSuchElementException => throw new Exception("Failed to parse")
  }
}