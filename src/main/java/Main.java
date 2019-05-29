import exception.ArgumentNotFoundException;
import exception.AvailableAmountException;
import exception.InvalidAmountException;
import model.Quote;
import service.LoanService;

import java.io.IOException;

public class Main {

    public static void main(String... args) {
        try {
            String csv = args[0];
            int loanAmount = Integer.parseInt(args[1]);
            argumentCheck(csv, loanAmount);
            LoanService loanService = new LoanService(csv);
            Quote quote = loanService.getRate(loanAmount);
            buildOutput(loanAmount, quote);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough arguments");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format");
        } catch (ArgumentNotFoundException e) {
            System.out.println("Argument not found");
        } catch (InvalidAmountException e) {
            System.out.println("Invalid amount");
        } catch (IOException e) {
            System.out.println("Invalid csv file");
        } catch (AvailableAmountException e) {
            System.out.println("Available loan amount exceeded");
        }
    }

    private static void buildOutput(int amount, Quote quote) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Requested amount: £%d\n", amount));
        sb.append(String.format("Rate: %.1f%%\n", quote.getMonthlyRate() * 100));
        sb.append(String.format("Monthly repayment: £%.2f\n", quote.getMonthlyRepayment()));
        sb.append(String.format("Total repayment: £%.2f", quote.getTotalRepayment()));
        System.out.println(sb.toString());
    }

    private static void argumentCheck(String csv, int loanAmount) throws ArgumentNotFoundException, InvalidAmountException {
        if (csv == null || !csv.endsWith(".csv")) {
            throw new ArgumentNotFoundException();
        } else if(loanAmount < 1000 || loanAmount > 15000 || loanAmount % 100 != 0) {
            throw new InvalidAmountException();
        }
    }
}
