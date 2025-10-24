package persistence.entities

import org.mongodb.scala.bson.ObjectId
import play.api.libs.json.{Format, Json, Reads, Writes, JsString, JsSuccess, JsError}
import play.api.libs.json.Reads.mapReads

import java.sql.Date
import scala.collection.mutable

def toMutableMap(m: Map[String, Int]): mutable.Map[String, Int] = {
  val mm = collection.mutable.Map(m.toSeq: _*)
  mm
}


case class Commit(_id: ObjectId, id: String, message: String, description: String, status: mutable.Map[String, Int], timestamp: Date) {

  def toDTO: CommitDTO = CommitDTO(_id, Some(id), message, description, status.toMap, timestamp)

}


object Commit{

  def fromDTO(commitDTO: CommitDTO): Commit = Commit(commitDTO._id, commitDTO.id.getOrElse("0L"), commitDTO.message, commitDTO.description, toMutableMap(commitDTO.status), commitDTO.timestamp )

}


case class CommitDTO(_id: ObjectId, id: Option[String], message: String, description: String, status: Map[String, Int], timestamp: Date) {

  def toCommit: Commit = Commit(_id, id.getOrElse("0L"), message, description, toMutableMap(status), timestamp)

}


object CommitDTO{
  implicit val objectIdFormat: Format[ObjectId] = Format(
    Reads[ObjectId] {
      case s: JsString => try JsSuccess(new ObjectId(s.toString)) catch case _ => JsError()
      case _           => JsError()
    },
    Writes[ObjectId]((o: ObjectId) => JsString(o.toHexString))
  )
  implicit val format: Format[CommitDTO] = Json.format[CommitDTO]

  def fromCommit(commit: Commit): CommitDTO = CommitDTO(commit._id, Some(commit.id), commit.message, commit.description, commit.status.toMap, commit.timestamp)

}