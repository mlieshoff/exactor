package com.exoftware.exactor.command.abbot;

import abbot.script.InvalidScriptException;
import abbot.script.Step;
import abbot.script.StepRunner;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import com.exoftware.util.QuotedStringTokenizer;
import junit.framework.TestCase;

import java.awt.Component;
import java.io.IOException;

public class AbbotActionTest extends TestCase
{
   private Script script;

   protected void setUp() throws Exception
   {
      super.setUp();

      script = new Script();
      script.getContext().put(AbbotAction.ABBOT_STEP_RUNNER_KEY, getStepRunner());
   }

   public void testXMLActionCreation() throws Exception
   {
      assertActionXml("\"\" \"actionClick\" \"?\" ", "<action method=\"actionClick\" args=\"?\" />");
      assertActionXml("\"\" \"actionClick\" \"High Button\" ", "<action method=\"actionClick\" args=\"High Button\" />");
      assertActionXml("\"\" \"actionClick\" \"Low Button\" ", "<action method=\"actionClick\" args=\"Low Button\" />");
      assertActionXml("\"\" \"actionSelectMenuItem\" \"New Script...\" ", "<action method=\"actionSelectMenuItem\" args=\"New Script...\" />");
      assertActionXml("\"\" \"actionSelectMenuItem\" \"Run To\" ", "<action method=\"actionSelectMenuItem\" args=\"Run To\" />");
      assertActionXml("\"\" \"actionKeyStroke\" \"VK_ESCAPE\" ", "<action method=\"actionKeyStroke\" args=\"VK_ESCAPE\" />");
      assertActionXml("\"java.awt.Dialog\" \"actionClose\" \"Dialog\" ", "<action class=\"java.awt.Dialog\" method=\"actionClose\" args=\"Dialog\" />");
      assertActionXml("\"java.awt.TextComponent\" \"actionFocus\" \"textField\" ", "<action class=\"java.awt.TextComponent\" method=\"actionFocus\" args=\"textField\" />");
      assertActionXml("\"javax.swing.AbstractButton\" \"actionClick\" \"OK\" ", "<action class=\"javax.swing.AbstractButton\" method=\"actionClick\" args=\"OK\" />");
      assertActionXml("\"javax.swing.AbstractButton\" \"actionClick\" \"Select Test Suite...\" ", "<action class=\"javax.swing.AbstractButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Button\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Button\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"OK Button\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK Button\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"OK\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"OK\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"OK\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"OK\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Run\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Run\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Run\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Run\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
      assertActionXml("\"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\" ", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
      assertActionXml("\"javax.swing.JComboBox\" \"actionSelectItem\" \"JComboBox Instance,Combo 10\" ", "<action class=\"javax.swing.JComboBox\" method=\"actionSelectItem\" args=\"JComboBox Instance,Combo 10\" />");
      assertActionXml("\"javax.swing.JComboBox\" \"actionSelectItem\" \"JComboBox Instance,Combo 10\" ", "<action class=\"javax.swing.JComboBox\" method=\"actionSelectItem\" args=\"JComboBox Instance,Combo 10\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\" ", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\" ", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\" ", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\" ", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,-1,-1,Blue\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,-1,-1,Blue\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,-1,-1,Blue\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,-1,-1,Blue\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,Blue\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,Blue\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,Blue\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,Blue\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,-1,-1,Black\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,-1,-1,Black\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,-1,-1,White\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,-1,-1,White\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,Black\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,Black\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,Black\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,Black\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,White\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,White\" />");
      assertActionXml("\"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,White\" ", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,White\" />");
      assertActionXml("\"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\" ", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
      assertActionXml("\"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\" ", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
      assertActionXml("\"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\" ", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
      assertActionXml("\"javax.swing.JList\" \"actionSelectIndex\" \"My List,0\" ", "<action class=\"javax.swing.JList\" method=\"actionSelectIndex\" args=\"My List,0\" />");
      assertActionXml("\"javax.swing.JList\" \"actionSelectIndex\" \"My List,7\" ", "<action class=\"javax.swing.JList\" method=\"actionSelectIndex\" args=\"My List,7\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
      assertActionXml("\"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\" ", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
      assertActionXml("\"javax.swing.JTable\" \"actionSelectCell\" \"ScriptTable Instance,8,0\" ", "<action class=\"javax.swing.JTable\" method=\"actionSelectCell\" args=\"ScriptTable Instance,8,0\" />");
      assertActionXml("\"javax.swing.JTextField\" \"actionClick\" \"My Text Field,3,7\" ", "<action class=\"javax.swing.JTextField\" method=\"actionClick\" args=\"My Text Field,3,7\" />");
      assertActionXml("\"javax.swing.JTextField\" \"actionClick\" \"My Text Field,3,7\" ", "<action class=\"javax.swing.JTextField\" method=\"actionClick\" args=\"My Text Field,3,7\" />");
      assertActionXml("\"javax.swing.JTextField\" \"actionText\" \"My Text Field,Get outta here\" ", "<action class=\"javax.swing.JTextField\" method=\"actionText\" args=\"My Text Field,Get outta here\" />");
      assertActionXml("\"javax.swing.JTextField\" \"actionText\" \"My Text Field,Get outta here\" ", "<action class=\"javax.swing.JTextField\" method=\"actionText\" args=\"My Text Field,Get outta here\" />");
   }

   private void assertActionXml (String params, String expectedXML)
         throws Exception
   {
      MyAbbotAction runAbbotCommand = new MyAbbotAction();
      runAbbotCommand.setScript(script);

      String expectedActionXML = expectedXML;

      QuotedStringTokenizer toker = new QuotedStringTokenizer(params, " ");
      while (toker.hasMoreTokens())
      {
         runAbbotCommand.addParameter(new Parameter(toker.nextToken()));
      }

      runAbbotCommand.execute();

      assertEquals(expectedActionXML, runAbbotCommand.actualActionXML);
      assertEquals(extractComponentName(runAbbotCommand), runAbbotCommand.actualComponentName);
      assertTrue( ( (MyStepRunner)runAbbotCommand.getStepRunner() ).runCalled);
   }

   private String extractComponentName(MyAbbotAction runAbbotCommand)
   {
      return runAbbotCommand.getParameter(2).stringValue().split( "," )[0];
   }

   private static class MyAbbotAction extends AbbotAction
   {
      String actualActionXML = "";
      String actualComponentName = "";

      protected Step createStep(abbot.script.Script script, String str) throws InvalidScriptException, IOException
      {
         actualActionXML = str;
         return super.createStep(script, str);
      }

      public Component findComponent(String name)
      {
         actualComponentName = name;
         return new Component() {

         };
      }
   }

   private StepRunner getStepRunner()
   {
      return new MyStepRunner();
   }

   private static class MyStepRunner extends StepRunner
   {
      boolean runCalled = false;
      public void run(Step step) throws Throwable
      {
         runCalled = true;
      }
   }
}
