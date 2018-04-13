import org.joda.time.{DateTime, LocalDate}
import scalikejdbc.WrappedResultSet

case class Member(
                   id: Long,
                   name: String,
                   description: Option[String] = None,
                   birthday: Option[LocalDate] = None,
                   createdAt: DateTime)


object Member{
  import scalikejdbc.jodatime.JodaWrappedResultSet._
  val allColumns = (rs:WrappedResultSet) => Member(
    id = rs.long("id"),
    name = rs.string("name"),
    description = rs.stringOpt("description"),
    birthday = rs.jodaLocalDateOpt("birthday"),
    createdAt = rs.jodaDateTime("created_at")
  )
}
