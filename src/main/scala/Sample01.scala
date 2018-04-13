import org.slf4j.LoggerFactory
import scalikejdbc.{ConnectionPool, DB, GlobalSettings, LoggingSQLAndTimeSettings, SQL}
import scalikejdbc.config._

object Sample01 extends App {

  private val LOG = LoggerFactory.getLogger(Sample01.getClass)

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'INFO
  )
  DBs.setupAll()
  //  Class.forName("org.h2.Driver")
  //  ConnectionPool.singleton("jdbc:h2:file:./scalikejdbc", "user", "pass")

  DB autoCommit { implicit session => {
    SQL(
      """
         drop table members if exists
      """).execute.apply()
  }
  }
  DB autoCommit { implicit session =>
    SQL(
      """
         create table members (
            id bigint primary key auto_increment,
            name varchar(30) not null,
            description varchar(1000),
            birthday date,
            created_at timestamp not null
          )
      """).execute.apply()
  }

  import org.joda.time._

  DB localTx { implicit session =>
    val insertSql = SQL("insert into members (name, birthday, created_at) values (?, ?, ?)")
    val createdAt = DateTime.now

    insertSql.bind("Alice", Option(new LocalDate("1980-01-01")), createdAt).update.apply()
    insertSql.bind("Bob", None, createdAt).update.apply()
  }

  LOG.info(s"listMap: ${MemberDao.listMap}")
  LOG.info(s"rollback : ${MemberDao.rollback}")
  LOG.info(s"listMember：${MemberDao.listMember}")

}
