package app

import am.message.Message

import scala.collection.immutable

case class CountWordsFromFileMessage(filename: String) extends Message

case class CountWordsInLineMessage(line: String) extends Message

case class CountedWordsMessage(result: immutable.Map[String, Int]) extends Message
