package com.nolmax.database.clientDatabase;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

class DatabaseManager {
    private static final String URL = "jdbc:sqlite:nolmax_chat_beta1.db"; //Đặt tên file database ở đây
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //Chuyển đổi LocalDateTime sang String

    // Hàm mở kết nối
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;"); // Kích hoạt khóa ngoại
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
        return conn;
    }

    //Hàm tạo bảng
    public static void setupDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" + "   id INTEGER PRIMARY KEY, " + "   username TEXT UNIQUE NOT NULL, " + "   avatar_url TEXT" + "); " +

                "CREATE TABLE IF NOT EXISTS conversations (" + "   id INTEGER PRIMARY KEY, " + "   type INTEGER NOT NULL, " + "   name TEXT, " + "   avatar_url TEXT, " + "   last_message_id INTEGER" + "); " +

                "CREATE TABLE IF NOT EXISTS conversation_participants (" + "   conversation_id INTEGER, " + "   user_id INTEGER, " + "   role INTEGER, " + "   joined_at TEXT, " + "   left_at TEXT, " + "   last_read_message_id INTEGER, " + "   PRIMARY KEY (conversation_id, user_id), " + "   FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE, " + "   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" + "); " +

                "CREATE TABLE IF NOT EXISTS messages (" + "   id INTEGER PRIMARY KEY, " + "   conversation_id INTEGER NOT NULL, " + "   sender_id INTEGER, " + "   content TEXT, " + "   sent_at TEXT, " + "   FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE" + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("--- Tạo bảng local Database thành công! ---");
        } catch (SQLException e) {
            System.out.println("Lỗi setup Local Database: " + e.getMessage());
        }
    }


    //Hàm Insert hoặc Update dữ liệu của tất cả các class.
    public static void InsertorUpdateUser(User user) {
        String sql = "INSERT OR REPLACE INTO users(id, username, avatar_url) VALUES (?,?,?)";
        executeUpsert(sql, user.getId(), user.getUsername(), user.getAvatarurl());
    }

    public static void InsertorUpdateConversations(Conversations conv) {
        String sql = "INSERT OR REPLACE INTO conversations(id, type, name, avatar_url, last_message_id) VALUES (?,?,?,?,?)";
        executeUpsert(sql, conv.getId(), conv.getType(), conv.getName(), conv.getAvatarUrl(), conv.getLastMessageId());
    }

    public static void insertOrUpdateMessage(Message msg) {
        String sql = "INSERT OR REPLACE INTO messages(id, conversation_id, sender_id, content, sent_at) VALUES (?,?,?,?,?)";
        // Chuyển LocalDateTime thành String trước khi lưu
        String sentAtStr = (msg.getSentAt() != null) ? msg.getSentAt().format(formatter) : null;
        executeUpsert(sql, msg.getId(), msg.getConversationid(), msg.getSenderid(), msg.getContent(), sentAtStr);
    }

    public static void insertOrUpdateParticipant(Participant p) {
        String sql = "INSERT OR REPLACE INTO conversation_participants(conversation_id, user_id, role, joined_at, left_at, last_read_message_id) VALUES (?,?,?,?,?,?)";

        String joinedAtStr = (p.getJoined_at() != null) ? p.getJoined_at().format(formatter) : null;
        String leftAtStr = (p.getLeft_at() != null) ? p.getLeft_at().format(formatter) : null;

        executeUpsert(sql, p.getConversationId(), p.getUserId(), p.getRole(), joinedAtStr, leftAtStr, p.getLastReadMessageId());
    }


    // Hàm để thực thi SQL nhanh hơn
    private static void executeUpsert(String sql, Object... params) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi thực thi: " + e.getMessage());
        }
    }


    // Hàm truy vấn lấy 50 tin nhắn mơi nhất.
    public static List<Message> getLastestMessage(int conversationId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE conversation_id = ? ORDER BY id DESC LIMIT 50";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, conversationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Ép kiểu ngược từ String trong DB ra LocalDateTime cho Java
                LocalDateTime sentAt = null;
                String sentAtStr = rs.getString("sent_at");
                if (sentAtStr != null && !sentAtStr.isEmpty()) {
                    // Xử lý linh hoạt lỡ trong SQLite có dấu 'T' hoặc khoảng trắng
                    sentAtStr = sentAtStr.replace("T", " ");
                    sentAt = LocalDateTime.parse(sentAtStr, formatter);
                }

                Message msg = new Message(rs.getLong("id"), rs.getLong("conversation_id"), rs.getLong("sender_id"), rs.getString("content"), sentAt);
                list.add(msg);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy tin nhắn: " + e.getMessage());
        }
        return list;
    }


    //Hàm lấy danh sách tất cả phòng chat
    public static List<Conversations> getAllConversations() {
        List<Conversations> list = new ArrayList<>();
        String sql = "SELECT * FROM conversations";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Conversations conv = new Conversations(rs.getLong("id"), rs.getInt("type"), rs.getString("name"), rs.getString("avatar_url"), rs.getLong("last_message_id"));
                list.add(conv);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách phòng chat: " + e.getMessage());
        }
        return list;
    }

    //Hàm xóa vĩnh viễn tin nhắn khỏi db
    public static void DeleteMessageLocal(Long messageId) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, messageId);
            pstmt.executeUpdate();
            System.out.println("Đã xóa vĩnh viễn tin nhắn thành công! ID: " + messageId);
        } catch (SQLException e) {
            System.out.println("Lỗi xóa tin nhắn: " + e.getMessage());
        }
    }

    //Hàm cập nhật nội dung tin nhắn
    public static void UpdateMessageContent(Long messageId, String newContent) {
        String sql = "UPDATE MESSAGE SET content = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newContent);
            pstmt.setLong(2, messageId);
            pstmt.executeUpdate();
            System.out.println("Đã cập nhật nội dung tin nhắn thành công! ID: " + messageId);
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật tin nhắn: " + e.getMessage());
        }
    }


    //Hàm lấy ID của tin nhăn mới nhất trong phòng chat
    public static Long getMaxMessageIdInConversation(Long conversationId) {
        String sql = "SELECT MAX(id) AS max_id FROM messages WHERE conversation_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, conversationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("max_id");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy max message ID: " + e.getMessage());
        }
        return 0L; // Trả về 0 nếu không có tin nhắn nào trong cuộc trò chuyện
    }


}