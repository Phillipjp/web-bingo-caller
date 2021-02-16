package webbingocaller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.io.Source
import scala.util.Properties

object Server{
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()

    val port = Properties.envOrElse("PORT", "80").toInt
    val route = {
      get{
        pathSingleSlash{
          complete{
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              Source.fromResource("index.html").mkString
            )
          }
        } ~
          getFromResourceDirectory("")
      }
    }
    Http().newServerAt("0.0.0.0", port = port).bind(route)
  }
}
