package plc.semantics

import plc.ir._

class Semantics {
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
	  val updatedApp = app.copy()
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
	  
	  app.locs.foreach(loc => {
	    app.scenes.foreach(scene => {
	    	if(scene.place == loc) {
	    	  updatedApp -= loc
	    	  updatedApp += loc.copy(scenes = loc.scenes + scene, 
	    	        				 visitors = loc.visitors ++ scene.characters,
	    	        				 info = loc.info ++ scene.info.filter(_.target.name == loc.name))
	    	}
	    })
	  })
	    
	  
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