package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Menu;

public class Menuservice {
	private static Database database = Database.getInstance();


	public static void add(Menu menu) {
		 try {
			 PreparedStatement ps = database.prepareStatement("INSERT INTO item (Kode, Nama, Harga, Stok) VALUES (?, ?, ?, ?)");
			 ps.setString(1, menu.getCode());
			 ps.setString(2, menu.getName());
			 ps.setInt(3, menu.getPrice());
			 ps.setInt(4, menu.getStock());
		        
		        int rowsAffected = ps.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("Menu added successfully to the database");
		        } else {
		            System.out.println("Failed to add menu to the database");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}
	
	public static void remove(Menu menu) {
		 try {
		        PreparedStatement ps = database.prepareStatement("DELETE FROM item WHERE Kode = ?");
		        ps.setString(1, menu.getCode());
		        ps.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}
	
	public static void update(Menu menu) {
		 try {
		    PreparedStatement ps = database.prepareStatement("UPDATE item SET Nama = ?, Harga = ?, Stok = ? WHERE Kode = ?");
		    ps.setString(1, menu.getName());
			ps.setInt(2, menu.getStock());
			ps.setInt(3, menu.getPrice());
			ps.setString(4, menu.getCode());
		     ps.executeUpdate();
	 } catch (SQLException e) {
	     e.printStackTrace();
	 }
	}
	
	public static ObservableList<Menu> getAllItems(){
		String query = "SELECT * FROM item";
		ObservableList<Menu> menuList = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = database.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				menuList.add(new Menu(rs.getString("Kode"), rs.getString("Nama"), (rs.getInt("Stok")), rs.getInt("Harga")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return menuList;
	}

}

	
     
