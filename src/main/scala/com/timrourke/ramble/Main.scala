package com.timrourke.ramble

import akka.actor.{ActorRef, ActorSystem}
import com.timrourke.ramble.delegate.PrinterDelegate
import com.typesafe.config._
import redis.RedisClient

import scala.concurrent.duration._

object Main extends App {
  val conf = ConfigFactory.load()

  val system: ActorSystem = ActorSystem("ramble")

  val redisClient = RedisClient(
      conf.getString("ramble.redis-host"),
      conf.getInt("ramble.redis-port")
  )(system)

  val redisReader = new RedisReader(
    redisClient,
    conf.getString("ramble.queue-topic")
  )

  val printerDelegate: ActorRef = system.actorOf(PrinterDelegate.props)

  for (_ <- 0 until 5) {
    val queueWorker: ActorRef = system.actorOf(QueueWorker.props(redisReader, printerDelegate))

    system.scheduler.schedule(
      0 millis,
      1000 millis,
      queueWorker,
      QueueWorker.Work)(system.dispatcher)
  }
}
