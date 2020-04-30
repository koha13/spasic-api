package music.server.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import music.server.models.PlaylistModel;
import music.server.models.PlaylistAddToEndPoint;
import music.server.models.SongModel;
import music.server.services.PlaylistService;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService plService;

    @PostMapping("/add")
    public PlaylistModel addPlayList(@PathParam("name") String name) {
        return plService.addPL(name);
    }

    @GetMapping("")
    public List<PlaylistModel> getAllPlayList() {
        return plService.getAllPlayList();
    }

    @GetMapping("/{id}")
    public PlaylistModel getPlayListById(@PathVariable int id) {
        return plService.getPlayListById(id);
    }

    @GetMapping("/delete/{id}")
    public void deletePlayListById(@PathVariable int id) {
        plService.deletePlayListById(id);
    }

    @PostMapping("/{id}/song")
    public SongModel addSong(@PathVariable int id, @PathParam("idSong") String idSong) {
        return plService.addSong(id, Integer.parseInt(idSong));
    }

    @GetMapping("/checksong")
    public List<PlaylistAddToEndPoint> checkSong(@PathParam("idSong") String idSong) {
        return plService.checkSong(Integer.parseInt(idSong));
    }

    @GetMapping("/{id}/deletesong")
    public SongModel deleteSong(@PathVariable int id, @PathParam("idSong") String idSong) {
        return plService.deleteSong(id, Integer.parseInt(idSong));
    }
}