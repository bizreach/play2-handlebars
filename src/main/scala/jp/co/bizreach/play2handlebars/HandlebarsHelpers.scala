package jp.co.bizreach.play2handlebars

import play.api.i18n.Messages

/**
 * Miscellaneous helpers for Play framework usage
 */
object HandlebarsHelpers {

  def playI18n(s:String):String = {
    Messages(s)
  }

}
