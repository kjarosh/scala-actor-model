package app

import am.message.Message

import scala.collection.immutable

case class CountWordsFromFile(filename: String) extends Message

case class CountWordsInLineMessage(line: String) extends Message

case class CountedWords(result: immutable.Map[String, Int]) extends Message
