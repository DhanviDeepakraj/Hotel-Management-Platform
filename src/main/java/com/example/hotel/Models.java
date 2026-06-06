package com.example.hotel;

// ================= ENUM =================
// Enum defines fixed constants for bed types with base prices.
enum BedType {
    SINGLE(800), DOUBLE(1200), TRIPLE(1800);

    // Stores base price for each type.
    private int basePrice;

    // Constructor assigns price to each type.
    BedType(int price) {
        this.basePrice = price;
    }

    // Returns base price of selected type.
    public int getBasePrice() {
        return basePrice;
    }
}

// Enum defines room categories with extra cost.
enum Category {
    STANDARD(0), PREMIUM(600), DELUXE(1200);

    // Stores extra cost for category.
    private int extra;

    // Constructor assigns extra cost.
    Category(int extra) {
        this.extra = extra;
    }

    // Returns extra cost of category.
    public int getExtra() {
        return extra;
    }
}

// -----------ABSTRACTION---------
// Abstract class defines common properties for rooms.
abstract class RoomBase {

    int roomNo;
    double basePrice;

    // Abstract method to calculate total price.
    abstract double calculateTariff();
}

// ---------INTERFACE----------
// Interface defines extra services a room can provide.
interface Amenities {

    // Method to provide WiFi service.
    void provideWifi();

    // Method to provide breakfast service.
    void provideBreakfast();
}

// ------INHERITANCE-----
// Room class inherits from RoomBase.
class Room extends RoomBase {

    // Default constructor.
    Room() {
    }

    // Parameterized constructor to set values
    Room(int roomNo, double basePrice) {
        this.roomNo = roomNo;
        this.basePrice = basePrice;
    }

    // Returns base price as tariff.
    @Override
    double calculateTariff() {
        return basePrice;
    }
}

// DeluxeRoom extends Room and implements Amenities.
class DeluxeRoom extends Room implements Amenities {

    // Indicates whether AC is available.
    boolean ac;

    // Default constructor.
    DeluxeRoom() {
    }

    // Constructor with AC option.
    DeluxeRoom(int roomNo, double basePrice, boolean ac) {
        super(roomNo, basePrice);
        this.ac = ac;
    }

    // Calculates tariff including AC cost if applicable.
    @Override
    double calculateTariff() {
        return basePrice + (ac ? 500 : 0);
    }

    // Provides WiFi service.
    @Override
    public void provideWifi() {
        System.out.println("WiFi Provided");
    }

    // Provides breakfast service.
    @Override
    public void provideBreakfast() {
        System.out.println("Breakfast Provided");
    }
}


// -------ENCAPSULATION + SERIALISATION---------
// Encapsulation hides data and provides controlled access using getters/setters.
class Booking {

    // Private fields for user data..
    private String name;
    private String phone;

    // Other booking details.
    String dob;
    String address;
    String bookingId;
    String password;
    java.time.LocalDate fromDate;
    java.time.LocalDate toDate;
    boolean singleAC;
    boolean doubleAC;
    boolean tripleAC;

    // Category selection
    // Stores selected category for each room type.
    Category singleCategory;
    Category doubleCategory;
    Category tripleCategory;

    // Getter returns booking ID.
    public String getBookingId() {
        return bookingId;
    }

    // Setter sets booking ID.
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    // Getter returns password.
    public String getPassword() {
        return password;
    }

    // Setter sets password.
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter returns user name.
    public String getName() {
        return name;
    }

    // Setter sets user name.
    public void setName(String name) {
        this.name = name;
    }

    // Getter returns phone number.
    public String getPhone() {
        return phone;
    }

    // Setter sets phone number.
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Stores quantity of each room type.
    int singleQty, doubleQty, tripleQty;

    // Stores summary and booked room numbers.
    java.util.List<String> summaryLines = new java.util.ArrayList<>();
    java.util.List<Integer> bookedRooms = new java.util.ArrayList<>();

