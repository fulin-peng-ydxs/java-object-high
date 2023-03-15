import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 事务测试
 * @date 2021/10/14 22:53
 */
public class JdbcTransaction {

    private static Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.5:3307/fullres", "root", "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into demo values(?,?)");
            preparedStatement.setString(1,"123");
            preparedStatement.setString(2,"789");

            boolean execute = preparedStatement.execute();
            if (!connection.isClosed()) {
                connection.close();
            }
//            connection.commit();
//            connection.rollback();
//            if (!connection.isClosed()) {
//                connection.close();
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
