package crawler.hackernews.model;

import crawler.BaseCard;

public class HNCard extends BaseCard {

    public HNCard(String title, String mainTopic, String url, String author, String articleLink,
                  String selfText, Integer upvotes, Integer numberOfComments) {
        super(title, mainTopic, url, author, articleLink, selfText, upvotes, numberOfComments);
    }
}
