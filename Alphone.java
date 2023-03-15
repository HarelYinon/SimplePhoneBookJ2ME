import javax.microedition.io.ConnectionNotFoundException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import java.util.Vector;

public class Alphone extends MIDlet implements CommandListener{

    private Display display;

    //main screen commands
    private Command cmdSearch;

    //search screen commands
    private Command cmdBack;


    //screens
    private List relevantContactsScreen;
    private Form mainSearchScreen;

    private static final int MAX_NAME_LEN = 45;
    private TextField nameForSearch;

    private String[] reasultAlphone;

    // Here you should add your contacts in format: "first_name phonenumber"
    private String[] alphoneList = {
        "jhon_doe 14255551212"
    };

    private int ALPHONE_LENGTH = alphoneList.length;

    public Alphone() {

        display = Display.getDisplay(this);
        cmdSearch = new Command("Search", Command.OK, 1);
        cmdBack = new Command("Back", Command.OK, 1);

    }

    public void startApp() {
        generatemainSearchScreeneen();
    }
    protected void pauseApp() {
    }

  /**
   * Called by the system to end our MIDlet. No actions required by our
   * MIDlet.
   */
    protected void destroyApp(boolean unconditional) {
    }

    //Personal functiions

    private Screen showRelevantContacts(){

            String nameToSearch=nameForSearch.getString();
            int relevant_names_count = 0;
            Vector relvantNamesVector = new Vector(ALPHONE_LENGTH);
            for(int i=0;i<ALPHONE_LENGTH;++i)
            {
                String contact=alphoneList[i];
                if(contact.indexOf(nameToSearch, 0)!=-1){
                    relvantNamesVector.addElement(contact);
                    ++relevant_names_count;
                }
            }
            reasultAlphone=null;
            reasultAlphone = new String[relevant_names_count];

            for(;relevant_names_count>0;--relevant_names_count)
                reasultAlphone[relevant_names_count-1]=(String) relvantNamesVector.elementAt(relevant_names_count-1);

            relevantContactsScreen = new List("results", List.IMPLICIT, reasultAlphone, null);
            relevantContactsScreen.addCommand(cmdBack);
            relevantContactsScreen.setCommandListener(this);

        display.setCurrent(relevantContactsScreen);
        return relevantContactsScreen;
    }

    private Screen generatemainSearchScreeneen() {
        if (mainSearchScreen == null) {
            mainSearchScreen = new Form("Phonebook created by HY");
            mainSearchScreen.addCommand(cmdSearch);
            mainSearchScreen.setCommandListener(this);

            nameForSearch = new TextField("Search:", "", MAX_NAME_LEN,
                    TextField.ANY);
            mainSearchScreen.append(nameForSearch);
        }
    display.setCurrent(mainSearchScreen);
    return mainSearchScreen;
  }

    public void commandAction(Command command, Displayable displayable) {
        if(displayable == relevantContactsScreen)
        {
            if(command == List.SELECT_COMMAND){
                Dial();
            }else if(command == cmdBack){
                generatemainSearchScreeneen();
            }
        }else if(displayable == mainSearchScreen){
            if(command == List.SELECT_COMMAND || command == cmdSearch && !nameForSearch.getString().equals("")){
                showRelevantContacts();
            }
        }else{
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private void Dial(){
        String contact=reasultAlphone[relevantContactsScreen.getSelectedIndex()];
        String phone_number = contact.substring(contact.indexOf(' ')+1);
        try {
            platformRequest("tel:" + phone_number);
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}