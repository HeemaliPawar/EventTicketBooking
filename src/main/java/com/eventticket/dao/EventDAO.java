////package com.eventticket.dao;
////
////import com.eventticket.bean.Event;
////import com.eventticket.util.DBConnection;
////
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.util.ArrayList;
////import java.util.List;
////
////public class EventDAO {
////
////    public List<Event> findAll() throws SQLException {
////        String sql = "SELECT * FROM events ORDER BY event_date, event_time";
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql);
////             ResultSet rs = ps.executeQuery()) {
////            return mapList(rs);
////        }
////    }
////
////    public List<Event> findUpcoming(int limit) throws SQLException {
////        String sql = "SELECT * FROM events WHERE event_date >= CURDATE() ORDER BY event_date LIMIT ?";
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql)) {
////            ps.setInt(1, limit);
////            try (ResultSet rs = ps.executeQuery()) {
////                return mapList(rs);
////            }
////        }
////    }
////
////    public List<Event> search(String keyword, String category) throws SQLException {
////        StringBuilder sql = new StringBuilder("SELECT * FROM events WHERE 1=1");
////        List<String> params = new ArrayList<>();
////        if (keyword != null && !keyword.trim().isEmpty()) {
////            sql.append(" AND (title LIKE ? OR venue LIKE ? OR city LIKE ? OR description LIKE ?)");
////            String like = "%" + keyword.trim() + "%";
////            params.add(like);
////            params.add(like);
////            params.add(like);
////            params.add(like);
////        }
////        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
////            sql.append(" AND category = ?");
////            params.add(category);
////        }
////        sql.append(" ORDER BY event_date, event_time");
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql.toString())) {
////            for (int i = 0; i < params.size(); i++) {
////                ps.setString(i + 1, params.get(i));
////            }
////            try (ResultSet rs = ps.executeQuery()) {
////                return mapList(rs);
////            }
////        }
////    }
////
////    public Event findById(int eventId) throws SQLException {
////        String sql = "SELECT * FROM events WHERE event_id = ?";
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql)) {
////            ps.setInt(1, eventId);
////            try (ResultSet rs = ps.executeQuery()) {
////                if (rs.next()) {
////                    return map(rs);
////                }
////            }
////        }
////        return null;
////    }
////
////    public boolean insert(Event event) throws SQLException {
////        String sql = "INSERT INTO events (title, category, description, venue, city, event_date, event_time, price, total_seats, available_seats) "
////                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql)) {
////            fill(ps, event);
////            ps.setInt(9, event.getTotalSeats());
////            ps.setInt(10, event.getAvailableSeats() > 0 ? event.getAvailableSeats() : event.getTotalSeats());
////            return ps.executeUpdate() > 0;
////        }
////    }
////
////    public boolean update(Event event) throws SQLException {
////        String sql = "UPDATE events SET title=?, category=?, description=?, venue=?, city=?, event_date=?, event_time=?, price=?, total_seats=?, available_seats=? "
////                + "WHERE event_id=?";
////        try (Connection con = DBConnection.getConnection();
////             PreparedStatement ps = con.prepareStatement(sql)) {
////            fill(ps, event);
////            ps.setInt(9, event.getTotalSeats());
////            ps.setInt(10, event.getAvailableSeats());
////            ps.setInt(11, event.getEventId());
////            return ps.executeUpdate() > 0;
////        }
////    }
////
////    public boolean delete(int eventId) throws SQLException {
////        try (Connection con = DBConnection.getConnection()) {
////            con.setAutoCommit(false);
////            try {
////                try (PreparedStatement ps = con.prepareStatement("DELETE FROM bookings WHERE event_id = ?")) {
////                    ps.setInt(1, eventId);
////                    ps.executeUpdate();
////                }
////                int rows;
////                try (PreparedStatement ps = con.prepareStatement("DELETE FROM events WHERE event_id = ?")) {
////                    ps.setInt(1, eventId);
////                    rows = ps.executeUpdate();
////                }
////                con.commit();
////                return rows > 0;
////            } catch (SQLException e) {
////                con.rollback();
////                throw e;
////            } finally {
////                con.setAutoCommit(true);
////            }
////        }
////    }
////
////    public boolean reduceSeats(int eventId, int tickets, Connection con) throws SQLException {
////        String sql = "UPDATE events SET available_seats = available_seats - ? WHERE event_id = ? AND available_seats >= ?";
////        try (PreparedStatement ps = con.prepareStatement(sql)) {
////            ps.setInt(1, tickets);
////            ps.setInt(2, eventId);
////            ps.setInt(3, tickets);
////            return ps.executeUpdate() > 0;
////        }
////    }
////
////    public boolean restoreSeats(int eventId, int tickets, Connection con) throws SQLException {
////        String sql = "UPDATE events SET available_seats = available_seats + ? WHERE event_id = ?";
////        try (PreparedStatement ps = con.prepareStatement(sql)) {
////            ps.setInt(1, tickets);
////            ps.setInt(2, eventId);
////            return ps.executeUpdate() > 0;
////        }
////    }
////
////    private void fill(PreparedStatement ps, Event event) throws SQLException {
////        ps.setString(1, event.getTitle());
////        ps.setString(2, event.getCategory());
////        ps.setString(3, event.getDescription());
////        ps.setString(4, event.getVenue());
////        ps.setString(5, event.getCity());
////        ps.setDate(6, event.getEventDate());
////        ps.setTime(7, event.getEventTime());
////        ps.setBigDecimal(8, event.getPrice());
////    }
////
////    private List<Event> mapList(ResultSet rs) throws SQLException {
////        List<Event> list = new ArrayList<>();
////        while (rs.next()) {
////            list.add(map(rs));
////        }
////        return list;
////    }
////
////    private Event map(ResultSet rs) throws SQLException {
////        Event event = new Event();
////        event.setEventId(rs.getInt("event_id"));
////        event.setTitle(rs.getString("title"));
////        event.setCategory(rs.getString("category"));
////        event.setDescription(rs.getString("description"));
////        event.setVenue(rs.getString("venue"));
////        event.setCity(rs.getString("city"));
////        event.setEventDate(rs.getDate("event_date"));
////        event.setEventTime(rs.getTime("event_time"));
////        event.setPrice(rs.getBigDecimal("price"));
////        event.setTotalSeats(rs.getInt("total_seats"));
////        event.setAvailableSeats(rs.getInt("available_seats"));
////        return event;
////    }
////}
//
//
//package com.eventticket.dao;
//
//import com.eventticket.bean.Event;
//import com.eventticket.util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EventDAO {
//
//    // Get all events
//    public List<Event> findAll() throws SQLException {
//
//        String sql = "SELECT e.*, c.category_name " +
//                     "FROM events e " +
//                     "JOIN categories c ON e.category_id = c.category_id " +
//                     "ORDER BY e.event_date, e.event_time";
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            return mapList(rs);
//        }
//    }
//
//    // Get upcoming events
//    public List<Event> findUpcoming(int limit) throws SQLException {
//
//        String sql = "SELECT e.*, c.category_name " +
//                     "FROM events e " +
//                     "JOIN categories c ON e.category_id = c.category_id " +
//                     "WHERE e.event_date >= CURDATE() " +
//                     "ORDER BY e.event_date, e.event_time " +
//                     "LIMIT ?";
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, limit);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                return mapList(rs);
//            }
//        }
//    }
//
//    // Search events
//    public List<Event> search(String keyword, String category) throws SQLException {
//
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("SELECT e.*, c.category_name ");
//        sql.append("FROM events e ");
//        sql.append("JOIN categories c ON e.category_id = c.category_id ");
//        sql.append("WHERE 1=1 ");
//
//        List<String> params = new ArrayList<>();
//
//        // Search by event name, venue, city or description
//        if (keyword != null && !keyword.trim().isEmpty()) {
//
//            sql.append("AND (");
//            sql.append("e.event_name LIKE ? ");
//            sql.append("OR e.venue LIKE ? ");
//            sql.append("OR e.city LIKE ? ");
//            sql.append("OR e.description LIKE ?");
//            sql.append(") ");
//
//            String like = "%" + keyword.trim() + "%";
//
//            params.add(like);
//            params.add(like);
//            params.add(like);
//            params.add(like);
//        }
//
//        // Filter by category name
//        if (category != null
//                && !category.trim().isEmpty()
//                && !"ALL".equalsIgnoreCase(category)) {
//
//            sql.append("AND c.category_name = ? ");
//            params.add(category.trim());
//        }
//
//        sql.append("ORDER BY e.event_date, e.event_time");
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                ps.setString(i + 1, params.get(i));
//            }
//
//            try (ResultSet rs = ps.executeQuery()) {
//                return mapList(rs);
//            }
//        }
//    }
//
//    // Find event by ID
//    public Event findById(int eventId) throws SQLException {
//
//        String sql = "SELECT e.*, c.category_name " +
//                     "FROM events e " +
//                     "JOIN categories c ON e.category_id = c.category_id " +
//                     "WHERE e.event_id = ?";
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, eventId);
//
//            try (ResultSet rs = ps.executeQuery()) {
//
//                if (rs.next()) {
//                    return map(rs);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    // Convert ResultSet into Event object
//    private Event map(ResultSet rs) throws SQLException {
//
//        Event event = new Event();
//
//        event.setEventId(rs.getInt("event_id"));
//
//        // Database event_name → Event title
//        event.setTitle(rs.getString("event_name"));
//
//        // Database category_name → Event category
//        event.setCategory(rs.getString("category_name"));
//
//        event.setDescription(rs.getString("description"));
//        event.setVenue(rs.getString("venue"));
//        event.setCity(rs.getString("city"));
//
//        event.setEventDate(rs.getDate("event_date"));
//        event.setEventTime(rs.getTime("event_time"));
//
//        // Database ticket_price → Event price
//        event.setPrice(rs.getBigDecimal("ticket_price"));
//
//        // Database total_tickets → Event totalSeats
//        event.setTotalSeats(rs.getInt("total_tickets"));
//
//        // Database available_tickets → Event availableSeats
//        event.setAvailableSeats(rs.getInt("available_tickets"));
//
//        return event;
//    }
//
//    // Convert multiple rows
//    private List<Event> mapList(ResultSet rs) throws SQLException {
//
//        List<Event> list = new ArrayList<>();
//
//        while (rs.next()) {
//            list.add(map(rs));
//        }
//
//        return list;
//    }
//}






