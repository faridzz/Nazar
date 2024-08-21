package org.example.nazar;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public class LoggingTestExecutionListener implements BeforeTestExecutionCallback, CloseableResource {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTestExecutionListener.class);
    private static final String RESET = "\033[0m";
    private static final String BLUE = "\033[34m";
    private static final String CYAN = "\033[36m";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String className = context.getRequiredTestClass().getName();
        String methodName = context.getRequiredTestMethod().getName();
        logger.info("Executing test method - Class : {}{}{} Method: {} ", BLUE, className, CYAN, methodName );
    }

    @Override
    public void close() throws Throwable {

    }
}
