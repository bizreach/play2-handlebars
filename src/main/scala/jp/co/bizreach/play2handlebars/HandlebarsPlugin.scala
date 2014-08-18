package jp.co.bizreach.play2handlebars


import com.github.jknack.handlebars.Handlebars.SafeString
import com.github.jknack.handlebars.io.{ClassPathTemplateLoader, FileTemplateLoader}
import com.github.jknack.handlebars.{Helper, Template, Handlebars}
import play.api.{Play, Plugin, Application, Logger}
import play.twirl.api.{HtmlFormat, Html}

import play.api.Play.current

import scala.collection.concurrent.TrieMap
import scala.collection.JavaConverters._


/**
 * HandlebarsPlugin initializes handlebars.java configuration and keep the engine's singleton
 */
class HandlebarsPlugin(app: Application) extends Plugin {

  private lazy val logger = Logger("jp.co.bizreach.play2handlebars.HandlebarsPlugin")

  private val confBasePath = "play2handlebars"

  trait Engine {
    val templates: TrieMap[String, Template]
    val rootPath: String
    val enableCache: Boolean
    val handlebars: Handlebars
  }

  lazy val engine = new Engine {
    val templates = TrieMap[String, Template]()
    val rootPath = app.configuration.getString(confBasePath + ".root").getOrElse("/app/views")
    val useClassPathLoader = app.configuration.getBoolean(confBasePath + ".useClassPathLoader").getOrElse(Play.isProd(current))
    val enableCache = app.configuration.getBoolean(confBasePath + ".enableCache").getOrElse(Play.isProd(current))
    val handlebars = new Handlebars(createLoader(useClassPathLoader, rootPath))
    
    instantiateHelpers()
    
    def instantiateHelpers() = {
//      val helperClasses = app.configuration.getConfig(confBasePath + ".helpers")
//      val classloader = Thread.currentThread.getContextClassLoader
//      def instantiateHelper(key: String, className: String) = {
//        val helper = classloader.loadClass(className).asInstanceOf[Helper[String]]
//        handlebars.registerHelper(key, helper)
//        (key, helper)
//      }
//
//      helperClasses.map(cfg => {
//        cfg.subKeys.map(key =>
//          instantiateHelper(key, cfg.getString(key).get)
//        ).toMap
//      }).getOrElse(Map.empty)
      val helperClasses = app.configuration.getStringList(confBasePath + ".helpers")
      val classloader = Thread.currentThread.getContextClassLoader

      def instantiateHelper(className: String) = {
        val helper = classloader.loadClass(className)
        handlebars.registerHelpers(helper)
        helper
      }

      helperClasses.foreach(list => list.asScala.foreach(clazz => instantiateHelper(clazz)))
    }
  }


  /**
   * Initiate the engine
   */
  override def onStart(): Unit = {
    engine
    logger.info("Handlebars plugin is started")
  }

  override def onStop(): Unit = {
    logger.info("Handlebars plugin is shutting down")
  }

  override def enabled: Boolean = true


  private def createLoader(isClassPath:Boolean, rootPath:String) =
    if (isClassPath) new ClassPathTemplateLoader(rootPath)
    else new FileTemplateLoader(concatPath(System.getProperty("user.dir"), rootPath))


  private def concatPath(path1:String, path2:String) = {
    if (path2.startsWith("/")) path1 + path2
    else path1 + "/" + path2
  }
}


/**
 *
 */
object HBS {

  /**
   * Just a shorthand for Tuple2 attribute values
   * @param templatePath
   * @param attributes
   * @return
   */
  def apply(templatePath: String, attributes: (String, Any)*): Html = {
    apply(templatePath, attributes.toMap)
  }


  /**
   *
   * @param templatePath
   * @param charset
   * @param attributes
   * @return
   */
  def apply(templatePath: String, attributes: Map[String, Any] = Map.empty, charset: String = "utf-8"): Html = {

    def compile = engine.handlebars.compile(templatePath)

    // Get the cache from the cache or just compile.
    val template =
      if (engine.enableCache)
        engine.templates.getOrElseUpdate(templatePath, { compile })
      else
        compile



    // Wrap as html. See play.api.http.Writeable
    HtmlFormat.raw(template.apply(attributes.asJava))
  }


  /**
   * Shorthand for Handlebars.SafeString class instantiation
   */
  def safeString(safeString:Any): SafeString = new Handlebars.SafeString(safeString.toString)


  private[play2handlebars] def engine = current.plugin[HandlebarsPlugin].map(_.engine)
    .getOrElse(throw new IllegalStateException("HandlebarsPlugin is not installed"))
}