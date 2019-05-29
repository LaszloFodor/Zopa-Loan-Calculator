package service;

import exception.AvailableAmountException;
import model.Lender;
import model.Quote;
import org.junit.Before;
import org.junit.Test;
import parser.CSVParser;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LoanServiceTest {

    LoanService loanService;

    Quote quote;

    String CSVFile = "MarketData.csv";

    @Before
    public void setup() throws IOException, AvailableAmountException {
        loanService = new LoanService(CSVFile);
        quote = loanService.getRate(1000);
    }

    @Test
    public void checkRate() {
        assertEquals(0.07003999999999999, quote.getMonthlyRate(), 0.01);
    }

    @Test
    public void checkMonthlyRepayment() {
        assertEquals(30.780594385542404, quote.getMonthlyRepayment(), 0.01);
    }

    @Test
    public void checkTotalPayment() {
        assertEquals(1108.1013978795265, quote.getTotalRepayment(), 0.01);
    }

    @Test
    public void checkLoanLength() {
        assertEquals(36, loanService.getLoanLength());
    }

    @Test
    public void checkAvailableLoanAmount() {
        assertEquals(2330, loanService.getAvailableAmount());
    }

    @Test
    public void checkLenders() throws IOException {
        List<Lender> lenders = CSVParser.parseCSV(CSVFile);
        assertEquals(lenders.size(), loanService.getLenders().size());
        Collections.sort(lenders, Comparator.comparingDouble(Lender::getRate));
        for (int i = 0; i < lenders.size(); i++) {
            assertEquals(lenders.get(i).getAmount(), loanService.getLenders().get(i).getAmount());
            assertEquals(lenders.get(i).getRate(), loanService.getLenders().get(i).getRate(), 0.01);
            assertEquals(lenders.get(i).getName(), loanService.getLenders().get(i).getName());
        }
    }

    @Test(expected = IOException.class)
    public void testLoanServiceWithNotExistingCSV() throws IOException {
        loanService = new LoanService("asdf");
    }

    @Test(expected = AvailableAmountException.class)
    public void testGetRateWithUnavailableAmount() throws AvailableAmountException {
        loanService.getRate(100000000);
    }

}
