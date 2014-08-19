package jp.co.bizreach.play2handlebars

import org.scalatest.FunSpec
//import play.api.{Application, GlobalSettings}
import play.api.test.FakeApplication

/**
 * Created by scova0731 on 8/16/14.
 */
class HandlebarsPluginSpec extends FunSpec with FakePlayHelper {


  describe("Handlebars plugin") {

    describe("when the application is started WITHOUT plugin") {
      runApp(FakeApplication()) { app =>
        it("should not have the plugin") {
            assert(app.plugin[HandlebarsPlugin] === None)
        }

//        it("should produce NoSuchElementException when head is invoked") {
//          intercept[NoSuchElementException] {
//            Set.empty.head
//          }
//        }
      }
    }

    describe("when the application is started WITH plugin") {
      it("should have the plugin") {
        runApp(PlayApp()) { app =>
          assert(app.plugin[HandlebarsPlugin].isDefined === true)
        }
      }
      it("should be enabled") {
        runApp(PlayApp()) { app =>
          assert(app.plugin[HandlebarsPlugin].get.enabled === true)
        }
      }
    }

    //TODO settings test

    describe("when the plugin generates template") {
      it("should generate a simple view") {
        runApp(PlayApp()) { app =>
          assert(HBS.engine.templates.keys.size == 0)
        }
      }
    }

    describe("when the cache is enabled") {
      it("should cache templates") {
        runApp(PlayApp("play2handlebars.enableCache" -> "true")) { app =>
          assert(HBS.engine.templates.keys.size == 0)
          assert(HBS("test-template1", "who" -> "World").toString === "Hello World!")
          assert(HBS.engine.templates.keys.size == 1)
          assert(HBS("test-template1", "who" -> "Play").toString === "Hello Play!")
          assert(HBS.engine.templates.keys.size == 1)
        }
      }
    }

    describe("when the cache is NOT enabled") {
      it("should NOT cache templates") {
        runApp(PlayApp()) { app =>
          assert(HBS.engine.templates.keys.size == 0)
          assert(HBS("test-template1", "who" -> "World").toString === "Hello World!")
          assert(HBS.engine.templates.keys.size == 0)
          assert(HBS("test-template1", "who" -> "Play").toString === "Hello Play!")
          assert(HBS.engine.templates.keys.size == 0)
        }
      }
    }
  }
}
