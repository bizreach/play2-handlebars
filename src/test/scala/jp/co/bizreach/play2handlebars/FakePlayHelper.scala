package jp.co.bizreach.play2handlebars

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._

trait FakePlayHelper {

  def PlayApp(configs:(String, Any)*): Application = {
    new GuiceApplicationBuilder()
      .bindings(new HandlebarsModule())
      .configure( Map(
        "play2handlebars.root" -> "/views",
        "play2handlebars.useClassPathLoader" -> true,
        "ehcacheplugin" -> "disabled"
      ) ++ configs.toMap)
      .build()
  }

  def runApp[T](app: Application)(block: => T): T = {
    running(app) {
      block
    }
  }
}
