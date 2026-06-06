package com.example.hotel;

// This class stores data that needs to be shared across different screens in the application.
public class AppState {

    // This stores the current user's booking details so all controllers can access and modify it.
    public static Booking booking = new Booking();

    // This manages room booking, availability, and handles multiple operations safely using threads.
    public static RoomManager manager = new RoomManager();

    // This map stores all bookings using booking ID as key for quick access.
    public static java.util.HashMap<String, Booking> bookingMap = new java.util.HashMap<>();

    // This flag checks if user came from dashboard to control navigation flow.
    // if user clicks back, system must know where to go
    public static boolean fromDashboard = false;

    // This flag tells whether a user is currently logged in or not.
    public static boolean isLoggedIn = false;

}