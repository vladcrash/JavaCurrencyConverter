import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Rate {
    private String[] currencies = {"AUD", "GBP", "KRW", "SEK",
            "BGN", "HKD", "MXN", "SGD",
            "BRL", "HRK", "MYR", "THB",
            "CAD", "HUF", "NOK", "TRY",
            "CHF", "IDR", "NZD", "USD",
            "CNY", "ILS", "PHP", "ZAR",
            "CZK", "INR", "PLN", "EUR",
            "DKK", "JPY", "RON", "RUB"};

    private double currency;
    private String from;
    private String to;

    public void start() {
        inputData();
        getResult();
    }

    private void inputData()  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean isCorrect = false;

        do {
            try {
                System.out.println("Enter from currency: ");
                from = reader.readLine().toUpperCase();
                System.out.println("Enter to currency: ");
                to = reader.readLine().toUpperCase();
            } catch (IOException e) {
                System.err.println("IOException");
            }

            if (isCorrect()) {
                isCorrect = true;
            } else {
                System.out.println("No such currency found");
                System.out.println("Try again :)\n");
            }
        } while (!isCorrect);
    }

    private void getResult() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future future = service.submit(new Runnable() {
            public void run() {
                currency = new RateCache().getRateCurrency(from, to);
            }
        });

        while (!future.isDone()) {
            System.out.print(".");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException");
            }
        }

        System.out.format("%n%s => %s : %.3f%n", from, to, currency);
        service.shutdown();
    }

    private boolean isCorrect() {
        int count = 0;
        if (from.equals(to)) {
            return false;
        }

        for (String s : currencies) {
            if (s.equals(from) || s.equals(to)) {
                count++;
            }
        }

        return count == 2;
    }
}
