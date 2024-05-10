package ch.arbonia.views;

import java.util.Optional;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import ch.arbonia.data.User;
import ch.arbonia.security.AuthenticatedUser;
import ch.arbonia.views.administration.AdministrationView;
import ch.arbonia.views.spesenabrechnung.SpesenabrechnungView;

public class MainLayout extends AppLayout {

   private H2 viewTitle;

   private AuthenticatedUser authenticatedUser;
   private AccessAnnotationChecker accessChecker;

   public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
      this.authenticatedUser = authenticatedUser;
      this.accessChecker = accessChecker;

      setPrimarySection(Section.DRAWER);
      addDrawerContent();
      addHeaderContent();
   }

   private void addHeaderContent() {
      DrawerToggle toggle = new DrawerToggle();

      viewTitle = new H2();
      viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

      Image image = new Image("images/logo.png", "Logo");
      image.getStyle().set("margin-left", "auto");
      image.getStyle().set("padding", "15px");
      image.setHeight("20px");

      Footer footer = createFooter();

      addToNavbar(true, toggle, viewTitle, image, footer);
   }

   private void addDrawerContent() {
      H1 appName = new H1("Spesenabrechnung");
      appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
      Header header = new Header(appName);

      Scroller scroller = new Scroller(createNavigation());


      addToDrawer(header, scroller);
   }

   private Button createNavigation() {
      Button nav = new Button("Home");
      nav.setIcon(VaadinIcon.HOME.create());
      nav.getStyle().set("margin-left", "30px");
      nav.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
      return nav;
   }

   /*
   private SideNav createNavigation() {
      SideNav nav = new SideNav();

      if (accessChecker.hasAccess(SpesenabrechnungView.class)) {
         nav.addItem(new SideNavItem("Spesenabrechnung", SpesenabrechnungView.class));

      }
      if (accessChecker.hasAccess(AdministrationView.class)) {
         nav.addItem(new SideNavItem("Administration", AdministrationView.class));

      }

      return nav;
   }
    */

   private Footer createFooter() {
      Footer layout = new Footer();

      Optional<User> maybeUser = authenticatedUser.get();
      if (maybeUser.isPresent()) {
         User user = maybeUser.get();

         Avatar avatar = new Avatar(user.getName());
         avatar.setThemeName("xsmall");
         avatar.getElement().setAttribute("tabindex", "-1");

         MenuBar userMenu = new MenuBar();
         userMenu.setThemeName("tertiary-inline contrast");

         MenuItem userName = userMenu.addItem("");
         Div div = new Div();
         div.add(avatar);
         div.add(user.getName());
         div.add(new Icon("lumo", "dropdown"));
         div.getElement().getStyle().set("display", "flex");
         div.getElement().getStyle().set("align-items", "center");
         div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
         userName.add(div);
         userName.getSubMenu().addItem("Ausloggen", e -> {
            authenticatedUser.logout();
         });

         layout.add(userMenu);
      } else {
         Anchor loginLink = new Anchor("login", "Eingollen");
         layout.add(loginLink);
      }

      return layout;
   }

   @Override
   protected void afterNavigation() {
      super.afterNavigation();
      viewTitle.setText(getCurrentPageTitle());
   }

   private String getCurrentPageTitle() {
      PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
      return title == null ? "" : title.value();
   }
}
