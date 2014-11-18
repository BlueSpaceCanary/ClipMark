package plc.ir

sealed abstract class AST
sealed abstract class MD extends AST

class Element()

case class Scene(place: Location = Location(),
                 characters: List[Character] = List.empty,
                 info: List[String] = List.empty) extends MD
                 
case class Location(name: String = "",
                    altNames: List[String] = List.empty,
                    visitors: List[Character] = List.empty,
                    info: List[String] = List.empty,
                    scenes: List[Scene] = List.empty) extends Element
                    

case class Character(name: String = "",
                     altNames: List[String] = List.empty,
                     info: List[String] = List.empty,
                     scenes: List[Scene] = List.empty,
                     locations: List[Location] = List.empty) extends Element
                     
