import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/searchProperty")
public class SearchPropertyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String country = request.getParameter("country");
        String state = request.getParameter("state");
        String district = request.getParameter("district");
        String city = request.getParameter("city");
        String area = request.getParameter("area");
        String priceFrom = request.getParameter("priceFrom");
        String priceTo = request.getParameter("priceTo");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/homeease", "root", "password");

            String query = "SELECT * FROM properties WHERE country = ? AND state = ? AND district = ? AND city = ? AND area = ? AND price BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, country);
            pstmt.setString(2, state);
            pstmt.setString(3, district);
            pstmt.setString(4, city);
            pstmt.setString(5, area);
            pstmt.setInt(6, Integer.parseInt(priceFrom));
            pstmt.setInt(7, Integer.parseInt(priceTo));

            rs = pstmt.executeQuery();

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("Property ID: ").append(rs.getInt("property_id"))
                      .append(", Address: ").append(rs.getString("address"))
                      .append(", Price: ₹").append(rs.getInt("price"))
                      .append("<br>");
            }

            if (result.length() == 0) {
                result.append("No properties found matching the criteria.");
            }

            response.setContentType("text/html");
            response.getWriter().write(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
