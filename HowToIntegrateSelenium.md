## Note: In version 1.1.29 Selenium ist supported by default in the described way here with support for WebDrivers too. ##

Kerry Buckley

November 16th, 2006 at 2:09 pm

Integrating Selenium with Exactor

We use Exactor to automate acceptance/regression testing for our web application. This is fine for most things, but once we started introducing DHTML and AJAX features, we found that Exactor (or more specifically the version of JWebUnit that it uses behind the scenes) couldn’t handle testing them.

The obvious candidate for testing these enhanced features was Selenium, which uses a real browser to perform its tests. We wanted to stick with Exactor though, for a few reasons:

**Rewriting existing tests would waste a lot of time, and running two separate sets of tests isn’t ideal;** Some tests need to also perform operations outside of the web interface, like inserting test data into the database and running mock instances of external systems that we connect to;
**Exactor’s simple text format for test scripts is easier to follow than tests written in Java, particularly for non-developers.**

The obvious solution was to wrap Selenium within an Exactor command. We could have written (or more likely generated) an Exactor command for each Selenese command, but it seemed simpler to just use the one, passing the Selenese command as a parameter.

Of course you have to have the Selenium server running before you can use Selenium RC commands, and it seemed to make sense to do this in the Exactor test runner. Our runner has some other customisation too (mainly to make it return a non-zero status if any regression tests fail, and to report progress – eg ‘running test 42/150 (1 failed)’ – to CruiseControl’s status file as it runs), but here’s just the Selenium stuff, with everything else stripped out:

```java

package com.bt.csam.exactor;

import java.io.FileNotFoundException;

import org.openqa.selenium.server.SeleniumServer;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.ExecutionSet;
import com.exoftware.exactor.ExecutionSetListener;
import com.exoftware.exactor.Runner;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.listener.HtmlOutputListener;
import com.exoftware.exactor.listener.SimpleListener;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.Selenium;

public class CsamRunner extends Runner {

public static final String SELENIUM_COMMAND_PROCESSOR_KEY = "seleniumCommandProcessor";

private static final int SELENIUM_SERVER_PORT = 4444;

private static CommandProcessor commandProcessor = null;

private static Selenium selenium = null;

private static boolean useSelenium;

private static String browser;

public static void main(final String[] args) {
browser = System.getProperty("selenium.browser");
useSelenium = !(browser.equalsIgnoreCase("none"));
System.out.println(useSelenium ? "Running Selenium using browser: " + browser : "Ignoring Selenium tests");
try {
if (useSelenium) {
SeleniumServer.main(new String[] {});
}
Runner runner = new CsamRunner(args[0]);
runner.addListener(new SimpleListener());
runner.addListener(new HtmlOutputListener(runner.getBaseDir()));
runner.addListener(new SeleniumListener());
runner.run();
} catch (Exception e) {
System.err.println(e.getMessage());
}
}

public CsamRunner(final String fileName) throws FileNotFoundException {
super(fileName);
if (useSelenium) {
String baseUrl = System.getProperty("base.url");
commandProcessor = new HttpCommandProcessor("localhost",
SELENIUM_SERVER_PORT, browser, baseUrl);
selenium = new DefaultSelenium(commandProcessor);
selenium.start();
}
}

public void run() {
try {
super.run();
} finally {
if (useSelenium) {
selenium.stop();
}
}
}

public static class SeleniumListener implements ExecutionSetListener {

public void executionSetStarted(final ExecutionSet es) {
if (useSelenium) {
es.getContext().put(SELENIUM_COMMAND_PROCESSOR_KEY, commandProcessor);
}
}

public void commandEnded(final Command arg0, final Throwable arg1) {
// ignore
}

public void commandStarted(final Command arg0) {
// ignore
}

public void executionSetEnded(final ExecutionSet arg0) {
// ignore
}

public void scriptEnded(final Script arg0) {
// ignore
}

public void scriptStarted(final Script arg0) {
// ignore
}
}
}
```

Here are the important bits:

**Line 34: read in a property which is set by our ant script, to tell us which browser Selenium should use. If this is set to the special value of ‘none’, Selenium is not used (this is really only there until we sort out getting Firefox onto our CruiseControl server).** Line 40: create a new Selenium server.
**Lines 55—59: start a Selenium RC interface with the right base URL (also passed in from ant). We create our own command processor, so that we can run arbitrary commands rather than using the methods for individual commands – for some reason there’s no DefaultSelenium.getCommandProcessor().** Line 68: stop the Selenium server when we’ve run all the tests.
**Line 45: add an execution set listener, so we can…** Line 78: …put a reference to the Selenium interface in the execution set context map when the execution set starts.

On to the command wrapper itself:

