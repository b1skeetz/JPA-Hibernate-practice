package damir;

import damir.entity.Category;
import damir.entity.Product;
import damir.entity.PropValues;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UpdateProductMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ID товара для обновления информации: ");
        Long productId = Long.valueOf(scanner.nextLine());

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Product product = manager.find(Product.class, productId);
        Category category = product.getCategory();

        System.out.println(product);
        System.out.print("Введите новое название продукта: ");
        String newName = scanner.nextLine();
        if (newName.equals("")) {
            newName = product.getName();
        }
        System.out.print("Введите новую цену: ");
        Integer newPrice = Integer.valueOf(scanner.nextLine());
        if (newPrice == 0) {
            newPrice = product.getPrice();
        }

        product.setName(newName);
        product.setPrice(newPrice);

        try {
            manager.getTransaction().begin();
            manager.persist(product);

            for (int i = 0; i < category.getProperties().size(); i++) {
                System.out.println(category.getProperties().get(i).getName() + ": " + product.getPropValues().get(i).getValue());
                System.out.print("Введите новое значение свойства: ");
                String newPropValue = scanner.nextLine();
                if (newPropValue.equals("")) {
                    newPropValue = product.getPropValues().get(i).getValue();
                }
                product.getPropValues().get(i).setValue(newPropValue);
                manager.persist(product.getPropValues().get(i));
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        factory.close();
        manager.close();
    }
}
