package controllers

import play.api.mvc._
import jp.co.bizreach.play2handlebars.HBS

class Application extends InjectedController {

  def index = Action {
    Ok(views.html.index("Your new application is ready without play2-handlebars."))
  }

  def indexHbs = Action {
    Ok(HBS("index",
      "welcome" -> "Your new application is ready with play2-handlebars.",
      "title" -> "Welcome to Play"))
  }

  def simple = Action {
    Ok(HBS("simple", "who" -> "World with 'WithMr' helper"))
  }

}