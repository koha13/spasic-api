package music.server.services;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import music.server.models.SongUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import music.server.entities.Song;
import music.server.entities.User;
import music.server.models.SongModel;
import music.server.repositories.SongRepository;
import music.server.utils.Entity2DTO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import music.server.services.SongService;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    UserService userService;

    @Value("${source}")
    private String source;

    @Value("${relative_path_to_song_folder}")
    private String pathToSongFolder;

    public Song getSongById(int idSong) {
        return songRepository.findById(idSong).get();
    }

    public SongModel getSongInfo(Integer id) {
        User userRepo = userService.getUserRepo();
        Song song = songRepository.findById(id).get();
        return Entity2DTO.toSongModelWithLyric(song, userRepo.getLikedSong());
    }

    public List<SongModel> getAllSongs() {
        List<Song> songs = songRepository.findAll();

        User userRepo = userService.getUserRepo();
        List<Song> likedSong = userRepo.getLikedSong();
        return Entity2DTO.toSongModelList(songs, likedSong);
    }

    public List<SongModel> getSongsByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Song> songs = songRepository.findAll(pageable);
        User userRepo = userService.getUserRepo();
        List<Song> likedSong = userRepo.getLikedSong();
        return Entity2DTO.toSongModelList(songs.getContent(), likedSong);
    }

    public ResponseEntity<Resource> serveSong(HttpServletResponse response, String fileName) {
        try {
            String rootPath = pathToSongFolder + fileName;
            File file = new File(rootPath);
            HttpHeaders header = new HttpHeaders();
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            header.add("Accept-Ranges", "bytes");
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok().headers(header).contentLength(file.length())
                    .contentType(MediaType.parseMediaType("audio/mpeg")).body(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public int scanAllSong() {
        songRepository.deleteAll();
        String root = pathToSongFolder;
        File folder = new File(root);
        File[] listOfFiles = folder.listFiles();
        int count = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    scanSongAndAddToDB(file);
                    count++;
                } catch (Exception e) {
                    continue;
                }

            }
        }
        return count;
    }

    public SongModel uploadSong(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!checkSongFile(fileName.substring(fileName.length() - 3)))
            throw new RuntimeException("Not audio file.");
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(pathToSongFolder + fileName).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            File musicFile = targetLocation.toFile();
            Song song = scanSongAndAddToDB(musicFile);
            return Entity2DTO.toSongModel2(song, false);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not store file " + fileName + ". Please try again!. Error: " + ex.getMessage());
        }
    }

    private boolean checkSongFile(String fileExtension) {
        String[] allowedExtension = new String[] { "mp3", "m4a", "wav", "aac", "ogg", "wma", "flac", "alac", "pcm",
                "aiff" };
        List<String> allowedExtensionList = Arrays.asList(allowedExtension);
        return allowedExtensionList.contains(fileExtension.toString());
    }

    private Song scanSongAndAddToDB(File file) throws Exception {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        if (!checkSongFile(fileExtension))
            return null;
        String name = file.getName();
        String artists = "Unknown";
        int length = 0;
        String lyric = null;
        String imageSong = source + "/image/" + "default.png";
        String album = "Unknown";
        AudioFile audioFile = AudioFileIO.read(file);
        Tag audioTag = audioFile.getTag();
        String nameValue = audioTag.getFirst(FieldKey.TITLE);
        String artistsValue = audioTag.getFirst(FieldKey.ARTIST);
        int lengthValue = audioFile.getAudioHeader().getTrackLength() + 1;
        String lyricValue = audioTag.getFirst(FieldKey.LYRICS);
        String albumValue = audioTag.getFirst(FieldKey.ALBUM);
        Artwork artwork = audioTag.getFirstArtwork();
        if (!nameValue.isEmpty()) {
            name = nameValue;
        }
        if (!artistsValue.isEmpty()) {
            artists = artistsValue;
        }
        if (lengthValue > 1) {
            length = lengthValue;
        }
        if (!lyricValue.isEmpty()) {
            lyric = lyricValue;
        }
        if (!albumValue.isEmpty()) {
            album = albumValue;
        }
        if (artwork != null) {
            String photoName = String.valueOf(name.hashCode()) + new SimpleDateFormat("dd-MM-yyyy").format(new Date())
                    + ".png";
            String photoURI = System.getProperty("user.dir") + "/photos/" + photoName;
            FileUtils.writeByteArrayToFile(new File(photoURI), artwork.getBinaryData());
            imageSong = source + "/image/" + photoName;
        }
        String link = source + "/song/" + URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");
        Song songSave = new Song(name, length, imageSong, artists, lyric, file.getName(), album, link);
        songRepository.save(songSave);
        return songSave;
    }

    public boolean updateSongInfo(int id, SongUpdateRequest req) throws Exception {
        Song song = songRepository.findById(id).get();
        if (song != null) {
            if (req.getName() != null) {
                song.setName(req.getName());
            }
            if (req.getArtists() != null) {
                song.setArtists(req.getArtists());
            }
            if (req.getLyric() != null) {
                song.setLyric(req.getLyric());
            }
            if (req.getLength() > 0) {
                song.setLength(req.getLength());
            }
            if (req.getAlbum() != null) {
                song.setAlbum(req.getAlbum());
            }
            songRepository.save(song);
            return true;
        }
        return false;
    }

    public void deleteSong(int id) throws Exception {
        Song song = songRepository.findById(id).get();
        if (song != null) {
            String rootPath = System.getProperty("user.dir") + "/songs/";
            File file = new File(rootPath + song.getFileName());
            if (!file.delete())
                throw new Exception("Delete failed");
            songRepository.delete(song);
        }
    }

    public List<SongModel> searchSongByAlbum(String album) {
        if (album != null) {
            List<Song> songs = songRepository.findByAlbum(album);
            User userRepo = userService.getUserRepo();
            List<Song> likedSong = userRepo.getLikedSong();
            return Entity2DTO.toSongModelList(songs, likedSong);
        }
        return null;
    }
}