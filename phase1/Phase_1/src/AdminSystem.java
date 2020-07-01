import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminSystem extends UserSystem{
    private boolean running;
    private AdminActions adminActions;
    private Admin currentAdmin;
    private AdminPrompts adminPrompts;
    private String fileName;
    private int atLeast;
    private int maxIncomplete;
    private int maxWeekly;
    private TraderActions traderActions;
    private Scanner scanner;
    private final String toMainMenu = "-1";


    /**
     * Constructor for the AdminSystem
     * @param admin The admin user that has logged into the system
     * @param fileName The name of the file
     */
    public AdminSystem(Admin admin, String fileName){
        //I think we should read in from files.
        this.fileName = fileName;
        currentAdmin = admin;
        scanner = new Scanner(System.in);

        ArrayList<Admin> admins = new ArrayList<>();
        //gotta read this too
        ArrayList<Admin> adminRequests = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line = in.readLine();
            while (line != null){
                String[] info = line.split(",");
                admins.add(new Admin(info[0], info[1]));
                line = in.readLine();
            }
            in.close();

        }catch (IOException iox){
            System.out.println("File Not Found");
        }
        adminActions = new AdminActions(admins, adminRequests);
        traderActions = new TraderActions(new ArrayList<>());
        //May need to change constructor
        adminPrompts = new AdminPrompts();
        //may want to change the following


    }


    //Everything below here right now is part of the loop
    //This method helps set up some stuff
    private void init(){
        running = true;
        //this is a temporary holder
        adminPrompts.displayOptions();

    }

    /**
     * Runs the program in a loop
     */
    @Override
    public void run() {
        init();
        while (running){
            int option = scanner.nextInt();
            switch (option){
                case 1:
                    adminApproval();
                    break;
                case 2:
                    freezeMenu();
                    break;
                case 3:
                    approveItemsMenu();
                    break;
                case 4:
                    viewTraders();
                    break;
                case 5:
                    changeLimit();
                    break;
                case 6:
                    changeUserInfo();
                    break;
                case 7:
                    stop();
                    break;
                default:
                    System.out.println("Command not recognized. Try again.");
                    break;
            }
        }
    }

    protected void update(){
    }

    @Override
    protected void stop() {
        running = false;
    }

    /**
     * Display the menu that allows the admin to manage frozen/unfrozen accounts
     */
    public void freezeMenu(){
        ArrayList<Trader> traders = traderActions.getTraders();
        adminPrompts.displayFreezeMenu();
        int option = scanner.nextInt();
        do {
            switch (option) {
                case 1:
                    ArrayList<Trader> flaggedAccounts =
                            traderActions.getListOfFlaggedAccounts(atLeast, maxIncomplete, maxWeekly);
                    adminPrompts.displayFreezeOptions(1, flaggedAccounts);
                    int chosenFlag = scanner.nextInt();
                    boolean freeze = adminActions.freezeAccount(flaggedAccounts.get(chosenFlag - 1));
                    adminPrompts.displayFreezeConfirmation(freeze, "Freeze");
                    break;
                case 2:
                    ArrayList<Trader> frozenAccounts = traderActions.getAllRequestToUnfreeze();
                    adminPrompts.displayFreezeOptions(2, frozenAccounts);
                    int chosenFrozen = scanner.nextInt();
                    boolean unfreeze = adminActions.unfreezeAccount(frozenAccounts.get(chosenFrozen - 1));
                    adminPrompts.displayFreezeConfirmation(unfreeze, "Unfreeze");
                    break;
                case 3:
                    adminPrompts.displayFreezeOptions(3, traders);
                    int chosenAccount = scanner.nextInt();
                    boolean freezeGeneral = adminActions.freezeAccount(traders.get(chosenAccount - 1));
                    adminPrompts.displayFreezeConfirmation(freezeGeneral, "Freeze");
                    break;
                default:
                    adminPrompts.commandNotRecognized();
                    break;
            }
            System.out.print("Enter ["+toMainMenu+"] to return to the main menu or enter any number return to Freeze/" +
                    "Unfreeze Menu: ");
            option = scanner.nextInt();
        } while(option != Integer.parseInt(toMainMenu));
        //I don't think these two lines are needed since dowhile loop is already taking care of that
//        adminPrompts.setToMainMenu();
//        setToMainMenu();
    }

    /**
     * Display the menu that allows the admin to approve items
     */
    public void approveItemsMenu() {
        adminPrompts.displayItemMenu(traderActions.getTradersNeedingApproval());
        System.out.print("Enter the number beside trader that you want to view: ");
        String option = scanner.next();
        do {
            int traderID = Integer.parseInt(option);
            if (traderID <= traderActions.getTradersNeedingApproval().size()) {
                Trader trader = traderActions.getTradersNeedingApproval().get(traderID - 1);
                adminPrompts.displayTraderProposedItems(trader.getProposedItems());
                option = scanner.next();
                int itemID = Integer.parseInt(option);
                Boolean approved = approveOrReject();
                if (approved == null){
                    break;
                }
                else if (option.equals("all")) {
                    adminActions.approveAllItems(trader, approved);
                }
                else if (Integer.parseInt(option) <= trader.getProposedItems().size()){
                    adminActions.approveItem(trader, trader.getProposedItems().get(itemID - 1), approved);
                }
                adminPrompts.confirmApproval(approved);
            } else {
                adminPrompts.commandNotRecognized();
            }
        }
        while(Integer.parseInt(option) != Integer.parseInt(toMainMenu));
        setToMainMenu();
    }

    /**
     * Allows an admin to approve or reject administrative requests.
     */
    public void adminApproval(){
        adminPrompts.displayAdminApproval(adminActions.getAdminRequests());
        String option = scanner.next();
        Boolean approved = approveOrReject();
        boolean loop = false;
        if (approved == null){
            loop = true;
        }
        else {
            if (option.equals("0")) {
                setToMainMenu();
            } else if (option.equals("all")) {
                System.out.println("Processing...");
                confirmApproval(adminActions.approveAllAdmins(approved));
            } else if (adminActions.getAdminRequests().toString().contains(option)) {
                System.out.println("Processing");
                confirmApproval(adminActions.approveAdmin(option, approved));
            } else {
                loop = true;
                System.out.println("Input not recognized.");
            }
        }
        if (loop){
            adminApproval();
        }
    }

    private void confirmApproval(boolean approved){
        adminPrompts.confirmApproval(approved);
        adminApproval();
    }

    private Boolean approveOrReject(){
        adminPrompts.displayApproveOrReject();
        String choice = scanner.next();
        switch (choice) {
            case "1":
                return true;
            case "2":
                return false;
            case "3":
                //The method returns a boolean so
                //for now, I changed it to false (Junhee)
                //I made it a reference type Boolean which has null, the null case
                // is handled in the adminApproval method which is a bit messy rn
                //but will fix (Jeffrey)
                return null;
            default:
                adminPrompts.commandNotRecognized();
                return approveOrReject();
        }
    }


    /**
     * Change username or password of an admin.
     */
    public void changeUserInfo(){
        //Maybe we want to add the option to go back to the main menu
        String option;
        int succeed = -1;
        do {
            adminPrompts.displayChangeUserInfoMenu();
            option = scanner.next();
            switch (option) {
                case "1":
                    succeed = changeUsername();
                    break;
                case "2":
                    succeed = changePassword();
                    break;
                default:
                    adminPrompts.displayErrorMessage();
            }

            if (succeed != -1) {
                adminPrompts.displaySuccessMessage(succeed, "username/password");
            }
            adminPrompts.displayReturnToMainMenu();
            option = scanner.next();
        }while(!option.equals(toMainMenu));
    }

    /**
     * Change limit on transactions
     */
    public void changeLimit(){
        //Option for main menu?
        String option;
        int succeed = -1;
        do{
            adminPrompts.displayChangeLimitMenu();
            option = scanner.next();
            switch (option){
                case "1":
                case "2":
                case "3":
                    succeed = setNewLimit(option);
                    break;
                default:
                    adminPrompts.displayErrorMessage();
            }
            if (succeed != -1) {
                adminPrompts.displaySuccessMessage(succeed, "limit");
            }
            adminPrompts.displayReturnToMainMenu();
            option = scanner.next();

        }while(!option.equals(toMainMenu));
    }

    private int setNewLimit(String option){
        adminPrompts.displayEnterNewMessage("limit");
        int newValue = scanner.nextInt();
        if (newValue < 0) {
            return 0;
        }else {
            switch (option) {
                case "1":
                    maxWeekly = newValue;
                    break;
                case "2":
                    maxIncomplete = newValue;
                    break;
                case "3":
                    atLeast = newValue;
                    break;
            }
            return 1;
        }
    }

    private int changeUsername(){
        adminPrompts.displayEnterNewMessage("username");
        String newName = scanner.next();
        if (newName.equals(currentAdmin.getUsername())){
            return 0;
        }
        currentAdmin.setUsername(newName);
        return 1;
    }

    private int changePassword(){
        adminPrompts.displayEnterNewMessage("password");
        String newPassword = scanner.next();
        if (newPassword.equals(currentAdmin.getPassword())){
            return 0;
        }
        currentAdmin.setPassword(newPassword);
        return 1;

    }

    /**
     * Prints a trader with the input username to the screen
     */
    public void viewTraders(){
        adminPrompts.displayTraderMenu();
        String user = scanner.next();
        switch (user){
            case "0":
                setToMainMenu();
            case "all":
                adminPrompts.displayAllTraders(traderActions.getTraders());
                break;
            default:
                adminPrompts.displayTrader(findTrader(user));
        }
        restartViewTraders();
    }

    private Trader findTrader(String user){
        for (Trader trader: traderActions.getTraders()){
            if (trader.getUsername().equals(user)){
                return trader;
            }
        }
        return null;
    }

    private void restartViewTraders() {
        adminPrompts.displayRestartTrader();
        String choice = scanner.next();
        switch (choice){
            case "0":
                setToMainMenu();
            case "1":
                viewTraders();
                break;
            default:
                adminPrompts.commandNotRecognized();
                restartViewTraders();
                break;
        }
    }

    /**
     * Returns the administrator to the main menu for administrator options
     */
    public void setToMainMenu(){
        adminPrompts.setToMainMenu();
        stop();
        init();
    }
}
