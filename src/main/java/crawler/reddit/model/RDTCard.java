package crawler.reddit.model;

import crawler.BaseCard;

// Reddit
public class RDTCard extends BaseCard {

    public RDTCard(String title, String mainTopic, String url, String author, String articleLink,
                   String selfText, Integer ups, Integer num_comments) {
        super(title, mainTopic, url, author, articleLink, selfText, ups, num_comments);
    }
}
