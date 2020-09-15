# 15장_고급 주제와 성능 최적화

## 15.1 예외 처리

### 15.1.1 JPA 표준 예외 처리

- JPA 표준 예외들은 javax.persistence.PersistenceException의 자식 클래스
    - 이 예외 클래스는 RuntimeException의 자식
    - 따라서 모드 언체크 예외
- 크게 2가지로 나눌 수 있다.
    - 트랜잭션 롤백을 **표시**하는 예외
        - 심각한 예외이므로 복구해선 안됨
        - 이 예외가 발생하면 트랜잭션을 강제로 커밋해도 트랜잭션이 커밋되지 않고 javax.persistence.RollbackException 예외가 발생
            - javax.persistence.EntityExistsException
                - EntityManger.persist(..)호출 시 이미 같은 엔티티가 있으면 발생
            - javax.persistence.EntityNotFoundException
                - EntityManger.getReference()...를 호출 했는데 실제 사용 시 엔티티가 존재하지 않으면 발생.
                - refresh(...), lock(...)에서도 발생.
            - javax.persistence.OptimistickLockExcetpion
                - 낙관적 락 충돌 시 발생
            - javax.persistence.PessimisticLockException
                - 비관적 락 충돌시 발생
            - javax.persistence.RollbackException
                - EntityTransaction.commit() 실패시 발생.
                - 롤백이 표시되어 있는 트랜잭션 커밋 시에도 발생.
            - javax.persistence.TransactionRequiredException
                - 트랜잭션이 필요할 때 트랜잭션이 없으면 발생.
                - 트랜잭션 없이 엔티티를 변경할 때 주로 발생

    - 트랜잭션 롤백을 **표시하지 않는** 예외
        - 심각한 예외가 아니므로 개발자가 트랜잭션을 커밋할지 롤백할지를 판단
            - javax.persistence.NoResultException
                - Query.getSingleResult() 호출 시 결과가 하나도 없을 때 발생
            - javax.persistence.NonUniqueResultException
                - Query.getSingleResult() 호출 시 결과가 둘 이상일 때 발생
            - javax.persistence.LockTimeoutException
                - 비관적 락에서 시간 초과 시 발생
            - javax.persistence.QueryTimeoutException
                - 쿼리 실행 시간 초과 시 발생

### 15.1.2 스프링 프레임워크의 JPA 예외 변환

- 서비스 계층에서 JPA의 예외를 직접 사용하면 JPA에 의존하게됨.
- 스프링 프레임워크는 이런 문제를 해결하려고 데이터 접근 계층에 대한 예외를 추상화해서 개발자에게 제공한다.

## 15.1.3 **스프링 프레임워크에 JPA 예외 변환기 적용**

위처럼 JPA예외를 스프링 예외로 변경해서 받으려면 `PersistenceExceptionTranslationPostProcessor`를 스프링 빈으로 등록하면 된다.이것은 `@Repository` 어노테이션을 사용한곳에 예외 변환 AOP를 적용해서 JPA 예외를 스프링 추상화 예외로 변환해준다.

```java
@Bean
public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
    return new PersistenceExceptionTranslationPostProcessor();
}
```

## 15.1.4 트랜잭션 롤백 시 주의사항

트랜잭션을 롤백하는 것은 데이터베이스의 반영사항만 롤백하는 것이지 수정한 자바 객체까지 원상태로 복구해주지 않음. 

그렇기 때문에 새로운 영속성 컨텍스트를 생성해서 사용하거나 `EntityManger.clear()`를 호출해서 초기화 한다음에 사용해야함

기본 전략인 트랜잭션당 영속성 컨텍스트 전략은 문제가 발생하면 트랜잭션 AOP 종료 시점에 트랜잭션을 롤밸하면서 영속성 컨텍스트도 종료하므로 문제가 발생하지 않지만

OSIV처럼 범위가 넓어지면 여러 트랜잭션이 하나의 영속성 컨텍스트를 사용하기 때문에 위험함 

이또한 `EntityManger.clear()`를 적절히 사용해줘야함.

