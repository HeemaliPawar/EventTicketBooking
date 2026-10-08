//package com.eventticket.dao;
//
//import com.eventticket.bean.Booking;
//import com.eventticket.bean.Event;
//import com.eventticket.util.DBConnection;
//
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookingDAO {
//    private final EventDAO eventDAO = new EventDAO();
//
//    public int create(int userId, int eventId, int tickets) throws SQLException {
//        Connection con = null;
//        try {
//            con = DBConnection.getConnection();
//            con.setAutoCommit(false);
//
//            Event event = eventDAO.findById(eventId);
//            if (event == null || event.getAvailableSeats() < tickets) {
//                con.rollback();
//                return -1;
//            }
//
//            if (!eventDAO.reduceSeats(eventId, tickets, con)) {
//                con.rollback();
//                return -1;
//            }
//
//            BigDecimal total = event.getPrice().multiply(BigDecimal.valueOf(tickets));
//            String sql = "INSERT INTO bookings (user_id, event_id, tickets, total_amount, status) VALUES (?, ?, ?, ?, 'CONFIRMED')";
//            try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                ps.setInt(1, userId);
//                ps.setInt(2, eventId);
//                ps.setInt(3, tickets);
//                ps.setBigDecimal(4, total);
//                ps.executeUpdate();
//                try (ResultSet keys = ps.getGeneratedKeys()) {
//                    int id = -1;
//                    if (keys.next()) {
//                        id = keys.getInt(1);
//                    }
//                    con.commit();
//                    return id;
//                }
//            }
//        } catch (SQLException e) {
//            if (con != null) {
//                con.rollback();
//            }
//            throw e;
//        } finally {
//            if (con != null) {
//                con.setAutoCommit(true);
//                con.close();
//            }
//        }
//    }
//
//    public boolean cancel(int bookingId, int userId, boolean admin) throws SQLException {
//        Connection con = null;
//        try {
//            con = DBConnection.getConnection();
//            con.setAutoCommit(false);
//
//            String findSql = admin
//                    ? "SELECT * FROM bookings WHERE booking_id = ? AND status = 'CONFIRMED'"
//                    : "SELECT * FROM bookings WHERE booking_id = ? AND user_id = ? AND status = 'CONFIRMED'";
//            Booking booking;
//            try (PreparedStatement ps = con.prepareStatement(findSql)) {
//                ps.setInt(1, bookingId);
//                if (!admin) {
//                    ps.setInt(2, userId);
//                }
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (!rs.next()) {
//                        con.rollback();
//                        return false;
//                    }
//                    booking = new Booking();
//                    booking.setBookingId(rs.getInt("booking_id"));
//                    booking.setEventId(rs.getInt("event_id"));
//                    booking.setTickets(rs.getInt("tickets"));
//                }
//            }
//
//            try (PreparedStatement ps = con.prepareStatement("UPDATE bookings SET status='CANCELLED' WHERE booking_id=?")) {
//                ps.setInt(1, booking.getBookingId());
//                ps.executeUpdate();
//            }
//            eventDAO.restoreSeats(booking.getEventId(), booking.getTickets(), con);
//            con.commit();
//            return true;
//        } catch (SQLException e) {
//            if (con != null) {
//                con.rollback();
//            }
//            throw e;
//        } finally {
//            if (con != null) {
//                con.setAutoCommit(true);
//                con.close();
//            }
//        }
//    }
//
//    public List<Booking> findByUser(int userId) throws SQLException {
//        String sql = "SELECT b.*, e.title, e.venue, e.city, e.event_date, e.event_time "
//                + "FROM bookings b JOIN events e ON b.event_id = e.event_id "
//                + "WHERE b.user_id = ? ORDER BY b.booking_date DESC";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            try (ResultSet rs = ps.executeQuery()) {
//                return mapList(rs, false);
//            }
//        }
//    }
//
//    public List<Booking> findAll() throws SQLException {
//        String sql = "SELECT b.*, e.title, e.venue, e.city, e.event_date, e.event_time, u.full_name, u.email "
//                + "FROM bookings b "
//                + "JOIN events e ON b.event_id = e.event_id "
//                + "JOIN users u ON b.user_id = u.user_id "
//                + "ORDER BY b.booking_date DESC";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            return mapList(rs, true);
//        }
//    }
//
//    public Booking findById(int bookingId) throws SQLException {
//        String sql = "SELECT b.*, e.title, e.venue, e.city, e.event_date, e.event_time "
//                + "FROM bookings b JOIN events e ON b.event_id = e.event_id WHERE b.booking_id = ?";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, bookingId);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return map(rs, false);
//                }
//            }
//        }
//        return null;
//    }
//
//    public int countBookings() throws SQLException {
//        return count("SELECT COUNT(*) FROM bookings");
//    }
//
//    public int countUsers() throws SQLException {
//        return count("SELECT COUNT(*) FROM users WHERE role='USER'");
//    }
//
//    public int countEvents() throws SQLException {
//        return count("SELECT COUNT(*) FROM events");
//    }
//
//    private int count(String sql) throws SQLException {
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            return rs.next() ? rs.getInt(1) : 0;
//        }
//    }
//
//    private List<Booking> mapList(ResultSet rs, boolean withUser) throws SQLException {
//        List<Booking> list = new ArrayList<>();
//        while (rs.next()) {
//            list.add(map(rs, withUser));
//        }
//        return list;
//    }
//
//    private Booking map(ResultSet rs, boolean withUser) throws SQLException {
//        Booking b = new Booking();
//        b.setBookingId(rs.getInt("booking_id"));
//        b.setUserId(rs.getInt("user_id"));
//        b.setEventId(rs.getInt("event_id"));
//        b.setTickets(rs.getInt("tickets"));
//        b.setTotalAmount(rs.getBigDecimal("total_amount"));
//        b.setStatus(rs.getString("status"));
//        b.setBookingDate(rs.getTimestamp("booking_date"));
//        b.setEventTitle(rs.getString("title"));
//        b.setVenue(rs.getString("venue"));
//        b.setCity(rs.getString("city"));
//        b.setEventDate(rs.getDate("event_date"));
//        b.setEventTime(rs.getTime("event_time"));
//        if (withUser) {
//            b.setUserName(rs.getString("full_name"));
//            b.setUserEmail(rs.getString("email"));
//        }
//        return b;
//    }
//}








package com.eventticket.dao;

import com.eventticket.bean.Booking;
import com.eventticket.bean.Event;
import com.eventticket.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private final EventDAO eventDAO = new EventDAO();

    // Create Booking
    public int create(int userId, int eventId, int tickets) throws SQLException {

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            Event event = eventDAO.findById(eventId);

            if (event == null || event.getAvailableSeats() < tickets) {
                con.rollback();
                return -1;
            }

            if (!eventDAO.reduceSeats(eventId, tickets, con)) {
                con.rollback();
                return -1;
            }

            BigDecimal total = event.getPrice().multiply(BigDecimal.valueOf(tickets));

            String sql = "INSERT INTO bookings (user_id, event_id, quantity, total_amount, status) VALUES (?, ?, ?, ?, 'CONFIRMED')";

            try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, userId);
                ps.setInt(2, eventId);
                ps.setInt(3, tickets);
                ps.setBigDecimal(4, total);

                ps.executeUpdate();

                int bookingId = -1;

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        bookingId = keys.getInt(1);
                    }
                }

                con.commit();
                return bookingId;
            }

        } catch (SQLException e) {

            if (con != null) {
                con.rollback();
            }

            throw e;

        } finally {

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    // Cancel Booking
    public boolean cancel(int bookingId, int userId, boolean admin) throws SQLException {

        Connection con = null;

        try {

            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String findSql = admin
                    ? "SELECT * FROM bookings WHERE booking_id=? AND status='CONFIRMED'"
                    : "SELECT * FROM bookings WHERE booking_id=? AND user_id=? AND status='CONFIRMED'";

            Booking booking = new Booking();

            try (PreparedStatement ps = con.prepareStatement(findSql)) {

                ps.setInt(1, bookingId);

                if (!admin) {
                    ps.setInt(2, userId);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setEventId(rs.getInt("event_id"));
                    booking.setTickets(rs.getInt("quantity"));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE bookings SET status='CANCELLED' WHERE booking_id=?")) {

                ps.setInt(1, booking.getBookingId());
                ps.executeUpdate();
            }

            eventDAO.restoreSeats(booking.getEventId(), booking.getTickets(), con);

            con.commit();

            return true;

        } catch (SQLException e) {

            if (con != null) {
                con.rollback();
            }

            throw e;

        } finally {

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    // User Bookings
    public List<Booking> findByUser(int userId) throws SQLException {

        String sql =
                "SELECT b.*, e.event_name, e.venue, e.city, e.event_date, e.event_time " +
                "FROM bookings b " +
                "JOIN events e ON b.event_id=e.event_id " +
                "WHERE b.user_id=? " +
                "ORDER BY b.booking_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs, false);
            }
        }
    }

    // Admin Bookings
    public List<Booking> findAll() throws SQLException {

        String sql =
                "SELECT b.*, e.event_name, e.venue, e.city, e.event_date, e.event_time, u.full_name, u.email " +
                "FROM bookings b " +
                "JOIN events e ON b.event_id=e.event_id " +
                "JOIN users u ON b.user_id=u.user_id " +
                "ORDER BY b.booking_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapList(rs, true);
        }
    }

    // Booking by ID
    public Booking findById(int bookingId) throws SQLException {

        String sql =
                "SELECT b.*, e.event_name, e.venue, e.city, e.event_date, e.event_time " +
                "FROM bookings b " +
                "JOIN events e ON b.event_id=e.event_id " +
                "WHERE b.booking_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return map(rs, false);
                }
            }
        }

        return null;
    }

    // Dashboard Counts
    public int countBookings() throws SQLException {
        return count("SELECT COUNT(*) FROM bookings");
    }

    public int countUsers() throws SQLException {
        return count("SELECT COUNT(*) FROM users WHERE role='USER'");
    }

    public int countEvents() throws SQLException {
        return count("SELECT COUNT(*) FROM events");
    }

    private int count(String sql) throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Map List
    private List<Booking> mapList(ResultSet rs, boolean withUser) throws SQLException {

        List<Booking> list = new ArrayList<>();

        while (rs.next()) {
            list.add(map(rs, withUser));
        }

        return list;
    }

    // Map Single Booking
    private Booking map(ResultSet rs, boolean withUser) throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setEventId(rs.getInt("event_id"));
        booking.setTickets(rs.getInt("quantity"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        booking.setStatus(rs.getString("status"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));

        booking.setEventTitle(rs.getString("event_name"));
        booking.setVenue(rs.getString("venue"));
        booking.setCity(rs.getString("city"));
        booking.setEventDate(rs.getDate("event_date"));
        booking.setEventTime(rs.getTime("event_time"));

        if (withUser) {
            booking.setUserName(rs.getString("full_name"));
            booking.setUserEmail(rs.getString("email"));
        }

        return booking;
    }
}