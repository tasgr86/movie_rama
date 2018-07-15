package grigoris.tasos.movierama;

public class TheMovie {

    private int id;;
    private String genre, title, poster, overview, date, rating;

    public TheMovie (int id, String title, String genre, String poster, String overview,
                     String date, String rating){

        this.id = id;
        this.title = title;
        this.genre = genre;
        this.poster = poster;
        this.overview = overview;
        this.date = date;
        this.rating = rating;

    }


    public TheMovie (int id, String title, String poster){

        this.id = id;
        this.title = title;
        this.poster = poster;

    }

    public int getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getOverview() {
        return overview;
    }

    public String getDate() {
        return date;
    }

    public String getRating() {
        return rating;
    }
}
