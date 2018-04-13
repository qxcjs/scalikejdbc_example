import org.joda.time.{DateTime, LocalDate}
import scalikejdbc._

object MemberDao {

  def listMap: List[Map[String, Any]] = {
    val members: List[Map[String, Any]] = DB readOnly { implicit session =>
      SQL("select * from members").map(rs => rs.toMap).list.apply()
    }
    members
  }

  def listMember: List[Member] = {
    import Member.allColumns
    val members: List[Member] = DB readOnly { implicit session =>
      val value = SQL("select * from members limit 10")
      value.map(allColumns).list()
      SQL("select * from members limit 10").map(allColumns).list.apply()
    }
    members
  }

  def create(name: String, birthday: Option[LocalDate])(implicit session: DBSession): Member = {
    val createdAt = DateTime.now
    val id: Long = DB localTx { implicit session =>
      sql"insert into members (name, birthday, created_at) values (${name}, ${birthday}, ${createdAt})"
        .updateAndReturnGeneratedKey.apply()
    }
    Member(id = id, name = name, birthday = birthday, createdAt = createdAt)
  }

  @throws[org.h2.jdbc.JdbcSQLException]
  def rollback = DB localTx { implicit session:DBSession =>
    try {
      val insertSql = SQL("insert into members (id,name, birthday, created_at) values (?,?, ?, ?)")
      insertSql.bind(3, "LISS", Option(new LocalDate("1980-01-01")), DateTime.now).update.apply()
      insertSql.bind(3, "Alice", Option(new LocalDate("1980-01-01")), DateTime.now).update.apply()
    }catch {
//      throw new org.h2.jdbc.JdbcSQLException
      case e:org.h2.jdbc.JdbcSQLException => println(e.printStackTrace());session.connection.rollback()
    }
  }

  import scala.util.Try
  import scalikejdbc.TxBoundary.Try._

  def result: Try[Int] = DB localTx { implicit session =>
    Try {
      val insertSql = SQL("insert into members (id,name, birthday, created_at) values (?,?, ?, ?)")
      insertSql.bind(3, "LISS", Option(new LocalDate("1980-01-01")), DateTime.now).update.apply()
      insertSql.bind(3, "Alice", Option(new LocalDate("1980-01-01")), DateTime.now).update.apply()
    }
  }

}
