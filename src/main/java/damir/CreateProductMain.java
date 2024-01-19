package damir;

import damir.entity.Category;
import damir.entity.Product;
import damir.entity.PropValues;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class CreateProductMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите название товара: ");
        String productName = scanner.nextLine();
        System.out.print("Введите цену: ");
        Integer productPrice = Integer.valueOf(scanner.nextLine());
        System.out.print("Введите ID категории для товара: ");
        Long categoryId = Long.valueOf(scanner.nextLine());

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Category category = manager.find(Category.class, categoryId);
        try{
            manager.getTransaction().begin();
            Product product = new Product();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setCategory(category);
            manager.persist(product);

            for (int i = 0; i < category.getProperties().size(); i++) {
                PropValues propValue = new PropValues();
                System.out.print(category.getProperties().get(i).getName() + ": ");
                String property = scanner.nextLine();
                propValue.setValue(property);
                propValue.setProduct(manager.find(Product.class, product.getId()));
                propValue.setProperty(category.getProperties().get(i));
                manager.persist(propValue);
            }

            manager.getTransaction().commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }

        factory.close();
        manager.close();
    }
}
