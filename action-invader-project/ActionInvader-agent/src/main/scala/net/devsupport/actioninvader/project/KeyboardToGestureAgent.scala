package net.devsupport.actioninvader.project
import net.devsupport.config.Config
import net.devsupport.framework.{ Agent, AgentInfo }
import net.devsupport.actioninvader.api._
import net.devsupport.gesturedetection.api._
import net.devsupport.gesturedetection.api.internal._
import net.devsupport.hci.keyboard.api._
/**
 * Agent that transforms Events that he received from the KeyboardEvents Group
 * into expected Gestures for the Gesture group and sends them.
 * Q = LeftHand()
 * W = RightHand()
 * A = LeftFoot()
 * S = RightFoot()
 * T = BothHandsUp()
 * @author Krystian.Graczyk
 * 
 */
class KeyboardToGestureAgent(val config: Config) extends Agent with ActionInvaderAgentConfig {
  // Make the Serialization available by importing it
  import KeyboardSerialization._
  import GestureDetectionSerialization._
  // Set agents name
  val agentInfo = AgentInfo("action-invader")

  subscribe(Groups.KeyboardEvents)(Deserializer)
  subscribe(Groups.Gesture)(GestureDetectionDeserializer)

  def receive = {
    
    case KeyPressed("Q") =>
      val gesture = LeftHand()
      log.info("LeftHand()")
      Groups.Gesture ! gesture
      
    case KeyPressed("W") =>
      val gesture = RightHand()
      log.info("RightHand()")
      Groups.Gesture ! gesture
    
    case KeyPressed("A") =>
      val gesture = LeftFoot()
      log.info("LeftFoot()")
      Groups.Gesture ! gesture
   
    case KeyPressed("S") =>
      val gesture = RightFoot()
      log.info("RightFoot()")
      Groups.Gesture ! gesture
    
    case KeyPressed("T") =>
      val gesture = BothHandsUp()
      log.info("BothHandsUp()")
      Groups.Gesture ! gesture
      
    case KeyPressed(msg) =>
      log.info("not a valid key: " + msg)
  }
}