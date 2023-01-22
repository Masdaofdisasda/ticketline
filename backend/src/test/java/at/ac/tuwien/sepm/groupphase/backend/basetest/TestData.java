package at.ac.tuwien.sepm.groupphase.backend.basetest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

  Long ID = 1L;
  String TEST_NEWS_TITLE = "Title";
  String TEST_NEWS_SUMMARY = "Summary";
  String TEST_NEWS_TEXT = "TestMessageText";
  LocalDateTime TEST_NEWS_PUBLISHED_AT =
    LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

  String BASE_URI = "/api/v1";
  String NEWS_BASE_URI = BASE_URI + "/news";
  String USER_BASE_URI = BASE_URI + "/user";
  String EVENT_BASE_URI = BASE_URI + "/events";
  String ARTIST_BASE_URI = BASE_URI + "/artists";
  String AUTH_BASE_URI = BASE_URI + "/authentication";

  String VENUE_BASE_URI = BASE_URI + "/venue";

  String PERFORMANCE_BASE_URI = BASE_URI + "/performance";

  String ADMIN_USER = "admin@email.com";
  List<String> ADMIN_ROLES = List.of("ROLE_ADMIN", "ROLE_USER");
  String DEFAULT_USER = "user@email.com";
  List<String> USER_ROLES = new ArrayList<>() {
    {
      add("ROLE_USER");
    }
  };

}
