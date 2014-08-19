  package controllers

  import com.github.jknack.handlebars.Handlebars
  import play.api._
  import play.api.mvc._
  import jp.co.bizreach.play2handlebars.HBS

  object Application extends Controller {

    def index = Action {
      Ok(views.html.index("Your new application is ready."))
    }

    def indexHbs = Action {
      Ok(HBS("index",
        "welcome" -> HBS.safeString(views.html.play20.welcome("Your new application is ready.")),
        "title" -> "Wlecome to Play"))
    }

    def simple = Action {
      Ok(HBS("simple", "who" -> "World"))
    }

  }