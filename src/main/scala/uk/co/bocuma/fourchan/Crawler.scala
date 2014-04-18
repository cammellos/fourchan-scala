package uk.co.bocuma.fourchan


class Crawler {
  val api = new Api()
  val board = "b"


  def perform = {
    api.catalog(this.board)
  }

  def handleCatalogData(threads: List[Thread]) {


  }


}

