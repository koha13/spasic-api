package music.server.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    @GetMapping("/image/{name}")
    public void getImage(HttpServletResponse response, @PathVariable String name) throws IOException {
        String rootPath = System.getProperty("user.dir") + "/photos/" + name;
        File file = new File(rootPath);
        InputStream myStream = new FileInputStream(file);

        response.setContentType("image/jpeg");
        response.addHeader("Cache-Control", "max-age=3600");
        response.setContentLengthLong(file.length());

        IOUtils.copy(myStream, response.getOutputStream());
        response.flushBuffer();
    }
}