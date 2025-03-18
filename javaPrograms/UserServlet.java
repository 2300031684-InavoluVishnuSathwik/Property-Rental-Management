import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/UserProfile")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        if (username == null) {
            out.println("<h3>User not logged in.</h3>");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/homeease", "root", "password");

            // Fetch user details
            String userQuery = "SELECT email, phone FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(userQuery);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                out.println("<h3>User Information</h3>");
                out.println("<p><strong>Name:</strong> " + username + "</p>");
                out.println("<p><strong>Email:</strong> " + email + "</p>");
                out.println("<p><strong>Phone:</strong> " + phone + "</p>");
            } else {
                out.println("<p>User details not available.</p>");
            }
          
            String propertyQuery = "SELECT title, location, price, type FROM properties WHERE username = ?";
            pstmt = conn.prepareStatement(propertyQuery);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            out.println("<h3>Your Properties</h3>");
            while (rs.next()) {
                out.println("<div class='property-item'>");
                out.println("<h4>" + rs.getString("title") + "</h4>");
                out.println("<p><strong>Location:</strong> " + rs.getString("location") + "</p>");
                out.println("<p><strong>Price:</strong> " + rs.getString("price") + " Rs</p>");
                out.println("<p><strong>Type:</strong> " + rs.getString("type") + "</p>");
                out.println("</div>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
} 
