package actors

import javax.inject.Inject
import javax.inject.Named
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext
import play.api.libs.json.{Json, __}

import org.apache.pekko.actor.ActorRef
import org.apache.pekko.actor.ActorSystem
import persistence.entities.Config

import java.io.FileInputStream
class UnityCompileTask @Inject() (actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {
  private val stream = new FileInputStream("./resources/config.json")
  val json: Config = try {
    Json.parse(stream).as[Config]
  } finally {
    stream.close()
  }
  actorSystem.scheduler.scheduleAtFixedRate(initialDelay = 0.seconds, interval = json.interval.seconds) { () =>
    json.targets.foreach{target =>
      println(s"Compiling for target ${target}")
    }
  }
}
