alter table APPLICATION_USER
drop
CONSTRAINT if exists FK1YU9I3S0BXUI9307P7W344D46;

drop table if exists APPLICATION_USER_HAS_SEEN cascade;

drop table if exists BOOKING_CANCELED_TICKETS cascade;

drop table if exists NEWS_SEEN_BY cascade;

drop table if exists NEWS cascade;

drop table if exists PASSWORD_RESET_TOKEN cascade;

drop table if exists PERFORMANCE_ARTISTS cascade;

drop table if exists ARTIST cascade;

drop table if exists PRICING cascade;

drop table if exists SEATING_PLAN cascade;

drop table if exists TICKET cascade;

drop table if exists BOOKING cascade;

drop table if exists APPLICATION_USER cascade;

drop table if exists PERFORMANCE cascade;

drop table if exists EVENT cascade;

drop table if exists SEAT cascade;

drop table if exists SECTOR cascade;

drop table if exists PRICE_CATEGORY cascade;

drop table if exists ROOM cascade;

drop table if exists VENUE cascade;

create table PUBLIC.APPLICATION_USER
(
  ID                 BIGINT auto_increment primary key,
  ACCOUNT_NON_LOCKED BOOLEAN                NOT NULL DEFAULT TRUE,
  ADMIN              BOOLEAN                NOT NULL DEFAULT FALSE,
  EMAIL              CHARACTER VARYING(255) NOT NULL
    CONSTRAINT UK_CB61P28HANADV7K0NX1EC0N5L UNIQUE,
  FAILED_ATTEMPT     INTEGER                NOT NULL DEFAULT 0,
  FIRST_NAME         CHARACTER VARYING(255) NOT NULL,
  LAST_NAME          CHARACTER VARYING(255) NOT NULL,
  LOCK_TIME          TIMESTAMP,
  PASSWORD           CHARACTER VARYING(100) NOT NULL,
  PASSWORD_TOKEN_ID  BIGINT
);

create table PUBLIC.ARTIST
(
  ID   BIGINT auto_increment PRIMARY KEY,
  NAME CHARACTER VARYING(255)
);

create table PUBLIC.BOOKING
(
  ID           BIGINT auto_increment PRIMARY KEY,
  BOOKING_TYPE INTEGER   NOT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  USER_ID      BIGINT,
  CONSTRAINT FK2RUQO140I7PI1IOQQBVRB3B49
    FOREIGN KEY (USER_ID) REFERENCES PUBLIC.APPLICATION_USER ON DELETE CASCADE
);

create table PUBLIC.EVENT
(
  ID         BIGINT auto_increment PRIMARY KEY,
  CATEGORY   CHARACTER VARYING(100) NOT NULL,
  END_DATE   TIMESTAMP,
  NAME       CHARACTER VARYING(100) NOT NULL,
  START_DATE TIMESTAMP
);

create table PUBLIC.NEWS
(
  ID           BIGINT auto_increment PRIMARY KEY,
  FILE_NAME    CHARACTER VARYING(100),
  PUBLISHED_AT TIMESTAMP                NOT NULL,
  SUMMARY      CHARACTER VARYING(500)   NOT NULL,
  TEXT         CHARACTER VARYING(10000) NOT NULL,
  TITLE        CHARACTER VARYING(100)   NOT NULL
);

create table PUBLIC.APPLICATION_USER_HAS_SEEN
(
  APPLICATION_USER_ID BIGINT NOT NULL,
  HAS_SEEN_ID         BIGINT NOT NULL,
  primary key (APPLICATION_USER_ID, HAS_SEEN_ID),
  CONSTRAINT FKT35JSROWHHJT0LC5FHUWPF1GS
    FOREIGN KEY (APPLICATION_USER_ID) REFERENCES PUBLIC.APPLICATION_USER ON DELETE CASCADE,
  CONSTRAINT FKT9RX6UQ1MY44244PBIWTN4SRJ
    FOREIGN KEY (HAS_SEEN_ID) REFERENCES PUBLIC.NEWS ON DELETE CASCADE
);

