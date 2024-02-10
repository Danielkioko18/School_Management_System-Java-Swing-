
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DANIEL
 */
public class LabProgram {
    // Create a connection to a sqlite in-memeory database
    // Returns Connection object
    private static Connection conn=null;
    private static Statement stmt = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    
	public static Connection createConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
                        conn = DriverManager.getConnection("jdbc:sqlite::memory:");

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}

		return conn;

	}


	

	// Create Horse table
	// Parameter conn is database connection created in createConnection()
	public static void createTable(Connection conn) {
		
		try {
                        String CREATE_HORSE_TABLE_SQL = "CREATE TABLE HORSE "+
                        "(ID INT PRIMARY KEY  NOT NULL, NAME  TEXT, BREED TEXT, "
                                + "HEIGHT  REAL, BIRTHDATE TEXT)";
			stmt = conn.createStatement();
			stmt.execute(CREATE_HORSE_TABLE_SQL);
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					
				}
			}
		}

	}

	// Insert row into Horse table using a parameterized query
	// Parameter conn is database connection created in createConnection()
	// Parameters id, name, breed, height, and birthDate contain values to be inserted
	public static void insertHorse(Connection conn, int id, String name, String breed, double height,
			String birthDate) {
		
		try {
                    String INSERT_HORSE_SQL
                    = "INSERT INTO HORSE(ID, NAME, BREED, HEIGHT, BIRTHDATE) VALUES (?,?,?,?,?)";
			
                    pst = conn.prepareStatement(INSERT_HORSE_SQL);
                    pst.setInt(1, id);
                    pst.setString(2, name);
                    pst.setString(3, breed);
                    pst.setDouble(4, height);
                    pst.setString(5, birthDate);
                    pst.execute();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
                    if(pst != null) {
			try {
                        	pst.close();
			} catch (SQLException e) {
                            System.err.println(e.getMessage());
			}
                    } 
		}
		

	}
	
	// Select and print all rows of Horse table
	// Parameter conn is database connection created in createConnection()
	public static void selectAllHorses(Connection conn) {		
		try {
                        String SELECT_HORSE_SQL = "SELECT * FROM HORSE";
			pst = conn.prepareStatement(SELECT_HORSE_SQL);
			rs = pst.executeQuery();
			
			System.out.println("All horses:");
			
			if (rs != null) {
				
				while(rs.next()) {
					
					int id = rs.getInt("ID");
					String name = rs.getString("NAME");
					String breed = rs.getString("BREED");
					double height = rs.getDouble("HEIGHT");
					String dob = rs.getString("BIRTHDATE");
					
					System.out.print("(" + id +", '" + name + "', '" + breed + "', " + height +", '" + dob + "')");
					
				}// while

			} else {
				System.out.println("No horse found in database.");
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
			
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
	}

	public static void main(String[] args) {
		
		// Create connection to sqlite in-memory database
		Connection conn = createConnection();
		
		// Create Horse table
                createTable(conn);

                // Insert row into Horse table
                insertHorse(conn, 1, "Babe", "Quarter Horse", 15.3, "2015-02-10");

                // Select and print all Horse table rows
                selectAllHorses(conn);
        }
}
