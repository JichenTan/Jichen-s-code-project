// To save as “ecomputershop\WEB-INF\classes\QueryMultiValueServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/eshopquery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EshopQueryServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/electronic_device_shop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
        String[] category = request.getParameterValues("category");  // Returns an array of Strings
        if (category == null) {
            out.println("<h2>No category selected. Please go back to select category(s)</h2><body></html>");
            return; // Exit doGet()
         } 

       String sqlStr = "SELECT * FROM computers WHERE category IN (";
         for (int i = 0; i < category.length; ++i) {
            if (i < category.length - 1) {
               sqlStr += "'" + category[i] + "', ";  // need a commas
            } else {
               sqlStr += "'" + category[i] + "'";    // no commas
            }
         }
         sqlStr += ") AND price < " + request.getParameter("price") + " AND qty > 0 ORDER BY category ASC, price ASC";
       
         out.println("<h3>Thank you for your query.</h3>");
         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         out.println("<form method='get' action='eshoporder'>");
         out.println("<table><tr><th></th> <th>Category</th> <th>Product</th> <th>Brand</th> <th>Price</th> </tr>");
         // For each row in ResultSet, print one checkbox inside the <form>
         while(rset.next()) {
            out.println("<tr><td><input type='checkbox' name='product_id' value="
                  +"</td> "+ "<td>"
                  + rset.getString("category") + "</td> "+ "<td>"
                  + rset.getString("product") + "</td> "+"<td>"
                  + rset.getString("brand") + "</td> "+ "<td>"
                  + rset.getString("price") + "</td> </tr>");
         }
         out.println("</table>");
         out.println("<p>Enter your Name: <input type='text' name='cust_name' /></p><p>Enter your Email: <input type='text' name='cust_email' /></p><p>Enter your Phone Number: <input type='text' name='cust_phone' /></p>"); 
 
         // Print the submit button and </form> end-tag
         out.println("<p><input type='submit' value='ORDER' />");
         out.println("</form>");}
         catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }
  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}