play2-handlebars
================

Play framework plugin for handlebars.java


## Pros and Cons

### Pros

- Knowledge sharing between frontend and backend:
  - Reuse or migrate Handlebars template without any changes
  - Frontend developers do not need to learn any new technologies
- Quick reloading: 
-

### Cons

- loss of type safety: Cannot detect file existence, parameter mapping and other things unless the request is not called.
  - parameter
  - routing
- Sometimes the grammer conflicts with other formats such as AngularJS. 
-

## Getting Started

### Add a dependency in your build setting

```scala
libraryDependencies ++= Seq(
  "jp.co.bizreach" %% "play2-handlebars" % "0.1-SNAPSHOT"
)
```

### Add a line in the plugin configuration 

```
1000:jp.co.bizreach.play2handlebars.HandlebarsPlugin
```

### Add a template in -views/simple.hbs-

```
Hello {{who}}!
```

### Use HBS object instead of Twirl template in your controller  

package controllers

import play.api.mvc._
import jp.co.bizreach.play2handlebars.HBS

object Application extends Controller {

  def simple = Action {
    Ok(HBS("simple", "who" -> "World"))
  }

}

## Configuration

TODO

play2handlebars {
  root = "/app/views"
  enableCache = false
  useClassLoaderTemplate = false
  helpers = [
    "helpers.HelperSourceJa"
    "helpers.HelperSourceEn"
  ]
}

### Change views location

### Choose the template loader

### Add Handlebars helpers

## Releases

No release versions have been published yet. Wait a while.

## Appendix

### Cheat Sheet of Handlebars.java in Scala 