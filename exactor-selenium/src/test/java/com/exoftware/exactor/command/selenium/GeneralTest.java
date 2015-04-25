package com.exoftware.exactor.command.selenium;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author Andoni del Olmo
 * @since 25.04.15
 */
public class GeneralTest {

    protected static final String RESOURCES_DIRECTORY = System.getProperty("user.dir") + "/src/test/resources";
    protected static final File TEMP_DIRECTORY = FileUtils.getTempDirectory();
    protected static final String DEFAULT_HTML = "out.html";
    protected static final String DEFAULT_STYLE_SHEET = "style.css";

}