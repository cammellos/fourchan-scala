package uk.co.bocuma.fourchan

import dispatch._
import org.json4s._, org.json4s.jackson.JsonMethods._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class Api {
  implicit val formats = DefaultFormats 

  val baseUrl = "http://a.4cdn.org/"

  val catalogEndPoint = "/catalog.json"
  val threadsEndPoint = "/threads.json"
  val boardsEndPoint = "boards.json"


  def boardsJsonToFourChan(json: JValue): List[Board] = {
    List(new Board("b"))
  }

  def extractInt(json: JValue,field: String): Int = {
    (json \ field).extract[Int]
  }

  def extractString(json: JValue,field: String): String = {
    (json \ field).extract[String]
  }

  def pagesJsonToThreads(page: JValue): List[Thread] = {
    (page \ "threads").extract[List[JValue]].map(this.threadJsonToThread)
  }

  def threadJsonToThread(threadJson: JValue): Thread = {
    var s = new Stats(this.extractInt(threadJson, "replies"), this.extractInt(threadJson, "images"))
    var b = new Board("b")
    var d = new Dimensions(this.extractInt(threadJson, "w"), this.extractInt(threadJson, "h"))
    var i = new Image(this.extractString(threadJson, "filename"), this.extractString(threadJson, "tim"), this.extractString(threadJson, "ext"), this.extractString(threadJson ,"md5"), d, b)
    //var p = new Post(this.extractInt(threadJson, "no"),this.extractString(threadJson, "name"), this.extractInt(threadJson, "resto"), this.extractInt(threadJson, "time"), this.extractString(threadJson, "com"),Some(i))
    var p = new Post(this.extractInt(threadJson, "no"),this.extractString(threadJson, "name"), this.extractInt(threadJson, "resto"), this.extractInt(threadJson, "time"), "c",Some(i))
    new Thread(p,s,b)
  }

  def catalogsJsonToFourChan(json: JValue): List[Thread] = {
     json.extract[List[JValue]].map(this.pagesJsonToThreads).flatten
  }


  def boards(onSuccess: List[Board] => Unit = this.defaultOnSuccess, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + this.boardsEndPoint
    this.handle(endpoint,this.boardsJsonToFourChan,onSuccess,onFailure)
  }

  def catalog(board: String,onSuccess: List[Thread] => Unit = this.defaultOnSuccess, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + board + this.catalogEndPoint
    this.handle(endpoint,this.catalogsJsonToFourChan,onSuccess,onFailure)
  }
  
  def threads(board: String,onSuccess: List[Thread] => Unit, onFailure: Throwable => Unit = this.defaultOnError) {
    val endpoint = this.baseUrl + board + this.threadsEndPoint
    this.handle(endpoint,this.catalogsJsonToFourChan,onSuccess,onFailure)
  }

  private def defaultOnError(error: Throwable) {
    println(error)
  }

  private def defaultOnSuccess(entities: List[Any]) {
    println(entities)
  }

  private def handle[A](endpoint: String, toFourChan: JValue => List[A], onSuccess: List[A] => Unit, onFailure: Throwable => Unit) {
    val response = Http(url(endpoint) OK dispatch.as.json4s.Json)
    response onComplete {
      case Success(json) => onSuccess(toFourChan(json))
      case Failure(error) => onFailure(error)
    }
  }

}
