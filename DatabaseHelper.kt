import java.sql.DriverManager
import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.sql.Timestamp
import java.util.Calendar
import android.annotation.SuppressLint

data class UserData(
    val username: String,
    val expiryDate: LocalDateTime
)

class DatabaseHelper {
    private val url = "jdbc:mysql://sql.freedb.tech:3306/freedb_netflixapp"
    private val dbUser = "freedb_emmaomondi"
    private val dbPassword = "7&WZ#9n*fK4M@Sz"

    @SuppressLint("NewApi")
    fun getUserData(username: String): UserData? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, dbUser, dbPassword).use { connection ->
                val query = "SELECT username, expiry_date FROM users WHERE username = ?"
                connection.prepareStatement(query).use { preparedStatement ->
                    preparedStatement.setString(1, username)
                    val resultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        val timestamp = resultSet.getTimestamp("expiry_date")
                        // Convert Timestamp to LocalDateTime using Calendar
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = timestamp.time
                        return UserData(
                            username = resultSet.getString("username"),
                            expiryDate = LocalDateTime.of(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                calendar.get(Calendar.SECOND)
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("NewApi")
    fun storeCredentials(username: String, password: String): Boolean {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, dbUser, dbPassword).use { connection ->
                val query = """
                    INSERT INTO users (username, password, expiry_date) 
                    VALUES (?, ?, ?)
                """
                connection.prepareStatement(query).use { preparedStatement ->
                    preparedStatement.setString(1, username)
                    preparedStatement.setString(2, password)

                    // Set expiry date to 30 days from now using Calendar
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, 30)
                    val timestamp = Timestamp(calendar.timeInMillis)

                    preparedStatement.setTimestamp(3, timestamp)
                    preparedStatement.executeUpdate()
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun validateCredentials(username: String, password: String): Boolean {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, dbUser, dbPassword).use { connection ->
                val query = "SELECT * FROM users WHERE username = ? AND password = ?"
                connection.prepareStatement(query).use { preparedStatement ->
                    preparedStatement.setString(1, username)
                    preparedStatement.setString(2, password)
                    val resultSet = preparedStatement.executeQuery()
                    return resultSet.next()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @SuppressLint("NewApi")
    fun updateExpiryDate(username: String, newExpiryDate: LocalDateTime): Boolean {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, dbUser, dbPassword).use { connection ->
                val query = "UPDATE users SET expiry_date = ? WHERE username = ?"
                connection.prepareStatement(query).use { preparedStatement ->
                    // Convert LocalDateTime to Timestamp using Calendar
                    val calendar = Calendar.getInstance()
                    calendar.set(
                        newExpiryDate.year,
                        newExpiryDate.monthValue - 1,
                        newExpiryDate.dayOfMonth,
                        newExpiryDate.hour,
                        newExpiryDate.minute,
                        newExpiryDate.second
                    )
                    val timestamp = Timestamp(calendar.timeInMillis)

                    preparedStatement.setTimestamp(1, timestamp)
                    preparedStatement.setString(2, username)
                    preparedStatement.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

// Add these imports at the top of your file
