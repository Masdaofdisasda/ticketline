package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
  /**
   * Uploads a picture.
   *
   * @param picture to upload
   * @return filename of the picture
   */
  String uploadPicture(MultipartFile picture) throws IOException;

}
