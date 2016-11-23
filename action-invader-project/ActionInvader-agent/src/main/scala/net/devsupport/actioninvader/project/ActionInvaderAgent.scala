// Project internal package name
package net.devsupport.actioninvader.project

// Import dependencies for reading the config, use framework support for agents
// ,the project specific API and some scala imports
import net.devsupport.config.Config
import net.devsupport.framework.{ Agent, AgentInfo }
import net.devsupport.actioninvader.api._
import net.devsupport.gesturedetection.api._
import net.devsupport.gesturedetection.api.internal._
import net.devsupport.hci.keyboard.api._
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import akka.actor.Cancellable;
import scala.io.Source
import scala.math._

/**
 * Class that implements the game logic consisting of the creation of gestureblocks,
 * the completion of gestureblocks and the lifepoint and timer handler.
 * @author Krystian.Graczyk
 */
// Agent main class, takes agent config as argument
class ActionInvaderAgent(val config: Config) extends Agent with ActionInvaderAgentConfig {

  // Make the Serialization available by importing it
  import GestureDetectionSerialization._
  import ActionInvaderSerialization._
  // Set agents name
  val agentInfo = AgentInfo("action-invader")

  // Initialize Instancevariables
  var startFlag = 0
  var gestureBlockArray = new ArrayBuffer[GestureBlock]()
  var lifepoints = 5
  var timer = 0
  var points = 0
  var gestureBlockPositionUpdater: Cancellable = null
  var gameTimeUpdater: Cancellable = null
  var gameStateUpdater: Cancellable = null

  // Subscribe one group (or more) to listen to
  subscribe(Groups.Gesture)(GestureDetectionDeserializer)
  subscribe(Groups.Gamestate)(ActionInvaderDeserializer)

  /**
   * Method that starts all the necessary schedules to let the game run.
   */
  def startGame() = {
    import context.dispatcher

    pipelineNewGestureBlock

    // Scheduler that updates the position for the GestureBlocks
    // and checks if the first GestureBlock has reached it's limit. If it 
    // did it will be dropped out of the pipeline and the lifepoints will be reduced by 1
    gestureBlockPositionUpdater = context.system.scheduler.schedule(100.millisecond, 30.millisecond) {
      // checks if the gestureBlockArray isn't empty before he tries 
      // to iterate over them and change the position of the gestureblocks.
      if (!gestureBlockArray.isEmpty) {
        gestureBlockArray = gestureBlockArray.map(gestureBlock =>
          GestureBlock(gestureBlock.gestureList, gestureBlock.x, gestureBlock.y + 1))
        if (gestureBlockArray(0).y >= Settings.canvasSizeY) {
          gestureBlockArray = gestureBlockArray.drop(1)
          pipelineNewGestureBlock
          log.info("gestureblock reached the bottom of the screen.")
          lifepoints -= 1
          sendGamestate(2)
          checkLifepoints
        }
      }
    }

    // Scheduler that updates the gametime
    gameTimeUpdater = context.system.scheduler.schedule(0.second, 1.second) {
      timer += 1
    }

    // Scheduler that publishes the gamestate in a specific period of time
    gameStateUpdater = context.system.scheduler.schedule(0.second, 30.millisecond) {
      sendGamestate(0)
    }
  }

  /**
   * Cancels the schedules which are necessary to let the game run.
   */
  def pauseGame() = {
    //checks if the scheduler aren't null before he tries to cancell them.
    if (gestureBlockPositionUpdater != null && gameTimeUpdater != null && gameStateUpdater != null) {
      if (!gestureBlockPositionUpdater.isCancelled) {
        gestureBlockPositionUpdater.cancel()
      }
      if (!gameTimeUpdater.isCancelled) {
        gameTimeUpdater.cancel()
      }
      if (!gameStateUpdater.isCancelled) {
        gameStateUpdater.cancel()
      }
    }
  }

  /**
   * Resets all values that represent the Gamestate and cancels all schedules.
   */
  def resetGame() = {
    gestureBlockArray = new ArrayBuffer[GestureBlock]()
    lifepoints = 5
    points = 0
    timer = 0
    pauseGame
  }

