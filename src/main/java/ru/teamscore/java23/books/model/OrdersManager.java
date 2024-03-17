package ru.teamscore.java23.books.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Customer;
import ru.teamscore.java23.books.model.entities.Order;

import java.util.*;

@RequiredArgsConstructor
public class OrdersManager {
    private final EntityManager entityManager;
    @Getter
    private final CustomerManager customerManager = new CustomerManager();

    public long getOrdersCount() {
        return entityManager
                .createQuery("select count(*) from Order", Long.class)
                .getSingleResult();
    }

    public Order[] getOrdersAll() {
        return entityManager
                .createQuery("from Order order by id", Order.class)
                .getResultList()
                .toArray(Order[]::new);
    }

    // Этот метод в OrdersManager или в CustomerManager
    public Order[] getActiveOrdersByCustomer(long idCustomer) {
        return entityManager
                .createQuery("select o from Order as o where o.customer.id=:id and status!='CANCELED'",
                        Order.class)
                .setParameter("id", idCustomer)
                .getResultList()
                .toArray(Order[]::new);
    }

    public Optional<Order> getOrder(long id) {
        return Optional.of(
                entityManager.find(Order.class, id)
        );
    }

    public void addOrder(@NonNull Order order) {
        var transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(order);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public int updateOrder(@NonNull Order order) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(order);
            entityManager.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return -1;
        }
    }

    public class CustomerManager {
        public long getCustomersCount() {
            return entityManager
                    .createNamedQuery("customersCount", Long.class)
                    .getSingleResult();
        }
        public Author[] getOrders(){
            return entityManager
                    .createQuery("from Customer c order by firstName, lastName, middleName", Author.class)
                    .getResultList()
                    .toArray(new Author[0]);
        }
        public Optional<Customer> getCustomer(long id) {
            try {
                /*return Optional.of(entityManager
                        .createNamedQuery("customerById", Customer.class)
                        .setParameter("id", id)
                        .getSingleResult());*/
                return Optional.ofNullable(entityManager.find(Customer.class, id));
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }

        public Optional<Customer> getCustomer(@NonNull Customer customer) {
            if (entityManager.contains(customer)) {
                return Optional.of(customer);
            }
            return getCustomer(customer.getId());
        }

        public int addCustomer(@NonNull Customer customer) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(customer);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }
        }

        public int updateCustomer(@NonNull Customer customer) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(customer);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }
        }
    }

}
