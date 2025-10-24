package persistence

import com.google.inject.ImplementedBy
import persistence.MongoDBPersistence

@ImplementedBy(classOf[MongoDBPersistence])
trait Persistence[A, B]{
  def insert(entity: A): A
  def find(id: B): Option[A]
  def update(entity: A): Unit
  def delete(id: B): Unit
  def findAll(): List[A]
}