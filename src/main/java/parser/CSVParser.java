package parser;

import model.Lender;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParser {

    public static List<Lender> parseCSV(String path) throws IOException {
        File csv = new File(path);
        FileInputStream fis = new FileInputStream(csv);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        List<Lender> lenders = br.lines().skip(1).map(line -> {
            String[] args = line.split(",");
            return new Lender(args[0], Double.valueOf(args[1]), Integer.valueOf(args[2]));
        }).collect(Collectors.toList());
        br.close();
        return lenders;
    }
}
