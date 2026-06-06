package com.example.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/hotel_db";

    private static final String USER = "postgres";

    private static final String PASSWORD = "123";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    public static void initialize() {

    try {

        Class.forName("org.postgresql.Driver");

        try (Connection con = getConnection();
             java.sql.Statement st = con.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS bookings (

                    booking_id VARCHAR(20) PRIMARY KEY,

                    name VARCHAR(100) NOT NULL,

                    phone VARCHAR(20) UNIQUE NOT NULL,

                    password VARCHAR(100) NOT NULL,

                    dob DATE,

                    address TEXT,

                    from_date DATE,

                    to_date DATE,

                    status VARCHAR(20) DEFAULT 'ACTIVE'
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS rooms (

                    room_no INT PRIMARY KEY,

                    room_type VARCHAR(20) NOT NULL,

                    is_available BOOLEAN DEFAULT TRUE
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS booked_rooms (

                    id SERIAL PRIMARY KEY,

                    booking_id VARCHAR(20) NOT NULL,

                    room_no INT NOT NULL,

                    FOREIGN KEY (booking_id)
                    REFERENCES bookings(booking_id)
                    ON DELETE CASCADE,

                    FOREIGN KEY (room_no)
                    REFERENCES rooms(room_no)
                    ON DELETE CASCADE
                )
            """);

            st.execute("""
                INSERT INTO rooms (room_no, room_type, is_available)
                SELECT generate_series(101,130), 'SINGLE', TRUE
                ON CONFLICT DO NOTHING
            """);

            st.execute("""
                INSERT INTO rooms (room_no, room_type, is_available)
                SELECT generate_series(201,240), 'DOUBLE', TRUE
                ON CONFLICT DO NOTHING
            """);

            st.execute("""
                INSERT INTO rooms (room_no, room_type, is_available)
                SELECT generate_series(301,330), 'TRIPLE', TRUE
                ON CONFLICT DO NOTHING
            """);

            System.out.println("Database initialized.");

            st.execute("""
    ALTER TABLE bookings
    ADD COLUMN IF NOT EXISTS status VARCHAR(20)
    DEFAULT 'ACTIVE'
""");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}