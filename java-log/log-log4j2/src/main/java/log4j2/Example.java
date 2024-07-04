package log4j2;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 案例
 *
 * @author pengshuaifeng
 * 2024/7/4
 */
public class Example {

    // 创建 Logger 实例
    private static final Logger logger = LogManager.getLogger(Example.class);

    public static void main(String[] args) {
        // 示例日志记录
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        logger.fatal("This is a fatal message");

        // 模拟应用逻辑
        try {
            int result = divide(10, 0);
        } catch (ArithmeticException e) {
            logger.error("An error occurred: ", e);
        }
    }

    // 示例方法
    public static int divide(int a, int b) {
        return a / b;
    }


}
