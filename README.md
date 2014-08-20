play2-handlebars
================

## Play framework plugin for handlebars.java

play2-handlebars is a tiny play framework plugin to generate html and other test output from Hanldebars template. It also provides some helper features for Scala specific processing.

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


```scala
libraryDependencies += Seq(
  "jp.co.bizreach" %% "play2-handlebars" % "0.2-SNAPSHOT"
)
```


### 2. Add a line in `conf/play.plugins` 

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
  useClassLoaderTemplate = false
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
| play2handlebars.useClassLoaderTemplate | If true, `ClassPathTemplateLoader` is used if false, `FileTemplateLoader` is used. The default value depends on the mode  | `false` in Development/Test, `true` in Production |
| play2handlebars.helpers| List of helpers. See [HelperSource](https://github.com/jknack/handlebars.java#using-a-helpersource) section of Handlebars.java | empty list | 

## Usage

### Helpers

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


## Sample Application

See `src/test/play2-handlebars-sample`

## Releases

| Version | Description |
| ------- | ----------- |
| 0.1     | Scala `2.10` / `2.11` are supported. Initial release. |

## Appendix

### Cheat Sheet of Handlebars.java in Scala 

TODO