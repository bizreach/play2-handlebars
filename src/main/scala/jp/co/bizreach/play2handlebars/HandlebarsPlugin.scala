package jp.co.bizreach.play2handlebars


import javax.inject._

import com.github.jknack.handlebars.Handlebars.SafeString
import com.github.jknack.handlebars.io.{ClassPathTemplateLoader, FileTemplateLoader}
import com.github.jknack.handlebars._
import play.api.inject.{Binding, Module, ApplicationLifecycle}
import play.api._
import play.twirl.api.{HtmlFormat, Html}

import scala.collection.concurrent.TrieMap
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Handlebars Module for Play 2.4 application
 */
class HandlebarsModule extends Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[HandlebarsPlugin]] =
    Seq(
      bind[HandlebarsPlugin].toProvider[HandlebarsProvider].eagerly()
    )
}


/**
 * HandlebarsPlugin initializes handlebars.java configuration and keep the engine's singleton
 */
@Singleton
class HandlebarsProvider @Inject() (app: Application, lifecycle: ApplicationLifecycle) extends Provider[HandlebarsPlugin] {

  private lazy val logger = Logger(this.getClass)

  lazy val get: HandlebarsPlugin = new HandlebarsPlugin(app)

  /**
   * Shutdown the engine
   */
  lifecycle.addStopHook { () =>
    Future {
      logger.info("Handlebars plugin is shutting down")
    }
  }
}


class HandlebarsPlugin(app: Application) {

  private lazy val logger = Logger(this.getClass)

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
    val useClassPathLoader = app.configuration.getBoolean(confBasePath + ".useClassPathLoader").getOrElse(Play.isProd(app))
    val enableCache = app.configuration.getBoolean(confBasePath + ".enableCache").getOrElse(Play.isProd(app))
    val handlebars = new Handlebars(createLoader(useClassPathLoader, rootPath))

    /**
     * Override the default {{#each}} helper for java.lang.Iterable to recognize scala.Iterable
     */
    handlebars.registerHelper(EachHelper4S.NAME, EachHelper4S.INSTANCE)

    instantiateHelpers()
    
    def instantiateHelpers() = {
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
   * Initiate the engine (Hmm ...)
   */
  new HBS(engine)
  logger.info("Handlebars plugin is started")


  private def createLoader(isClassPath:Boolean, rootPath:String) =
    if (isClassPath) new ClassPathTemplateLoader(rootPath)
    else new FileTemplateLoader(concatPath(System.getProperty("user.dir"), rootPath))


  private def concatPath(path1:String, path2:String) = {
    if (path2.startsWith("/")) path1 + path2
    else path1 + "/" + path2
  }
}


/**
 * This is just a small class to be used only for DI
 */
class HBS(private[HBS] val engine: HandlebarsPlugin#Engine) {
  HBS.injectMe(this)
}


/**
 *
 */
object HBS {

  private lazy val logger = Logger(this.getClass)

  /**
   * Object HBS has an instance of HBS which has the Handlebars engine.
   * Since Play's DI cannot combine with Scala's object, this "var" variable is used.
   */
  private[this] var hbs:Option[HBS] = None


  /**
   * Inject an instance of HBS to set the Handlebars engine. This can be called only once.
   */
  private[HBS] def injectMe(hbs:HBS): Unit = {
    this.hbs match {
      case Some(_) => logger.warn("HandlebarsPlugin is instantiated multiple times")
      case None =>
    }
    this.hbs = Some(hbs)
  }


  /**
   * Get the engine. If this is not injected, throws an exception.
   */
  def engine: HandlebarsPlugin#Engine = {
    hbs.map(_.engine).getOrElse(throw new IllegalStateException("HandlebarsPlugin is not installed"))
  }

  
  /**
   * Just a shorthand for Tuple2 attribute values
   * @param templatePath
   * @param attributes
   * @return
   */
  def apply(templatePath: String, attributes: (String, Any)*): Html = {
    generate(templatePath, attributes.toMap)
  }


  /**
   *
   * @param templatePath
   * @param attributes
   * @return
   */
  def withProduct(templatePath: String, attributes: Product, charset: String = "utf-8"): Html = {
    generate(templatePath, attributes, charset)
  }


  /**
   *
   * @param templatePath
   * @param attributes
   * @return
   */
  def any(templatePath: String, attributes: Any, charset: String = "utf-8"): Html = {
    generate(templatePath, attributes, charset)
  }

  /**
   *
   * @param templatePath
   * @param charset
   * @param attributes
   * @return
   */
  def apply(templatePath: String, attributes: Map[String, Any] = Map.empty, charset: String = "utf-8"): Html = {
    generate(templatePath, attributes.toMap, charset)
  }


  /**
   *
   * @param templatePath
   * @param attributes
   * @param charset
   * @return
   */
  private def generate(templatePath: String, attributes: Any = Map.empty, charset: String = "utf-8"): Html = {
    def compile = engine.handlebars.compile(templatePath)

    // Get the cache from the cache or just compile.
    val template =
      if (engine.enableCache)
        engine.templates.getOrElseUpdate(templatePath, { compile })
      else
        compile

    // Add several resolvers for scala
    val resolvers = ValueResolver.VALUE_RESOLVERS ++
      Array(
        ScalaMapValueResolver,
        CaseClassValueResolver,
        JsonNodeValueResolver.INSTANCE)

    val context = Context
      .newBuilder(attributes)
      .resolver(resolvers:_*)
      .build()


    // Wrap as html. See play.api.http.Writable
    HtmlFormat.raw(template.apply(context))
  }


  /**
   * Shorthand for Handlebars.SafeString class instantiation
   */
  def safeString(safeString:Any): SafeString = new Handlebars.SafeString(safeString.toString)


  /**
   * Convert any case class to java map using reflection.
   *
   * CAUTION: this may be slow
   */
  def toJavaMap[A <: Product](product:A):java.util.Map[String, Any] =
    product.getClass.getDeclaredFields.map(_.getName).zip(product.productIterator.toList).toMap.asJava

}