package io.github.mufasa1976.meetup.springboottest.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "personal_events")
@Data
@NoArgsConstructor // used by Hibernate
@AllArgsConstructor(access = PRIVATE) // used by @Builder
@EntityListeners(AuditingEntityListener.class)
@Builder
public class PersonalEventEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NotNull
  @Column(unique = true, updatable = false)
  @Builder.Default
  private UUID reference = UUID.randomUUID();

  @NotNull
  @Column(name = "from_timestamp")
  private OffsetDateTime from;

  @NotNull
  @Column(name = "to_timestamp")
  private OffsetDateTime to;

  @NotNull
  @Size(max = 255)
  private String header;
  @Lob
  private String body;

  @LastModifiedBy
  private String lastModifiedBy;
  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;
}
