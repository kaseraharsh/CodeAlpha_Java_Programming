import java.io.*;
import java.util.*;

// 1. Room Class
class Room {
    int roomNumber;
    String category; // Standard, Deluxe, Suite
    double price;
    boolean isAvailable;

    public Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }
}

// 2. Reservation Class
class Reservation {
    int reservationId;
    String customerName;
    Room room;
    double amountPaid;

    public Reservation(int reservationId, String customerName, Room room, double amountPaid) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.room = room;
        this.amountPaid = amountPaid;
    }
}

// 3. Main Hotel System Class
public class HotelReservationSystem {
    private static List<Room> rooms = new ArrayList<>();
    private static List<Reservation> reservations = new ArrayList<>();
    private static int nextReservationId = 101;
    private static final String FILE_NAME = "reservations.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadReservationsFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("   WELCOME TO CODEALPHA GRAND HOTEL       ");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n1. View Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. View My Booking Details");
            System.out.println("4. Cancel a Reservation");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                displayAvailableRooms();
            } else if (choice == 2) {
                makeReservation(scanner);
            } else if (choice == 3) {
                viewBookingDetails(scanner);
            } else if (choice == 4) {
                cancelReservation(scanner);
            } else if (choice == 5) {
                saveReservationsToFile();
                System.out.println("Thank you! Data saved successfully. Have a great day!");
                break;
            } else {
                System.out.println("❌ Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 100.0));
        rooms.add(new Room(102, "Standard", 100.0));
        rooms.add(new Room(201, "Deluxe", 200.0));
        rooms.add(new Room(202, "Deluxe", 200.0));
        rooms.add(new Room(301, "Suite", 500.0));
    }

    private static void displayAvailableRooms() {
        System.out.println("\n--- AVAILABLE ROOMS ---");
        System.out.println("Room No\tCategory\tPrice");
        boolean found = false;
        for (Room room : rooms) {
            if (room.isAvailable) {
                System.out.printf("%d\t%s\t\t$%.2f\n", room.roomNumber, room.category, room.price);
                found = true;
            }
        }
        if (!found) System.out.println("Sorry, no rooms are currently available.");
        System.out.println("-----------------------");
    }

    private static void makeReservation(Scanner scanner) {
        displayAvailableRooms();
        System.out.print("\nEnter Room Number to book: ");
        int roomNum = scanner.nextInt();
        scanner.nextLine(); 

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.roomNumber == roomNum && r.isAvailable) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("❌ Room not available or invalid Room Number.");
            return;
        }

        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();

        System.out.printf("Total price is $%.2f. Process payment? (y/n): ", selectedRoom.price);
        String payChoice = scanner.next();

        if (payChoice.equalsIgnoreCase("y")) {
            selectedRoom.isAvailable = false;
            Reservation res = new Reservation(nextReservationId++, name, selectedRoom, selectedRoom.price);
            reservations.add(res);
            System.out.println("\n✅ Payment Successful! Room Booked.");
            System.out.println("Your Reservation ID is: " + res.reservationId);
        } else {
            System.out.println("❌ Booking cancelled due to payment failure.");
        }
    }

    private static void viewBookingDetails(Scanner scanner) {
        System.out.print("Enter your Reservation ID: ");
        int id = scanner.nextInt();
        
        for (Reservation res : reservations) {
            if (res.reservationId == id) {
                System.out.println("\n--- BOOKING DETAILS ---");
                System.out.println("ID: " + res.reservationId);
                System.out.println("Name: " + res.customerName);
                System.out.println("Room No: " + res.room.roomNumber);
                System.out.println("Category: " + res.room.category);
                System.out.printf("Paid: $%.2f\n", res.amountPaid);
                return;
            }
        }
        System.out.println("❌ No reservation found with this ID.");
    }

    private static void cancelReservation(Scanner scanner) {
        System.out.print("Enter Reservation ID to cancel: ");
        int id = scanner.nextInt();

        for (Reservation res : reservations) {
            if (res.reservationId == id) {
                res.room.isAvailable = true;
                reservations.remove(res);
                System.out.println("✅ Reservation cancelled. Refund processed.");
                return;
            }
        }
        System.out.println("❌ No reservation found with this ID.");
    }

    // File I/O: Save bookings data
    private static void saveReservationsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Reservation res : reservations) {
                writer.println(res.reservationId + "," + res.customerName + "," + res.room.roomNumber + "," + res.amountPaid);
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // File I/O: Load bookings data back
    private static void loadReservationsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] tokens = fileScanner.nextLine().split(",");
                if (tokens.length == 4) {
                    int id = Integer.parseInt(tokens[0]);
                    String name = tokens[1];
                    int roomNum = Integer.parseInt(tokens[2]);
                    double paid = Double.parseDouble(tokens[3]);

                    for (Room r : rooms) {
                        if (r.roomNumber == roomNum) {
                            r.isAvailable = false;
                            reservations.add(new Reservation(id, name, r, paid));
                            if (id >= nextReservationId) nextReservationId = id + 1;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }
}