package com.eventticket.dao;

import com.eventticket.bean.Event;
import com.eventticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // =========================
    // GET ALL EVENTS
    // =========================
    public List<Event> findAll() throws SQLException {

        String sql = "SELECT e.*, c.category_name " +
                     "FROM events e " +
                     "JOIN categories c ON e.category_id = c.category_id " +
                     "ORDER BY e.event_date, e.event_time";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapList(rs);
        }
    }

    // =========================
    // GET UPCOMING EVENTS
    // =========================
    public List<Event> findUpcoming(int limit) throws SQLException {

        String sql = "SELECT e.*, c.category_name " +
                     "FROM events e " +
                     "JOIN categories c ON e.category_id = c.category_id " +
                     "WHERE e.event_date >= CURDATE() " +
                     "ORDER BY e.event_date, e.event_time " +
                     "LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs);
            }
        }
    }

    // =========================
    // SEARCH EVENTS
    // =========================
    public List<Event> search(String keyword, String category) throws SQLException {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT e.*, c.category_name ");
        sql.append("FROM events e ");
        sql.append("JOIN categories c ON e.category_id = c.category_id ");
        sql.append("WHERE 1=1 ");

        List<String> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {

            sql.append("AND (");
            sql.append("e.event_name LIKE ? ");
            sql.append("OR e.venue LIKE ? ");
            sql.append("OR e.city LIKE ? ");
            sql.append("OR e.description LIKE ?");
            sql.append(") ");

            String like = "%" + keyword.trim() + "%";

            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (category != null
                && !category.trim().isEmpty()
                && !"ALL".equalsIgnoreCase(category)) {

            sql.append("AND c.category_name = ? ");
            params.add(category.trim());
        }

        sql.append("ORDER BY e.event_date, e.event_time");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs);
            }
        }
    }

    // =========================
    // GET EVENT BY ID
    // =========================
    public Event findById(int eventId) throws SQLException {

        String sql = "SELECT e.*, c.category_name " +
                     "FROM events e " +
                     "JOIN categories c ON e.category_id = c.category_id " +
                     "WHERE e.event_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    // =========================
    // INSERT EVENT
    // =========================
    public boolean insert(Event event) throws SQLException {

        int categoryId = getCategoryId(event.getCategory());

        String sql = "INSERT INTO events " +
                "(category_id, event_name, description, event_date, event_time, " +
                "venue, city, ticket_price, total_tickets, available_tickets) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setString(2, event.getTitle());
            ps.setString(3, event.getDescription());
            ps.setDate(4, event.getEventDate());
            ps.setTime(5, event.getEventTime());
            ps.setString(6, event.getVenue());
            ps.setString(7, event.getCity());
            ps.setBigDecimal(8, event.getPrice());
            ps.setInt(9, event.getTotalSeats());

            int available = event.getAvailableSeats();

            if (available <= 0) {
                available = event.getTotalSeats();
            }

            ps.setInt(10, available);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // UPDATE EVENT
    // =========================
    public boolean update(Event event) throws SQLException {

        int categoryId = getCategoryId(event.getCategory());

        String sql = "UPDATE events SET " +
                "category_id=?, " +
                "event_name=?, " +
                "description=?, " +
                "event_date=?, " +
                "event_time=?, " +
                "venue=?, " +
                "city=?, " +
                "ticket_price=?, " +
                "total_tickets=?, " +
                "available_tickets=? " +
                "WHERE event_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setString(2, event.getTitle());
            ps.setString(3, event.getDescription());
            ps.setDate(4, event.getEventDate());
            ps.setTime(5, event.getEventTime());
            ps.setString(6, event.getVenue());
            ps.setString(7, event.getCity());
            ps.setBigDecimal(8, event.getPrice());
            ps.setInt(9, event.getTotalSeats());
            ps.setInt(10, event.getAvailableSeats());
            ps.setInt(11, event.getEventId());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // DELETE EVENT
    // =========================
    public boolean delete(int eventId) throws SQLException {

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                // Delete booking coupons first
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE bc FROM booking_coupons bc " +
                        "JOIN bookings b ON bc.booking_id = b.booking_id " +
                        "WHERE b.event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete payments
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE p FROM payments p " +
                        "JOIN bookings b ON p.booking_id = b.booking_id " +
                        "WHERE b.event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete tickets
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE t FROM tickets t " +
                        "JOIN bookings b ON t.booking_id = b.booking_id " +
                        "WHERE b.event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete bookings
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM bookings WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete reviews
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM reviews WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete favorites
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM favorites WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete event images
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM event_images WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Delete event organizers
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM event_organizers WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    ps.executeUpdate();
                }

                // Finally delete event
                int rows;

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM events WHERE event_id = ?")) {

                    ps.setInt(1, eventId);
                    rows = ps.executeUpdate();
                }

                con.commit();

                return rows > 0;

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    // =========================
    // REDUCE AVAILABLE TICKETS
    // =========================
    public boolean reduceSeats(int eventId, int tickets, Connection con)
            throws SQLException {

        String sql = "UPDATE events " +
                     "SET available_tickets = available_tickets - ? " +
                     "WHERE event_id = ? " +
                     "AND available_tickets >= ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tickets);
            ps.setInt(2, eventId);
            ps.setInt(3, tickets);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // RESTORE AVAILABLE TICKETS
    // =========================
    public boolean restoreSeats(int eventId, int tickets, Connection con)
            throws SQLException {

        String sql = "UPDATE events " +
                     "SET available_tickets = available_tickets + ? " +
                     "WHERE event_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tickets);
            ps.setInt(2, eventId);

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // GET CATEGORY ID
    // =========================
    private int getCategoryId(String categoryName) throws SQLException {

        String sql = "SELECT category_id FROM categories " +
                     "WHERE category_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        }

        throw new SQLException(
                "Category not found: " + categoryName
        );
    }

    // =========================
    // MAP RESULT SET
    // =========================
    private Event map(ResultSet rs) throws SQLException {

        Event event = new Event();

        event.setEventId(rs.getInt("event_id"));

        event.setTitle(rs.getString("event_name"));

        event.setCategory(rs.getString("category_name"));

        event.setDescription(rs.getString("description"));

        event.setVenue(rs.getString("venue"));

        event.setCity(rs.getString("city"));

        event.setEventDate(rs.getDate("event_date"));

        event.setEventTime(rs.getTime("event_time"));

        event.setPrice(rs.getBigDecimal("ticket_price"));

        event.setTotalSeats(rs.getInt("total_tickets"));

        event.setAvailableSeats(rs.getInt("available_tickets"));

        return event;
    }

    // =========================
    // MAP MULTIPLE EVENTS
    // =========================
    private List<Event> mapList(ResultSet rs) throws SQLException {

        List<Event> list = new ArrayList<>();

        while (rs.next()) {
            list.add(map(rs));
        }

        return list;
    }
}

