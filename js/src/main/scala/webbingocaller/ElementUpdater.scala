package webbingocaller

import org.scalajs.dom.document

object ElementUpdater {

  def updatePar(text: String, id: String): Unit = {
    val par = document.getElementById(id)
    par.textContent = text
  }

}
