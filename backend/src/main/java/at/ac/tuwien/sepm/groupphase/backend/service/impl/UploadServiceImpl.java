package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.UploadService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Service
public class UploadServiceImpl implements UploadService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  @Override
  public String uploadPicture(MultipartFile picture) throws IOException {
    String fileName = generateFileId();
    fileName += FilenameUtils.getExtension(picture.getOriginalFilename());
    picture.transferTo(new File("/news/images/" + fileName));
    return fileName;
  }

  private String generateFileId() {
    return Integer.toString((int) (Math.random() * 1000000));
  }
}
