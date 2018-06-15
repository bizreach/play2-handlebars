package jp.co.bizreach.play2handlebars

import org.scalatest.FunSpec

class Product2MapSpec extends FunSpec {

  describe("Product2Map") {
    describe("when the case class has symbolic name fields") {
      it("should extract values") {
        case class TestClass(** : String, && : Int, !! : Boolean)
        val value = TestClass("a", 42, false)
        assert(Product2Map.convert(value) === Map("**" -> "a", "&&" -> 42, "!!" -> false))
      }
    }
  }

}
