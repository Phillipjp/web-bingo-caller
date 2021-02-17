package webbingocaller

import org.scalajs.dom
import org.scalajs.dom.{document, html}
import org.scalajs.dom.html.{Audio, Document}
import org.scalajs.dom.raw.Element
import webbingocaller.ElementCreator._
import webbingocaller.ElementIds._
import webbingocaller.ElementUpdater.updatePar
import scala.scalajs.js.timers._

import scala.scalajs.js.annotation.JSExportTopLevel

object Main {

  private val caller = Caller(1, 90, None)

  trait PreviousNumbersState
  case object SHOW_ALL extends PreviousNumbersState
  case object RECENT extends PreviousNumbersState

  trait AutoPlayState
  case object PLAYING extends AutoPlayState
  case object NOT_PLAYING extends AutoPlayState

  private var previousNumbersState: PreviousNumbersState = RECENT

  private var autoPlayState: AutoPlayState = NOT_PLAYING

  private var setIntervalHandle: Option[SetIntervalHandle] = None

  def deleteElement(targetNode: dom.Node, id: String): Unit = {
    document.getElementById(id) match {
      case node: Element => targetNode.removeChild(node)
      case _ => println("no such element")
    }
  }


  def clickToStart(): Unit = {
    val div = createDiv(Option(CLICK_TO_START_DIV_ID))
    val button = createButton("Start Game", () => {
      bingo(); deleteElement(document.body, "cts")
    }, "ctsb")
    div.appendChild(button)
    document.body.appendChild(div)
  }

  def bingo(): Unit = {

    val currentNumberDiv = createDiv(Option(CURRENT_NUMBER_DIV_ID))

    val audio = document.createElement("audio").asInstanceOf[Audio]
    document.body.appendChild(audio)

    val optionsDiv = createDiv(Option(OPTIONS_DIV_ID))

    val muteButton = createMuteButton(MUTE_BUTTON_ID, audio)
    optionsDiv.appendChild(muteButton)

    val autoPlayButton = createButton("Auto Play", () => {}, AUTO_PLAY_BUTTON_ID)
    attachEventListener(autoPlayButton, () => {toggleAutoPlay(autoPlayButton, audio)})
    optionsDiv.appendChild(autoPlayButton)

    val restartButton = createButton("Restart", () => {restartGame()}, RESTART_BUTTON_ID)
    optionsDiv.appendChild(restartButton)

    document.body.appendChild(optionsDiv)

    val calledNumbersDiv = createDiv(Option(CALLED_NUMBERS_DIV_ID))

    currentNumber(currentNumberDiv, audio)
    calledNumbers(calledNumbersDiv)

  }

  def restartGame(): Unit ={
    caller.resetNumbers()
    updatePar(" ", CURRENT_NUMBER_PAR_ID)
    updatePar(" ", CALL_OUT_PAR_ID)
    updatePar(" ", CALLED_NUMBERS_PAR_ID)
  }

  def toggleAutoPlay(element: Element, audio: Audio): Unit = {
    autoPlayState match {
      case PLAYING =>
        autoPlayState = NOT_PLAYING
        setIntervalHandle match {
          case Some(handle) => clearInterval(handle)
          case None =>
        }
        element.classList.remove("buttonClicked")
      case NOT_PLAYING =>
        callNumber(audio)
        val handle: SetIntervalHandle = setInterval(9000) {
          callNumber(audio)
        }
        setIntervalHandle = Option(handle)
        autoPlayState = PLAYING
        element.classList.add("buttonClicked")
    }

  }


  def currentNumber(currentNumberDiv: Element, audio: Audio): Unit = {

    val button = createButton("Call Number", () => {
      callNumber(audio)
    }, "callNumberButton")

    val currentNumberPar = createPar(" ", Option(CURRENT_NUMBER_PAR_ID), None)
    currentNumberDiv.appendChild(currentNumberPar)
    val callOutPar = createPar(" ", Option(CALL_OUT_PAR_ID), None)
    currentNumberDiv.appendChild(callOutPar)
    currentNumberDiv.appendChild(button)
    document.body.appendChild(currentNumberDiv)
  }

  @JSExportTopLevel("callNumber")
  def callNumber(audio: Audio): Unit = {
    caller.callNumber match {
      case Some(i) =>
        updatePar(i.toString, CURRENT_NUMBER_PAR_ID)
        updatePar(CallOuts.callouts(i), CALL_OUT_PAR_ID)
        audio.src = s"callouts/${i.toString}.m4a"
        audio.play()
        previousNumbersState match {
          case SHOW_ALL => showAllPreviousNumbers()
          case RECENT => showRecentPreviousNumbers()
        }
      case None =>
        updatePar("No Numbers left, resetting", CURRENT_NUMBER_PAR_ID)
        updatePar(" ", CALL_OUT_PAR_ID)
    }
  }

  def showAllPreviousNumbers(): Unit = {
    updatePar(caller.getCalledNumbers.sorted.mkString(", "), CALLED_NUMBERS_PAR_ID)
    previousNumbersState = SHOW_ALL
  }

  def showRecentPreviousNumbers(): Unit = {
    updatePar(caller.getCalledNumbers.reverse.take(5).mkString(", "), CALLED_NUMBERS_PAR_ID)
    previousNumbersState = RECENT
  }

  def calledNumbers(calledNumbersDiv: Element): Unit = {

    val showAllButton = createButton("Show All", () => {
      showAllPreviousNumbers()
    }, All_CALLED_NUMBERS_BUTTON_ID)
    val showRecentButton = createButton("Show Recent", () => {
      showRecentPreviousNumbers()
    }, RECENT_CALLED_NUMBERS_BUTTON_ID)

    val previousNumbersPar = createPar("Previous Numbers:", Option(PREVIOUS_NUMBERS_PAR_ID), None)
    calledNumbersDiv.appendChild(previousNumbersPar)
    calledNumbersDiv.appendChild(showRecentButton)
    calledNumbersDiv.appendChild(showAllButton)

    val calledNumbersPar = createPar(caller.getCalledNumbers.take(5).mkString(", "), Option(CALLED_NUMBERS_PAR_ID), None)
    calledNumbersDiv.appendChild(calledNumbersPar)
    document.body.appendChild(calledNumbersDiv)
  }

  def main(args: Array[String]): Unit = {
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
      clickToStart()
    })
  }
}
