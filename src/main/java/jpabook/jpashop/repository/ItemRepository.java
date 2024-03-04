package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) { //merge에 관해서는 뒤에서 설명해주심, 스프링 데이터 JPA repo.save()가 이렇게 구현되어있음
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); //업데이트..인데 그냥 JPA는 dirtyChecking(변경감지) 쓰자, 얘는 처음에 select 쿼리 날아감
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
