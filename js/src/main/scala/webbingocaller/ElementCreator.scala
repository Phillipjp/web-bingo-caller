package webbingocaller

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.{Audio, Button}
import org.scalajs.dom.raw.Element

object ElementCreator {

  def createButton(text: String, event: () => Unit, id: String): Element ={
    val button = document.createElement("button")
    button.textContent = text
    button.id = id
    button.addEventListener("click", { (e: dom.MouseEvent) =>
      event()
    })
    button
  }

  def createDiv(id: Option[String]): Element ={
    val div = document.createElement("div")
    id match {
      case Some(i) => div.id = i
      case None =>
    }
    div
  }

  def createPar(text: String, id: Option[String], clazz: Option[String]): Element = {
    val parNode: Element = document.createElement("p")
    id match {
      case Some(i) => parNode.id = i
      case None =>
    }

    clazz match {
      case Some(c) => parNode.classList.add(c)
      case None =>
    }
    parNode.textContent = text
    parNode
  }

//  trait MuteButtonState
//  case object MUTED extends MuteButtonState
//  case object UN_MUTED extends MuteButtonState
//
  def createMuteButton(id: String, audio: Audio): Element = {
  val button = document.createElement("button")
  button.textContent = "Mute"
  button.id = id
  button.classList.add("muteButton")
  button.addEventListener("click", { (e: dom.MouseEvent) =>
    toggleAudioMute(audio, button)
  })
  button
  }

  private def toggleAudioMute(audio: Audio, button: Element): Unit = {
    if(audio.muted) {
      button.textContent = "Mute"
      button.classList.remove("muteButtonClicked")
    } else {
      button.textContent= "Unmute"
      button.classList.add("muteButtonClicked")
    }
    audio.muted = !audio.muted
  }

}
