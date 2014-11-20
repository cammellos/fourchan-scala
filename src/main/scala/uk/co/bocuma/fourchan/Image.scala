package uk.co.bocuma.fourchan

class Image(val filename: String, val tim: String, val ext: String, val md5: String, val dimension: Dimensions, val board: Board) extends FourChanEntity {
  val url = "http://i.4cdn.org/" + board.board + "/src/" + tim + ext
}
