import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class Product {
    String id;
    String name;
    String category;
    int price;
    double rating;
    int popularity;

    public Product(String id, String name, String category, int price, double rating, int popularity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.popularity = popularity;
    }
}