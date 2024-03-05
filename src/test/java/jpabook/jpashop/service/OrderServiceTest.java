package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품_주문() throws Exception {
        //given
        Member member = createMember();

        Item book = createBook("시골JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long order = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(order);
        Assertions.assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus()); //상품 주문시 상태는 ORDER 이다.
        Assertions.assertThat(1).isEqualTo(getOrder.getOrderItems().size()); //주문한 상품종류수가 정확해야함
        Assertions.assertThat(10000 * orderCount).isEqualTo(getOrder.getTotalPrice()); //주문 가격은 가격 * 수량
        Assertions.assertThat(8).isEqualTo(book.getStockQuantity()); //주문 수량만큼 재고가 줄어야한다.
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("시골JPA", 10000, 10);

        int orderCount = 11;
        //when
        //orderService.order(member.getId(), book.getId(), orderCount); //에러 발생

        //then
        org.junit.jupiter.api.Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    public void 상품_주문취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus()); //주문 취소시 상태는 CANCEL이다.
        Assertions.assertThat(10).isEqualTo(item.getStockQuantity()); //주문 취소시 수량은 원복되어야한다.

    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}