// Project internal package name
package net.devsupport.actioninvader.api

// Standard import for macro serialization
import net.devsupport.serialization.macros.MacroSerialization
import scala.collection.mutable.ArrayBuffer
import net.devsupport.gesturedetection.api._
import net.devsupport.gesturedetection.api.internal._
// API individual classes
// Definition of messages and containing argument with type definitions

/**
 * @author Krystian.Graczyk
 *
 */
case class GestureBlock(gestureList: Seq[Gesture], x: Int, y: Int)
case class Gamestate(gestureBlockArray: Seq[GestureBlock], lifepoints: Int, points: Int, timer: Int, state: Int)

// API serializer extends the imported macro serialization
object ActionInvaderSerialization extends MacroSerialization {
  import GestureDetectionSerialization._
  // define one format for each serializable message
  // follow the shema
  // implicit val NAMEOFMESSAGEFormat = createFormat[NAMEOFMESSAGE]
  implicit val GestureBlockFormat = createFormat[GestureBlock]
  implicit val GamestateFormat = createFormat[Gamestate]

  // define one deserializer for all formats listed above
  implicit val ActionInvaderDeserializer = createDeserializer
}
