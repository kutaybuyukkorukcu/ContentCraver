package crawler.hackernews.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HNCrawler {

//    public List<String> getArticleValues(String articleLink) {
//
//        get
//
//        List<String> articleValueList= new ArrayList<String>();
//
//        Elements articleValues = doc.select("td.subtext");
//
//        for (Element author : articleValues) {
//            System.out.println(author.text());
//            articleValueList.add(author.text());
//        }
//
//        return articleValueList;
//    }
//
//    public List<String> getArticleLinks(String articleLink) throws IOException {
//
//        Document doc;
//
//        doc = Jsoup.connect(articleLink).get();
//
//        List<String> articleLinkList = new ArrayList<String>();
//
//        List<String> articleValueList= new ArrayList<String>();
//
//        Elements articleLinks = doc.select("td.title > a[href]");
//        Elements articleValues = doc.select("td.subtext");
//
//        for (Element link : articleLinks) {
//            articleLinkList.add(link.text());
//        }
//
//        for (Element author : articleValues) {
//            System.out.println(author.text());
//            articleValueList.add(author.text());
//        }
//
//        return authorsxd;
//    }
}
