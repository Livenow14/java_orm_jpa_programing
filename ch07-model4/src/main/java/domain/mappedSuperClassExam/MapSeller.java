package domain.mappedSuperClassExam;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class MapSeller {
    //ID 상속
    //NAME 상속
    private String shopName;
}
