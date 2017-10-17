package jp.co.bizreach.play2handlebars

import com.github.jknack.handlebars.helper.EachHelper
import com.github.jknack.handlebars.Options
//import play.api.i18n.Messages


/**
 * Miscellaneous helpers for Play framework usage
 */
object HandlebarsHelpers {

//  def playI18n(s:String):String = {
//    Messages(s)
//  }

}


object EachHelper4S {
  val INSTANCE = new EachHelper4S
  val NAME = "each"
}


/**
 * EachHelper4S is an extension of EachHelper
 * to be aware of scala.Iterable
 */
class EachHelper4S extends EachHelper {
  @SuppressWarnings(Array("rawtypes", "unchecked"))
  override def apply(context: AnyRef, options: Options): Object = {

    context match {
      case ctx:Iterable[_] =>
        import scala.collection.JavaConverters._
        super.apply(ctx.asJava, options)
      case ctx =>
        super.apply(ctx, options)
    }
  }
}


