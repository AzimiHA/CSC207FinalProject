import users.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LoginSystem extends UserSystem{
    private final LoginPrompts prompts;

    private final TraderActions traderActions;
    private final AdminActions adminActions;

    private int nextSystem;
    private String nextUser;
    private final BufferedReader br;
    private boolean running;

    /**
     * Constructor for this login system
     * @param traderActions The traderActions use case class
     * @param adminActions The Admin Actions use case class
     * */
    public LoginSystem(TraderActions traderActions, AdminActions adminActions) {

        this.traderActions = traderActions;
        this.adminActions = adminActions;
        br = new BufferedReader(new InputStreamReader(System.in));

        prompts = new LoginPrompts();
    }

    /**
     * runs the the program from the login screen allowing the user to login, sign up, or exit.
     */
    public void run() {
        init();
        try {
            while (running) {
                prompts.resetPrompts();
                System.out.println(prompts.next());
                String input = br.readLine();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e){
                    choice = 0;
                }
                switch (choice){
                    case 1:
                        login();
                        break;
                    case 2:
                        signup();
                        break;
                    default:
                        if (!input.equals("exit")) {
                            System.out.println(prompts.invalidInput());
                        }
                        else{
                            break;
                        }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void login() throws IOException {
        System.out.println(prompts.next());
        String username = br.readLine();

        if (!traderActions.checkUsername(username)) {
            System.out.println(prompts.next());
            String password = br.readLine();

            Trader trader = traderActions.login(username, password);
            if (trader != null) {
                nextUser = username;
                nextSystem = 2;
                stop();
            }
            else{
                System.out.println(prompts.wrongPassword());
            }
        } else if (!adminActions.checkUsername(username)) {
                System.out.println(prompts.next());
                String password = br.readLine();

                Admin admin = adminActions.checkCredentials(username, password);
                if (admin != null) {
                    nextUser = username;
                    nextSystem = 3;
                    stop();
                }
                else{
                    System.out.println(prompts.wrongPassword());
                }
        } else {
                System.out.println(prompts.wrongUser());
            }
    }

    private void signup(){
        nextSystem = 1;
        stop();
    }

    @Override
    protected void init() {
        running = true;
        System.out.println(prompts.openingMessage());
    }

    public String getNextUser(){
        return nextUser;
    }

    protected int getNextSystem(){
        return nextSystem;
    }
    @Override
    protected void stop() {
        running = false;
    }

}