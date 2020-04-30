package music.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import music.server.entities.Song;

public interface SongRepository extends JpaRepository<Song, Integer> {
    public Song findByName(String name);

    public List<Song> findByAlbum(String album);

    public List<Song> findByArtists(String artists);
}