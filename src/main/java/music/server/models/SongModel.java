package music.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongModel {
    private int id;
    private String name;
    private int length;
    private String songImage;
    private String artists;
    private String lyric;
    private String link;
    private String album;
    private boolean isLike;
}