package piconot

import java.io.File

import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp

/**
 *  This is an intentionally bad internal language, but it is picobot complete.
 */

object GlobalVars {
  var UserState = 0
  var rules = List.empty[Rule]
  def state = UserState * 4  
  def nState = state
  def wState = state + 1
  def sState = state + 2
  def eState = state + 3

  
}

abstract class GoType {
  val specialSurrounding : RelativeDescription
  
  def stateFromUserState(userState:Int, direction:Int) : State = {
    State((userState * 4 + direction).toString)
  }

  def fallThroughRule(initialState:Int) : Rule = {
    Rule(State(initialState.toString), 
         Surroundings(Anything, Anything, Anything, Anything),
         StayHere,
         State((initialState + 4).toString))
  }

  def addRules(movement:MovementType) = {
    val ruleN = Rule(State(GlobalVars.nState.toString), 
                     Surroundings(specialSurrounding, Anything, Anything, Anything),
                     movement.move(GlobalVars.nState),
                     movement.nextState(GlobalVars.nState))
    val ruleW = Rule(State(GlobalVars.wState.toString), 
                     Surroundings(Anything, Anything, specialSurrounding, Anything),
                     movement.move(GlobalVars.wState),
                     movement.nextState(GlobalVars.wState))
    val ruleS = Rule(State((GlobalVars.sState).toString), 
                     Surroundings(Anything, Anything, Anything, specialSurrounding),
                     movement.move(GlobalVars.sState),
                     movement.nextState(GlobalVars.sState))
    val ruleE = Rule(State((GlobalVars.eState).toString), 
                     Surroundings(Anything, specialSurrounding, Anything, Anything),
                     movement.move(GlobalVars.eState),
                     movement.nextState(GlobalVars.eState))
    val allRules = List(ruleN, ruleW, ruleS, ruleE,
                        fallThroughRule(GlobalVars.nState),
                        fallThroughRule(GlobalVars.wState),
                        fallThroughRule(GlobalVars.sState),
                        fallThroughRule(GlobalVars.eState))
    GlobalVars.rules = GlobalVars.rules ++ allRules
    GlobalVars.UserState = GlobalVars.UserState + 1
  }

  def To(newUserState:Int) = {
    addRules(ToMovement(newUserState)) 
  }
  def TO(newUserState:Int) = {
    addRules(TOMovement(newUserState)) 
  }
  def T0(newUserState:Int) = {
    addRules(T0Movement(newUserState)) 
  }
}

object Go extends GoType {
  override val specialSurrounding = Blocked
}

object GO extends GoType {
  override val specialSurrounding = Open
}

object G0 extends GoType {
  override val specialSurrounding = Anything
}

abstract class MovementType(val newState:Int) {
  def move(state:Int) : MoveDirection
  def nextState(oldState:Int) : State = {
    // Maintain directionality. Also each "state" corresponds to four states.
    State((newState + (oldState % 4)).toString)
  }
}

// Turn left
class T0Movement(state:Int) extends MovementType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
  override def nextState(oldState:Int) : State = {
    super.nextState(oldState + 1)
  }
}
object T0Movement {
  def apply(userState:Int) = new T0Movement(userState * 4)
}

// Move forward
class TOMovement(state:Int) extends MovementType(state) {
  override def move(state:Int) : MoveDirection = {
    println("Move: " + state.toString)
    state % 4 match {
      case 0 => North
      case 1 => West
      case 2 => South
      case 3 => East
    }
  }
}
object TOMovement {
  def apply(userState:Int) = new TOMovement(userState * 4)
}

// Stand still
class ToMovement(state:Int) extends MovementType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
}
object ToMovement {
  def apply(userState:Int) = new ToMovement(userState * 4)
}

object GOTO {
  def apply(mazeName:String) = {
    println(GlobalVars.rules)
    val maze = Maze("resources" + File.separator + mazeName)

    object GOTOBot extends Picobot(maze, GlobalVars.rules)
      with TextDisplay with GUIDisplay

    GOTOBot
  }
}