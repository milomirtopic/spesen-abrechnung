package ch.arbonia.views.spesenabrechnung;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import ch.arbonia.data.Abrechnung;
import ch.arbonia.data.Ausgabe;
import ch.arbonia.security.AuthenticatedUser;
import ch.arbonia.services.AbrechnungService;
import ch.arbonia.services.AusgabeService;
import ch.arbonia.services.UserService;
import ch.arbonia.views.MainLayout;

@PageTitle("Spesenabrechnung")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
public class SpesenabrechnungView extends VerticalLayout {

   private final UserService userService;
   private final AbrechnungService abrechnungService;
   private final AusgabeService ausgabeService;
   private final AuthenticatedUser authenticatedUser;

   private Grid<AbrechnungDto> abrechnungen = new Grid<>(AbrechnungDto.class, false);

   public SpesenabrechnungView(UserService userService, AbrechnungService abrechnungService, AusgabeService ausgabeService,
         AuthenticatedUser authenticatedUser) {
      this.userService = userService;
      this.abrechnungService = abrechnungService;
      this.ausgabeService = ausgabeService;
      this.authenticatedUser = authenticatedUser;

      abrechnungen.addColumn(AbrechnungDto::getName).setHeader("Abrechnung");
      abrechnungen.addColumn(AbrechnungDto::getBetrag).setHeader("Betrag");
      abrechnungen.addColumn(AbrechnungDto::getStatus).setHeader("Status");
      abrechnungen.addColumn(createToggleDetailsRenderer(abrechnungen));
      abrechnungen.setItems(load());
      abrechnungen.addComponentColumn(dto -> createStatusIcon(dto.getAbrechnung().isUebermittelt()))
            .setTooltipGenerator(person -> person.getStatus())
            .setHeader("");

      Button newAbrechnungButton = new Button("Abrechnung erstellen");
      newAbrechnungButton.addClickListener((event) -> onNewAbrechnungButtonClicked(new Abrechnung()));

      ComboBox<Filter> filter = new ComboBox<>("Anzeige");
      filter.setItems(Filter.values());

      HorizontalLayout holder = new HorizontalLayout(abrechnungen, filter);
      holder.setWidthFull();
      holder.expand(abrechnungen);

      H3 title = new H3("* Unternehmenshinweise");
      title.getStyle().set("margin-top", "30px");
      title.getStyle().set("margin-bottom", "0px");
      Text text = new Text("Bitte reichen Sie nur eine Spesenabrechnung pro Monat ein. Bennen Sie die Spesenabrechnung wie folgt \"Monat, Jahr\"");

      add(holder, newAbrechnungButton, title, text);
   }

   private Icon createStatusIcon(boolean isUebermittelt) {
      Icon icon = VaadinIcon.CHECK.create();
      icon.setColor("#399939");

      return isUebermittelt ? icon : null;
   }

   private void onNewAbrechnungButtonClicked(Abrechnung abrechnung) {
      AbrechnungComponent abrechnungComponent =
            new AbrechnungComponent(abrechnung, userService, abrechnungService, ausgabeService, authenticatedUser);
      abrechnungComponent.addDetachListener(e -> {
         abrechnungen.setItems(load());
      });
      abrechnungComponent.open();
   }

   private Renderer<AbrechnungDto> createToggleDetailsRenderer(Grid<AbrechnungDto> grid) {
      return LitRenderer.<AbrechnungDto>of("<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">übermitteln</vaadin-button>")
            .withFunction("handleClick", dto -> uebermitteln(dto.getAbrechnung()));
   }

   private void uebermitteln(Abrechnung abrechnung) {
      abrechnung.setUebermittelt(true);
      abrechnungService.save(abrechnung);
      abrechnungen.setItems(load());
   }

   private List<AbrechnungDto> load() {
      return abrechnungService.findByUser(authenticatedUser.get().get())
            .stream()
            .map(abrechnung -> {
               List<Ausgabe> ausggaben = ausgabeService.findByAbrechnung(abrechnung);
               return new AbrechnungDto(abrechnung, ausggaben);
            })
            .collect(Collectors.toList());
   }

}
