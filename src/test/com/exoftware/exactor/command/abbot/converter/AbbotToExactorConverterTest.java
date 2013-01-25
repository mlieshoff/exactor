package com.exoftware.exactor.command.abbot.converter;

import com.exoftware.exactor.Constants;
import com.exoftware.exactor.command.abbot.Util;
import com.exoftware.util.FileCollector;
import junit.framework.TestCase;

import java.io.File;

public class AbbotToExactorConverterTest extends TestCase {

    public void testAcceptance() throws Exception {
        File dir = new File(Constants.DATA_DIR + "/abbot");
        File[] abbotFiles = FileCollector.filesWithExtension(dir, "xml");
        for (int i = 0; i < abbotFiles.length; i++) {
            File abbotFile = abbotFiles[i];
            File expectedExactorFile = new File(dir, abbotFile.getName().replace(".xml", ".act"));
            String expected = Util.getFileContent(expectedExactorFile.getAbsolutePath());
            assertEquals("Conversion of : " + abbotFile.getName() + " did not match content in "
                    + expectedExactorFile.getName(), expected.trim(), AbbotToExactorConverter.convertToExactor(
                    abbotFile, true).trim());
        }
    }

    public void testConvertAbbotComponentXMLToExactorScript() throws Exception {
        assertComponentXMLToExactorScript("AbbotComponent \"CLASS\" \"ID\" \"NAME\" \"INDEX\" \"PARENT\" \"WINDOW\" "
                + "\"ROOT\" \"TITLE\" \"TAG\" \"ICON\" \"INVOKER\" \"BORDERTITLE\"",
                "   <component class=\"CLASS\" \n"
                + "              id=\"ID\"\n"
                + "              index=\"INDEX\"\n"
                + "              name=\"NAME\"\n"
                + "              parent=\"PARENT\"\n"
                + "              window=\"WINDOW\"\n"
                + "              root=\"ROOT\"\n"
                + "              title=\"TITLE\"\n"
                + "              tag=\"TAG\"\n"
                + "              icon=\"ICON\"\n"
                + "              invoker=\"INVOKER\"\n"
                + "              borderTitle=\"BORDERTITLE\"/>");
    }

    public void testConvertAbbotEventXMLToExactorScript() throws Exception {
        assertComponentXMLToExactorScript("AbbotEvent \"COMPONENT\" \"KIND\" \"TYPE\" \"X\" \"Y\" \"KEYCODE\" "
                + "\"MODIFIERS\"",
                "    <event component=\"COMPONENT\" "
                + "           kind=\"KIND\" "
                + "           type=\"TYPE\" "
                + "           x=\"X\" "
                + "           y=\"Y\" "
                + "           keyCode=\"KEYCODE\" "
                + "           modifiers=\"MODIFIERS\"  />");
    }

    public void testConvertAbbotLaunchXMLToExactorScript() throws Exception {
        assertEquals("AbbotLaunch \"com.exoftware.exactor.ide.ExactorIDE\" \"main\" \"[]\" \".\"",
                AbbotToExactorConverter.processElement(Util.createElement(
                "<launch args=\"[]\" class=\"com.exoftware.exactor.ide.ExactorIDE\" classpath=\".\" method=\"main\" />")
                , false));
    }

    public void testConvertAbbotLaunchXMLToExactorScript_ChangeBackSlashesToForward() throws Exception {
        assertEquals("AbbotLaunch \"com.exoftware.exactor.ide.ExactorIDE\" \"main\" \"[]\" \"c:/adir/ajar.jar\"",
                AbbotToExactorConverter.processElement(Util.createElement(
                "<launch args=\"[]\" class=\"com.exoftware.exactor.ide.ExactorIDE\" classpath=\"c:\\adir\\ajar.jar\" "
                + "method=\"main\" />"), false));
    }

