package jp.co.bizreach.play2handlebars

import java.lang.reflect.{Method, Modifier}

import com.github.jknack.handlebars.ValueResolver
import java.util.{Map => jMap, Set => jSet}

import scala.collection.JavaConverters._

trait OptionResolvable {

  def flattenOpt(value: AnyRef): AnyRef =
    value match {
      case Some(null) => null
      case Some(v) => v.asInstanceOf[AnyRef]
      case None => null
      case v => v
    }

}


object ScalaMapValueResolver extends ValueResolver with OptionResolvable {

  override def resolve(context: Any, name: String): AnyRef = {
    context match {
      case map: Map[_, _] =>
        map
          .asInstanceOf[Map[String, AnyRef]]
          .map { case (k, v) => k -> flattenOpt(v) }
          .getOrElse(name, ValueResolver.UNRESOLVED)
      case _ => ValueResolver.UNRESOLVED
    }
  }


  override def resolve(context: Any): AnyRef = {
    ValueResolver.UNRESOLVED
  }


  override def propertySet(context: Any): jSet[jMap.Entry[String, AnyRef]] = {
    context match {
      case map: Map[_, _] => map.asInstanceOf[Map[String, AnyRef]].asJava.entrySet()
      case _ => java.util.Collections.emptySet()
    }
  }
}


object CaseClassValueResolver extends ValueResolver with OptionResolvable {

  override def resolve(context: scala.Any, name: String): AnyRef = {
    context match {
      case product: Product =>
        Product2Map.convert(product)
          .get(name)
          .map(v => flattenOpt(v.asInstanceOf[AnyRef]))
          .getOrElse(ValueResolver.UNRESOLVED)
      case _ => ValueResolver.UNRESOLVED
    }
  }


  override def resolve(context: scala.Any): AnyRef = {
    ValueResolver.UNRESOLVED
  }


  override def propertySet(context: scala.Any): jSet[jMap.Entry[String, AnyRef]] = {
    context match {
      case product: Product =>
        Product2Map.convert(product)
          .asJava
          .entrySet().asInstanceOf[jSet[jMap.Entry[String, AnyRef]]]
      case _ => java.util.Collections.emptySet()
    }
  }
}

object Product2Map {
  private[this] val excludeMethodNames = Seq(
    "canEqual",
    "unapply",
    "hashCode",
    "productElement",
    "productIterator",
    "productArity",
    "productPrefix",
    "wait",
    "toString",
    "getClass",
    "notify",
    "notifyAll"
  )

  private def extractFieldMethods(clazz: Class[_]): Array[Method] = {
    clazz.getMethods().filterNot { m => excludeMethodNames.contains(m.getName) }
      .filterNot { m => m.getName.indexOf("$") >= 0 }
      .filterNot { m => m.getReturnType.toString == "void" } // we don't want side effects in rendering
      .filterNot { m => (m.getModifiers & Modifier.PUBLIC) == 0 }
      .filter { m => m.getGenericParameterTypes.length == 0 }
  }

  def convert(product: Product): Map[String, Any] = extractFieldMethods(product.getClass)
    .map(m => (m.getName -> m.invoke(product))).toMap
}

