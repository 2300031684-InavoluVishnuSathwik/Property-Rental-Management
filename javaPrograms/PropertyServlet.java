import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/submitProperty")
public class PropertyServlet extends HttpServlet {

    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/homeease";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String propertyType = request.getParameter("property-type");
        String address = request.getParameter("house-address");
        if (address == null || address.isEmpty()) {
            address = request.getParameter("land-address");
        }
        String contact = request.getParameter("contact");
        if (contact == null || contact.isEmpty()) {
            contact = request.getParameter("land-contact");
        }
        String price = request.getParameter("expected-price");
        if (price == null || price.isEmpty()) {
            price = request.getParameter("land-price");
        }

        try (Connection conn = connect()) {
            String sql = "INSERT INTO properties (propertyType, address, contact, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, propertyType);
                stmt.setString(2, address);
                stmt.setString(3, contact);
                stmt.setString(4, price);
                stmt.executeUpdate();
                response.getWriter().println("Property submitted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
}  


