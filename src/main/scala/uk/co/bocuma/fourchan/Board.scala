package uk.co.bocuma.fourchan
case class Board(board: String, title: Option[String] = None, threads: Option[List[Thread]] = None) extends FourChanEntity
