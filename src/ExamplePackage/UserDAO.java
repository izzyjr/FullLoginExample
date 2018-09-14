package ExamplePackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

public class UserDAO {
   
	private static DataSource dataSource;
	
	public UserDAO(DataSource theDataSource) {
		super();
		dataSource = theDataSource;
	}
	
   public UserBean login(UserBean bean) throws Exception {
	
	   //preparing some objects for connection 
	   Connection myConn = null;
	   Statement myStmt = null;
	   ResultSet myRs = null;
	   
	   String username = bean.getUsername();    
	   String password = bean.getPassword();   
	    
	   String searchQuery =
            "select * from users where username='"
                     + username
                     + "' AND password='"
                     + password
                     + "'";
	    
	   // "System.out.println" prints in the console; Normally used to trace the process
	   System.out.println("Your user name is " + username);          
	   System.out.println("Your password is " + password);
	   System.out.println("Query: "+searchQuery);
	    
	   try {
	      //connect to DB 
	      myConn = dataSource.getConnection();
	      myStmt = myConn.createStatement();
	      myRs = myStmt.executeQuery(searchQuery);	        
	      boolean more = myRs.next();
		       
	      // if user does not exist set the isValid variable to false
	      if (!more) 
	      {
	         System.out.println("Sorry, you are not a registered user! Please sign up first");
	         bean.setValid(false);
	      } 
		        
	      //if user exists set the isValid variable to true
	      else if (more) 
	      {
	         String firstName = myRs.getString("first_name");
	         String lastName = myRs.getString("last_name");
		     	
	         System.out.println("Welcome " + firstName);
	         bean.setFirstName(firstName);
	         bean.setLastName(lastName);
	         bean.setValid(true);
	      }
	   } 

   catch (Exception ex) 
   {
      System.out.println("Log In failed: An Exception has occurred! " + ex);
   } 
	    
   //some exception handling
	   finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}

	   return bean;
	
   }
   
   private static void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close(); // Doesn't really close it...just puts back in connection pool
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}
   
}
