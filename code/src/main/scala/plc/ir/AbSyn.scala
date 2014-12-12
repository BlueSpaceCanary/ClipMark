package plc.ir

sealed abstract class AST
sealed abstract class MD extends AST

abstract class Element

case class Story(scene: Scene, rest: MD) extends MD

case class Scene(place: Location = Location(),
                 characters: Set[Character] = Set.empty,
                 info: collection.mutable.Set[Info] = collection.mutable.Set.empty) extends MD
                 
case class PlaceholderElement(name: String = "")
case class Info(target: PlaceholderElement = PlaceholderElement(), content: String = "")
                 
case class Location(name: String = "",
                    altNames: Set[String] = Set.empty,
                    visitors: Set[Character] = Set.empty,
                    info: Set[Info] = Set.empty,
                    scenes: Set[Scene] = Set.empty) extends Element
                    
case class Character(name: String = "",
                     altNames: Set[String] = Set.empty,
                     info: Set[Info] = Set.empty,
                     scenes: Set[Scene] = Set.empty,
                     locations: Set[Location] = Set.empty) extends Element
                     
case class Appendix(scenes: collection.mutable.ListBuffer[Scene] = collection.mutable.ListBuffer.empty, 
                    chars: collection.mutable.Set[Character] = collection.mutable.Set.empty, 
                    locs: collection.mutable.Set[Location] = collection.mutable.Set.empty) {
  /**
   * These make our second pass in our semantics SO MUCH cleaner.
   */
  def -=(char: Character): Appendix = {
    chars -= char
    return this
  }
  def -=(loc: Location): Appendix = {
    locs -= loc
    return this
  }
  
  def +=(char: Character): Appendix = {
    chars += char
    return this
  }
  
  def +=(loc: Location): Appendix = {
    locs -= loc
    return this
  }
  
  override def toString: String = {
    var outString = "\nScenes: \n"
      
    for(i <- 0 to scenes.length - 1) {
      outString += "\t Scene #" + i
      val scene = scenes(i)
      outString += "\n\t\tLocation: " + scene.place
      outString += "\n + \t\t Characters in scene: "
      scene.characters.foreach({case char => {
        outString += "\n\t\t\t" + char.name
      }})
      
      outString += "\n\t\tKey info: "
      scene.info.foreach({case info => {
        outString += "\n\t\t\t" + info.content 
      }})
    }
    
    outString += "\nCharacters: "
    chars.foreach{case char => {
      outString += "\n\t" + char.name
      outString += "\n\tKey info: "
      char.info.foreach{case info => {
        outString += "\n\t\t" + info.content
      }}
    }}
    
    outString += "\nLocations: " 
    
    locs.foreach{case loc => {
      outString += "\n\t" + loc.name
      outString += "\n\tKey info: "
      loc.info.foreach{case info => {
        outString += "\n\t\t" + info.content
      }}
    }}
    
    return outString
  }
} 
                    
                     
