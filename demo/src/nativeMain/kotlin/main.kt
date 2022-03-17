// docker run -d -p 5432:5432 ghusta/postgres-world-db:latest

import io.github.sobotkami.postgresql.*
import io.github.sobotkami.postgresql.io.Closeable
import io.github.sobotkami.postgresql.io.use
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

/*
@kotlinx.serialization.Serializable
data class CodeNameDTO(
    val code: String,
    val name: String
)
 */

// https://www.postgresql.org/docs/current/libpq-connect.html#LIBPQ-PARAMKEYWORDS
data class ConnectionStringBuilder(
    val hostname: String = "localhost",
    val port: Int = 5432,
    val username: String,
    val password: String,
    val databaseName: String = "postgres"
) {
    fun build(): String = "host=$hostname port=$port user=$username password=$password dbname=$databaseName"

    override fun toString(): String {
        return "host=$hostname port=$port user=$username password=********* dbname=$databaseName"
    }
}

class Connection(connectionString: ConnectionStringBuilder) : Closeable {
    val connection = PQconnectdb(connectionString.build())!!

    init {
        if (PQstatus(connection) != ConnStatusType.CONNECTION_OK) {
            throw RuntimeException("Could not connect to the database: $connectionString")
        }
    }

    override fun close() {
        PQfinish(connection)
    }
}

class ExecuteQuery(
    connection: CPointer<PGconn>, sql: String, expectedStatus: ExecStatusType = PGRES_TUPLES_OK
) : Closeable {
    val result = when (val res = PQexec(connection, sql)) {
        null -> throw RuntimeException("Error by executing $sql")
        else -> when (PQresultStatus(res)) {
            expectedStatus -> res
            else -> throw RuntimeException("Error by executing $sql: ${PQresultErrorMessage(res)}")
        }
    }

    override fun close() {
        PQclear(result)
    }
}

fun main() {
    println("PQlibVersion: ${PQlibVersion()}")

    val connectionString = ConnectionStringBuilder(
        username = "world", password = "world123", databaseName = "world-db"
    )

    Connection(connectionString).use { c ->
        val conn = c.connection

        val serverVersion = PQserverVersion(conn)
        val user = PQuser(conn)?.toKString()
        val dbName = PQdb(conn)?.toKString()

        println("Server version: $serverVersion")
        println("User: $user\n")
        println("Database name: $dbName")

        val sql = "select code, name from country where code like 'AT%'"

        ExecuteQuery(conn, sql).use { eq ->
            val res = eq.result

            val ncols = PQnfields(res)
            val nrows = PQntuples(res)
            println("There are $ncols columns and $nrows rows.")

            /*
            val x: List<CodeNameDTO> = decodePgResult(res)
            println(x)
             */

            (0 until nrows).map { rowIndex ->
                (0 until ncols).map { columnIndex ->
                    PQfname(res, columnIndex)?.toKString()!! to PQgetvalue(res, rowIndex, columnIndex)?.toKString()
                }
            }.forEach {
                println(it)
            }
        }
    }
}
