package music.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import music.server.entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    public Playlist findByName(String name);

    public List<Playlist> findByUserId(int id);

    public Playlist findByIdAndUserId(int id, int userId);
}