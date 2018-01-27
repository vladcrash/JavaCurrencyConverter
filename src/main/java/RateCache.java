import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RateCache {
    private Set<ApiResponse> rateItems = new HashSet<ApiResponse>();
    private String cacheFolder;
    private File file;

    public double getRateCurrency(String from, String to) {
        makeCacheGreatAgain();

        ApiResponse response = searchItem(new ApiResponse(from, to));
        if (response == null) {
            URIBuilder builder = new URIBuilder()
                    .setScheme("http")
                    .setHost("api.fixer.io")
                    .setPath("latest")
                    .addParameter("base", from)
                    .addParameter("symbols", to);
            response = new FixerFetcher().parseItem(builder.toString());
            writeToCache(response);
        }
        return response.getRates().getRate();
    }

    private void writeToCache(ApiResponse response) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            String toCache = buildString(response);
            bufferWritter.write(toCache);
            bufferWritter.newLine();
            bufferWritter.close();
        } catch (IOException e) {
            System.err.println("IOException");
        }
    }

    private void readFromCache() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                String[] oneLine = line.split(" ");
                ApiResponse item = new ApiResponse();
                item.setBase(oneLine[0]);
                item.getRates().setName(oneLine[1]);
                item.getRates().setRate(Double.valueOf(oneLine[2]));
                rateItems.add(item);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
        } catch (IOException e) {
            System.err.println("IOException");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("IOException");
            } catch (NullPointerException e) {
                System.err.println("NullPointerException");
            }
        }
    }

    private String buildString(ApiResponse response) {
        StringBuilder sb = new StringBuilder()
                .append(response.getBase())
                .append(" ")
                .append(response.getRates().getName())
                .append(" ")
                .append(response.getRates().getRate());
        return sb.toString();
    }

    private ApiResponse searchItem(ApiResponse item) {
        readFromCache();
        for (ApiResponse rateItem : rateItems) {
            if (rateItem.equals(item)) {
                return rateItem;
            }
        }
        return null;
    }

    private boolean isEmpty() {
        cacheFolder = new File("").getAbsolutePath();
        cacheFolder += "\\src\\main\\resources";
        File[] files = new File(cacheFolder).listFiles();
        return files.length == 0;
    }

    private String getTodayFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now) + ".txt";
    }

    private void createFile() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void makeCacheGreatAgain() {
        String todayFileName = getTodayFileName();
        if (isEmpty()) {
            file = new File(cacheFolder + "\\" + todayFileName);
            createFile();
        } else {
            File[] files = new File(cacheFolder).listFiles();
            String existingFileName = files[0].getName();
            if (existingFileName.equals(todayFileName)){
                file = files[0];
            } else {
                files[0].delete();
                file = new File(cacheFolder + "\\" + todayFileName);
                createFile();
            }
        }
    }
}
