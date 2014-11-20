package uk.co.bocuma.fourchan
import scredis._


class Crawler {
  val api = new Api()
  val board = "b"
  val redis = Redis()
  val redisNs = "fourchan:" + board 
  val redisImageNs = redisNs + ":images"
  val redisExpireNs = redisNs + ":expire"


  def perform = {
    api.catalog(this.board,this.handleCatalogData)
  }

  def handleCatalogData(threads: List[Thread]) {
    redis.pipelined { p =>
      threads.filter(t => t.op.image.isDefined).map { t =>
          p.zAdd(redisExpireNs,(redisImageNs +  t.op.image.get.url,System.currentTimeMillis()/1000))
          p.zAdd(redisImageNs,(t.op.image.get.url,t.stats.replies))
      } 
    }
  }

}

