package grigoris.tasos.movierama;

public class TheReview {

    private String id, content, url, author;

    public TheReview(String id, String content, String url, String author){

        this.id = id;
        this.content = content;
        this.url = url;
        this.author = author;

    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
