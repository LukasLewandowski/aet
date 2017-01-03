package com.cognifide.aet.job.common.modifiers;

import com.cognifide.aet.job.api.exceptions.ParametersException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract public class WebElementsLocatorParams {

    private static final String PARAM_TIMEOUT = "timeout";
    private static final String XPATH_PARAM = "xpath";
    private static final String CSS_PARAM = "css";
    private static final long TIMEOUT_SECONDS_MAX_VALUE = 15L;
    private static final long TIMEOUT_SECONDS_DEFAULT_VALUE = 1L;

    /**
     * Xpath to element to click.
     */
    private String xpath;

    /**
     * Css selector of element to click.
     */
    private String css;

    /**
     * Timeout for waiting for element (in seconds).
     */
    private long timeoutInSeconds = TIMEOUT_SECONDS_DEFAULT_VALUE;

    protected long getTimeoutInSeconds() {
        return timeoutInSeconds;

    }

    protected void setElementParams(Map<String, String> params) throws ParametersException {
        String timeoutString = params.get(PARAM_TIMEOUT);
        xpath = params.get(XPATH_PARAM);
        css = params.get(CSS_PARAM);

        if (StringUtils.isNotBlank(xpath) == StringUtils.isNotBlank(css)) {
            throw new ParametersException("Only 'xpath' OR 'css' parameter must be provided for element modifier.");
        }
        if(StringUtils.isNotBlank(timeoutString)) {
            if (StringUtils.isNumeric(timeoutString)) {
                timeoutInSeconds = TimeUnit.SECONDS.convert(Long.valueOf(timeoutString), TimeUnit.MILLISECONDS);
                if (timeoutInSeconds < 0) {
                    throw new ParametersException("'timeout' parameter value on Click Modifier should be greater or equal zero.");
                } else if (TIMEOUT_SECONDS_MAX_VALUE < timeoutInSeconds) {
                    throw new ParametersException("'timeout' parameter value on Click Modifier can't be greater than 15 seconds.");
                }
            } else {
                throw new ParametersException("Parameter 'timeout' on Click Modifier isn't a numeric value.");
            }
        }
    }

    protected String getSelectorType() {
        return (xpath != null) ? XPATH_PARAM : CSS_PARAM;
    }

    protected String getSelectorValue() {
        return StringUtils.defaultString(xpath, css);
    }

    protected By getLocator() {
        By elementLocator;
        if (xpath != null) {
            elementLocator = By.xpath(xpath);
        } else {
            elementLocator = By.cssSelector(css);
        }
        return elementLocator;
    }
}