create table PUBLIC.NEWS_SEEN_BY
(
  NEWS_ID    BIGINT NOT NULL,
  SEEN_BY_ID BIGINT NOT NULL,
  primary key (NEWS_ID, SEEN_BY_ID),
  CONSTRAINT FK1G7626H3PN3MKUC3HGVG2JXKJ
    FOREIGN KEY (SEEN_BY_ID) REFERENCES PUBLIC.APPLICATION_USER ON DELETE CASCADE,
  CONSTRAINT FK9UWQHORB3W5IUTSLPBUCOI0A5
    FOREIGN KEY (NEWS_ID) REFERENCES PUBLIC.NEWS ON DELETE CASCADE
);

create table PUBLIC.PASSWORD_RESET_TOKEN
(
  ID          BIGINT                 NOT NULL PRIMARY KEY,
  EXPIRY_DATE TIMESTAMP              NOT NULL,
  TOKEN       CHARACTER VARYING(255) NOT NULL
    CONSTRAINT UK_G0GUO4K8KRGPWUAGOS61OC06J
      UNIQUE,
  USER_ID     BIGINT,
  CONSTRAINT FKCUERW7H3F8BQRBI0R8I0SGKBU
    FOREIGN KEY (USER_ID) REFERENCES PUBLIC.APPLICATION_USER ON DELETE CASCADE
);

alter table PUBLIC.APPLICATION_USER
  add CONSTRAINT FK1YU9I3S0BXUI9307P7W344D46
    FOREIGN KEY (PASSWORD_TOKEN_ID) REFERENCES PUBLIC.PASSWORD_RESET_TOKEN ON DELETE SET NULL;

create table PUBLIC.PRICE_CATEGORY
(
  ID    BIGINT auto_increment PRIMARY KEY,
  COLOR CHARACTER VARYING(8),
  NAME  CHARACTER VARYING(255)
);

create table PUBLIC.VENUE
(
  ID           BIGINT auto_increment PRIMARY KEY,
  CITY         CHARACTER VARYING(255),
  COUNTRY      CHARACTER VARYING(255),
  HOUSE_NUMBER CHARACTER VARYING(255),
  NAME         CHARACTER VARYING(255),
  STREET       CHARACTER VARYING(255),
  ZIP_CODE     CHARACTER VARYING(255)
);

create table PUBLIC.ROOM
(
  ID          BIGINT auto_increment PRIMARY KEY,
  COLUMN_SIZE INTEGER,
  NAME        CHARACTER VARYING(255),
  ROW_SIZE    INTEGER,
  VENUE_ID    BIGINT,
  CONSTRAINT FKPEJ81GQ1P63TPV7EDK69I68FG
    FOREIGN KEY (VENUE_ID) REFERENCES PUBLIC.VENUE ON DELETE CASCADE,
  check (("COLUMN_SIZE" >= 0)
    AND ("COLUMN_SIZE" <= 1024)),
  check (("ROW_SIZE" >= 0)
    AND ("ROW_SIZE" <= 1024))
);

create table PUBLIC.PERFORMANCE
(
  ID         BIGINT auto_increment PRIMARY KEY,
  END_DATE   TIMESTAMP,
  START_DATE TIMESTAMP,
  EVENT_ID   BIGINT,
  ROOM_ID    BIGINT,
  CONSTRAINT FKGUS6NU3SL41CKXCHXPR49YXYV
    FOREIGN KEY (EVENT_ID) REFERENCES PUBLIC.EVENT ON DELETE CASCADE,
  CONSTRAINT FKLOV84J4H1PSU8PVCK06KUSXV8
    FOREIGN KEY (ROOM_ID) REFERENCES PUBLIC.ROOM ON DELETE SET NULL
);

create table PUBLIC.PERFORMANCE_ARTISTS
(
  PERFORMANCE_ID BIGINT NOT NULL,
  ARTIST_ID      BIGINT NOT NULL,
  CONSTRAINT FKJ1KFL5XSLBU34XM6OJ7FV8WE0
    FOREIGN KEY (PERFORMANCE_ID) REFERENCES PUBLIC.PERFORMANCE,
  CONSTRAINT FKK2ICJ71M8YG8HGSM1MESK0WP2
    FOREIGN KEY (ARTIST_ID) REFERENCES PUBLIC.ARTIST
);

