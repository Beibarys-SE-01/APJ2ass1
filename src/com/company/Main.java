package com.company;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        File[] csv = Objects.requireNonNull(new File("D:\\ASUS\\AITU\\lessons\\APJ2\\simplified_input_csv").listFiles());
        ArrayList<ArticlesList> csvThreads = new ArrayList<>();
        for (File f: csv) {
            csvThreads.add(new ArticlesList(f.getPath()));
        }
        for (ArticlesList t: csvThreads) {
            t.start();
        }
        for (ArticlesList t: csvThreads) {
            t.join();
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (ArticlesList al: csvThreads) {
            al.createFinalReport(reports);
        }
        try {
            boolean flag = false;
            File report = new File("D:\\ASUS\\AITU\\lessons\\APJ2\\report.csv");
            if (report.createNewFile()) {
                flag = true;
            }
            PrintWriter pw = new PrintWriter(report);
            if (flag) {
                pw.write("name,ID,published_from,published_to,avg_content_length\n");
            }
            for (Report r: reports) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.name).append(",").append(r.id).append(",").append(r.published_from).append(",")
                .append(r.published_to).append(",").append(r.avg_content_length).append("\n");
                pw.write(sb.toString());
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