  /**
   *  Changes a random List of Integers to a List of Gestures
   * @param len
   * @return
   */
  def randomGestureBlock(len: Int) = {
    import scala.util.Random
    val intSeq = List.fill(len)(Random.nextInt(3))

    val gestureList = intSeq.map(x => x match {
      case 0 => LeftHand()
      case 1 => RightHand()
      case 2 => LeftFoot()
      case 3 => RightFoot()

    })
    gestureList
  }

  /**
   * Method checks if the input gesture is equal to
   * the expected first gesture of the pipeline.
   * If it is it will be dropped out of the pipeline. If it's not and
   * the configparameter LoseLifepointsIfWrongGesture is set to 1
   *  the lifepoints will be reduced by 1.
   * Furthermore if the first GestureBlock is completely
   * empty it will be dropped as well.
   * @param gesture
   */
  def checkGesture(gesture: Gesture) {
    // checks if the gestureBlockArray isn't empty before he tries 
    // to compare the incoming gesture with the first in the Array
    if (!gestureBlockArray.isEmpty) {
      if (gestureBlockArray(0).gestureList(0) == gesture) {
        calculatePoints(gestureBlockArray(0).y)
        gestureBlockArray(0) = GestureBlock(gestureBlockArray(0).gestureList.drop(1), gestureBlockArray(0).x + Settings.gestureImageSizeX, gestureBlockArray(0).y)
        sendGamestate(1)
        if (gestureBlockArray(0).gestureList.isEmpty) {
          gestureBlockArray = gestureBlockArray.drop(1)
          pipelineNewGestureBlock
        }
      } else {if (Settings.LoseLifepointsIfWrongGesture == 1) {
        lifepoints -= 1
        
        checkLifepoints
      }
	sendGamestate(2)
     }
    }
  }

  /**
   * Calculates the added points depending on the 
   * position of the gesture that got processed.
 * @param gestureY
 */
def calculatePoints(gestureY: Int) {
    points += (Settings.canvasSizeY - gestureY) / 100 + 1
  }

  /**
 * puts a new gesture(block) into the pipeline with a specific
 *  length depending on the reached point status.
 */
def pipelineNewGestureBlock() {
    var amount: Int = min(points / 10, Settings.maxGestureAmount)
    if (amount < 1) amount = 1
    gestureBlockArray += GestureBlock(randomGestureBlock(amount), Random.nextInt(Settings.canvasSizeX - (Settings.gestureImageSizeX * amount)), 0)
  }

  /**
   * checks if the Lifepoints reached 0 if they did the gamestate is published
   * one last time before it gets reseted and the game stops.
   */
  def checkLifepoints() = {
    if (lifepoints <= 0) {
      sendGamestate(3)
      resetGame
      startFlag = 0
    }
  }

  /**
   * Method that publishes the Gamestate
   * @param state (0 = standard state, 1 = a right gesture was recognized,
   * 2 = a wrong gesture was recognized, 3 = game was lost, 4 = game got reset)
   */
  def sendGamestate(state: Int) = {
    Groups.Gamestate ! Gamestate(gestureBlockArray, lifepoints, points, timer, state)
  }

  /**
   * Has a defined reaction for every input message it receives.
   */
  def receive = {

    // Event for Gesture LeftHand()
    case LeftHand() =>
      val gesture = LeftHand()
      checkGesture(gesture)
      log.info("LeftHand()")
    // Event for Gesture RightHand()
    case RightHand() =>
      val gesture = RightHand()
      checkGesture(gesture)
      log.info("RightHand()")
    // Event for Gesture LeftFoot()
    case LeftFoot() =>
      val gesture = LeftFoot()
      checkGesture(gesture)
      log.info("LeftFoot()")
    // Event for Gesture RightFoot()
    case RightFoot() =>
      val gesture = RightFoot()
      checkGesture(gesture)
      log.info("RightFoot()")
    // Event for Gesture BothHandsUp()
    case BothHandsUp() =>
      val gesture = BothHandsUp()
      // resets the game if gesture is recognized while game is active
      // and starts the game if gesture is recognized while game is inactive
      if (startFlag == 1) {
        startFlag = 0
        resetGame
        sendGamestate(4)
      } else {
        startFlag = 1
        startGame
      }
      log.info("BothHandsUp()")
  }
}