create table PUBLIC.PRICING
(
  ID               BIGINT auto_increment PRIMARY KEY,
  PRICING          NUMERIC(19, 2),
  PERFORMANCE_ID   BIGINT,
  PRICECATEGORY_ID BIGINT,
  CONSTRAINT FK4ULBY9GC7HLFC50Y6S7GT8D1M
    FOREIGN KEY (PERFORMANCE_ID) REFERENCES PUBLIC.PERFORMANCE,
  CONSTRAINT FK7RU3OHF8574DUC3UN0RWNBAAY
    FOREIGN KEY (PRICECATEGORY_ID) REFERENCES PUBLIC.PRICE_CATEGORY
);

create table PUBLIC.SEATING_PLAN
(
  ID      BIGINT auto_increment PRIMARY KEY,
  COLUMNS INTEGER NOT NULL,
  ROWS    INTEGER NOT NULL,
  ROOM_ID BIGINT,
  CONSTRAINT FKQB99KU64RC55X0VWW0GCOMUXT
    FOREIGN KEY (ROOM_ID) REFERENCES PUBLIC.ROOM ON DELETE CASCADE
);

create table PUBLIC.SECTOR
(
  ID                BIGINT auto_increment PRIMARY KEY,
  NAME              CHARACTER VARYING(255) NOT NULL,
  PRICE_CATEGORY_ID BIGINT,
  ROOM_ID           BIGINT,
  CONSTRAINT FK4J4P0CX7ET18HIA8NYQ562PBS
    FOREIGN KEY (ROOM_ID) REFERENCES PUBLIC.ROOM ON DELETE CASCADE,
  CONSTRAINT FKG3GXPB2W1JPAFQA6AO43BHOUE
    FOREIGN KEY (PRICE_CATEGORY_ID) REFERENCES PUBLIC.PRICE_CATEGORY ON DELETE SET NULL
);

create table PUBLIC.SEAT
(
  ID         BIGINT auto_increment PRIMARY KEY,
  COL_NAME   CHARACTER VARYING(16),
  COL_NUMBER INTEGER NOT NULL,
  ROW_NAME   CHARACTER VARYING(16),
  ROW_NUMBER INTEGER NOT NULL,
  SECTOR_ID  BIGINT,
  CONSTRAINT FKTL6F3LSBPK36U074R6C9PO8PM
    FOREIGN KEY (SECTOR_ID) REFERENCES PUBLIC.SECTOR ON DELETE SET NULL
);

create table PUBLIC.TICKET
(
  ID              BIGINT auto_increment PRIMARY KEY,
  PRICE           NUMERIC(19, 2),
  USED            BOOLEAN NOT NULL DEFAULT FALSE,
  VALIDATION_HASH BINARY VARYING(255),
  BOOKING_ID      BIGINT,
  PERFORMANCE_ID  BIGINT,
  SEAT_ID         BIGINT,
  CONSTRAINT FK5FACSURR5FEX38ATFR4N0M7VH
    FOREIGN KEY (PERFORMANCE_ID) REFERENCES PUBLIC.PERFORMANCE ON DELETE CASCADE,
  CONSTRAINT FKQAHAO9A85DRT47IKJP0B8SYVH
    FOREIGN KEY (SEAT_ID) REFERENCES PUBLIC.SEAT ON DELETE CASCADE,
  CONSTRAINT FKRG7X158T96NUCWSLHQ2BAD6QM
    FOREIGN KEY (BOOKING_ID) REFERENCES PUBLIC.BOOKING ON DELETE SET NULL
);

create table PUBLIC.BOOKING_CANCELED_TICKETS
(
  CANCELED_BOOKINGS_ID BIGINT NOT NULL,
  CANCELED_TICKETS_ID  BIGINT NOT NULL,
  primary key (CANCELED_BOOKINGS_ID, CANCELED_TICKETS_ID),
  CONSTRAINT FK3P41SCM60KLA9P23CS6SMLKK9
    FOREIGN KEY (CANCELED_TICKETS_ID) REFERENCES PUBLIC.TICKET ON DELETE SET NULL,
  CONSTRAINT FK5HF0DDDOCVSMXGFISYRVUFYK1
    FOREIGN KEY (CANCELED_BOOKINGS_ID) REFERENCES PUBLIC.BOOKING ON DELETE SET NULL
);