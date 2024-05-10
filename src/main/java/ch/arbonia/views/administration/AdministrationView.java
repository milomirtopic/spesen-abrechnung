package ch.arbonia.views.administration;

import java.util.HashSet;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ch.arbonia.data.Role;
import ch.arbonia.data.User;
import ch.arbonia.services.UserService;
import ch.arbonia.views.MainLayout;


@PageTitle("Administration")
@Route(value = "empty", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdministrationView extends VerticalLayout {

   private final UserService userService;
   private final Grid<User> userGrid;

   public AdministrationView(UserService userService) {
      this.userService = userService;
      this.userGrid = new Grid<>(User.class, false);

      userGrid.addColumn(User::getName).setHeader("Name");
      userGrid.addColumn(User::getUsername).setHeader("Benutzername");

      loadData();

      TextField nameField = new TextField("Name");
      TextField usernameField = new TextField("Benutzername");
      TextField passwordField = new TextField("Password");

      Binder<User> binder = new Binder<>(User.class);
      binder.forField(nameField)
            .asRequired()
            .bind(User::getName, User::setName);
      binder.forField(usernameField)
            .asRequired()
            .bind(User::getUsername, User::setUsername);
      binder.forField(passwordField)
            .asRequired()
            .bind(User::getHashedPassword, User::setRawPassword);

      Button createUserButton = new Button("Benutzer erstellen");
      createUserButton.addClickListener((event) -> addUserByBinder(binder));

      HorizontalLayout addUserHl = new HorizontalLayout(nameField, usernameField, passwordField, createUserButton);
      addUserHl.setVerticalComponentAlignment(Alignment.END, createUserButton);
      add(addUserHl, userGrid);
   }

   private void loadData() {
      userGrid.setItems(userService.listAll());
   }

   private void addUserByBinder(Binder<User> binder) {
      if (!binder.validate().isOk()) {
         return;
      }

      HashSet<Role> roles = new HashSet<>();
      roles.add(Role.USER);

      User user = new User();
      try {
         binder.writeBean(user);
         if (userService.existsByUsername(user.getUsername())) {
            Notification.show("Benutzername ist bereits vergeben", 3000, Notification.Position.MIDDLE);
         } else {
            userService.save(user);
            binder.readBean(new User());
         }
      } catch (ValidationException e) {
         Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
      }

      loadData();
   }

}
