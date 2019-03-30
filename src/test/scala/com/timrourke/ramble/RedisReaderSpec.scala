package com.timrourke.ramble

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.libs.json.Json
import redis._

import scala.concurrent.duration._
import scala.concurrent.Future

class RedisReaderSpec extends TestKit(ActorSystem("RedisReaderSpec"))
  with WordSpecLike
  with Matchers
  with MockFactory
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  /**
    * Mocking RedisClient is very difficult because it references implicit
    * constructor parameters, so using a subclassed fake is unfortunately
    * necessary to create deterministic behavior in a unit test where a real
    * Redis server may not exist.
    */
  class RedisClientFake(llenReturn: Long, lpopReturn: String) extends RedisClient(connectTimeout = Some(0 millis)) {
    override def llen(key: String): Future[Long] = Future[Long] { llenReturn }
    override def lpop[A](key: String)(
      implicit evidence$5: ByteStringDeserializer[A]
    ): Future[Option[A]] = Future[Option[A]] { Some(lpopReturn.asInstanceOf[A]) }
  }

  "A RedisReader" should {
    "have nothing to iterate when queue has no length" in {
      val redisClientMock = new RedisClientFake(0, "")

      val redisReader = new RedisReader(redisClientMock, "unimportant")

      redisReader.hasNext shouldEqual false
    }

    "have something to iterate when queue has length" in {
      val redisClientMock = new RedisClientFake(1, "")

      val redisReader = new RedisReader(redisClientMock, "unimportant")

      redisReader.hasNext shouldEqual true
    }

    "parse message into JSON" in {
      val expectedMessage = """{"foo":"bar"}"""

      val redisClientMock = new RedisClientFake(0, expectedMessage)

      val redisReader = new RedisReader(redisClientMock, "unimportant")

      val message = redisReader.next()

      message shouldEqual Some(QueueMessage(Json.parse(expectedMessage)))
    }
  }
}
