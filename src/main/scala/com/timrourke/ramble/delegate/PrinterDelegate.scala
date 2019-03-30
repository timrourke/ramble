package com.timrourke.ramble.delegate

import akka.actor.{Actor, ActorLogging, Props}
import com.timrourke.ramble.QueueMessage

object PrinterDelegate {
  def props: Props = Props[PrinterDelegate]
}

class PrinterDelegate extends Actor with ActorLogging {
  override def receive: Receive = {
    case Some(QueueMessage(message)) =>
      log.info("PrinterDelegate " + this + " got message:\n" + message)
  }
}
