package Model;

public class Random {
    // Generating Random for Account
    public static String createAccountNumber(){
        String accountNumber;

        long timeSeed = System.nanoTime();
        double randSeed = Math.random() * 1000;
        long midSeed = (long) (timeSeed * randSeed);
        accountNumber = String.valueOf(midSeed);
        return accountNumber;
    }
}
