package persistence.entities

import play.api.libs.json.{Format, Json}

case class File(id: String, target: String, path: String) {
  def toDTO: FileDTO = new FileDTO(id, target, path)
}

object File {
  def fromDTO(fileDTO: FileDTO) = new File(fileDTO.id, fileDTO.target, fileDTO.path)
}

case class FileDTO(id: String, target: String, path: String){
  def toFile: File = new File(id, target, path)
}

object FileDTO {
  implicit val format: Format[FileDTO] = Json.format[FileDTO]

  def fromFile(file: File) = new FileDTO(file.id, file.target, file.path)
}