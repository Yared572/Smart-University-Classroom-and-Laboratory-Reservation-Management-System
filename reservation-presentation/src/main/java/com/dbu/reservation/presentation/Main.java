package com.dbu.reservation.presentation;

import com.dbu.reservation.application.dto.ReservationRequest;
import com.dbu.reservation.application.dto.RegistrationRequest;
import com.dbu.reservation.application.service.*;
import com.dbu.reservation.domain.model.*;
import com.dbu.reservation.domain.repository.*;
import com.dbu.reservation.infrastructure.repository.*;
import com.dbu.reservation.infrastructure.security.PasswordHasher;
import javafx.application.Application;import javafx.collections.FXCollections;import javafx.geometry.Insets;import javafx.scene.Scene;import javafx.scene.control.*;import javafx.scene.layout.*;import javafx.stage.Stage;import java.time.*;import java.util.function.Function;

public final class Main extends Application {
 private final UserRepository users=new JdbcUserRepository();private final RoomRepository rooms=new JdbcRoomRepository();private final ReservationRepository reservations=new JdbcReservationRepository();
 private final AvailabilityService availability=new AvailabilityService(rooms);private final ReservationService booking=new ReservationService(reservations);private final ApprovalService approvals=new ApprovalService(reservations);private final PasswordHasher passwordHasher=new PasswordHasher();private final AuthenticationService authentication=new AuthenticationService(users,passwordHasher);private Stage stage;private User current;private TableView<Reservation> mine;
 @Override public void start(Stage stage){this.stage=stage;stage.setTitle("DBU Smart Reservation System");loginPage();stage.show();}
 private void loginPage(){
  Label title=new Label("Debre Berhan University");title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
  Label subtitle=new Label("Classroom and Laboratory Reservation Management System");
  TextField universityId=new TextField();universityId.setPromptText("University ID");universityId.setMaxWidth(340);
  PasswordField password=new PasswordField();password.setPromptText("Password");password.setMaxWidth(340);
  Label status=new Label();status.setWrapText(true);Button signIn=new Button("Sign in");signIn.setDefaultButton(true);Button register=new Button("Create account");
  signIn.setOnAction(e->{try{current=authentication.login(universityId.getText(),password.getText());dashboard();}catch(Exception x){status.setText(msg(x));}});
  register.setOnAction(e->registrationPage());
  VBox content=new VBox(14,title,subtitle,new Separator(),new Label("Sign in"),universityId,password,new HBox(10,signIn,register),status);
  content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);content.setMaxWidth(520);content.setPadding(new Insets(30));
  StackPane root=new StackPane(content);root.setPadding(new Insets(30));stage.setScene(new Scene(root,760,520));
 }
 private void registrationPage(){
  Label title=new Label("Create an account");title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
  TextField name=new TextField();name.setPromptText("Full name");TextField id=new TextField();id.setPromptText("University ID");TextField email=new TextField();email.setPromptText("University email");TextField phone=new TextField();phone.setPromptText("Phone (optional)");
  ComboBox<Department> department=new ComboBox<>();department.setMaxWidth(Double.MAX_VALUE);
  department.setButtonCell(departmentCell());
  department.setCellFactory(value->departmentCell());
  PasswordField pass=new PasswordField();pass.setPromptText("Password (at least 8 characters)");
  ComboBox<Role> role=new ComboBox<>(FXCollections.observableArrayList(Role.STUDENT,Role.LECTURER));role.setValue(Role.STUDENT);role.setMaxWidth(Double.MAX_VALUE);
  Label status=new Label("Choose a department ID listed in the university database.");status.setWrapText(true);Button create=new Button("Create account");Button back=new Button("Back to sign in");
  try{department.setItems(FXCollections.observableArrayList(users.departments()));department.getSelectionModel().selectFirst();}
  catch(Exception x){status.setText(msg(x));create.setDisable(true);}
  create.setOnAction(e->{try{Department selectedDepartment=department.getValue();if(selectedDepartment==null)throw new IllegalArgumentException("Select a department.");authentication.register(new RegistrationRequest(name.getText(),id.getText(),email.getText(),phone.getText(),selectedDepartment.id(),pass.getText(),role.getValue()));status.setText("Account created. You can now sign in.");}catch(Exception x){status.setText(msg(x));}});
  back.setOnAction(e->loginPage());VBox content=new VBox(10,title,name,id,email,phone,new Label("Department ID"),department,new Label("Account type"),role,pass,new HBox(10,create,back),status);
  content.setMaxWidth(480);content.setPadding(new Insets(26));ScrollPane scroll=new ScrollPane(content);scroll.setFitToWidth(true);stage.setScene(new Scene(scroll,760,620));
 }
 private void dashboard(){Label title=new Label("DBU Reservation Dashboard");Label session=new Label("Signed in: "+current.fullName()+" ("+current.role()+")");Button logout=new Button("Sign out");logout.setOnAction(e->{current=null;mine=null;loginPage();});HBox header=new HBox(12,title,new Region(),session,logout);HBox.setHgrow(header.getChildren().get(1),Priority.ALWAYS);TabPane tabs=new TabPane();tabs.getTabs().add(tab("Find a room",search()));tabs.getTabs().add(tab("My reservations",myBookings()));if(current.role()==Role.ADMIN)tabs.getTabs().add(tab("Approvals",approvalPage()));BorderPane root=new BorderPane(tabs,header,null,null,null);root.setPadding(new Insets(18));stage.setScene(new Scene(root,1120,700));}
 private VBox search(){ComboBox<RoomType> type=new ComboBox<>(FXCollections.observableArrayList(RoomType.values()));type.setValue(RoomType.CLASSROOM);DatePicker date=new DatePicker(LocalDate.now().plusDays(1));TextField start=new TextField("09:00"),end=new TextField("10:00"),count=new TextField("1"),purpose=new TextField();purpose.setPromptText("Purpose");TextArea desc=new TextArea();desc.setPromptText("Description");desc.setPrefRowCount(2);TableView<Room> table=new TableView<>();table.getColumns().add(roomCol("Room",Room::name));table.getColumns().add(roomCol("Type",r->r.type().name()));table.getColumns().add(roomCol("Building",Room::building));table.getColumns().add(roomCol("Capacity",Room::capacity));table.getColumns().add(roomCol("Equipment",Room::equipment));table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);Label status=new Label();Button find=new Button("Search availability"),request=new Button("Request reservation");find.setOnAction(e->{try{table.setItems(FXCollections.observableArrayList(availability.search(type.getValue(),date.getValue(),LocalTime.parse(start.getText()),LocalTime.parse(end.getText()),Integer.parseInt(count.getText()))));status.setText("Times use 24-hour HH:mm.");}catch(Exception x){status.setText(msg(x));}});request.setOnAction(e->{try{Room room=table.getSelectionModel().getSelectedItem();if(room==null)throw new IllegalArgumentException("Select a room first.");booking.request(current,new ReservationRequest(room,date.getValue(),LocalTime.parse(start.getText()),LocalTime.parse(end.getText()),Integer.parseInt(count.getText()),purpose.getText(),desc.getText()));status.setText("Request submitted as PENDING.");refreshMine();}catch(Exception x){status.setText(msg(x));}});GridPane grid=new GridPane();grid.setHgap(8);grid.setVgap(8);grid.addRow(0,new Label("Type"),type,new Label("Date"),date);grid.addRow(1,new Label("Start"),start,new Label("End"),end);grid.addRow(2,new Label("Participants"),count,new Label("Purpose"),purpose);grid.add(new Label("Description"),0,3);grid.add(desc,1,3,3,1);return new VBox(10,grid,new HBox(10,find,request,status),table);}
 private VBox myBookings(){mine=new TableView<>();populateMine();Button refresh=new Button("Refresh");refresh.setOnAction(e->populateMine());return new VBox(10,refresh,mine);}
 private VBox approvalPage(){TableView<Reservation> table=reservationTable();TextField reason=new TextField();reason.setPromptText("Reason when rejecting");Button load=new Button("Refresh"),yes=new Button("Approve"),no=new Button("Reject");Runnable reload=()->{try{table.setItems(FXCollections.observableArrayList(approvals.pending(current)));}catch(Exception e){error(msg(e));}};load.setOnAction(e->reload.run());yes.setOnAction(e->decide(table,true,reason.getText(),reload));no.setOnAction(e->decide(table,false,reason.getText(),reload));reload.run();return new VBox(10,new HBox(8,load,yes,no,reason),table);}
 private void decide(TableView<Reservation> t,boolean approve,String why,Runnable reload){try{Reservation r=t.getSelectionModel().getSelectedItem();if(r==null)throw new IllegalArgumentException("Select a pending reservation.");approvals.decide(current,r.id(),approve,why);reload.run();}catch(Exception e){error(msg(e));}}
 private void populateMine(){if(mine!=null)try{mine.setItems(FXCollections.observableArrayList(booking.mine(current)));}catch(Exception e){error(msg(e));}}
 private void refreshMine(){populateMine();}
 private TableView<Reservation> reservationTable(){TableView<Reservation> t=new TableView<>();t.getColumns().add(resCol("ID",Reservation::id));t.getColumns().add(resCol("Requester",Reservation::requester));t.getColumns().add(resCol("Room",Reservation::room));t.getColumns().add(resCol("Date",r->r.date()));t.getColumns().add(resCol("Time",r->r.start()+"–"+r.end()));t.getColumns().add(resCol("People",Reservation::participants));t.getColumns().add(resCol("Purpose",Reservation::purpose));t.getColumns().add(resCol("Status",r->r.status()));t.getColumns().add(resCol("Rejection reason",r->r.rejectionReason()==null?"":r.rejectionReason()));t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);return t;}
 private <T> TableColumn<Reservation,T> resCol(String name,Function<Reservation,T> f){TableColumn<Reservation,T> c=new TableColumn<>(name);c.setCellValueFactory(x->new javafx.beans.property.SimpleObjectProperty<>(f.apply(x.getValue())));return c;}
 private <T> TableColumn<Room,T> roomCol(String name,Function<Room,T> f){TableColumn<Room,T> c=new TableColumn<>(name);c.setCellValueFactory(x->new javafx.beans.property.SimpleObjectProperty<>(f.apply(x.getValue())));return c;}
 private Tab tab(String name,javafx.scene.Node node){Tab t=new Tab(name,node);t.setClosable(false);return t;}private String msg(Exception e){String message=e.getMessage()==null?"Operation failed":e.getMessage();Throwable cause=e.getCause();if(cause!=null&&cause.getMessage()!=null&&!cause.getMessage().isBlank())message+="\n"+cause.getMessage();return message;}private void error(String s){new Alert(Alert.AlertType.ERROR,s,ButtonType.OK).showAndWait();}private void alert(String s){new Alert(Alert.AlertType.INFORMATION,s,ButtonType.OK).showAndWait();}
 private ListCell<Department> departmentCell(){return new ListCell<>(){@Override protected void updateItem(Department department,boolean empty){super.updateItem(department,empty);setText(empty||department==null?"":department.name()+" (ID "+department.id()+")");}};}
 public static void main(String[] args){launch(args);}
}
