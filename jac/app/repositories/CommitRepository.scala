package repositories

import persistence.Persistence
import persistence.entities.Commit
import errors.RepositoryError
import javax.inject._

import scala.collection.mutable.ListBuffer
import scala.util.Try

class CommitRepository @Inject (val db: Persistence[Commit, String]) {
  def findById(id: String): Either[RepositoryError, Option[Commit]] = handleIfError(db.find(id))
  def save(commit: Commit): Either[RepositoryError, Commit] = handleIfError(db.insert(commit))
  def update(commit: Commit): Either[RepositoryError, Unit] = handleIfError(db.update(commit))
  def delete(id: String): Either[RepositoryError, Unit] = handleIfError(db.delete(id))
  def findAll(): Either[RepositoryError, List[Commit]] = handleIfError(db.findAll())
  private def handleIfError[A](f: => A) =
    Try(f).fold(e => Left(RepositoryError(e.getMessage)), v => Right(v))
}