```java

package com.bt.csam.exactor.command.selenium;

import com.bt.csam.exactor.CsamRunner;
import com.exoftware.exactor.Command;
import com.exoftware.exactor.Parameter;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.SeleniumException;

/**
* Command wrapping a Selenium command. Specify the Selenium command and its
* arguments as arguments to the Exactor command, eg:
*
*
*  Sel assertValue "id=logTypeFilter" "%"
*
*
* @author Kerry Buckley
*/
public class Sel extends Command {

public void execute() throws Exception {
CommandProcessor commandProcessor = (CommandProcessor) getScript().getExecutionSet().getContext().get(CsamRunner.SELENIUM_COMMAND_PROCESSOR_KEY);
if (commandProcessor == null) {
fail("Selenium is not running. Ensure the selenium.browser property is set.");
}
Parameter[] parameters = getParameters();
String command = parameters[0].stringValue();
String[] arguments = new String[parameters.length - 1];
for (int i = 1; i < parameters.length; i++) {
arguments[i - 1] = parameters[i].stringValue();
}
try {
commandProcessor.doCommand(command, arguments);
} catch (SeleniumException e) {
fail(e.getMessage());
}
}
}
```

Again, the interesting lines:

**Line 22: Get the command processor that the runner put in the context.** Lines 29—33 : extract the Selenium command and its arguments from the Exactor command arguments.
**Line 35: Run the command, throwing an assertion failure if the command fails.**

Now you can turn a Selenium script like this:

```java

open http://www.google.com/
type "hello world"
click btnG
assertTitle "hello world - Google Search"
```

into an Exactor script like this:

```java

Sel open http://www.google.com/
Sel type "hello world"
Sel click btnG
Sel assertTitle "hello world - Google Search"
```

Integration with Selenium IDE

That’s all very well, but wouldn’t it be nice if Selenium IDE could understand your Exactor scripts, so you could record them in the IDE and save them to a .act file, or open a .act file in the IDE and run them? What you need is a custom format, and here’s one I prepared earlier:

```java

/**
* Parse source and update TestCase. Throw an exception if any error occurs.
*
* @param testCase TestCase to update
* @param source The source to parse
*/
function parse(testCase, source) {
var lines = source.split(/\r?\n/);
var commands = [];
for (var n = 0; n < lines.length; n++) {
commands.push(parseLine(lines[n]));
}
testCase.setCommands(commands);
}

function parseLine(line) {
var rtn;
if (line.match(/^#/)) {
rtn = new Comment(line.replace(/^#\W*/, ""));
} else if (line.match(/^Sel\W/)) {
rtn = new Command();
rtn.command = line.replace(/^\W*Sel\W*(\w*).*/, "$1");
var remaining = line.replace(/^\W*Sel\W*(\w*)/, "");
target = nextValue(remaining);
rtn.target = stripQuotes(target);
remaining = remaining.replace(/^\W*/, "").substr(target.length);
rtn.value = stripQuotes(nextValue(remaining));
} else {
rtn = new Comment(line.replace(/^/, "[exactor]"));
}
return rtn;
}

function nextValue(str) {
if (str.match(/^\W*'/)) {
return str.replace(/\W*('.*?[^\\]').*/, "$1");
} else if (str.match(/^\W*"/)) {
return str.replace(/\W*(".*?[^\\]").*/, "$1");
} else {
return str.replace(/^\W*(\w*).*/, "$1");
}
}

function stripQuotes(str) {
return str.replace(/^(['"]?)(.*)\1$/, "$2");
}

/**
* Format TestCase and return the source.
*
* @param testCase TestCase to format
* @param name The name of the test case, if any. It may be used to embed title into the source.
*/
function format(testCase, name) {
return formatCommands(testCase.commands);
}

/**
* Format an array of commands to the snippet of source.
* Used to copy the source into the clipboard.
*
* @param The array of commands to sort.
*/
function formatCommands(commands) {
var result = '';
for (var i = 0; i < commands.length; i++) {
var command = commands[i];
if (command.type == 'command') {
var target = command.target;
var value = command.value;
result += "Sel " + command.command;
if (target != "") {
result += " \"" + target.replace(/\"/, "\\\"") + "\"";
}
if (value != "") {
result += " \"" + value.replace(/\"/, "\\\"") + "\"";
}
} else if (command.comment.match(/^\[exactor\]/)) {
// non-selenium exactor command
result += command.comment.replace(/^\[exactor\]/, "");
} else {
// normal comment
result += "# " + command.comment;
}
result += newline();
}
return result;
}

function newline() {
if (navigator.appVersion.lastIndexOf('Win') != -1) {
return "\r\n"
} else {
return "\n"
}
}
```