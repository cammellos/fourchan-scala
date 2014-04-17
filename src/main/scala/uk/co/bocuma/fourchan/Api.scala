import dispatch._
import org.json4s._, org.json4s.jackson.JsonMethods._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class Api {

  val baseUrl = "http://a.4cdn.org/"

  val catalogEndPoint = "/catalog.json"
  val threadsEndPoint = "/threads.json"
  val boardsEndPoint = "boards.json"


  def boards(onSuccess: JValue => Unit = this.defaultOnSuccess, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + this.boardsEndPoint
    this.handle(endpoint,onSuccess,onFailure)
  }

  def catalog(board: String,onSuccess: JValue => Unit = this.defaultOnSuccess, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + board + this.catalogEndPoint
    this.handle(endpoint,onSuccess,onFailure)
  }
  
  def threads(board: String,onSuccess: JValue => Unit = this.defaultOnSuccess, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + board + this.threadsEndPoint
    this.handle(endpoint,onSuccess,onFailure)
  }

  def defaultOnError(error: Throwable) {
    println(error)
  }

  def defaultOnSuccess(json: JValue) {
    println(json)
  }

  def handle(endpoint: String, onSuccess: JValue => Unit, onFailure: Throwable => Unit) {
    val response = Http(url(endpoint) OK dispatch.as.json4s.Json)
    response onComplete {
      case Success(json) => onSuccess(json)
      case Failure(error) => onFailure(error)
    }
  }

}