    public void testConvertAbbotActionXMLToExactorScript() throws Exception {
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTabbedPane\" \"actionSelectTab\" \"Tabbed Pane,\\\"tab 1\\\"\"", "<action args=\"Tabbed Pane,&quot;tab 1&quot;\" class=\"javax.swing.JTabbedPane\" method=\"actionSelectTab\" />");
        assertActionXMLToExactorScript("AbbotAction \"java.awt.Dialog\" \"actionClose\" \"Dialog\"", "<action class=\"java.awt.Dialog\" method=\"actionClose\" args=\"Dialog\" />");
        assertActionXMLToExactorScript("AbbotAction \"java.awt.TextComponent\" \"actionFocus\" \"textField\"", "<action class=\"java.awt.TextComponent\" method=\"actionFocus\" args=\"textField\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.AbstractButton\" \"actionClick\" \"OK\"", "<action class=\"javax.swing.AbstractButton\" method=\"actionClick\" args=\"OK\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.AbstractButton\" \"actionClick\" \"Select Test Suite...\"", "<action class=\"javax.swing.AbstractButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Button\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Button\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"OK Button\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK Button\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"OK\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"OK\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"OK\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"OK\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"OK\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Run\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Run\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Run\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Run\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JButton\" \"actionClick\" \"Select Test Suite...\"", "<action class=\"javax.swing.JButton\" method=\"actionClick\" args=\"Select Test Suite...\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JComboBox\" \"actionSelectItem\" \"JComboBox Instance,Combo 10\"", "<action class=\"javax.swing.JComboBox\" method=\"actionSelectItem\" args=\"JComboBox Instance,Combo 10\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JComboBox\" \"actionSelectItem\" \"JComboBox Instance,Combo 10\"", "<action class=\"javax.swing.JComboBox\" method=\"actionSelectItem\" args=\"JComboBox Instance,Combo 10\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\"", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\"", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\"", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionShowPopupMenu\" \"Dynamic\"", "<action class=\"javax.swing.JLabel\" method=\"actionShowPopupMenu\" args=\"Dynamic\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,-1,-1,Blue\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,-1,-1,Blue\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,-1,-1,Blue\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,-1,-1,Blue\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,32,4,Green\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,32,4,Green\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,Blue\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,Blue\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Dynamic,Blue\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Dynamic,Blue\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,-1,-1,Black\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,-1,-1,Black\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,-1,-1,White\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,-1,-1,White\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,Black\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,Black\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,Black\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,Black\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,White\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,White\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JLabel\" \"actionSelectPopupMenuItem\" \"Static,White\"", "<action class=\"javax.swing.JLabel\" method=\"actionSelectPopupMenuItem\" args=\"Static,White\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\"", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\"", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JList\" \"actionSelectValue\" \"JList Instance,MyCodeTest - example\"", "<action class=\"javax.swing.JList\" method=\"actionSelectValue\" args=\"JList Instance,MyCodeTest - example\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JList\" \"actionSelectIndex\" \"My List,0\"", "<action class=\"javax.swing.JList\" method=\"actionSelectIndex\" args=\"My List,0\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JList\" \"actionSelectIndex\" \"My List,7\"", "<action class=\"javax.swing.JList\" method=\"actionSelectIndex\" args=\"My List,7\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Copy\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Copy\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Item 1\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Item 1\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JMenuItem\" \"actionSelectMenuItem\" \"Submenu item\"", "<action class=\"javax.swing.JMenuItem\" method=\"actionSelectMenuItem\" args=\"Submenu item\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTable\" \"actionSelectCell\" \"ScriptTable Instance,8,0\"", "<action class=\"javax.swing.JTable\" method=\"actionSelectCell\" args=\"ScriptTable Instance,8,0\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTextField\" \"actionClick\" \"My Text Field,3,7\"", "<action class=\"javax.swing.JTextField\" method=\"actionClick\" args=\"My Text Field,3,7\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTextField\" \"actionClick\" \"My Text Field,3,7\"", "<action class=\"javax.swing.JTextField\" method=\"actionClick\" args=\"My Text Field,3,7\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTextField\" \"actionText\" \"My Text Field,Get outta here\"", "<action class=\"javax.swing.JTextField\" method=\"actionText\" args=\"My Text Field,Get outta here\" />");
        assertActionXMLToExactorScript("AbbotAction \"javax.swing.JTextField\" \"actionText\" \"My Text Field,Get outta here\"", "<action class=\"javax.swing.JTextField\" method=\"actionText\" args=\"My Text Field,Get outta here\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionClick\" \"?\"", "<action method=\"actionClick\" args=\"?\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionClick\" \"High Button\"", "<action method=\"actionClick\" args=\"High Button\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionClick\" \"Low Button\"", "<action method=\"actionClick\" args=\"Low Button\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionSelectMenuItem\" \"New Script...\"", "<action method=\"actionSelectMenuItem\" args=\"New Script...\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionSelectMenuItem\" \"Run To\"", "<action method=\"actionSelectMenuItem\" args=\"Run To\" />");
        assertActionXMLToExactorScript("AbbotAction \"\" \"actionKeyStroke\" \"VK_ESCAPE\"", "<action method=\"actionKeyStroke\" args=\"VK_ESCAPE\" />");
    }

    private void assertActionXMLToExactorScript(String expectedExactorScript, String actionXML) throws Exception {
        assertEquals(expectedExactorScript, AbbotToExactorConverter.processElement(Util.createElement(actionXML), false)
        );
    }

    private void assertComponentXMLToExactorScript(String expectedExactorScript, String compXML) throws Exception {
        assertEquals(expectedExactorScript, AbbotToExactorConverter.processElement(Util.createElement(compXML), false));
    }
}

