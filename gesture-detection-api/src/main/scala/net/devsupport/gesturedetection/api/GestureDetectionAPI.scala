/**
 * @author Nick.Diedrich
 *
 */
package net.devsupport.gesturedetection.api

import net.devsupport.serialization.macros.MacroSerialization

package internal {
  sealed trait Gesture
}
import internal._

/**
 * Indicates that the left foot was angled to the side
 *
 */
case class LeftFoot() extends Gesture

/**
 * Indicates that the right foot was angled to the side
 *
 */
case class RightFoot() extends Gesture

/**
 * Indicates that the left hand was angled to the side
 *
 */
case class LeftHand() extends Gesture

/**
 * Indicates that the right hand was angled to the side
 *
 */
case class RightHand() extends Gesture

/**
 * Indicates that the right hand and left hand were raised above the head
 *
 */
case class BothHandsUp() extends Gesture

object GestureDetectionSerialization extends MacroSerialization {
  implicit val LeftFootFormat = createFormat[LeftFoot]
  implicit val RightFootFormat = createFormat[RightFoot]
  implicit val LeftHandFormat = createFormat[LeftHand]
  implicit val RightHandFormat = createFormat[RightHand]
  implicit val BothHandsUpFormat = createFormat[BothHandsUp]
  
  implicit val GestureFormat = createInternalFormat[Gesture]

  implicit val GestureDetectionDeserializer = createDeserializer
}
