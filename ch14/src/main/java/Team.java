import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @OrderBy
 * 데이터베이스의 ORDER BY절을 상요해서 컬렉션을 정렬, 따라서 순서용 컬럼을 매핑하지 않아도 됨
 * 모든 컬렉션에서 사용할 수 있다.
 */
@Data
@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    @OrderBy("username desc , id asc ")
    private List<Member> members = new ArrayList<>();
}
