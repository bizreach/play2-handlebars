package jp.co.bizreach.play2handlebars

import org.scalatest.FunSpec


class HelpersSpec extends FunSpec with FakePlayHelper {

  case class Who(who:String)

  describe("Handlebars Iterable aware helper") {
    describe("when the template has each and feed scala.Iterable") {
      it("should list values") {
        runApp(PlayApp()) {
          val sList = List(Who("Foo"),Who("Bar"))

          assert(HBS("test-template-each", "list" -> sList).toString === "Hello FooBar!")
        }
      }
    }

    describe("when the template has each and feed java.lang.Iterable") {
      it("should list values") {
        runApp(PlayApp()) {
          import scala.collection.JavaConverters._
          val jList: java.util.List[Who] = List(Who("Java"), Who("Mocha")).asJava

          assert(HBS("test-template-each", "list" -> jList).toString === "Hello JavaMocha!")
        }
      }
    }
  }

}
