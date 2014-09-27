package piconot

import java.io.File

import picolib.maze.Maze
import picolib.semantics.Anything
import picolib.semantics.Blocked
import picolib.semantics.East
import picolib.semantics.GUIDisplay
import picolib.semantics.North
import picolib.semantics.Open
import picolib.semantics.Picobot
import picolib.semantics.Rule
import picolib.semantics.South
import picolib.semantics.State
import picolib.semantics.Surroundings
import picolib.semantics.TextDisplay
import picolib.semantics.West
import scalafx.application.JFXApp

import 

/**
 *  This is an intentionally bad internal language, but it is picobot complete.
 */
class OnO(val state:Int) {
  require state % 4 == 0
  def Go(to:ToType) : Seq[Rule] = {
    val ruleN = Rule(State(state.toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to(state),
                     to.nextState(state))
    val ruleE = Rule(State((state + 1).toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to(state),
                     to.nextState(state + ))
    val ruleN = Rule(State((state + 2).toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to(state),
                     to.nextState(state))
    val ruleN = Rule(State((state + 3).toString), 
                     Surroundings(Anything, Anything, Anything, Anything),
                     to(state),
                     to.nextState(state + 3))
  }
  def GO(to:ToType) : Seq[Rule] = {}
  def G0(to:ToType) : Seq[Rule] = {}
}

object OnO {
  def apply(userState:Int) = new OnO(userState * 4)
}

abstract class ToType(val newState:Int) {
  require newState % 4 == 0
  def move(state:Int) : MoveDirection
  def nextState(oldState:Int) : Int = {
    // Maintain directionality. Also each "state" corresponds to four states.
    newState + (oldState % 4)
  }
}

class To(state:Int) extends ToType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
  override def nextState(oldState:Int) : Int = {
    super.nextState(oldState + 1)
  }
}
object To {
  def apply(userState:Int) = new To(userState * 4)
}
class TO(state:Int) extends ToType(state) {
  override def move(state:Int) : MoveDirection = {
    StayHere
  }
}
object TO {
  def apply(userState:Int) = new TO(userState * 4)
}
class T0(state:Int) extends ToType(state) {
  override def move(state:Int) : MoveDirection = {
    state % 4 match {
      case 0 => North
      case 1 => East
      case 2 => West
      case 3 => South
    }
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
