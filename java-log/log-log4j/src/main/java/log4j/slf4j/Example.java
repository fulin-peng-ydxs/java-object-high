package log4j.slf4j;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 案例
 *
 * @author pengshuaifeng
 * 2024/7/4
 */
public class Example {

    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) {
        logger.info("This is an info message");
        logger.debug("This is a debug message");
        logger.error("This is an error message");
    }
}
