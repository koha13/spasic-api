package music.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongUpdateRequest {
    private String name;
    private String artists;
    private String lyric;
    private int length;
    private String album;
}
