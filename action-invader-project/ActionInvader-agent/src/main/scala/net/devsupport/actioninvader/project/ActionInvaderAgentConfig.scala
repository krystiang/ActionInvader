// Project internal package name
package net.devsupport.actioninvader.project

// Standard imports for group communication and agent configuration
import net.devsupport.framework.Group
import net.devsupport.framework.AgentConfig

// Define names of groups for group communication
// The concrete values of the groups names are stored in
// src/main/resources/application.conf
// So "Request" is a group object witch is called by a name stored in a value named "groups.request"
trait ActionInvaderAgentConfig extends AgentConfig {
  object Groups {
    val Gesture = config.get[Group]("groups.gesture")
    val KeyboardEvents = config.get[Group]("groups.events")
    val StringEvents = config.get[Group]("groups.strings")
    val Gamestate = config.get[Group]("groups.gamestate")
  }
  
  object Settings {
    val LoseLifepointsIfWrongGesture = config.get[Int]("settings.loseLifepointsIfWrongGesture")
    val canvasSizeX = config.get[Int]("settings.canvasSizeX")
    val canvasSizeY = config.get[Int]("settings.canvasSizeY")
    val gestureImageSizeX = config.get[Int]("settings.gestureImageSizeX")
    val gestureImageSizeY = config.get[Int]("settings.gestureImageSizeY")
    val maxGestureAmount = config.get[Int]("settings.maxGestureAmount")
  }
}
