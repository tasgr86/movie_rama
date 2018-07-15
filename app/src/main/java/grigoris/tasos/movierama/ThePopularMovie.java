package grigoris.tasos.movierama;

public class ThePopularMovie {

    private String poster, title, releaseDate, rating;
    private int movie_id, favorite_status;

    public ThePopularMovie(int movie_id, String poster, String title, String releaseDate, String rating){

        this.movie_id = movie_id;
        this.poster = poster;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;

    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getTitle() {
        return title;
    }

    public int getFavorite_status() {
        return favorite_status;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
