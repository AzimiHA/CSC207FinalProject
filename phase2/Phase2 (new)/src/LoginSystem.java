
import adminsys.*;
import gateway.*;
import items.ItemManager;
import signupsys.SignupSystem;
import tradersys.*;
import trades.TradeManager;
import users.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginSystem extends UserSystem{
    private String path;
    private ConfigReader configReader;
    private ConfigWriter configWriter;

    private TraderSystem traderSystem;
    private AdminSystem adminSystem;
    private SignupSystem signupSystem;
    private LoginPrompts prompts;

    private TraderActions traderActions;
    private AdminActions adminActions;
    private ItemManager itemManager;
    private TradeManager tradeManager;
    private int nextSystem;

    private Admin admin;

    /**
     * Constructor for the Login System
     * @param path the file path.
     * @throws IOException Throws an input/output exception
     */
    public LoginSystem(String path) throws IOException {
        this.path = path;
        configReader = new ConfigReader(this.path);
        configWriter = new ConfigWriter();

        traderActions = configReader.getTraderActions();
        adminActions = configReader.getAdminActions();
        itemManager = configReader.getItemManager();
        tradeManager = configReader.getTradeManager();

        prompts = new LoginPrompts();
        signupSystem = new SignupSystem(traderActions, adminActions);
    }

    /**
     * runs the the program from the login screen allowing the user to login, sign up, or exit.
     */
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println(prompts.openingMessage());
            String input;
            do {
                ArrayList<String> userInfo = new ArrayList<>();
                prompts.resetPrompts();
                System.out.println(prompts.next());
                input = br.readLine();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e){
                    choice = 0;
                }
                switch (choice) {
                    //TODO split this into private methods
                    case 1:
                        System.out.println(prompts.next());

                        input = br.readLine();
                        userInfo.add(input);

                        if (!traderActions.checkUsername(userInfo.get(0))) {

                            System.out.println(prompts.next());
                            input = br.readLine();
                            userInfo.add(input);

                            Trader trader = traderActions.login(userInfo.get(0), userInfo.get(1));
                            if (trader != null) {
                                traderSystem = new TraderSystem(trader, traderActions, itemManager, tradeManager, adminActions);
                                traderSystem.run();
                                configWriter.saveFile(this.path, traderActions, adminActions, tradeManager);
                            }
                            else{
                                System.out.println(prompts.wrongPassword());
                            }
                        } else {
                            if (!adminActions.checkUsername(userInfo.get(0))) {
                                System.out.println(prompts.next());
                                input = br.readLine();
                                userInfo.add(input);

                                admin = adminActions.checkCredentials(userInfo.get(0), userInfo.get(1));
                                if (admin != null) {
                                    adminSystem = new AdminSystem(traderActions, adminActions, tradeManager);
                                    adminSystem.setCurrentAdmin(admin);
                                    adminSystem.run();
                                    configWriter.saveFile(this.path, traderActions, adminActions,tradeManager);
                                }
                                else{
                                    System.out.println(prompts.wrongPassword());
                                }
                            } else {
                                System.out.println(prompts.wrongUser());
                                continue;
                            }
                        }
                        break;
                    case 2:
                        nextSystem = 1;
//                        signupSystem.run();
//                        configWriter.saveFile(this.path, traderActions, adminActions,tradeManager);
                        break;
                    default:
                        if (!input.equals("exit")) {
                            System.out.println(prompts.invalidInput());
                    }
                }

            } while (!input.equals("exit"));
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }

    private void login(int choice){

    }

    private void signup(){

    }

    @Override
    protected void init() {

    }

    @Override
    protected int stop() {
        return nextSystem;
    }

}
