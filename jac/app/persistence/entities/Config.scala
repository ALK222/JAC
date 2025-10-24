package persistence.entities

import play.api.libs.json.{Format, Json}

case class Config(repo: String, interval: Int, targets: List[String])

object Config {
  implicit val format: Format[Config] = Json.format[Config]
}
