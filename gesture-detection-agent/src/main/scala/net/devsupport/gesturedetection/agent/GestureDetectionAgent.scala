package net.devsupport.gesturedetection.agent

import net.devsupport.config.Config
import akka.actor.Cancellable;
import net.devsupport.framework.{ Agent, AgentInfo }
import net.devsupport.skeleton.api._
import net.devsupport.skeleton.withjoints.api._
import net.devsupport.gesturedetection.api._
import net.devsupport.gesturedetection.api.internal._
import scala.concurrent.duration._
/**
 * Agent that receives a human skeleton and recognizes specific gestures
 * depending on conditions they have to fulfill. Gestures will only be
 * recognized for the skeleton which id is being hold. The id depends on the
 * last skeleton which registered itself by putting both hands over his head. 
 * @author Krystian.Graczyk
 *
 */
class GestureDetectionAgent(val config: Config) extends Agent with GestureDetectionAgentConfig {
  import SerializationSkeletonPartsWithJoints._
  import GestureDetectionSerialization._
  import context.dispatcher
  val agentInfo = AgentInfo("example-agent")

  // Holds the last gesture
  var gesture: Gesture = null
  // Holds the gesture before the last one
  var lastGesture: Gesture = null
  // Holds the id of the skeleton that is the "playing one"
  var id: Int = -1

  subscribe(Groups.Kinect)(DeserializerSkeletonPartsWithJoints)
  subscribe(Groups.Gesture)(GestureDetectionDeserializer)
  /**
 * Receives the skeleton and divides it into parts to recognize specific gestures
 * depending on conditions they have to fulfill.
 * Regonizeable gestures are: LeftHand(), RightHand(), LeftFoot(), 
 * RightFoot(), BothHandsUp() and a null gesture with both hands and legs down
 * which has to be achieved before the next gesture can be regonized.
 */
def receive = {
    case x: HumanSkeletonWithJoints =>
      val leftHandY = x.leftHand.palm.jointProximal.position.y
      val rightHandY = x.rightHand.palm.jointProximal.position.y
      val leftHandX = x.leftHand.palm.jointProximal.position.x
      val rightHandX = x.rightHand.palm.jointProximal.position.x
      val headY = x.head.cranium.position.y
      val headX = x.head.cranium.position.x
      val leftClavicleY = x.torso.leftClavicle.jointProximal.position.y
      val rightClavicleY = x.torso.rightClavicle.jointProximal.position.y
      val leftFootY = x.leftFoot.metatarsus.jointProximal.position.y
      val rightFootY = x.rightFoot.metatarsus.jointProximal.position.y
      val height = (x.head.cranium.position.y - x.leftFoot.metatarsus.jointProximal.position.y).abs

      if (leftHandY > headY && rightHandY > headY
        && (leftFootY - rightFootY).abs < 0.02
        && (leftFootY < headY) && (rightFootY < headY)
        && (leftClavicleY < headY) && (rightClavicleY < headY)) {
        gesture = BothHandsUp()
        log.info("gesture {}:", gesture)
        id = x.id
        if (gesture != lastGesture) {
          Groups.Gesture ! BothHandsUp()
        }
        lastGesture = gesture
      } else if ((leftClavicleY - leftHandY) < 0 && ((leftHandX - headX).abs > height / 3) && x.id == id) {
        gesture = LeftHand()
        log.info("gesture {}:", gesture)
        if (gesture != lastGesture) {
          Groups.Gesture ! LeftHand()
        }
        lastGesture = gesture
      } else if ((rightClavicleY - rightHandY) < 0 && ((rightHandX - headX).abs > height / 3) && x.id == id) {
        gesture = RightHand()
        log.info("gesture {}:", gesture)
        if (gesture != lastGesture) {
          Groups.Gesture ! RightHand()
        }
        lastGesture = gesture
      } else if ((leftFootY - rightFootY) > 0.2 && x.id == id) {
        gesture = LeftFoot()
        log.info("gesture {}:", gesture)
        if (gesture != lastGesture) {
          Groups.Gesture ! LeftFoot()
        }
        lastGesture = gesture
      } else if ((leftFootY - rightFootY) < -0.2 && x.id == id) {
        gesture = RightFoot()
        log.info("gesture {}:", gesture)
        if (gesture != lastGesture) {
          Groups.Gesture ! RightFoot()
        }
        lastGesture = gesture
      } else if (leftHandY < headY && rightHandY < headY
        && (leftHandY - rightHandY).abs < 0.06
        && (leftFootY - rightFootY).abs < 0.06
        && x.id == id) {
        log.info("gesture: {}", "nothing")
        gesture = null
        lastGesture = null
      }
    case x: Gesture =>
      log.info("gesture: {}", x)

  }
}