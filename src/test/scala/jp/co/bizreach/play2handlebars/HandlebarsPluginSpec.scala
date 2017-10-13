package jp.co.bizreach.play2handlebars

import org.scalatest.FunSpec

class HandlebarsPluginSpec extends FunSpec with FakePlayHelper {


  describe("Handlebars plugin") {
    describe("when the plugin generates template") {
      it("should generate a simple view") {
        runApp(PlayApp()) { app =>
          assert(HBS.engine.templates.keys.size === 0)
        }
      }
    }

    describe("when the cache is enabled") {
      it("should cache templates") {
        runApp(PlayApp("play2handlebars.enableCache" -> "true")) { app =>
          assert(HBS.engine.templates.keys.size === 0)
          assert(HBS("test-template1", "who" -> "World").toString === "Hello World!")
          assert(HBS.engine.templates.keys.size === 0) // TODO should be one ? something wrong with caching
          assert(HBS("test-template1", "who" -> "Play").toString === "Hello Play!")
          assert(HBS.engine.templates.keys.size === 0) // TODO should be one ? something wrong with caching
        }
      }
    }

    describe("when the cache is NOT enabled") {
      it("should NOT cache templates") {
        runApp(PlayApp()) { app =>
          assert(HBS.engine.templates.keys.size === 0)
          assert(HBS("test-template1", "who" -> "World").toString === "Hello World!")
          assert(HBS.engine.templates.keys.size === 0)
          assert(HBS("test-template1", "who" -> "Play").toString === "Hello Play!")
          assert(HBS.engine.templates.keys.size === 0)
        }
      }
    }
  }
}
