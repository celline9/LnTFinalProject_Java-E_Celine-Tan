package main.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.Main;
import database.Database;
import database.Menuservice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.model.Menu;

public class HomePage {
	
	private Stage stage;
	private BorderPane root = new BorderPane();
	private GridPane gp = new GridPane();
	private Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
	private Label insertLabel = new Label("PT PUDDING");
	private Label codeLabel = new Label("Kode menu: ");
	private Label namaLabel = new Label("Nama Menu: ");
	private Label hargaLabel = new Label("Harga Menu: ");
	private Label stokLabel = new Label("Stok Menu: ");
//	private Label codeContent = new Label(Menu.);
	private TableColumn<Menu, String> idCol = new TableColumn<>("Menu's Code");
	private TableColumn<Menu, String> nameCol = new TableColumn<>("Menu's Name");
	private TableColumn<Menu, Integer> priceCol = new TableColumn<>("Menu's Price");
	private TableColumn<Menu, Integer> stockCol = new TableColumn<>("Menu's Stock");
	
	
	private TextField codeTf = new TextField();
	private TextField namaTf = new TextField();
	private TextField hargaTf = new TextField();
	private TextField stokTf = new TextField();
	private TableView<Menu> table = new TableView<>();
	private Button addBtn = new Button("Add Menu Baru" );
	private Button updateBtn = new Button("Update Menu");
	private Button removeBtn = new Button("Remove Menu");
	
//	private List<Menu> menuList= new ArrayList<>();
	private ObservableList<Menu> menuList = FXCollections.observableArrayList();
	
	private Database database = Database.getInstance();
	String fontUrl2= "https://fonts.googleapis.com/css2?family=Delius&family=Itim&family=Lilita+One&family=M+PLUS+Rounded+1c&display=swap";
	
	private Menu selectedMenus;
	
	public HomePage(Stage stage) {
		this.stage = stage;
		this.setComponent();
		this.setStyle();
		this.setTable();
		this.populateTable();
		this.handleButton();
		this.handleTableListener();
	}
	
	private void handleTableListener() {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
		if(newValue!= null) {
			selectedMenus = newValue;
			codeTf.setText(newValue.getCode());
			namaTf.setText(newValue.getName());
			stokTf.setText(String.valueOf(newValue.getStock()));
			hargaTf.setText(String.valueOf(newValue.getPrice()));
		}
		});
	}
	private void populateTable() {
//		try {
//			PreparedStatement ps = database.prepareStatement("SELECT * FROM product");
//			ResultSet resultSet = ps.executeQuery();
//			 if (resultSet.next()) {
//			        System.out.println("View success");
//			    } else {
//			        System.out.println("No data found");
//			    }
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		menuList = Menuservice.getAllItems();
		table.setItems(FXCollections.observableArrayList(menuList));
		codeTf.clear();
		namaTf.clear();
		hargaTf.clear();
		stokTf.clear();
	}
	
	private void handleButton() {
		root.getStylesheets().add(fontUrl2);
		addBtn.setOnAction(event -> {
			String Kode = codeTf.getText();
			String Nama = namaTf.getText();
			String Harga = hargaTf.getText();
			String Stok = stokTf.getText();
			
			if (Kode.isEmpty() || Nama.isEmpty() || Harga.isEmpty() || Stok.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "All fields must be filled");
				return;
			} 
			
			if(Integer.valueOf(Stok) > 99 ) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Stock must be less than 99");
				return;
			}
			
			if (Integer.valueOf(Harga) > 10000000) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be less than 10000000");
				return;
			}
			
			for (Menu menu : menuList) {
				if(menu.getCode().equals(Kode)) {
					alert(AlertType.ERROR, "Error", "Validation Error", "Id must be unique!");
					return;
				}
			}
			
			try {
				Integer.valueOf(Harga);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be numeric.");
				return;
			}
			
			try {
				Integer.valueOf(Stok);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Stock must be numeric.");
				return;
			}
			
			if (!Kode.matches("PD-\\d{3}")) {
				alert(AlertType.ERROR, "Error", "Validation Error", "ID must start with PD- and 3 numbers be followed by 3 digits!");
				return;
			}
		
