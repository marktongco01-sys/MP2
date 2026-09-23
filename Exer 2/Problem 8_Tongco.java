import java.util.Scanner;
class Product {
    // Encapsulated attributes
    private String code;
    private String name;
    private double price;
    private int stock;

    // Constructor
    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // Controlled method for reducing stock
    public boolean reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            return false;
        }

        stock -= quantity;
        return true;
    }
}


// =========================
// CartItem Class
// =========================
class CartItem {
    private Product product;
    private int quantity;

    // Constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getCode() {
        return product.getCode();
    }

    public String getName() {
        return product.getName();
    }

    public double getPrice() {
        return product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    // Increase quantity
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}



// ShoppingCart Class

class ShoppingCart {

    // Array of CartItem objects
    private CartItem[] items;

    // Number of items currently in cart
    private int itemCount;

    // Constructor
    public ShoppingCart() {
        items = new CartItem[100];
        itemCount = 0;
    }

    // Add item to cart
    public void addItem(Product product, int quantity) {

        // Check quantity
        if (quantity <= 0) {
            System.out.println(
                "Addition rejected: quantity must be positive."
            );
            return;
        }

        // Check stock
        if (quantity > product.getStock()) {
            System.out.println(
                "Addition rejected: insufficient stock."
            );
            return;
        }

        // Check if product already exists in cart
        for (int i = 0; i < itemCount; i++) {

            if (items[i].getCode().equals(product.getCode())) {

                // Reduce stock
                if (product.reduceStock(quantity)) {

                    // Increase existing quantity
                    items[i].addQuantity(quantity);

                    System.out.println(
                        "Added " + quantity + " more "
                        + product.getName() + " to cart."
                    );
                }

                return;
            }
        }

        // Check cart capacity
        if (itemCount >= items.length) {
            System.out.println("Addition rejected: cart is full.");
            return;
        }

        // Reduce stock
        if (product.reduceStock(quantity)) {

            // Create a new CartItem
            items[itemCount] = new CartItem(product, quantity);

            itemCount++;

            System.out.println(
                "Added " + quantity + " "
                + product.getName() + " to cart."
            );
        }
    }

    // Calculate subtotal
    public double getSubtotal() {

        double subtotal = 0;

        // Loop through cart
        for (int i = 0; i < itemCount; i++) {
            subtotal += items[i].getLineTotal();
        }

        return subtotal;
    }

    // Calculate discount
    public double getDiscount() {

        double subtotal = getSubtotal();

        if (subtotal >= 5000) {
            return subtotal * 0.10;
        }
        else if (subtotal >= 2000) {
            return subtotal * 0.05;
        }
        else {
            return 0;
        }
    }

    // Calculate VAT
    public double getVAT() {

        double subtotal = getSubtotal();
        double discount = getDiscount();

        double discountedAmount = subtotal - discount;

        return discountedAmount * 0.12;
    }

    // Calculate final total
    public double getFinalTotal() {

        double subtotal = getSubtotal();
        double discount = getDiscount();
        double vat = getVAT();

        return subtotal - discount + vat;
    }

    // Display receipt
    public void displayReceipt() {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("                 RECEIPT");
        System.out.println("==============================================");

        if (itemCount == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.printf(
            "%-10s %-15s %-8s %-12s %-12s%n",
            "Code",
            "Product",
            "Qty",
            "Price",
            "Total"
        );

        System.out.println("----------------------------------------------");

        // Loop through cart items
        for (int i = 0; i < itemCount; i++) {

            System.out.printf(
                "%-10s %-15s %-8d %-12.2f %-12.2f%n",
                items[i].getCode(),
                items[i].getName(),
                items[i].getQuantity(),
                items[i].getPrice(),
                items[i].getLineTotal()
            );
        }

        System.out.println("----------------------------------------------");

        double subtotal = getSubtotal();
        double discount = getDiscount();
        double vat = getVAT();
        double finalTotal = getFinalTotal();

        System.out.printf("Subtotal:       %.2f%n", subtotal);
        System.out.printf("Discount:       %.2f%n", discount);
        System.out.printf("VAT (12%%):      %.2f%n", vat);
        System.out.printf("Final Total:    %.2f%n", finalTotal);

        System.out.println("==============================================");
    }
}


// =========================
// Main Class
// =========================
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // =================================
        // Product Catalog
        // =================================

        // Array of Product objects
        Product[] catalog = {
            new Product("P1", "Mouse", 500, 10),
            new Product("P2", "Keyboard", 2500, 5),
            new Product("P3", "Cable", 100, 2)
        };

        int catalogSize = catalog.length;

        // Create ShoppingCart object
        ShoppingCart cart = new ShoppingCart();

        System.out.println("===== SHOPPING CART SYSTEM =====");

        System.out.print("Enter number of add-to-cart actions: ");
        int actions = input.nextInt();

        // =================================
        // Process Add-to-Cart Actions
        // =================================

        for (int i = 0; i < actions; i++) {

            System.out.println();
            System.out.println("Action " + (i + 1));

            System.out.print("Enter product code: ");
            String code = input.next();

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();

            boolean found = false;

            // Search product catalog
            for (int j = 0; j < catalogSize; j++) {

                if (catalog[j].getCode().equals(code)) {

                    cart.addItem(catalog[j], quantity);

                    found = true;
                    break;
                }
            }

            // Product was not found
            if (!found) {
                System.out.println(
                    "Addition rejected: product not found."
                );
            }
        }

        // =================================
        // Display Receipt
        // =================================

        cart.displayReceipt();

        // =================================
        // Display Remaining Stock
        // =================================

        System.out.println();
        System.out.println("===== REMAINING CATALOG STOCK =====");

        // Loop through catalog
        for (int i = 0; i < catalogSize; i++) {

            System.out.println(
                catalog[i].getCode()
                + " - "
                + catalog[i].getName()
                + ": "
                + catalog[i].getStock()
                + " remaining"
            );
        }

        input.close();
    }
}

