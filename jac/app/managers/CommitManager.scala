package managers

import persistence.entities.{CommitDTO, FileDTO}
import repositories.CommitRepository
import errors.{ManagerError, RepositoryError}
import javax.inject.*
import com.google.inject.ImplementedBy

/**
 * Commit business layer
 */
@ImplementedBy(classOf[CommitManagerLive])
trait CommitManager {
  /**
   * Looks for the commit in the repository
   * @param id String of the commit
   * @return Error if the commit was not found, the commit with that id if found
   */
  def get(id: String): Either[ManagerError, Option[CommitDTO]]

  /**
   * Saves a new commit to the repository
   * @param commit Commit to save
   * @return the Commit saved if worked correctly
   */
  def save(commit: CommitDTO): Either[ManagerError, CommitDTO]

  /**
   * Updates an already present commit in the repository
   * @param commit Updated commit info
   * @return Error if the commit is not present in the repository
   */
  def update(commit: CommitDTO): Either[ManagerError, Unit]

  /**
   * Deletes a given commit from the repository
   * @param commit Commit to delete
   * @return Error if the commit is not present in the repository
   */
  def delete(commit: CommitDTO): Either[ManagerError, Unit]

  /**
   * Gets all the commits from the repository
   * @return Error if the repository is not available, list of commits if its available
   */
  def getAll: Either[ManagerError, List[CommitDTO]]

  def download(fileDTO: FileDTO): Either[ManagerError, FileDTO]
}

/**
 * Live commit business layer
 * @param repository repository to operate
 */
@Singleton
class CommitManagerLive @Inject (repository: CommitRepository) extends CommitManager{
  override def get(id: String): Either[ManagerError, Option[CommitDTO]] = {
    repository
      .findById(id)
      .fold(
        error => Left(toManagerError(error)),
        commit => Right(commit.map(_.toDTO)))

  }

  override def save(commit: CommitDTO): Either[ManagerError, CommitDTO] = {

    repository.save(commit.toCommit).fold(error => Left(toManagerError(error)), commit => Right(commit.toDTO))
  }

  override def update(commit: CommitDTO): Either[ManagerError, Unit] = {
    repository.update(commit.toCommit).fold(error => Right(toManagerError(error)), _ => Right(()))
  }

  override def delete(commit: CommitDTO): Either[ManagerError, Unit] = {
    repository.delete(commit.id.getOrElse("0"))
      .fold(error => Right(toManagerError(error)), _ => Right(()))
  }

  override def getAll: Either[ManagerError, List[CommitDTO]] ={
    repository.findAll()
      .fold(
        error => Left(toManagerError(error)),
        commitList => Right(commitList.map(_.toDTO))
      )
  }

  override def download(fileDTO: FileDTO): Either[ManagerError, FileDTO] = {
    val commit: Either[ManagerError,CommitDTO] = repository
      .findById(fileDTO.id)
      .fold(
        error => Left(toManagerError(error)),
        commit => Right(commit.map(_.toDTO).get))


    val compilationStatus: Int = commit.toOption.get.status.getOrElse(fileDTO.target, 1)

    val res: Either[ManagerError, FileDTO] = if (compilationStatus == 0) Right(fileDTO) else Left(toManagerError(RepositoryError("compilation not present")))

    res
  }

  private def toManagerError(error: RepositoryError) = ManagerError(error.message)

}