play2-handlebars
================

Play framework plugin for handlebars.java



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

## Releases

No release versions have been published yet. Wait a while.