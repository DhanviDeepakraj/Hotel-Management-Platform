package com.example.hotel;

import java.sql.Connection;

public class TestDB {

    public static void main(String[] args) {

        try (Connection con = Database.getConnection()) {

           System.out.println(
        BookingDAO.generateBookingId()
);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}