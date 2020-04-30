package music.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistAddToEndPoint {
	private int id;
	private String name;
	private boolean isInPlaylist;
}