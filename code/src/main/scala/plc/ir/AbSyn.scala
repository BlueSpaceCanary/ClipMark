package plc.ir

sealed abstract class AST
sealed abstract class MD extends AST

abstract class Element

case class Story(scene: Scene, rest: MD) extends MD

case class Scene(place: Location = Location(),
                 characters: Set[Character] = Set.empty,
                 info: Set[Info] = Set.empty) extends MD
                 
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
                     
case class Appendix(scenes: List[Scene] = List.empty, 
                    chars: Set[Character] = Set.empty, 
                    locs: Set[Location] = Set.empty) {
  /**
   * These make our second pass in our semantics SO MUCH cleaner.
   * TODO: but they might make performance awful depending on if Scala is smart
   *       like Haskell :( need to stress test
   */
  def -(char: Character): Appendix = this.copy(chars = chars - char)
  def -(loc: Location): Appendix = this.copy(locs = locs - loc)
}
                    
                     
