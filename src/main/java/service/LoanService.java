package service;

import exception.AvailableAmountException;
import model.Lender;
import model.Quote;
import parser.CSVParser;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoanService {

    private static final int LOAN_LENGTH = 36;

    private List<Lender> lenders;

    private int availableAmount;

    public LoanService(String csv) throws IOException {
        this.lenders = CSVParser.parseCSV(csv);
        Collections.sort(lenders, Comparator.comparingDouble(Lender::getRate));
        for (Lender lender : lenders) {
            availableAmount += lender.getAmount();
        }
    }

    public Quote getRate(int desiredAmount) throws AvailableAmountException {
        checkAvailableAmountExceeded(desiredAmount);

        double totalInterest = 0.0;

        int remainingAmount = desiredAmount;
        for (Lender lender : lenders) {
            int borrowed = Math.min(remainingAmount, lender.getAmount());
//            int borrowed = remainingAmount < lender.getAmount() ? remainingAmount : lender.getAmount();
            totalInterest += borrowed * lender.getRate();
            remainingAmount -= borrowed;
            if (remainingAmount == 0)
                break;
        }

        double annualRate = totalInterest / desiredAmount;
        double interestRatePerMonth = Math.pow((1 + annualRate), (1. / 12.)) - 1;

        double monthlyPayment = (interestRatePerMonth * desiredAmount) /
                (1 - (Math.pow(1 + interestRatePerMonth, -LOAN_LENGTH)));



        return new Quote(annualRate, monthlyPayment, monthlyPayment * LOAN_LENGTH);
    }

    private void checkAvailableAmountExceeded(int desiredAmount) throws AvailableAmountException {
        if (availableAmount < desiredAmount) {
            throw new AvailableAmountException();
        }
    }

    public static int getLoanLength() {
        return LOAN_LENGTH;
    }

    public List<Lender> getLenders() {
        return lenders;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    //    private static double round(double value) {
//        BigDecimal bd = new BigDecimal(Double.toString(value));
//        bd = bd.setScale(2, RoundingMode.HALF_UP);
//        System.out.println("value : " + bd.doubleValue());
//        return bd.doubleValue();
//        double scale = Math.pow(10, 2);
//        System.out.println("value : " + Math.round(value * scale) / scale);
//        return Math.round(value * scale) / scale;
//    }
}
