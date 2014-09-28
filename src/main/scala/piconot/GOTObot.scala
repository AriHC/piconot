package piconot

import java.io.File

import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp

/**
 *  This is an intentionally bad internal language, but it is picobot complete.
 */
class COMEFROM(state:Int) {
  require ((state % 4) == 0)
  
  val nState = state
  val wState = state + 1
  val sState = state + 2
  val eState = state + 3

  // Anything ahead
  def Go(to:ToType) = {

    val ruleN = Rule(State(nState.toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to.move(nState),
                     to.nextState(nState))
    val ruleW = Rule(State(wState.toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to.move(wState),
                     to.nextState(wState))
    val ruleS = Rule(State((sState).toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to.move(sState),
                     to.nextState(sState))
    val ruleE = Rule(State((eState).toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to.move(eState),
                     to.nextState(eState))
    val allRules = List(ruleN, ruleW, ruleS, ruleE)
    COMEFROM.rules = COMEFROM.rules ++ allRules
  }

  def fallThroughRule(initialState:Int) : Rule = {
    Rule(State(initialState.toString), 
         Surroundings(Anything, Anything, Anything, Anything),
         StayHere,
         State((initialState + 4).toString))
  }

  // Open ahead
  def GO(to:ToType) = {
    val ruleN = Rule(State(nState.toString), 
                     Surroundings(Open, Anything, Anything, Anything),
                     to.move(nState),
                     to.nextState(nState))
    val ruleW = Rule(State(wState.toString), 
                     Surroundings(Anything, Anything, Open, Anything),
                     to.move(wState),
                     to.nextState(wState))
    val ruleS = Rule(State((sState).toString), 
                     Surroundings(Anything, Anything, Anything, Open),
                     to.move(sState),
                     to.nextState(sState))
    val ruleE = Rule(State((eState).toString), 
                     Surroundings(Anything, Open, Anything, Anything),
                     to.move(eState),
                     to.nextState(eState))
    val allRules = List(ruleN, ruleW, ruleS, ruleE,
                        fallThroughRule(state),
                        fallThroughRule(state + 1),
                        fallThroughRule(sState),
                        fallThroughRule(eState))
    COMEFROM.rules = COMEFROM.rules ++ allRules
  }

  // Blocked ahead
  def G0(to:ToType) = {
    val ruleN = Rule(State(nState.toString), 
                     Surroundings(Blocked, Anything, Anything, Anything),
                     to.move(nState),
                     to.nextState(nState))
    val ruleW = Rule(State(wState.toString), 
                     Surroundings(Anything, Anything, Blocked, Anything),
                     to.move(wState),
                     to.nextState(wState))
    val ruleS = Rule(State((sState).toString), 
                     Surroundings(Anything, Anything, Anything, Blocked),
                     to.move(sState),
                     to.nextState(sState))
    val ruleE = Rule(State((eState).toString), 
                     Surroundings(Anything, Blocked, Anything, Anything),
                     to.move(eState),
                     to.nextState(eState))
    val allRules = List(ruleN, ruleW, ruleS, ruleE,
                        fallThroughRule(state),
                        fallThroughRule(state + 1),
                        fallThroughRule(sState),
                        fallThroughRule(eState))
    COMEFROM.rules = COMEFROM.rules ++ allRules 
  }
}

object COMEFROM {
  var rules : List[Rule] = List.empty[Rule]
  def apply(userState:Int) = new COMEFROM(userState * 4)
}

object GOTO {
  def apply(mazeName:String) = {
    println(COMEFROM.rules)
    val maze = Maze("resources" + File.separator + mazeName)

    object GOTOBot extends Picobot(maze, COMEFROM.rules)
      with TextDisplay with GUIDisplay

    GOTOBot
  }
}

abstract class ToType(val newState:Int) {
  def move(state:Int) : MoveDirection
  def nextState(oldState:Int) : State = {
    // Maintain directionality. Also each "state" corresponds to four states.
    State((newState + (oldState % 4)).toString)
  }
}

// Turn left
class To(state:Int) extends ToType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
  override def nextState(oldState:Int) : State = {
    super.nextState(oldState + 1)
  }
}
object To {
  def apply(userState:Int) = new To(userState * 4)
}

// Move forward
class TO(state:Int) extends ToType(state) {
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
object TO {
  def apply(userState:Int) = new TO(userState * 4)
}

// Stand still
class T0(state:Int) extends ToType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
}
object T0 {
  def apply(userState:Int) = new T0(userState * 4)
}



/*
object EmptyRoom extends JFXApp {
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  val rules = List(
    Rule(State("0"),
      Surroundings(Anything, Anything, Open, Anything),
      West,
      State("0")),

    Rule(State("0"),
      Surroundings(Open, Anything, Blocked, Anything),
      North,
      State("1")),

    Rule(State("0"),
      Surroundings(Blocked, Open, Blocked, Anything),
      South,
      State("2")),

    Rule(State("1"),
      Surroundings(Open, Anything, Anything, Anything),
      North,
      State("1")),

    Rule(State("1"),
      Surroundings(Blocked, Anything, Anything, Open),
      South,
      State("2")),

    Rule(State("2"),
      Surroundings(Anything, Anything, Anything, Open),
      South,
      State("2")),

    Rule(State("2"),
      Surroundings(Anything, Open, Anything, Blocked),
      East,
      State("3")),

    Rule(State("3"),
      Surroundings(Open, Anything, Anything, Anything),
      North,
      State("3")),

    Rule(State("3"),
      Surroundings(Blocked, Open, Anything, Anything),
      East,
      State("2")))

  object EmptyBot extends Picobot(emptyMaze, rules)
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage
} */
