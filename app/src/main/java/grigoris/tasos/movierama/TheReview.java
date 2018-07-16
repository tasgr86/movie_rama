package grigoris.tasos.movierama;

import android.os.Parcel;
import android.os.Parcelable;

public class TheReview implements Parcelable{

    private String id, content, url, author;

    public TheReview(String id, String content, String url, String author){

        this.id = id;
        this.content = content;
        this.url = url;
        this.author = author;

    }

    protected TheReview(Parcel in) {
        id = in.readString();
        content = in.readString();
        url = in.readString();
        author = in.readString();
    }

    public static final Creator<TheReview> CREATOR = new Creator<TheReview>() {
        @Override
        public TheReview createFromParcel(Parcel in) {
            return new TheReview(in);
        }

        @Override
        public TheReview[] newArray(int size) {
            return new TheReview[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(author);
    }
}
