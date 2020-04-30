package music.server.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "userr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    private String role;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_song", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> likedSong = new ArrayList<>();

    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
        this.role = "user";
    }

    public User(@NotNull String username, @NotNull String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean likeSong(Song song) {
        for (int i = 0; i < likedSong.size(); i++) {
            if (likedSong.get(i).getId() == song.getId())
                throw new RuntimeException("Song is already liked");
        }
        return likedSong.add(song);
    }

    public boolean unlikeSong(Song song) {
        return likedSong.remove(song);
    }
}