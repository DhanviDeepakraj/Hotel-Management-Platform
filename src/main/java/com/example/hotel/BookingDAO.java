package com.example.hotel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class BookingDAO {

    // ================= PHONE EXISTS =================

    public static boolean phoneExists(String phone)
            throws Exception {

        String sql =
                "SELECT phone FROM bookings WHERE phone=?";

        try (Connection con = Database.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)) {

            ps.setString(1, phone);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

    // ================= GENERATE BOOKING ID =================

    public static String generateBookingId()
            throws Exception {

        while (true) {

            String id =
                    String.valueOf(
                            (int) (Math.random() * 90000) + 10000);

            String sql =
                    "SELECT booking_id FROM bookings WHERE booking_id=?";

            try (Connection con = Database.getConnection();
                    PreparedStatement ps =
                            con.prepareStatement(sql)) {

                ps.setString(1, id);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return id;
                }
            }
        }
    }

    // ================= SAVE BOOKING =================

    public static void saveBooking(Booking b)
            throws Exception {

        String sql =
                """
                INSERT INTO bookings
                (
                booking_id,
                name,
                phone,
                password,
                dob,
                address,
                from_date,
                to_date
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)) {

            ps.setString(1, b.getBookingId());
            ps.setString(2, b.getName());
            ps.setString(3, b.getPhone());
            ps.setString(4, b.getPassword());

            ps.setDate(
                    5,
                    Date.valueOf(b.dob));

            ps.setString(6, b.address);

            ps.setDate(
                    7,
                    Date.valueOf(b.fromDate));

            ps.setDate(
                    8,
                    Date.valueOf(b.toDate));

            ps.executeUpdate();
        }
    }

    // ================= LOGIN =================

    public static Booking login(
            String phone,
            String bookingId,
            String password)
            throws Exception {

        String sql =
                """
                SELECT *
                FROM bookings
                WHERE phone=?
                AND booking_id=?
                AND password=?
                """;

        try (Connection con = Database.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, bookingId);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Booking b = new Booking();

            b.setBookingId(
                    rs.getString("booking_id"));

            b.setName(
                    rs.getString("name"));

            b.setPhone(
                    rs.getString("phone"));

            b.setPassword(
                    rs.getString("password"));

            b.dob =
                    rs.getDate("dob")
                            .toString();

            b.address =
                    rs.getString("address");

            b.fromDate =
                    rs.getDate("from_date")
                            .toLocalDate();

            b.toDate =
                    rs.getDate("to_date")
                            .toLocalDate();

            return b;
        }
    }

    // ================= SAVE BOOKED ROOMS =================

    public static void saveBookedRooms(
            String bookingId,
            List<Integer> rooms)
            throws Exception {

        String sql =
                """
                INSERT INTO booked_rooms
                (booking_id, room_no)
                VALUES (?, ?)
                """;

        try (Connection con = Database.getConnection()) {

            for (Integer room : rooms) {

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setString(1, bookingId);
                ps.setInt(2, room);

                ps.executeUpdate();

                PreparedStatement update =
                        con.prepareStatement(
                                """
                                UPDATE rooms
                                SET is_available = false
                                WHERE room_no = ?
                                """);

                update.setInt(1, room);

                update.executeUpdate();
            }
        }
    }

    // ================= CHECKOUT =================

    public static void checkoutBooking(
            String bookingId,
            List<Integer> rooms)
            throws Exception {

        try (Connection con = Database.getConnection()) {

            for (Integer room : rooms) {

                PreparedStatement update =
                        con.prepareStatement(
                                """
                                UPDATE rooms
                                SET is_available = true
                                WHERE room_no = ?
                                """);

                update.setInt(1, room);

                update.executeUpdate();
            }

            PreparedStatement delete =
                    con.prepareStatement(
                            """
                            DELETE FROM booked_rooms
                            WHERE booking_id = ?
                            """);

            delete.setString(1, bookingId);

            delete.executeUpdate();

            PreparedStatement status =
        con.prepareStatement(
                """
                UPDATE bookings
                SET status='CHECKED_OUT'
                WHERE booking_id=?
                """);

status.setString(1, bookingId);

status.executeUpdate();
        }
    }
}