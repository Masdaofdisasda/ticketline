package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsEndpointTest implements TestData {
/*
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private NewsMapper newsMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  private final News news = News.builder()
    .title(TEST_NEWS_TITLE)
    .summary(TEST_NEWS_SUMMARY)
    .text(TEST_NEWS_TEXT)
    .publishedAt(TEST_NEWS_PUBLISHED_AT)
    .build();

  @BeforeEach
  public void beforeEach() {
    newsRepository.deleteAll();
  }

  @Test
  public void givenNothing_whenFindAll_thenEmptyList() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");
    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)).params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PageDtoResponse<NewsOverviewDto> pageDtoResponse = objectMapper.readValue(response.getContentAsString(), PageDtoResponse.class);

    assertEquals(0, pageDtoResponse.getData().size());
  }

  @Test
  public void givenOneMessage_whenFindAll_thenListWithSizeOneAndMessageWithAllPropertiesExceptSummary()
    throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");
    newsRepository.save(news);

    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PageDtoResponse<NewsOverviewDto> pageDtoResponse = objectMapper.readValue(response.getContentAsString(), PageDtoResponse.class);

    assertEquals(1, pageDtoResponse.getData().size());
  }

  @Test
  public void givenOneMessage_whenFindById_thenMessageWithAllProperties() throws Exception {
    userRepository.deleteAll();
    newsRepository.save(news);
    UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
      .email(ADMIN_USER)
      .firstName("a")
      .lastName("m")
      .password("password").build();
    userService.register(userRegistrationDto);
    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI + "/{id}", news.getId())
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertAll(
      () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
      () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
    );

    DetailedNewsDto detailedNewsDto = objectMapper.readValue(response.getContentAsString(),
      DetailedNewsDto.class);

    assertEquals(news, newsMapper.detailedNewsDtoToNews(detailedNewsDto));
  }

  @Test
  public void givenOneMessage_whenFindByNonExistingId_then404() throws Exception {
    newsRepository.save(news);

    MvcResult mvcResult = this.mockMvc.perform(get(NEWS_BASE_URI + "/{id}", -1)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void givenEmptyPublishedAtField_whenSubmittingMessage_then400() throws Exception {
    news.setPublishedAt(null);
    NewsCreationDto newsCreationDto = newsMapper.newsToNewsCreationDto(news);
    String body = objectMapper.writeValueAsString(newsCreationDto);

    MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void givenValidMessage_whenSubmittingMessage_then201() throws Exception {
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
  public void givenNothing_whenPostInvalid_then400() throws Exception {
    news.setTitle(null);
    news.setSummary(null);
    news.setText(null);
    NewsCreationDto newsCreationDto = newsMapper.newsToNewsCreationDto(news);
    String body = objectMapper.writeValueAsString(newsCreationDto);

    MvcResult mvcResult = this.mockMvc.perform(post(NEWS_BASE_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertAll(
      () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
      () -> {
        //Reads the errors from the body
        String content = response.getContentAsString();
        content = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
        String[] errors = content.split(",");
        assertEquals(3, errors.length);
      }
    );
  }

 */
}
