
public class main2 {
    public static void main(String[] args) {
        String command = "ls";
        String userName = "kromnikov";
        String password = "12345";
        String connectionIP = "10.0.0.16";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        String errorMessage = instance.connect();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
        }
        else {
            System.out.println("LOL");
        }

        String expResult = "\n";
        String result = instance.sendCommand(command);
        // close only after all commands are sent
        instance.close();
//        assertEquals(expResult, result);
    }
}
