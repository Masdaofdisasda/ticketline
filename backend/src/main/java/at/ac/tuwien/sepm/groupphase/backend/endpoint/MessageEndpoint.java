package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MessageCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UploadResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/messages")
public class MessageEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @Autowired
  public MessageEndpoint(MessageService messageService, MessageMapper messageMapper) {
    this.messageService = messageService;
    this.messageMapper = messageMapper;
  }

  @Secured("ROLE_USER")
  @GetMapping
  @Operation(summary = "Get list of messages without details", security = @SecurityRequirement(name = "apiKey"))
  public List<SimpleMessageDto> findAll() {
    LOGGER.info("GET /api/v1/messages");
    return messageMapper.messageToSimpleMessageDto(messageService.findAll());
  }

  @Secured("ROLE_USER")
  @GetMapping(value = "/{id}")
  @Operation(summary = "Get detailed information about a specific message", security = @SecurityRequirement(name = "apiKey"))
  public DetailedMessageDto find(@PathVariable Long id) {
    LOGGER.info("GET /api/v1/messages/{}", id);
    return messageMapper.messageToDetailedMessageDto(messageService.findOne(id));
  }

  @Secured("ROLE_ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @Operation(summary = "Publish a new message", security = @SecurityRequirement(name = "apiKey"))
  public DetailedMessageDto create(@Valid @RequestBody MessageCreationDto messageDto) {
    LOGGER.info("POST /api/v1/messages body: {}", messageDto);
    return messageMapper.messageToDetailedMessageDto(
      messageService.publishMessage(messageMapper.messageCreationDtoToMessage(messageDto)));
  }

  @Secured("ROLE_ADMIN")
  @ResponseBody
  @PostMapping(value = "/picture")
  @Operation(summary = "Upload a Picture to be used with news", security = @SecurityRequirement(name = "apiKey"))
  public UploadResponseDto uploadPicture(@RequestParam MultipartFile imageFile) throws ValidationException {
    LOGGER.info("POST /api/v1/messages/picture body: {}", imageFile);
    String generatedFilename = generateFilename(imageFile.getOriginalFilename());
    ClassLoader classLoader = getClass().getClassLoader();
    if (imageFile.getContentType() != null && imageFile.getContentType().startsWith("image/")) {
      try {
        imageFile.transferTo(new File(classLoader.getResource(".").getFile() + "/" + generatedFilename));
      } catch (IOException | NullPointerException e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new ValidationException("While submitting an image Errors have occured", List.of("The uploaded file does not contain an image"));
    }

    return UploadResponseDto.builder()
      .originalFilename(imageFile.getOriginalFilename())
      .generatedFilename(generatedFilename)
      .build();
  }

  private String generateFilename(String originalFilename) {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(".").getFile() + "/" + originalFilename);
    if (file.exists() && !file.isDirectory()) {
      return generateFilename((int) (Math.random() * 10) + originalFilename);
    } else {
      return originalFilename;
    }
  }
}
