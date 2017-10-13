play2-handlebars
================

## Play framework plugin for handlebars.java

play2-handlebars is a tiny play framework plugin to generate html and other test output from `Hanldebars` template. It also provides some helper features for Scala specific processing.

- Generate HTML text by HBS object
- Receive Case Class and Map object as parameters
- Cache compiled Handlebars templates
- Aware of Development/Production mode

```scala

object Application extends Controller {

  def simple = Action {
    Ok(HBS("simple", "who" -> "World"))
  }

}
```

## Twirl vs Handlebars

The reasons why we have created this plugins are :

- **Handlebars reloads much faster**. Twirl is a great framework and we can detect many error things in the compilation time and makes refactoring easy. But sometimes that's too much, we just want to add CSS class. 

- **Knowledge sharing between frontend and backend**. Handlebars is one of the most popular template libraries in JavaScript side also. Handlebars.java used in this plugin is also great and matured. We believe that front-end engineers and UI designers get understood soon without any new knowledge like Scala.

- But sometimes Handlebars' grammer conflicts with other formats such as AngularJS.
 
- But you cannot know any template issues before first access. No type safe accesses.


## Getting Started

### 1. Add a dependency in `project/Build.scala`

Create a new play application and update Build.scala. Specify `0.1.0` (Latest Stable) or `0.2-SNAPSHOT` (Latest Development) for the version part.


#### Play 2.6

```scala
libraryDependencies += Seq(
  "jp.co.bizreach" %% "play2-handlebars" % "0.4.0"
)
```

#### Play 2.4 (2.5)

```scala
libraryDependencies += Seq(
  "jp.co.bizreach" %% "play2-handlebars" % "0.3.1"
)
```
  
#### Play 2.3

```scala
libraryDependencies += Seq(
  "jp.co.bizreach" %% "play2-handlebars" % "0.2.0"
)
```

If you choose SNAPSHOT versions, add a resolver also like the below.

```scala
resolvers ++= Seq(
  "Maven Central Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

```


### 2. Add the plugin to your application

#### Play 2.4(or higher) runtime dependency injection

Add a line in `conf/application.conf`

```
play.modules.enabled += "jp.co.bizreach.play2handlebars.HandlebarsModule"
```

#### Play 2.4(or higher) compile time dependency injection

Extend your application's components with `jp.co.bizreach.play2handlebars.HandlebarsComponents` in your `ApplicationLoader`.

``` scala
import jp.co.bizreach.play2handlebars.HandlebarsComponents

class MyApplicationLoader extends ApplicationLoader {
  override def load(context: Context) = new MyAppComponents(context).application
}

class MyAppComponents(context: Context) extends BuiltInComponentsFromContext(context) with HandlebarsComponents

```

#### Play 2.3

Add a line in `conf/play.plugins`

```
1000:jp.co.bizreach.play2handlebars.HandlebarsPlugin
```


### 3. Add a template in `views/simple.hbs`

```
Hello {{who}}!
```

### 4. Use HBS object instead of Twirl in controller  

```scala
package controllers

import play.api.mvc._
import jp.co.bizreach.play2handlebars.HBS

object Application extends Controller {

  def simple = Action {
    Ok(HBS("simple", "who" -> "World"))
  }

}
```

Remember to update `rotues` file.

### 5. Finally, open your browser.

```
Hello World!
```

## Configuration

Add configuration in `conf/application.conf` if needed like the below.

```yaml
play2handlebars {
  root = "/app/hbs-views"
  enableCache = false
  useClassPathLoader = false
  helpers = [
    "helpers.HelperSourceJa"
    "helpers.HelperSourceEn"
  ]
}
```

| Property name  | How to set | Default |
| ------------- | ------------- | ------------- |
| play2handlebars.root  | The root path of the views. | `/app/views`  |
| play2handlebars.enableCache  | If compiled tamplates are cached of not. The default value depends on the mode| `true` in Development/Test, `false` in Production |
| play2handlebars.useClassPathLoader | If true, `ClassPathTemplateLoader` is used if false, `FileTemplateLoader` is used. The default value depends on the mode  | `false` in Development/Test, `true` in Production |
| play2handlebars.helpers| List of helpers. See [HelperSource](https://github.com/jknack/handlebars.java#using-a-helpersource) section of Handlebars.java | empty list | 


## Helpers

Helper feature which exist both in Java and JavaScript implementation is useful. In Handlebars.java, you can register objects and class instances which have public methods.

For example, create `HelperSourceJa` and add into the configuration, 

```scala
object HelperSourceJa {

  def WithSan(value: String):String =
    value + "-san"

}
```

Also set in templates:

```
Hello {{WithMr who}}!
```

Then you will see:

```
Hello World-san!
```


## Deployment

In `Development` mode, play2-handlebars reads template files based on relative file directory position. On the other hand,
in `Production` mode, it reads them inside class paths. Although you can change that by specifying `useClassPathLoader`,
you might need to configure a few things in `Build.scala`.

This may not be the best solution, but let me show our solution in our products. See the complete sample in the sample application.

```scala
object ApplicationBuild extends Build {

  // This code derives from play.PlayCommands trait
  // To skip unexpected reloading when static files and template files change
  // This unexpected phenomenon has started happening since unmanagedResourceDirectories is added.
  // Here, resources are removed from the original code
  val playMonitoredFilesTask = (thisProjectRef, state) map { (ref, state) =>
    val src = Play.inAllDependencies(ref, unmanagedSourceDirectories in Compile, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    val assets = Play.inAllDependencies(ref, unmanagedSourceDirectories in Assets, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    val public = Play.inAllDependencies(ref, unmanagedResourceDirectories in Assets, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    (src ++ assets ++ public).map { f =>
      if (!f.exists) f.mkdirs(); f
    }.map(_.getCanonicalPath).distinct
  }

  lazy val main = Project("root", file("."))
    .enablePlugins(play.PlayScala)
    .settings(

      // To include in the class path
      unmanagedResourceDirectories in Compile += baseDirectory.value / "resources",

      //...

    )
}

```



## Sample Application

See `src/test/play2-handlebars-sample`

## Releases

| Version | Description |
| ------- | ----------- |
| 0.4.0   | Play `2.6` and Scala `2.12` support |
| 0.3.1   | Support for compile time DI |
| 0.3     | Updated the initialization part for Play 2.4 |
| 0.2     | Added JSON resolver as default, and updated several small things. |
| 0.1     | Scala `2.10` / `2.11` are supported. Initial release. |

## Appendix

### Other solutions 

We have chosen the stable product, `hanldebars.java`, 
but [handlebars.scala](https://github.com/mwunsch/handlebars.scala) looks a great hanlebars fork for Scala developers.


