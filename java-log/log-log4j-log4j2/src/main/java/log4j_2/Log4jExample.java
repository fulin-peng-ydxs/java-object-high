package log4j_2;

import org.apache.log4j.Logger;
public class Log4jExample {

    // 获取日志记录器，参数为当前类的类对象
    private static final Logger logger = Logger.getLogger(Log4jExample.class);

    public static void main(String[] args) {
        // 不同级别的日志记录示例
        logger.debug("Debug Message");
        logger.info("Info Message");
        logger.warn("Warn Message");
        logger.error("Error Message");
        logger.fatal("Fatal Message");
    }
}