## 15.2 엔티티 비교

영속성 컨텍스트의 1차 캐시는 영속성 컨텍스트와 생명주기를 같이함.

1차 캐시 덕분에 변경 감지 기능도 동작하고, 데이터베이스를 통하지 않고 데이터를 바로 조회할 수 있다. 

1차 캐시의 가장큰 장점은 **애플리케이션 수준의 반복 가능한 읽기**이다. 

```java
Member member1 = em.find(Member.class, "1L");
Member member2 = em.find(Member.class, "1L");
assertTrue(member1 == member2);// 둘은 같은 인스턴스
```

- 같은 영속성 컨텍스트에서 엔티티를 조회하면 다음 코드와 같이 항상 같은 엔티티 인스턴스를 반환
    - 동등성 비교 수준이 아니라 주솟값이 같은 인스턴스를 반환한다.
        - 동일성(identical): == 비교가 같다
        - 동등성(equinalent): equals() 비교가 같다
        - 데이터베이스 동등성(equinalent): @Id인 데이터베이스 식별자가 같다.

- 다른 영속성 컨텍스트에 조회시 엔티티 비교
    - 동일성: == 비교가 실패
    - 동등성: equals() 비교가 만족한다. 단 equals()를 구현해야 한다. 보통 비즈니스 키로 구현.
    - 데이터베이스 동등성: @Id인 데이터베이스 식별자가 같다.

    - 영속성 컨텍스트가 달라지면 엔티티의 비교에 다른 방법을 사용한다.

    ```java
    #데이터베이스 동등성 비교 
    member.getId().equals(findMember.getId()) // 데이터 베이스 식별자 비교
    ```

    - 이는 비교하기전 엔티티를 영속화 해야한다는 제약이 있음

    - equals()를 사용한 동등성 비교
        - 이는 비즈니스키를 활용한 동등성 비교를 권장
            - 동등성 비교를 위해 equals()를 오버라이딩 할때는 비즈니스 키가 되는 필드를 선택한다.
            - 보통 중복되지 않고 거의 변하지 않는 데이터베이스 기본 키 후보들이 좋은 대상
            - ex) 주민등록번호 , 회원의 이름과 연락처의 조합

    - 정리
        - 동일성 비교는 같은 영속성 컨텍스트의 관리를 받는 영속 상태의 엔티티에만 적용가능
        - 그러지 않을때는 비즈니스 키를 사용한 동등성 비교 해야함

    ## 15.3 프록시 심화 주제

    프록시는 원본 엔티티를 상속받아서 만들어지므로 엔티티를 사용하는 클라이언트는 엔티티가 프록시인지 아니면 원본 엔티티인지 구분하지 않고 사용가능 

    하지만 프록시를 사용하는 방식의 기술적인 한계로 인해 예상하지 못한 문제들이 발생할 수 있다. 이를 알아본다.

    ### 15.3.1 영속성 컨텍스트와 프록시

    - 영속성 컨스트는 자신의 관리하는 영속 엔티티의 동일성을 보장한다. 그럼 프록시로 조회한 엔티티의 동일성도 보장되는지 확인 해보자 .

        ```java
        @Test 
            public void 영속성_컨텍스트와_프록시(){
                Member newMember = new Member("member1", "회원1");
                em.persist(newMember);
                em.flush();
                em.clear();
                
                Member refMember = em.getReference(Member.class, "member1");
                Member findMember = em.find(Member.class, "member1");

                System.out.println("refMember.getClass() = " + refMember.getClass());
                System.out.println("findMember.getClass() = " + findMember.getClass());
                
                assertThat(refMember).isEqualTo(findMember); // 성공
            }
        ```

        출력결과는

        ```java
        refMember.getClass() = class jpabook.advanced.Member_&&_jvst843_0
        findMember.getClass() =class jpabook.advanced.Member_&&_jvst843_0
        ```

        로 같은 값을 반환한다.

        영속성 컨텍스트는 프록시로 조회된 엔티티에 대해서 같은 엔티티를 찾는 요청이 오면 원본 엔티티가 아닌 처음 조회된 프록시를 반환함.

        그래서 처음 엔티티를 프록시로 조회했기 때문에 이후 em.find로 찾아도 프록시를 반환한 것임. 

        따라서 프록시로 조회해도 영속성 컨텍스트는 영속 엔티티의 동일성을 보장한다는 것을 알 수 있음 

    - 이와 반대로 원본 엔티티를 먼저 조회하고, 프록시를 조회해본다.

        ```java
        @Test 
            public void 영속성_컨텍스트와_프록시(){
                Member newMember = new Member("member1", "회원1");
                em.persist(newMember);
                em.flush();
                em.clear();
                
                Member findMember = em.find(Member.class, "member1");
                Member refMember = em.getReference(Member.class, "member1");
         
                System.out.println("refMember.getClass() = " + refMember.getClass());
                System.out.println("findMember.getClass() = " + findMember.getClass());
                
                assertThat(refMember).isEqualTo(findMember); // 성공
            }
        ```

        출력결과는

        ```java
        refMember.getClass() = class jpabook.advanced.Member
        findMember.getClass() =class jpabook.advanced.Member
        ```

        처음 원본 엔티티를 먼저 조회했으므로 프록시를 반환할 필요가 없다. 그렇기 때문에 getReference를 호출해도 프록시가 아닌 원본을 반환한다. 

        이 경우에도 엔티티의 동일성을 보장함

