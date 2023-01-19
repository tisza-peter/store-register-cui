package hu.lechnerkozpont.bootcamp.storage_ui;

import hu.lechnerkozpont.bootcamp.storage.SlimStoreRegister;
import hu.lechnerkozpont.bootcamp.storage.enumeration.StorePesristenceType;
import hu.lechnerkozpont.bootcamp.storage.exceptions.ItemNotAvailableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ConsoleApp {

    static StorePesristenceType selectedMode = null;
    static SlimStoreRegister sSR = new SlimStoreRegister();
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String commandline;
        String[] commandWithArgs;
        System.out.println("please enter the appropriate command");
        boolean exitIsSet = false;
        while(!exitIsSet) {
            commandline = reader.readLine();
            try {
                String trimmedcommandline=(commandline.trim().replaceAll("\\s{2,}", " "));
                exitIsSet=processCommand(trimmedcommandline.split(" "));
            }
            catch(IllegalArgumentException e) {
                System.err.println("Illegal Argument Exception: "+e.getMessage());
            }
            catch(IllegalStateException e) {
                System.err.println("Illegal State Exception: "+e.getMessage());
            }
            catch (ItemNotAvailableException e)
            {
                System.err.println("Item Not Available Exception: "+e.getMessage());
            }
        }
    }

    private static Boolean processCommand( String[] commandWithArgs)
    {
        String command = commandWithArgs[0];
        String[] arguments = Arrays.copyOfRange(commandWithArgs, 1, commandWithArgs.length);
        Boolean exitIsSet = false;
        switch (command) {
            case "quit":
            case "q":
                exitIsSet = true;
                break;
            case "mode":
            case "m":
                if (arguments.length<1)
                {
                    throw new IllegalArgumentException("there must be at least one argument");
                }
                selectedMode = null;
                switch (arguments[0]) {
                    case "TEST":
                        selectedMode = StorePesristenceType.InMemory;
                        System.out.println("You have successfully set the test mode");
                        break;
                    case "PERSIST":
                        selectedMode = StorePesristenceType.File;
                        System.out.println("You have successfully set the persist mode");
                        break;
                    default:
                        throw new IllegalArgumentException("it should be 'PERSIST' or 'TEST'");
                }
                sSR.setPersistanceType(selectedMode);
                break;
            case "sell":
            case "s":
                if(selectedMode == null)
                {
                    throw new IllegalStateException("a storage mode must be selected before sale");
                }
                if (arguments.length<2)
                {
                    throw new IllegalArgumentException("there must be a minimum of two arguments");
                }
                try {
                    Integer.parseInt(arguments[1]);
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("the second argument must be an integer");
                }
                int numberOfEffectiveSales = sSR.sellProductItem(arguments[0],  Integer.valueOf(arguments[1]));
                System.out.println("you have successfully sold " + numberOfEffectiveSales + " "+arguments[0] + "(s)");
                break;
            case "buy":
            case "b":
                if(selectedMode == null)
                {
                    throw new IllegalStateException("a storage mode must be selected before purchase");
                }
                if (arguments.length<2)
                {
                    throw new IllegalArgumentException("there must be a minimum of two arguments");
                }
                try {
                    Integer.parseInt(arguments[1]);
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("the second argument must be an integer");
                }
                sSR.buyProductItem(arguments[0], Integer.valueOf(arguments[1]));
                System.out.println("You have successfully purchased "+arguments[1]+" "+arguments[0]+"(s)");
                break;
            case "create":
            case "c":
                if(selectedMode == null)
                {
                    throw new IllegalStateException("a storage mode must be selected before creating a product");
                }
                if (arguments.length<1)
                {
                    throw new IllegalArgumentException("there must be at least one argument");
                }
                sSR.createProduct(arguments[0]);
                System.out.println("you have successfully added "+arguments[0]+"s to the product range");
                break;
            default:
                System.out.println("commands that can be entered:");
                System.out.println("   'quit' end of program");
                System.out.println("   'q' end of program");
                System.out.println("   'sell [OneWordProductName] [NumberOfItem]' you can specify how much of which product you are selling");
                System.out.println("   's [OneWordProductName] [NumberOfItem]' you can specify how much of which product you are selling");
                System.out.println("   'buy [OneWordProductName] [NumberOfItem]' you can specify how much of which product you buy");
                System.out.println("   'b [OneWordProductName] [NumberOfItem]' you can specify how much of which product you buy");
                System.out.println("   'create [OneWordProductName]' you can specify which new product you add to the product range");
                System.out.println("   'c [OneWordProductName]' you can specify which new product you add to the product range");
                System.out.println("   'mode [PERSIST|TEST]' you can specify whether you want to save your changes");
                System.out.println("   'm [PERSIST|TEST]' you can specify whether you want to save your changes");
                break;
        }
    return exitIsSet;
    }

}
