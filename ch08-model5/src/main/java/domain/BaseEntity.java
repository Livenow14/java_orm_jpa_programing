package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private Date createdDate;
    private Date modifiedDate;

}
