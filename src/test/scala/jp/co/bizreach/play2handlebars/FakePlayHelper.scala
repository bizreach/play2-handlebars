package jp.co.bizreach.play2handlebars

import play.api.test.FakeApplication
import play.api.test.Helpers._

trait FakePlayHelper {

  def PlayApp(configs:(String, Any)*) = {
    FakeApplication(
      //      withGlobal = Some(new GlobalSettings(){
      //        override def onStart(app: Application) {  }
      //      }),
      additionalPlugins = Seq("jp.co.bizreach.play2handlebars.HandlebarsPlugin"),
      // without ehcacheplugin, error [Cache play already exists] occurs.
      additionalConfiguration = configs.toSet.toMap
        + ("play2handlebars.root" -> "/views")
        + ("play2handlebars.useClassPathLoader" -> true)
        + ("ehcacheplugin" -> "disabled")
    )}

  def runApp[T](app: FakeApplication)(block: FakeApplication => T): T = {
    running(app) {
      block(app)
    }
  }
}
