package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Security is a cross-cutting concern, however for the sake of simplicity it is tested against the message endpoint
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest implements TestData {
/*
  private static final List<Class<?>> mappingAnnotations = Lists.list(
    RequestMapping.class,
    GetMapping.class,
    PostMapping.class,
    PutMapping.class,
    PatchMapping.class,
    DeleteMapping.class
  );

  private static final List<Class<?>> securityAnnotations = Lists.list(
    Secured.class,
    PreAuthorize.class,
    RolesAllowed.class,
    PermitAll.class,
    DenyAll.class,
    DeclareRoles.class
  );

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private NewsMapper newsMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private List<Object> components;

  private News news = News.builder()
    .title(TEST_NEWS_TITLE)
    .summary(TEST_NEWS_SUMMARY)
    .text(TEST_NEWS_TEXT)
    .publishedAt(TEST_NEWS_PUBLISHED_AT)
    .build();

  @BeforeEach
  public void beforeEach() {
    newsRepository.deleteAll();
    news = News.builder()
      .title(TEST_NEWS_TITLE)
      .summary(TEST_NEWS_SUMMARY)
      .text(TEST_NEWS_TEXT)
      .publishedAt(TEST_NEWS_PUBLISHED_AT)
      .build();
  }

  /**
   * This ensures every Rest Method is secured with Method Security.
   * It is very easy to forget securing one method causing a security vulnerability.
   * Feel free to remove / disable / adapt if you do not use Method Security (e.g. if you prefer Web Security to define who may perform which actions) or want to use Method Security on the service layer.
   */
  /*
  @Test
  public void ensureSecurityAnnotationPresentForEveryEndpoint() {
    List<ImmutablePair<Class<?>, Method>> notSecured = components.stream()
      .map(AopUtils::getTargetClass) // beans may be proxies, get the target class instead
      .filter(clazz -> clazz.getCanonicalName() != null && clazz.getCanonicalName().startsWith(BackendApplication.class.getPackageName())) // limit to our package
      .filter(clazz -> clazz.getAnnotation(RestController.class) != null) // limit to classes annotated with @RestController
      .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(method -> new ImmutablePair<Class<?>, Method>(clazz, method))) // get all class -> method pairs
      .filter(pair -> Arrays.stream(pair.getRight().getAnnotations()).anyMatch(annotation -> mappingAnnotations.contains(annotation.annotationType()))) // keep only the pairs where the method has a "mapping annotation"
      .filter(pair -> Arrays.stream(pair.getRight().getAnnotations()).noneMatch(annotation -> securityAnnotations.contains(annotation.annotationType()))).toList();

    assertThat(notSecured.size())
      .as("Most rest methods should be secured. If one is really intended for public use, explicitly state that with @PermitAll. "
        + "The following are missing: \n" + notSecured.stream().map(pair -> "Class: " + pair.getLeft() + " Method: " + pair.getRight()).reduce("", (a, b) -> a + "\n" + b))
      .isZero();

  }

  @Test
  public void givenUserLoggedIn_whenFindAll_then200() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");
    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        .params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertAll(
      () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
      () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
    );
  }

  @Test
  public void givenNoOneLoggedIn_whenFindAll_then401() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");
    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI).params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void givenAdminLoggedIn_whenPost_then201() throws Exception {
    NewsCreationDto newsCreationDto = newsMapper.newsToNewsCreationDto(news);
    String body = objectMapper.writeValueAsString(newsCreationDto);

    MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
  }

  @Test
  public void givenNoOneLoggedIn_whenPost_then403() throws Exception {
    NewsCreationDto newsCreationDto = newsMapper.newsToNewsCreationDto(news);
    String body = objectMapper.writeValueAsString(newsCreationDto);

    MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void givenUserLoggedIn_whenPost_then403() throws Exception {
    NewsCreationDto newsCreationDto = newsMapper.newsToNewsCreationDto(news);
    String body = objectMapper.writeValueAsString(newsCreationDto);

    MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

   */

}
