import lombok.Data;

import javax.persistence.*;

@Data
@Entity
/**
 * 리스너 사용
 * 데이터의 변경을 알려준다.
 *
 * javax.persistence.ExcludeDefaultListeners: 기본 리스너 무시
 * javax.persistence.ExcludeSuperclassListeners: 상위 클래스 이벤트 리스너 무시시 */
@ExcludeDefaultListeners
@ExcludeSuperclassListeners
@EntityListeners(DuckListener.class)
public class Duck {

    @Id @GeneratedValue
    private Long id;

    private String name;
}
