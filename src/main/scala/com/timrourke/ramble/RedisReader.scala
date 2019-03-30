package com.timrourke.ramble

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import play.api.libs.json.Json
import redis.RedisClient

class RedisReader(
    private val redisClient: RedisClient,
    private val queueTopic: String
  ) extends QueueReader {
  override def hasNext: Boolean = {
    val future = redisClient.llen(queueTopic)
    val result = Await.result[Long](future, 500 millis)

    result > 0
  }

  override def next(): Option[QueueMessage] = {
    val future = redisClient.lpop[String](queueTopic)
    val result = Await.result[Option[String]](future, 500 millis)

    result match {
      case Some(rawMessage) => parseMessage(rawMessage)
      case None => None
    }
  }

  private def parseMessage(rawMessage: String): Option[QueueMessage] = {
    val parsed = Json.parse(rawMessage)

    Some(QueueMessage(parsed))
  }
}
