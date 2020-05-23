package domain.mappedSuperClassExam;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AttributeOverride(name = "id", column =@Column(name="map_member_id"))      //부모로부터 물려받은 매핑정보를 재정의
public class MapMember extends MapBaseEntity{
    //ID 상속
    //NAME 상속
    private String email;
}