    // Default constructor.
    public Booking() {
    }
}

// ================= SYNCHRONIZATION =================
// Synchronization ensures safe access to shared resources in multi-threading.
class RoomManager {

    // Returns customer (booking ID) assigned to a room.
    public String getCustomer(int roomNo) {
        return roomCustomerMap.get(roomNo);
    }

    // Maps room number to customer booking ID.
    private java.util.HashMap<Integer, String> roomCustomerMap = new java.util.HashMap<>();

    // Lists storing available rooms by type.
    private java.util.List<Integer> singleList = new java.util.ArrayList<>();
    private java.util.List<Integer> doubleList = new java.util.ArrayList<>();
    private java.util.List<Integer> tripleList = new java.util.ArrayList<>();

    // Initializes room numbers for each type.
    public RoomManager() {
        for (int i = 101; i <= 130; i++)
            singleList.add(i);
        for (int i = 201; i <= 240; i++)
            doubleList.add(i);
        for (int i = 301; i <= 330; i++)
            tripleList.add(i);
    }

    // Returns number of available single rooms.
    public int getSingleRooms() {
        return singleList.size();
    }

    // Returns number of available double rooms.
    public int getDoubleRooms() {
        return doubleList.size();
    }

    // Returns number of available triple rooms.
    public int getTripleRooms() {
        return tripleList.size();
    }

    // // Synchronization
    // This method safely books a room ensuring multiple users don’t access the same room at once.
    public synchronized int bookRoom(String bookingId, BedType type) {

        try {
            // Waits if no rooms are available for the selected type.
            while ((type == BedType.SINGLE && singleList.isEmpty()) ||
                    (type == BedType.DOUBLE && doubleList.isEmpty()) ||
                    (type == BedType.TRIPLE && tripleList.isEmpty())) {
                wait();
            }

            // Variable to store allocated room number.
            int roomNo = 0;

            // Assigns room based on selected type and removes it from available list.
            switch (type) {
                case SINGLE -> roomNo = singleList.remove(0);
                case DOUBLE -> roomNo = doubleList.remove(0);
                case TRIPLE -> roomNo = tripleList.remove(0);
            }

            // Stores which booking ID owns the room.
            System.out.println("Room " + roomNo + " booked for Booking ID: " + bookingId);
            roomCustomerMap.put(roomNo, bookingId);

            // Notifies other waiting threads that a change has occurred.
            notifyAll();

            // Returns allocated room number.
            return roomNo;

        } catch (InterruptedException e) {
            // Handles interruption during waiting.
            e.printStackTrace();
        }

        // Returns -1 if booking fails.
        return -1;
    }

    // Synchronization
    // This method releases a room back to available list safely.
    public synchronized void releaseRoom(int roomNo) {

        // Adds room back to correct list based on its number range.
        if (roomNo >= 100 && roomNo < 200) {
            singleList.add(roomNo);
        } else if (roomNo >= 200 && roomNo < 300) {
            doubleList.add(roomNo);
        } else if (roomNo >= 300 && roomNo < 400) {
            tripleList.add(roomNo);
        }

        // Removes booking mapping for released room.
        System.out.println("Room " + roomNo + " released.");
        roomCustomerMap.remove(roomNo);

        // Notifies waiting threads that a room is now available.
        notifyAll();

    }

    // Returns list of rooms booked under a specific booking ID.
    public java.util.List<Integer> getRoomsByBookingId(String bookingId) {

        // List to store matching room numbers.
        java.util.List<Integer> rooms = new java.util.ArrayList<>();

        // Iterates through map to find all rooms linked to booking ID.
        for (java.util.Map.Entry<Integer, String> entry : roomCustomerMap.entrySet()) {
            if (entry.getValue().equals(bookingId)) {
                rooms.add(entry.getKey());
            }
        }

        // Returns list of rooms.
        return rooms;

    }
}