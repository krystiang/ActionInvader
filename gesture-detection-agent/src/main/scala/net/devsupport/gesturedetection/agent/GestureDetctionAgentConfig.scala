package net.devsupport.gesturedetection.agent

import net.devsupport.framework.Group
import net.devsupport.framework.AgentConfig

trait GestureDetectionAgentConfig extends AgentConfig {
  object Groups {
    val Kinect = config.get[Group]("groups.kinect")
    val Gesture = config.get[Group]("groups.gesture")
  }
}