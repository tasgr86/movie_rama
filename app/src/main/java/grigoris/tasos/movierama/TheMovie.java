package grigoris.tasos.movierama;

import android.os.Parcel;
import android.os.Parcelable;

public class TheMovie implements Parcelable{

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


    public TheMovie(int id, String poster, String title, String date, String rating){

        this.id = id;
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.rating = rating;

    }


    public TheMovie (int id, String title, String poster){

        this.id = id;
        this.title = title;
        this.poster = poster;

    }

    protected TheMovie(Parcel in) {
        id = in.readInt();
        genre = in.readString();
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        date = in.readString();
        rating = in.readString();
    }

    public static final Creator<TheMovie> CREATOR = new Creator<TheMovie>() {
        @Override
        public TheMovie createFromParcel(Parcel in) {
            return new TheMovie(in);
        }

        @Override
        public TheMovie[] newArray(int size) {
            return new TheMovie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(genre);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeString(rating);
    }
}