//			 try {
//				 PreparedStatement ps = database.prepareStatement("INSERT INTO product (Kode, Nama, Harga, Stok) VALUES (?, ?, ?, ?)");
//				 ps.setString(1, Kode);
//				 ps.setString(2, Nama);
//				 ps.setInt(3, Integer.valueOf(Harga));
//				 ps.setInt(4, Integer.valueOf(Stok));
//			        
//			        int rowsAffected = ps.executeUpdate();
//			        if (rowsAffected > 0) {
//			            System.out.println("Menu added successfully to the database");
//			        } else {
//			            System.out.println("Failed to add menu to the database");
//			        }
//			    } catch (SQLException e) {
//			        e.printStackTrace();
//			    }

			Menuservice.add(new Menu(Kode, Nama, Integer.valueOf(Harga), Integer.valueOf(Stok)));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu successfully added!");
		});
		
		addBtn.setStyle("-fx-background-color: #deb887; -fx-font-family:'Delius'; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		addBtn.setOnMouseEntered(e -> {
				    addBtn.setStyle("-fx-font-family:'Delius'; -fx-background-color: #191970; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold;");
				});
		addBtn.setOnMouseExited(e -> {
			addBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		});
		addBtn.setOnMousePressed(e -> {
		    addBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #dc143c; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 0.20; -fx-translate-y: 2;");
		});

		addBtn.setOnMouseReleased(e -> {
		    addBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 1;-fx-translate-y: 0;");
		});

		
		updateBtn.setOnAction(event -> {
		    Menu selectedMenu = table.getSelectionModel().getSelectedItem();
		    if (selectedMenu == null) {
		        alert(Alert.AlertType.ERROR, "Error", "Menu Not Selected", "Pilih menu yang ingin diperbarui");
		        return;
		    }

		    Dialog<Menu> dialog = new Dialog<>();
		    dialog.setTitle("Update Menu");
		    dialog.setHeaderText(null);
		    dialog.setResizable(true);
		    
		    Label priceLabel = new Label("Harga:");
		    TextField priceField = new TextField();
		    priceField.setText(String.valueOf(selectedMenu.getPrice()));
		    
		    Label stockLabel = new Label("Stock:");
		    TextField stockField = new TextField();
		    stockField.setText(String.valueOf(selectedMenu.getStock()));

		    GridPane grid = new GridPane();
		   
		    grid.add(priceLabel, 1, 2);
		    grid.add(priceField, 2, 2);
		    grid.add(stockLabel, 3, 2);
		    grid.add(stockField, 4, 2);

		    dialog.getDialogPane().setContent(grid);

		    ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
		    dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

		    dialog.setResultConverter(dialogButton -> {
		        if (dialogButton == updateButtonType) {
		           int updatedPrice = Integer.parseInt(priceField.getText().trim());
		           int updatedStock = Integer.parseInt(stockField.getText().trim());

		            if (updatedPrice <= 0 || updatedStock <= 0) {
		                alert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Nama menu harus diisi dan harga harus lebih dari 0.");
		                return null;
		            }
		            selectedMenu.setPrice(updatedPrice);
		            selectedMenu.setStock(updatedStock);
		            Menuservice.update(selectedMenu);
		            
//		            try {
//		                PreparedStatement ps = database.prepareStatement("UPDATE product SET Harga = ?, Stok = ? WHERE Kode = ?");
//		                ps.setInt(1, Integer.valueOf(updatedPrice));
//		                ps.setInt(2, Integer.valueOf(updatedStock));
//		                ps.setString(3, selectedMenu.getCode());
//		                ps.executeUpdate();
//		            } catch (SQLException e) {
//		                e.printStackTrace();
//		            }
//		            
	
		            alert(Alert.AlertType.INFORMATION, "Informasi", "Menu Diperbarui", "Menu berhasil diperbarui");
		            return selectedMenu;
		        }

		        return null;
		    });

		    Optional<Menu> result = dialog.showAndWait();
		    if (result.isPresent()) {
		        table.refresh();
		    }
		});
		
		updateBtn.setStyle("-fx-background-color: #deb887; -fx-font-family:'Delius'; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		updateBtn.setOnMouseEntered(e -> {
				    updateBtn.setStyle("-fx-font-family:'Delius'; -fx-background-color: #191970; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold;");
				});
		updateBtn.setOnMouseExited(e -> {
			updateBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		});
		updateBtn.setOnMousePressed(e -> {
		    updateBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #dc143c; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 0.20; -fx-translate-y: 2;");
		});

		updateBtn.setOnMouseReleased(e -> {
		    updateBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 1;-fx-translate-y: 0;");
		});

		removeBtn.setOnAction(event -> {
			    Menu selectedMenu = table.getSelectionModel().getSelectedItem();
			    if (selectedMenu == null) {
			        alert(Alert.AlertType.ERROR, "Error", "Menu Not Selected", "Pilih menu yang ingin dihapus");
			        return;
			    }
			    
//			    try {
//			        PreparedStatement ps = database.prepareStatement("DELETE FROM product WHERE Kode = ?");
//			        ps.setString(1, selectedMenu.getCode());
//			        ps.executeUpdate();
//			    } catch (SQLException e) {
//			        e.printStackTrace();
//			    }
			
			    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			    alert.setTitle("Konfirmasi");
			    alert.setHeaderText(null);
			    alert.setContentText("Anda yakin ingin menghapus menu?");

			    Optional<ButtonType> result = alert.showAndWait();
			    if (result.isPresent() && result.get() == ButtonType.OK) {
			        Menuservice.remove(selectedMenu);
			        table.getItems().remove(selectedMenu);
			        alert(Alert.AlertType.INFORMATION, "Informasi", "Menu Dihapus", "Menu berhasil dihapus");
			    }	
		});
		removeBtn.setStyle("-fx-background-color: #deb887; -fx-font-family:'Delius'; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		removeBtn.setOnMouseEntered(e -> {
				    removeBtn.setStyle("-fx-font-family:'Delius'; -fx-background-color: #191970; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold;");
				});
		removeBtn.setOnMouseExited(e -> {
			removeBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);");
		});
		removeBtn.setOnMousePressed(e -> {
		    removeBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #dc143c; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 0.20; -fx-translate-y: 2;");
		});

		removeBtn.setOnMouseReleased(e -> {
		    removeBtn.setStyle("-fx-font-family:'Delius';-fx-background-color: #deb887; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, darkgray, 5, 0.5, 0, 0);-fx-scale: 1;-fx-translate-y: 0;");
		});


	
	}
	
	private void alert(AlertType alertType, String title, String header, String content ) {
		Alert alert = new Alert(alertType);
		alert.initOwner(stage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		}
	
	private void setComponent() {
		root.setPadding(new Insets(20, 20, 20, 20));
	
		gp.add(insertLabel, 0, 0, 2, 1);
		gp.add(codeLabel, 0, 1);
		gp.add(codeTf, 1, 1);
		gp.add(namaLabel, 0, 2);
		gp.add(namaTf, 1, 2);
		gp.add(hargaLabel, 0, 3);
		gp.add(hargaTf, 1, 3);
		gp.add(stokLabel, 0, 4);
		gp.add(stokTf, 1, 4);
		gp.add(addBtn, 0, 5, 3, 3);
		gp.add(updateBtn, 4, 2);
		gp.add(removeBtn, 4, 3);
		table.getColumns().addAll(idCol, nameCol, priceCol, stockCol);
		
		root.setBottom(table);
		root.setCenter(gp);
		stage.setScene(scene);
	}
	
	private void setStyle() {
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(15);
		String fontUrl = "https://fonts.googleapis.com/css2?family=Rubik+Glitch+Pop&display=swap" ;
		root.getStylesheets().add(fontUrl);
		insertLabel.setStyle("-fx-font-size: 50; -fx-font-family: 'Rubik Glitch Pop';-fx-text-fill: black");
		
		
		
		codeTf.setStyle( "-fx-pref-width: 15px;" + "-fx-pref-height: 5px;" + "-fx-background-color: #f0f8ff;" + "-fx-border-color: #f8f8ff;" 
		+ "-fx-border-width: 2px;" + "-fx-border-radius: 8px;" + "-fx-padding: 2px;" +"-fx-font-size: 14px;");
		namaTf.setStyle( "-fx-pref-width: 15px;" + "-fx-pref-height: 5px;" + "-fx-background-color: #f0f8ff;" + "-fx-border-color: #f8f8ff;" 
				+ "-fx-border-width: 2px;" + "-fx-border-radius: 8px;" + "-fx-padding: 2px;" +"-fx-font-size: 14px;");
		hargaTf.setStyle( "-fx-pref-width: 15px;" + "-fx-pref-height: 5px;" + "-fx-background-color: #f0f8ff;" + "-fx-border-color: #f8f8ff;" 
				+ "-fx-border-width: 2px;" + "-fx-border-radius: 8px;" + "-fx-padding: 2px;" +"-fx-font-size: 14px;");
		stokTf.setStyle( "-fx-pref-width: 15px;" + "-fx-pref-height: 5px;" + "-fx-background-color: #f0f8ff;" + "-fx-border-color: #f8f8ff;" 
				+ "-fx-border-width: 2px;" + "-fx-border-radius: 8px;" + "-fx-padding: 2px;" +"-fx-font-size: 14px;");
		
//		Image myImage = new Image(getClass().getResourceAsStream("pudding.jpg"));
		root.getStylesheets().add(fontUrl2);
//		root.setStyle("-fx-background-color:lavender;");
		root.setStyle("-fx-background-image: url('file:///C:/Users/ASUS/eclipse-workspace/FinalProject/src/main/model/jelly3.jpg')");root.setStyle("-fx-background-image: url('file:///C:/Users/ASUS/eclipse-workspace/FinalProject/src/main/model/jelly3.jpg');" +
	              "-fx-background-size: 400px 200px;");
		namaLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Delius'; -fx-text-fill: black");
		codeLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Delius'; -fx-text-fill: black");
		hargaLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Delius'; -fx-text-fill: black");
		stokLabel.setStyle("-fx-font-size: 14; -fx-font-family: 'Delius'; -fx-text-fill: black");
		GridPane.setHalignment(insertLabel, HPos.CENTER);
		GridPane.setHalignment(addBtn, HPos.CENTER);
		GridPane.setHalignment(updateBtn, HPos.LEFT);
		GridPane.setHalignment(removeBtn, HPos.LEFT);
		GridPane.setMargin(addBtn, new Insets(0, 0, 0, 60));
		GridPane.setMargin(updateBtn, new Insets(0, 0, 0, 4));
		GridPane.setMargin(insertLabel, new Insets(0, 0, 0, 100));
		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	
	private void setTable() {
		idCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("code"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("price"));
		stockCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("stock"));
	}

}
