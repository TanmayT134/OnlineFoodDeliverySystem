import java.util.ArrayList;
import java.util.Scanner;
// Represents a food item
class FoodItem {
    private String name;
    private double price;
    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
}

// Represents a customer
class Customer {
    private static int customerCounter = 1;
    private int customerId;
    private String name;
    private String address;
    private ArrayList<Order> orderHistory = new ArrayList<>();
    public Customer(String name, String address) {
        this.customerId = customerCounter++;
        this.name = name;
        this.address = address;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
    public void viewPayments() {
        System.out.println("\nPayment Status:");
        for (Order order : orderHistory) {
            System.out.println("Order ID: " + order.getOrderId() + " - Total: ₹" + order.calculateTotal() + " - Paid: " + (order.needsPayment() ? "Pending" : "Yes"));
        }
    }
    public void makePayment(Order order) {
        double total = order.calculateTotal();
        System.out.print("Enter amount to pay (Total: ₹" + total + "): ₹");
        Scanner sc = new Scanner(System.in);
        double amountPaid = sc.nextDouble();
        if (amountPaid == total) {
            order.markPaid();
            System.out.println("Payment successful. Thank you for your order!");
        } else {
            System.out.println("Wrong amount. Payment failed.");
        }
    }
}
// Represents an order placed by a customer
class Order {
    private static int orderCounter = 1;
    private int orderId;
    private Customer customer;
    private ArrayList<FoodItem> items = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>(); // To track quantities
    private boolean isPaid;
    private boolean payOnDelivery;
    private boolean isDelivered;
    public Order(Customer customer, boolean payOnDelivery) {
        this.orderId = orderCounter++;
        this.customer = customer;
        this.payOnDelivery = payOnDelivery;
        this.isPaid = false;
        this.isDelivered = false;
    }
    public int getOrderId() {
        return orderId;
    }
    public void addItem(FoodItem item, int quantity) {
        items.add(item);
        quantities.add(quantity);
    }
    public double calculateTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i); // Calculate total based on quantities
        }
        return total;
    }
    public void markPaid() {
        this.isPaid = true;
    }
    public boolean needsPayment() {
        return !isPaid;
    }
    public boolean isPayOnDelivery() {
        return payOnDelivery;
    }
    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
    public boolean isDelivered() {
        return isDelivered;
    }
    public void displayOrder() {
        System.out.println("Order ID: " + orderId);
        for (int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).getName() + " (x" + quantities.get(i) + ") - ₹" + (items.get(i).getPrice() * quantities.get(i)));
        }
        System.out.println("Total: ₹" + calculateTotal());
        System.out.println("Paid: " + (isPaid ? "Yes" : "Pending"));
        System.out.println("Delivered: " + (isDelivered ? "Yes" : "Pending"));
    }
    public Customer getCustomer() {
        return customer;
    }
}
// Represents the owner
class Owner {
    private ArrayList<FoodItem> menu = new ArrayList<>();
    private ArrayList<Order> allOrders = new ArrayList<>();

