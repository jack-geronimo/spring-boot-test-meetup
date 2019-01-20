CREATE TABLE personal_events (
  id               BIGINT                   NOT NULL PRIMARY KEY AUTO_INCREMENT
 ,reference        UUID                     NOT NULL
 ,from_timestamp   TIMESTAMP WITH TIME ZONE NOT NULL
 ,to_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL
 ,header           VARCHAR(255)             NOT NULL
 ,body             LONGTEXT
 ,last_modified_by VARCHAR(30)              NOT NULL
 ,last_modified_at TIMESTAMP WITH TIME ZONE NOT NULL
 ,UNIQUE (
    reference
 )
);