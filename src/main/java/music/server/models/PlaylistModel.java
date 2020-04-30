package music.server.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistModel {
    private int id;
    private String name;
    private String avatar;
    private List<SongModel> songs = new ArrayList<>();
}