    public Owner() {
        // Initial menu items
        addFoodItem("Pizza", 140);
        addFoodItem("Burger", 100);
        addFoodItem("Cold Coffee", 60);
        addFoodItem("Pasta", 120);
        addFoodItem("Sandwich", 70);
        addFoodItem("French Fries",90);
    }
    public void addFoodItem(String name, double price) {
        menu.add(new FoodItem(name, price));
    }
    public void removeFoodItem(String name) {
        menu.removeIf(item -> item.getName().equalsIgnoreCase(name));
    }
    public void viewMenu() {
        System.out.println("\nMenu:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).getName() + " - ₹" + menu.get(i).getPrice());
        }
    }
    public ArrayList<FoodItem> getMenu() {
        return menu;
    }
    public void addOrder(Order order) {
        allOrders.add(order);
    }
    public ArrayList<Order> getAllOrders() {
        return allOrders;
    }
    public void viewPayments() {
        System.out.println("\nCompleted Payments:");
        for (Order order : allOrders) {
            if (!order.needsPayment()) {
                System.out.println("Order ID: " + order.getOrderId() + " - ₹" + order.calculateTotal() + " - Paid");
            }
        }
    }
    public void viewPendingPayments() {
        System.out.println("\nPending Payments:");
        for (Order order : allOrders) {
            if (order.needsPayment()) {
                System.out.println("Order ID: " + order.getOrderId() + " - ₹" + order.calculateTotal() + " - Pending");
            }
        }
    }
}
// Represents the delivery person
class DeliveryPerson {
    public void viewPendingOrders(ArrayList<Order> orders) {
        System.out.println("\nPending Orders:");
        for (Order order : orders) {
            if (!order.isDelivered()) {
                order.displayOrder();
            }
        }
    }
    public void deliverOrder(int orderId, ArrayList<Order> orders) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId && !order.isDelivered()) {
                if (order.isPayOnDelivery() && order.needsPayment()) {
                    System.out.println("Collecting payment of ₹" + order.calculateTotal() + " for Order ID " + orderId);
                    order.getCustomer().makePayment(order); // Directly prompt customer for payment
                }
                order.setDelivered(true);
                System.out.println("Order ID " + orderId + " marked as delivered.");
                break;
            }
        }
    }
}
// Main class to run the application
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Owner owner = new Owner();
        ArrayList<Customer> customers = new ArrayList<>();
        DeliveryPerson deliveryPerson = new DeliveryPerson();
        int choice;
        do {
            System.out.println("\n 1.Owner\n 2.Customer\n 3.Delivery Person\n 0.Exit");
            System.out.print("Choose your role: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    int ownerChoice;
                    do {
                        System.out.println("\nOwner Options:");
                        System.out.println("\n 1.Add Food Item\n 2.Remove Food Item\n 3.View Menu\n 4.View Payments\n 5.View Pending Payments\n 0.Go Back");
                        ownerChoice = sc.nextInt();
                        sc.nextLine();
                        switch (ownerChoice) {
                            case 1:
                                System.out.print("Enter item name: ");
                                String itemName = sc.nextLine();
                                System.out.print("Enter item price: ₹");
                                double itemPrice = sc.nextDouble();
                                owner.addFoodItem(itemName, itemPrice);
                                break;
                            case 2:
                                System.out.print("Enter item name to remove: ");
                                String removeName = sc.nextLine();
                                owner.removeFoodItem(removeName);
                                break;
                            case 3:
                                owner.viewMenu();
                                break;
                            case 4:
                                owner.viewPayments();
                                break;
                            case 5:
                                owner.viewPendingPayments();
                                break;
                        }
                    } while (ownerChoice != 0);
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter your address: ");
                    String address = sc.nextLine();
                    Customer customer = new Customer(name, address);
                    customers.add(customer);
                    int customerChoice;
                    do {
                        System.out.println("\nCustomer Options:");
                        System.out.println("\n 1.View Menu\n 2.Place Order\n 3.View Orders\n 4.Payment Status\n 0.Go Back");
                        customerChoice = sc.nextInt();
                        switch (customerChoice) {
                            case 1:
                                owner.viewMenu();
                                break;
                            case 2:
                                Order order = new Order(customer, true); // Assuming pay on delivery
                                owner.addOrder(order);
                                // Place multiple items
                                int addMoreItems;
                                do {
                                    System.out.print("Enter food number to add: ");
                                    int foodNumber = sc.nextInt();
                                    if (foodNumber > 0 && foodNumber <= owner.getMenu().size()) {
                                        System.out.print("Enter quantity: ");
                                        int quantity = sc.nextInt();
                                        order.addItem(owner.getMenu().get(foodNumber - 1), quantity);
                                    } else {
                                        System.out.println("Invalid food number!");
                                    }
                                    System.out.print("Do you want to add more items? (1 for Yes, 0 for No): ");
                                    addMoreItems = sc.nextInt();
                                } while (addMoreItems != 0);
                                // Payment options
                                System.out.println("Choose payment option:");
                                System.out.println("\n 1.Pay Now\n 2.Pay on Delivery");
                                int paymentOption = sc.nextInt();
                                if (paymentOption == 1) {
                                    double total = order.calculateTotal();
                                    System.out.print("Enter amount to pay (Total: ₹" + total + "): ₹");
                                    double amountPaid = sc.nextDouble();
                                    if (amountPaid == total) {
                                        order.markPaid();
                                        System.out.println("Payment successful. Thank you for your order!");
                                    } else {
                                        System.out.println("Wrong amount. Payment failed.");
                                    }
                                } else if (paymentOption == 2) {
                                    System.out.println("Order placed for Pay on Delivery. \nTotal: ₹" + order.calculateTotal());
                                }
                                owner.addOrder(order);
                                customer.addOrder(order);
                                break;
                            case 3:
                                System.out.println("\nYour Orders:");
                                for (Order o : customer.getOrderHistory()) {
                                    o.displayOrder();
                                }
                                break;
                            case 4:
                                customer.viewPayments();
                                break;
                        }
                    } while (customerChoice != 0);
                    break;
                case 3:
                    int deliveryChoice;
                    do {
                        System.out.println("\nDelivery Person Options:");
                        System.out.println("\n 1.View Pending Orders\n 2.Deliver Order\n 0.Go Back");
                        deliveryChoice = sc.nextInt();
                        sc.nextLine();
                        switch (deliveryChoice) {
                            case 1:
                                deliveryPerson.viewPendingOrders(owner.getAllOrders());
                                break;
                            case 2:
                                System.out.print("Enter Order ID to deliver: ");
                                int orderId = sc.nextInt();
                                deliveryPerson.deliverOrder(orderId, owner.getAllOrders());
                                break;
                        }
                    } while (deliveryChoice != 0);
                    break;
            }
        } while (choice != 0);

        System.out.println("Thank you for using the Online Food Delivery System!");
        sc.close();
    }
}
