package com.timrourke.ramble

import play.api.libs.json.JsValue

case class QueueMessage(message: JsValue)

trait QueueReader extends Iterator[Option[QueueMessage]] {
  def hasNext: Boolean
  def next(): Option[QueueMessage]
}
