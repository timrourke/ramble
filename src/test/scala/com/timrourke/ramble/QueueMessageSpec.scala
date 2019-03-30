package com.timrourke.ramble

import play.api.libs.json.Json
import org.scalatest._

class QueueMessageSpec extends WordSpecLike with Matchers {
  "A QueueMessage" should {
    "expose the JSON message body" in {
      val json = Json.parse("""{"foo":"bar"}""")
      val queueMessage = QueueMessage(json)

      queueMessage.message shouldEqual json
    }
  }
}
