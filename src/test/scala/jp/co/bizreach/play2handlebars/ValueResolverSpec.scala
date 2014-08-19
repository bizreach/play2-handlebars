package jp.co.bizreach.play2handlebars

import com.github.jknack.handlebars.Context
import org.scalatest.FunSpec


class ValueResolverSpec extends FunSpec with FakePlayHelper {


  describe("Handlebars Scala map value resolver") {
    describe("when the resolver receives a simple Map") {
      it("should extract values") {

        val context = Context
          .newBuilder(Map("someKey" -> "someValue"))
          .resolver(ScalaMapValueResolver)
          .build()

        assert(context.get("someKey") === "someValue")
      }
    }
  }

  describe("when the resolver receives a simple Map in Play application") {
    it("should extract values") {
      runApp(PlayApp()) { app =>
        assert(HBS("test-template2",
          Map("foo" -> "Foo", "bar" -> "Bar" )).toString === "Hello Foo and Bar!")
      }
    }
  }

  describe("when the resolver receives a AnyVal Map in Play application") {
    it("should extract values") {
      runApp(PlayApp()) { app =>
        assert(HBS("test-template2",
          Map("foo" -> "Foo", "bar" -> 123 )).toString === "Hello Foo and 123!")
      }
    }
  }
}
