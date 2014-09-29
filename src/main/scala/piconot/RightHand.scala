package piconot
import scalafx.application.JFXApp

// Actually the left hand rule, not right hand.
object mazeSolver extends JFXApp {
  G0 T0 1
  GO TO 0
  G0 T0 3
  G0 T0 0
  GOTO ("maze.txt")
}