### 15.3.2 프록시 타입 비교

프록시는 원본 엔티티를 상속 받아서 만들어지므로 프록시로 조회한 엔티티의 타입을 비교할 때는 `==` 비교를 하면 안되고 대신에 instanceof를 사용해야 한다.

```java
@Test
public void 프록시_타입비교() {
	Member newMember = new Member("member1", "회원1"):
	em.persist(newMember);
	em.flush();
	em.clear();

	Member refMember = em.getReference(Member.class, "member1");

	System.out.println("refMember Type = " + refMember.getClass());

	Assert.assertTrue(refMember instanceof Member);

}
```

출력 결과는 

reMembere Type = class jpabook.advanced.Member_$$_jvsteXXX 와 같다. 
프록시를 의미하는 $$_jvsteXXX 가 붙었다. 

### 15.3.3 프록시 동등성 비교

엔티티의 동등성을 비교하려면 비즈니스 키를 사용해서 equals() 메소드를 오버라이딩하고 비교하면 됨. 

그런데 IDE나 외부 라이브러리를 사용해서 구현한 equals() 메소드로 엔티티를 비교할 때, 비교 대상이 원본 엔티티면 문제가 없지만 프록시면 문제가 발생 

프록시 동등성 비교, 회원 엔티티

```java
@Entity
public class Member {
    
    @Id
    private String id;
    private String name;

    ...
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getCloass()) return false;   //  1.
    
        Member member = (Member) obj;
        
        if (name != null ? !name.equals(member.name) : member.name != null) //  2.
            return false;

        return true; 
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
```

name필드를 비즈니스키로 사용해서 equals()메소드를 오버리이딩 했음 (name이 중복되는 회원은 없다고 가정)

- 프록시 동등성 비교, 실행

```java
@Test
public void 프록시와_동등성비교() {

    Member saveMember = new Member("member1", "회원1");

    em.persist(saveMember);
    em.flush();
    em.clear();

    Member newMember = new Member("member1", "회원1");
    Member refMember = em.getReference(Member.class, "member1");

    Assert.assertTrue( newMember.equals(refMember) );
}
```

동등성 비교가 성공할 것 같지만 실패한다. 위에서 작성한 코드가 잘못 되어있기 때문.

위에서 말했듯이 프록시와  `equals()` 비교할 때는 `==`이 아닌 `instanceof`를 사용해야함 

원본 엔티티는 성공한다. 

따라서 1번은 아래와 같이 수정

```java
if (!(obj instanceof Member)) return false;
```

