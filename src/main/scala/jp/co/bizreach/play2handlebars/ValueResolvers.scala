package jp.co.bizreach.play2handlebars

import com.github.jknack.handlebars.ValueResolver

import java.util.{Set => jSet, Map => jMap}

import scala.collection.JavaConverters._

trait OptionResolvable {

  def flattenOpt(value:AnyRef): AnyRef =
    value match {
      case Some(null) => ""
      case Some(v) => v.asInstanceOf[AnyRef]
      case None => ""
      case v => v
    }

}


object ScalaMapValueResolver extends ValueResolver with OptionResolvable{

  override def resolve(context: Any, name: String): AnyRef = {
    context match {
      case map:Map[_, _] =>
        map
          .asInstanceOf[Map[String, AnyRef]]
          .map{case (k, v) => k -> flattenOpt(v)}
          .getOrElse(name, ValueResolver.UNRESOLVED)
      case _=> ValueResolver.UNRESOLVED
    }
  }


  override def resolve(context: Any): AnyRef = {
    ValueResolver.UNRESOLVED
  }


  override def propertySet(context: Any): jSet[jMap.Entry[String, AnyRef]] = {
    context match {
      case map:Map[_, _] => map.asInstanceOf[Map[String, AnyRef]].asJava.entrySet()
      case _=> java.util.Collections.emptySet()
    }
  }
}


object CaseClassValueResolver extends ValueResolver with OptionResolvable {

  override def resolve(context: scala.Any, name: String): AnyRef = {
    context match {
      case product:Product =>
        productAsMap(product)
          .get(name)
          .map(v => flattenOpt(v.asInstanceOf[AnyRef]))
          .getOrElse(ValueResolver.UNRESOLVED)
      case _=> ValueResolver.UNRESOLVED
    }
  }


  override def resolve(context: scala.Any): AnyRef = {
    ValueResolver.UNRESOLVED
  }


  override def propertySet(context: scala.Any): jSet[jMap.Entry[String, AnyRef]] = {
    context match {
      case product:Product =>
        productAsMap(product)
          .asJava
          .entrySet().asInstanceOf[jSet[jMap.Entry[String, AnyRef]]]
      case _=> java.util.Collections.emptySet()
    }
  }


  private def productAsMap(product:Product): Map[String, Any] =
    product.getClass.getDeclaredFields
      .map(_.getName)
      .zip(product.productIterator.toList).toMap

}


