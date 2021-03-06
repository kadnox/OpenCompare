package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

import scala.collection.immutable.List

class UnknownPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
    initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		 val value = factory.createNotAvailable()
		 Some(value)
  }

}