그 다음은 2번 부분을 보면 프록시의 멤버 변수에 직접 접근하는데, 프록시는 실제 데이터를 가지고 있지 않기 때문에 프록시의 멤버 변수에 직접 접근하면 아무 값도 얻을 수 없다. 따라서 member.name의 결과는 null이고 equals()는 false가 반환된다.

```
프록시의 데이터를 조회할 때는 접근자(Getter)를 사용
```

해야 한다. 따라서 2번은 아래와 같이 수정돼야 한다.

```java
Member member = (Member) obj;

if (name != null ? !name.equals(member.getName()) : member.getName() != null)
    return false;
```

정리하자면

- 프록시의 타입 비교는 == 비교 대신에 instanceof를 사용해야 한다.
- 프록시의 멤버변수에 직접 접근하면 안 되고 대신에 접근자 메소드를 사용해야 한다.

### 15.3.4 상관관계와 프록시

프록시를 부모 타입으로 조회하면 부모의 타입을 기반으로 프록시가 생성되는 문제가 있다. 

## 15.4 성능최적화

### 15.4.1 패치조인 사용

쿼리에 패치조인을 사용하면서

`hibernate.default_batch_fetch_size: 100`  을 yml옵션에 넣는다. 

### 15.4.2 읽기 전용 쿼리의 성능 최적화

`@Transactional(readOnly = true)` 사용

### 15.4.5 트랜잭션을 지원하는 쓰기 지연과 성능 최적화

### **플러시 란?**

JPA는 엔티티를 영속성 컨텍스트에서 관리한다. 영속성 컨텍스트에 있는 내용을 데이터베이스에 반영하는 것을 플러시라고 한다. 보통 트랜잭션을 커밋하면 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화(등록, 수정, 삭제) 작업을 진행하게 된다.

### **엔티티 등록**

```java
EntityMaanger em  = emf.createEnttiyManager();
ENtityTranscation transaction = em.getTransaction();
// 엔티티 매니저는 데이터 변경 시 트랜잭션을 시작해야한다.

transaction.begin();

em.persist(memberA);
em.persist(memberB);

// 여기까지 Insert SQL을 데이터베이스에 보내지 않는다.

// Commit을 하는 순간 데이터베이스에 Insert SQL을 보낸다
transaction.commit();
```

엔티티 **매니저는 트랜잭션을 커밋하기 직전까지 데이터베이스에 엔티티를 저장하지 않고 내부 쿼리 저장소에 INSERT SQL을 모아둔다.** 그리고 트랜잭션을 커밋할 때 모아둔 쿼리를 데이터베이스에 보내는데 이것을 **트랜잭션을 지원하는 쓰기 지연** 이라 한다.

![https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent.png](https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent.png)

회원 A를 영속화 했다. 영속성 컨텍스트는 1차 캐시에 회원 엔티티를 저장하면서 동시에 회원 엔티티 정보로 등록 쿼리를 만든다. 그리고 만들어진 등록 쿼리를 쓰기 지연 SQL 저장소에 보관한다.

![https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent-2.png](https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent-2.png)

다음으로 회원 B를 영속화했다. 마찬가지로 회원 엔티티 정보로 등록 쿼리를 생성해서 쓰지 지연 SQL 저장소에 보관한다. 현재 쓰기 지연 SQL저장소 에는 등록 쿼리가 2건이 저장되어 있다.

