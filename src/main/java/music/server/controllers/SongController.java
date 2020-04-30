package music.server.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import music.server.models.SongUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import music.server.models.SongModel;
import music.server.services.SongService;

@RestController
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("/song/{fileName}")
    public ResponseEntity<Resource> getSongById(HttpServletResponse response, @PathVariable String fileName) {
        return songService.serveSong(response, fileName);
    }

    @GetMapping("/songinfo/{id}")
    public SongModel getSongInfo(@PathVariable Integer id) {
        return songService.getSongInfo(id);
    }

    @GetMapping("/scan")
    public String scan() throws Exception {
        int count = songService.scanAllSong();
        return "Got " + count + " songs.";
    }

    @GetMapping("/songs")
    public List<SongModel> getAllSongs() {
        return songService.getAllSongs();
    }

    // Paging all songs
    @GetMapping("/allsongs")
    public List<SongModel> getSongsByPage(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {
        return songService.getSongsByPage(page, size);
    }

    @PostMapping("/song/upload")
    public SongModel uploadSong(@RequestParam("file") MultipartFile file) {
        return songService.uploadSong(file);
    }

    @PostMapping("/song/update/{id}")
    public String updateSongInfo(@PathVariable String id, @RequestBody SongUpdateRequest req) throws Exception {
        if (songService.updateSongInfo(Integer.parseInt(id), req))
            return "OK";
        throw new Exception("Can not update this song");
    }

    @PostMapping("/song/delete/{id}")
    public void deleteSong(@PathVariable String id) throws Exception {
        songService.deleteSong(Integer.parseInt(id));
    }

    @GetMapping("/song/searchbyalbum")
    public List<SongModel> searchSongByAlbum(@RequestParam String album) {
        return songService.searchSongByAlbum(album);
    }
}