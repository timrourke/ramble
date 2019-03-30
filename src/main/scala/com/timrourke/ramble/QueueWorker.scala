package com.timrourke.ramble

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }

object QueueWorker {
  def props(queueReader: QueueReader, delegate: ActorRef): Props =
    Props(new QueueWorker(queueReader, delegate))

  case object Work
}

class QueueWorker(private val queueReader: QueueReader,
                  private val delegate: ActorRef
                 ) extends Actor with ActorLogging {
  import QueueWorker._

  override def receive: Receive = {
    case Work =>
      log.debug("Worker " + this.toString + " starting work")
      queueReader.foreach(queueMessage => {
        delegate ! queueMessage
      })
  }
}
