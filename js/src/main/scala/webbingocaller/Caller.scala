package webbingocaller

import scala.util.Random

case class Caller(min: Int, max: Int, seed: Option[Int]) {

  private var numbers: Seq[Int] = setNumbers()
  private var calledNumbers: Seq[Int] = Seq()


  def getNumbers: Seq[Int] = numbers

  def getCalledNumbers: Seq[Int] = calledNumbers

  def resetNumbers(): Unit = {
    numbers = setNumbers()
    calledNumbers = Seq()
  }

  def removeNumbersHead(): Unit = numbers = numbers.tail

  def callNumber: Option[Int] = {
    numbers.headOption match {
      case Some(n) =>
        removeNumbersHead()
        calledNumbers = calledNumbers :+ n
        Option(n)
      case None =>
        resetNumbers()
        None
    }

  }

  private def setNumbers(): Seq[Int] = {
    val list: Seq[Int] = min to max
    shuffleList(list)
  }

  private def shuffleList(list: Seq[Int]): Seq[Int] = {
    val r = seed match {
      case Some(s) => new Random(s)
      case None => new Random()
    }

    r.shuffle(list)
  }

}
