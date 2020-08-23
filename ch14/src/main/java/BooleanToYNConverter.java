import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;

/**
 * 컨버터 클래스는 @Converter 어노테이션을 사용하고 AttributeConverter 인터페이스를 구현해야함
 * 여기서는 Boolean 타입을 String 타입으로 변환함
 */

/**
 * 모든 Boolean 타입에 컨버터를 적용하려면 현재와 같이 적용 가능
 */
@Converter(autoApply = true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

    /**
     * 엔티티의 데이터를 데이터베이스 컬럼에 저장할 데이터로 변환
     * 여기 예제에서는 true면 Y, false면 N을 반환하도록 함
     */
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y": "N";
    }

    /**
     * 데이터베이서에서 조회한 컬럼 데이터를 엔티티의 데이터로 변환 함
     */
    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }
}
