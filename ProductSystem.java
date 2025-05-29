import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductSystem extends JFrame {
    private List<Product> products = new ArrayList<>();
    private final List<Product> originalProducts = new ArrayList<>(); // To keep the original order
    private JTable table;
    private DefaultTableModel model;

    public ProductSystem() {
        setTitle("Product Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        String[] columns = {"ID", "Name", "Category", "Price", "Rating", "Popularity"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        generateProducts();
        displayProducts(products);

        // Reset Button
        JButton resetBtn = new JButton("Reset to Default");
        resetBtn.addActionListener(e -> {
            products = new ArrayList<>(originalProducts); // Restore original order
            displayProducts(products);
        });
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resetPanel.add(resetBtn);

        // SEARCH PANEL UI SETUP
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(resetPanel);

        // Search by Name
        JTextField nameField = new JTextField();
        JButton nameSearchBtn = new JButton("Search by Name");
        nameSearchBtn.addActionListener(e -> {
            String term = nameField.getText().toLowerCase();
            List<Product> result = new ArrayList<>();
            for (Product p : products) {
                if (p.name.toLowerCase().contains(term)) {
                    result.add(p);
                }
            }
            displayProducts(result);
        });

        JButton nameSearchBt = new JButton("Search by Name");
        JButton exactNameSearchBtn = new JButton("Search Exact Name");

// Linear search for substring match
        nameSearchBtn.addActionListener(e -> {
            String term = nameField.getText().toLowerCase();
            List<Product> result = new ArrayList<>();
            for (Product p : products) {
                if (p.name.toLowerCase().contains(term)) {
                    result.add(p);
                }
            }
            displayProducts(result);
        });

// Binary search for exact match (requires sorted list)
        exactNameSearchBtn.addActionListener(e -> {
            String term = nameField.getText().toLowerCase();
            products.sort(Comparator.comparing(p -> p.name.toLowerCase()));
            int index = setName(products, term);
            List<Product> result = new ArrayList<>();
            if (index != -1) {
                result.add(products.get(index));
            }
            displayProducts(result);
        });


        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Search by Name:"));
        nameField.setPreferredSize(new Dimension(200, 25));
        namePanel.add(nameField);
        namePanel.add(nameSearchBtn);

        // Search by Price Range
        JTextField minPriceField = new JTextField();
        JTextField maxPriceField = new JTextField();
        JButton priceSearchBtn = new JButton("Search by Price Range");
        priceSearchBtn.addActionListener(e -> {
    try {
        int min = Integer.parseInt(minPriceField.getText());
        int max = Integer.parseInt(maxPriceField.getText());
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.price >= min && p.price <= max) {
                result.add(p);
            }
        }
        // Sort result by price ascending
        mergeSort(result, 0, result.size() - 1, "Price Low to High");
        displayProducts(result);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter valid numbers for price range.");
    }
});

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.add(new JLabel("Price Range: Min"));
        minPriceField.setPreferredSize(new Dimension(100, 25));
        maxPriceField.setPreferredSize(new Dimension(100, 25));
        pricePanel.add(minPriceField);
        pricePanel.add(new JLabel("Max"));
        pricePanel.add(maxPriceField);
        pricePanel.add(priceSearchBtn);

        // Search by Category
        String[] categories = {"All", "Electronics", "Books", "Clothing", "Home", "Toys"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        JButton categorySearchBtn = new JButton("Search by Category");
        categorySearchBtn.addActionListener(e -> {
            String selected = (String) categoryBox.getSelectedItem();
            if (selected.equals("All")) {
                displayProducts(products);
                return;
            }
            List<Product> result = new ArrayList<>();
            for (Product p : products) {
                if (p.category.equals(selected)) {
                    result.add(p);
                }
            }
            displayProducts(result);
        });
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        categoryPanel.add(categoryBox);
        categoryPanel.add(categorySearchBtn);

        // Sort
        String[] sortOptions = {"Price Low to High", "Price High to Low", "Rating", "Popularity"};
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);
        JButton sortBtn = new JButton("Sort");
        sortBtn.addActionListener(e -> {
            String criteria = (String) sortBox.getSelectedItem();
            mergeSort(products, 0, products.size() - 1, criteria);
            displayProducts(products);
        });
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortBox);
        sortPanel.add(sortBtn);

        // Add all panels to search panel
        searchPanel.add(namePanel);
        searchPanel.add(pricePanel);
        searchPanel.add(categoryPanel);
        searchPanel.add(sortPanel);

        add(searchPanel, BorderLayout.NORTH);
    }

    private int setName(List<Product> products, String term) {
        return 0;
    }

    private void generateProducts() {
        String[] categories = {"Electronics", "Books", "Clothing", "Home", "Toys"};
        String[][] baseNamesPerCategory = {
            {"Realme", "Samsung", "Infinix", "Vivo", "Xiaomi"},
            {"Alamat", "Dekada", "ABNKKBSNPLAko", "SubtleArt", "Susan"},
            {"Bench", "Penshoppe", "Uniqlo", "Regatta", "Forever21"},
            {"Uratex", "Germania", "Fujidenzo", "Hanabishi", "Kolin"},
            {"Barbie", "Lego", "HotWheels", "RC", "Jollibee"}
        };

        for (int i = 1; i <= 150; i++) {
            int categoryIndex = (i - 1) % categories.length;
            String category = categories[categoryIndex];
            String baseName = baseNamesPerCategory[categoryIndex][ThreadLocalRandom.current().nextInt(baseNamesPerCategory[categoryIndex].length)];
            String name = baseName + " Model " + i;
            String id = "P" + i;
            int price = ThreadLocalRandom.current().nextInt(100, 20001);
            double rating = Math.round((1 + ThreadLocalRandom.current().nextDouble() * 4) * 10.0) / 10.0;
            int popularity = ThreadLocalRandom.current().nextInt(10, 5001);
            Product product = new Product(id, name, category, price, rating, popularity);
            products.add(product);
            originalProducts.add(product); // Save the original order
        }
    }

    private void displayProducts(List<Product> list) {
        model.setRowCount(0);
        for (Product p : list) {
            String formattedPrice = String.format("₱%,.2f", (double) p.price);
            model.addRow(new Object[]{p.id, p.name, p.category, formattedPrice, p.rating, p.popularity});
        }
    }

    private void mergeSort(List<Product> list, int left, int right, String criteria) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid, criteria);
            mergeSort(list, mid + 1, right, criteria);
            merge(list, left, mid, right, criteria);
        }
    }

    private void merge(List<Product> list, int left, int mid, int right, String criteria) {
        List<Product> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<Product> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (compare(leftList.get(i), rightList.get(j), criteria) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()) list.set(k++, leftList.get(i++));
        while (j < rightList.size()) list.set(k++, rightList.get(j++));
    }

    private int compare(Product a, Product b, String criteria) {
        switch (criteria) {
            case "Price Low to High":
                return Integer.compare(a.price, b.price);
            case "Price High to Low":
                return Integer.compare(b.price, a.price);
            case "Rating":
                return Double.compare(b.rating, a.rating);
            case "Popularity":
                return Integer.compare(b.popularity, a.popularity);
            default:
                return 0;
        }
    }
}