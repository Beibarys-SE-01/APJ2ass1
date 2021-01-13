package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ArticlesList extends Thread {
    private ArrayList<Article> articles;
    private int articleNum;
    private String fileName;
    ArrayList<Report> reports;

    public ArticlesList(String fileName) {
        reports = new ArrayList<Report>();
        articleNum = 0;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        int cLine = 0;
        articles = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {
            String line = br.readLine();
            while (line != null){
                cLine++;
                if (cLine == 1){
                    line = br.readLine();
                }
                String[] words = line.split(",");
                if (words.length != 6) {
                    line = br.readLine();
                    continue;
                }
                Article article = createArticle(Integer.parseInt(words[0]),words[1],words[2],words[3],words[4],words[5]);
                articles.add(article);
                articleNum++;
                line = br.readLine();
            }
            createReport();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    private Article createArticle(int id,String source_id,String source_name,String title,String content,String date) {
        if (date.contains(".")) {
            date = date.split("\\.")[0];
        } else if (date.contains("+")) {
            date = date.split("\\+")[0];
        } else {
            date = date.split("Z")[0];
        }
        date = date.replaceAll("T", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime published_at = LocalDateTime.parse(date, formatter);
        return new Article(id,source_id,source_name,title,content,published_at);
    }

    private void createReport() {
        boolean found;
        for (Article a: articles) {
            found = false;
            for (Report r: reports) {
                if (a.source_id.equals(r.id)) {
                    found = true;
                    r.numOfArticles++;
                    if (a.published_at.isBefore(r.published_from)) {
                        r.published_from = a.published_at;
                    }
                    if (a.published_at.isAfter(r.published_to)) {
                        r.published_to = a.published_at;
                    }
                    r.content_length += calculateLength(a.content);
                }
            }
            if (!found) {
                reports.add(new Report(a.source_name, a.source_id, calculateLength(a.content), a.published_at, a.published_at));
            }
        }
        for (Report r: reports) {
            r.setAvgContentLength();
        }
    }

    public int calculateLength(String content) {
        int len = 0;
        len += content.length();
        if (content.contains("chars]")) {
            String temp = content.split("\\[\\+")[1];
            temp = temp.split(" chars]")[0];
            len += Integer.parseInt(temp);
            len -= (9 + temp.length());
        }
        return len;
    }

    public synchronized void createFinalReport(ArrayList<Report> finalReport) {
        boolean found;
        for (Report r: reports) {
            found = false;
            for (Report fr: finalReport) {
                if (r.id.equals(fr.id)) {
                    found = true;
                    fr.numOfArticles += r.numOfArticles;
                    if (r.published_to.isAfter(fr.published_to)) {
                        fr.published_to = r.published_to;
                    }
                    if (r.published_from.isBefore(fr.published_from)) {
                        fr.published_from = r.published_from;
                    }
                    fr.content_length += r.content_length;
                }
            }
            if (!found) {
                finalReport.add(r);
            }
        }
        for (Report r: finalReport) {
            r.setAvgContentLength();
        }
    }
}
