package plc.semantics

import plc.ir._

class Semantics {
	def pass1(ast: AST): Appendix = ast match {
	  case scene: Scene => {
	    Appendix(List(scene), scene.characters, Set(scene.place))
	  }
	  case Story(scene, rest) => {
	    val restEval = pass1(rest)
	    restEval.copy(scene +: restEval.scenes, 
	    			  scene.characters ++ restEval.chars,
	    			  restEval.locs + scene.place)
	  }
	}
	
	/**
	 * Currently pass2 is O(c*s + l*s) where c is the number of characters,
	 * s is the number of scenes, and l is the number of locations, because
	 * for each character we iterate through all scenes and find the ones she
	 * occurs in, then do the same for each location, in order to build their 
	 * secondary lists.
	 */
	def pass2(app: Appendix): Appendix = {
	  var updatedApp = app
	  app.chars.foreach(char => {
		  app.scenes.foreach(scene =>{
		    if(scene.characters.contains(char)) {
		      updatedApp = updatedApp - char
		    }
		  })
	  })
	  
	  return updatedApp
	  
	}
}