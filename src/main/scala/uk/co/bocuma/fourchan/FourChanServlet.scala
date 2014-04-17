package uk.co.bocuma.fourchan

import org.scalatra._
import scalate.ScalateSupport

class FourChanServlet extends FourchanStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }
  
}
