package music.server.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 1000)
    private String name;

    @Column(length = 100)
    private String avatar;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "song_pl", joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> songs = new ArrayList<>();

    public Playlist(String name, String avatar, User user) {
        this.name = name;
        this.avatar = avatar;
        this.user = user;
    }

    public boolean addSong(Song song) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId() == song.getId())
                throw new RuntimeException("Song is already in this playlist");
        }
        if (songs.add(song))
            return true;
        return false;
    }

    public boolean deleteSong(Song song) {
        if (songs.remove(song))
            return true;
        throw new RuntimeException("Song isn't in this playlist");
    }

    public Playlist(int id, @NotNull String name, String avatar, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.songs = songs;
    }

}