![https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent-3.png](https://github.com/cheese10yun/TIL/raw/master/assets/jpa-insert-persistent-3.png)

마지막으로 트랜잭션을 커밋했다. **트랜잭션을 커밋하면 엔티티 매니저는 우선 영속성 컨텍스트를 플러시한다. 플러시는 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화하는 작업인데 이때 등록, 수정, 삭제한 엔티티를 데이터베이스에 반영한다.**

즉, 쓰기 지연 SQL 저장소에 모인 쿼리를 데이터베이스에 보낸다. 이렇게 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화한 후에 실제 데이터베이스 트랜잭션을 커밋한다.(flush가 먼저 동작하고 (데이터베이스에 동기화한 후에) 실제 데이터베이스 트랜잭션을 커밋한다.)

### 트랜잭션을 지원하는 쓰기 지연과 JDBC 배치

```java
insert(member1); // INSERT INTO ...
insert(member2); // INSERT INTO ...
insert(member3); // INSERT INTO ...
insert(member4); // INSERT INTO ...
insert(member5); // INSERT INTO ...

commit();
```

네트워크 호출 한번은 단순한 메소드를 수만 번 호출하는 것보다 더 큰 비용이 든다.

이 코드는 5번의 INSERT SQL과 1번의 커밋으로 총 6번 데이터 베이스와 통신한다.

이것을 최적화하라면 5번의 INSERT SQL을 모아서 한 번에 데이터베이스로 보내면 된다. 

JDBC가 제공하는 SQL 배치 기능을 사용하면 SQL을 모아서 데이터베이스에 한 번에 보낼 수 있다. 하지만 이 기능을 사용하려면 많은 코드를 수정해야한다. 

JPA는 플러시 기능이 있으므로 SQL 배치 기능을 효과적으로 사용할 수 있다.

yml속성에 `hibernate.jdbc.batch_size` 속성의 값을 50으로 주면 최대 50건씩 모아서 SQL 배치를 실행한다. 하지만 SQL 배치는 같은 SQL일 때만 유효하다. 중간에 다른 처리가 들어가면 SQL 배치를 다시 시작한다.

예를 들면

```java
em.persist(new Member()); // 1
em.persist(new Member()); // 2
em.persist(new Member()); // 3
em.persist(new Member()); // 4
em.persist(new Orders()); // 1-1, 다른연산
em.persist(new Member()); // 1
em.persist(new Member()); // 2
```

위 순서대로 총 3번 SQL 배치를 실행한다.

### 주의점

엔티티가 영속 상태가 되려면 식별자가 꼭 필요하다. 그런데 `IDENTITY` 식별자 생성 전략은 엔티티를데이터베이스에 저장해야 식별자를 구할 수 있으므로  `em.persist()`를 호출하는 즉시 `INSERT SQL`이 데이터베이스에 전달된다. 따라서 쓰기 지연을 활용한 성능 최적화를 할 수 없다. 

### **트랜잭션을 지원하는 쓰기 지연과 애플리케이션 확장성**

트랜잭션을 지원하는 쓰기 지연의 가장 큰 장점은 데이터베이스 테이블 로우에 락이 걸리는 시간을 ****최소한다는 것이다. 이 기능은 트랜잭션을 커밋해서 영속성 컨텍스트를 플러시하기 전까지는 데이터베이스에 데이터를 등록, 수정, 삭제 하지 않는다. 따라서 커밋 전까지 데이터베이스 로우에 락을 걸지 않는다**.**

```java
update(memberA); // UPDATE SQL Member A
비즈니스로직A(); // UPDATE SQL ...
비즈니스로직B(); // UPDATE SQL ...
commit();
```

JPQL를 사용하지 않고 SQL을 직접다루면 update(memberA)를 호출할 때 UPDATE SQL을 실행하면 데이터베이스 테이블 로우에 락을 건다. 이 락은 비즈니스 `로직A()`, `비즈니스 로직B()`를 모두 수행하고 `commit()`을 호출할 때까지 유지된다. 트랜잭션 격리 수준에 따라 다르지만 보통 많이 사용하는 커밋된 읽기(Read Committed) 격리 수준이나 그 이상에는 데이터베이스에 현재 수정 중인 데이터(로우)를 수정하려는 다른 트랜잭션은 락이 풀릴 때까지 대기한다.

JPA는 커밋을 해야 플러시를 호출하고 데이터베이스에 수정 쿼리를 보낸다. 예제에서 `commit()`을 호출할 때 UPDATE SQL을 실행하고 바로 데이터베이스 트랜잭션을 커밋한다. 쿼리를 보내고 보내고 바로 트랜잭션을 커밋하므로 결과적으로 데이터베이스에 락이 걸리는 시간을 최소화 한다. 이는 동시에 더 많은 트래잭션을 처리할 수 있다는 장점이 된다.