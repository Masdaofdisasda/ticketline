package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsOverviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PageDtoResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UploadResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final NewsService newsService;
  private final UserService userService;
  private final NewsMapper newsMapper;

  @Autowired
  public NewsEndpoint(NewsService newsService, NewsMapper newsMapper, UserService userService) {
    this.newsService = newsService;
    this.userService = userService;
    this.newsMapper = newsMapper;
  }

  @Secured("ROLE_USER")
  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of news")
  public PageDtoResponse<NewsOverviewDto> findAllPaginated(PageDto pageDto) {
    LOGGER.info("GET /api/v1/news");
    Page<News> page = newsService.findAllPaginated(pageDto);
    return buildResponseDto(pageDto.pageIndex(), pageDto.pageSize(), page.getTotalElements(), page.getTotalPages(),
      newsMapper.newsToNewsOverviewDto(page.getContent()));
  }

  @Secured("ROLE_USER")
  @GetMapping(value = "/unread")
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of news the current user hasnt seen yet")
  public PageDtoResponse<NewsOverviewDto> findAllUnreadPaginated(PageDto pageDto) {
    LOGGER.info("GET /api/v1/news/unread");
    Long userId = userService.getUser().getId();
    Page<News> page = newsService.findAllUnreadPaginated(userId, pageDto);
    return buildResponseDto(pageDto.pageIndex(), pageDto.pageSize(), page.getTotalElements(), page.getTotalPages(),
      newsMapper.newsToNewsOverviewDto(page.getContent()));
  }

  @Secured("ROLE_USER")
  @GetMapping(value = "/{id}")
  @Operation(summary = "Get detailed information about a specific message", security = @SecurityRequirement(name = "apiKey"))
  public DetailedNewsDto find(@PathVariable Long id) {
    LOGGER.info("GET /api/v1/news/{}", id);
    News news = newsService.findOne(id);
    DetailedNewsDto detailedNewsDto = newsMapper.newsToDetailedNewsDto(news);
    newsService.markAsSeen(userService.getUser().getId(), news);
    return detailedNewsDto;
  }

  @Secured("ROLE_ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @Operation(summary = "Publish a new news entry", security = @SecurityRequirement(name = "apiKey"))
  public DetailedNewsDto create(@Valid @RequestBody NewsCreationDto newsDto) {
    LOGGER.info("POST /api/v1/news body: {}", newsDto);
    return newsMapper.newsToDetailedNewsDto(
      newsService.publishMessage(newsMapper.newsCreationDtoToNews(newsDto)));
  }

  @Secured("ROLE_ADMIN")
  @ResponseBody
  @PostMapping(value = "/picture")
  @Operation(summary = "Upload a Picture to be used with news", security = @SecurityRequirement(name = "apiKey"))
  public UploadResponseDto uploadPicture(@RequestParam MultipartFile imageFile) throws ValidationException {
    LOGGER.info("POST /api/v1/news/picture body: {}", imageFile);
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

  @PermitAll
  @ResponseBody
  @GetMapping(value = "/picture")
  @Operation(summary = "Upload a Picture to be used with news", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<InputStreamResource> downloadPicture(@RequestParam String path) throws IOException {
    LOGGER.info("GET /api/v1/news/picture body: {}", path);
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(path);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(in));
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

  private <T extends NewsOverviewDto> PageDtoResponse<T> buildResponseDto(int pageIndex, int pageSize, long hits, int pagesTotal, List<T> data) {
    return new PageDtoResponse<T>(pageIndex, pageSize, hits, pagesTotal, data);
  }
}
