package piconot
import scalafx.application.JFXApp

object emptySolver extends JFXApp {
  COMEFROM (0) GO TO (0)
  COMEFROM (1) G0 To (2)
  COMEFROM (2) GO TO (2)
  GOTO("empty.txt")
}
