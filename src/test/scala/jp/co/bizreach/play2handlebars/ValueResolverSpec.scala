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

    describe("when the resolver receives a simple Map in Play application") {
      it("should extract values") {
        runApp(PlayApp()) {
          assert(HBS("test-template2",
            Map("foo" -> "Foo", "bar" -> "Bar" )).toString === "Hello Foo and Bar!")
        }
      }
    }

    describe("when the resolver receives a AnyVal Map in Play application") {
      it("should extract values") {
        runApp(PlayApp()) {
          assert(HBS("test-template2",
            Map("foo" -> "Foo", "bar" -> 123 )).toString === "Hello Foo and 123!")
        }
      }
      it("should extract values with any method") {
        runApp(PlayApp()) {
          assert(HBS.any("test-template2",
            Map("foo" -> "Foo", "bar" -> 123 )).toString === "Hello Foo and 123!")
        }
      }    }
  }


  describe("Handlebars Scala Case Class / Tuple resolver") {
    describe("when the case class is simple") {
      it("should extract values") {
        case class Person(name:String, age:Int)

        val context = Context
          .newBuilder(Person("Minami", 38))
          .resolver(CaseClassValueResolver)
          .build()

        // Assertions for resolve method
        assert(context.get("name") == "Minami")
        assert(context.get("age") === 38)

        // Assertions for propertySet method
        assert(context.propertySet().size === 2)
        val firstEntry = context.propertySet().iterator().next()
        val secondEntry = context.propertySet().iterator().next()
        val map = Map("name" -> "Minami", "age" -> 38)
        assert(firstEntry.getValue === map(firstEntry.getKey))
        assert(secondEntry.getValue === map(secondEntry.getKey))
      }
    }


    describe("when the case class is nested") {
      it("should extract the top layer values") {
        runApp(PlayApp()) {
          case class Address(city:String, isValid:Boolean = true)
          case class Person(name:String, address:Address)

          val context = Context
            .newBuilder(Person("Minami", Address("Kyoto")))
            .resolver(CaseClassValueResolver)
            .build()

          assert(context.get("name") === "Minami")
          assert(context.get("address").isInstanceOf[Address])
          assert(context.get("address").asInstanceOf[Address].city === "Kyoto")
        }
      }

      it("should extract all the nested values in an application instance") {
        runApp(PlayApp()) {
          case class Address(city:String, isValid:Boolean = true)
          case class Person(name:String, address:Address)

          assert(HBS.withProduct("test-template-nested",
            Person("Minami", Address("Kyoto"))).toString === "Hello Minami. Welcome to Kyoto")
        }
      }
    }
  }


  describe("Handlebars Option resolver") {
    describe("when Option is some") {
      it("should write the inside value") {
        runApp(PlayApp()) {

          assert(HBS("test-template1",
            Map("who" -> Some("World"))).toString === "Hello World!")

          assert(HBS("test-template1",
            Map("who" -> Some(123))).toString === "Hello 123!")
        }
      }

      describe("when Option is none") {
        it("should write no values") {
          runApp(PlayApp()) {

            assert(HBS("test-template1",
              Map("who" -> None)).toString === "Hello !")
          }
        }
      }
    }
  }
}
