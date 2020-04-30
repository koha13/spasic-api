package music.server.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 1000)
    private String name;

    private int length;

    @Column(length = 200)
    private String songImage;

    @Column(length = 100)
    private String artists;

    @Column(length = 5000)
    private String lyric;

    @Column(length = 200)
    private String fileName;

    @Column(length = 1000)
    private String link;

    private String album;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "song_pl", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private List<Playlist> playlists = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_song", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersLiked = new ArrayList<>();

    public Song(@NotNull String name, int length, String songImage, String artists, String lyric, String fileName,
            String album, String link) {
        this.name = name;
        this.length = length;
        this.songImage = songImage;
        this.artists = artists;
        this.lyric = lyric;
        this.fileName = fileName;
        this.album = album;
        this.link = link;
    }

}