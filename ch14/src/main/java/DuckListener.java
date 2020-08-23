import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * 별도의 리스너 사용
 *
 * @PostLoad: 해당 엔티티를 새로 불러오거나 refresh 한 이후.
 * @PrePersist: 해당 엔티티를 저장하기 이전
 * @PostPersist: 해당 엔티티를 저장한 이후
 * @PreUpdate: 해당 엔티티를 업데이트 하기 이전
 * @PostUpdate: 해당 엔티티를 업데이트 한 이후
 * @PreRemove: 해당 엔티티를 삭제하기 이전
 * @PostRemove: 해당 엔티티를 삭제한 이후
 */
public class DuckListener {

    private static final Logger log = LoggerFactory.getLogger(DuckListener.class);

    /* //이게 되는지 몰라서 나중에 해보자
        @PostLoad
        public void postLoad(Object obj) {
            log.info("post load: {}", obj);
        }
    */
    @PostLoad
    public void postLoad(Duck duck) {
        log.info("post load: {}", duck);
    }

    @PrePersist
    public void prePersist(Duck duck) {
        log.info("pre persist: {}", duck);
    }

    @PostPersist
    public void postPersist(Duck duck) {
        log.info("post persist: {}", duck);
    }

    @PreUpdate
    public void preUpdate(Duck duck) {
        log.info("pre update: {}", duck);
    }

    @PostUpdate
    public void postUpdate(Duck duck) {
        log.info("post update: {}", duck);
    }

    @PreRemove
    public void preRemove(Duck duck) {
        log.info("pre remove: {}", duck);
    }

    @PostRemove
    public void postRemove(Duck duck) {
        log.info("post remove: {}", duck);
    }
}
